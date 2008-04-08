/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.inferencer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.IEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.GoalEngineNullLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

/**
 * Main working class for type inference. Purpose of this class is simple:
 * evaluate goals and manage their dependencies of subgoals. Also this class
 * allows pruning: before evaluating every goal(except root goal) could be
 * pruned by provided pruner.
 *
 * This class isn't thread safe.
 */
public class GoalEngine {

	private final IEvaluatorFactory evaluatorFactory;

	private final LinkedList<WorkingPair> workingQueue = new LinkedList<WorkingPair>();
	private final Map<IGoal, GoalEvaluationState> goalStates = new HashMap<IGoal, GoalEvaluationState>();
	private final Map<AbstractEvaluator, EvaluatorState> evaluatorStates = new HashMap<AbstractEvaluator, EvaluatorState>();

	private IGoalEngineLogger logger;
	
	private static final boolean CACHING_ENABLED = true;

	private static class EvaluatorState {
		public long timeCreated;
		public int totalSubgoals;
		public int successfulSubgoals;
		public int subgoalsLeft;
		public List<IGoal> subgoals = new ArrayList<IGoal>();

		public EvaluatorState(int subgoalsLeft) {
			this.subgoalsLeft = subgoalsLeft;
			this.timeCreated = System.currentTimeMillis();
			totalSubgoals = subgoalsLeft;
		}
	}

	private static final class WorkingPair {

		private IGoal goal;
		private AbstractEvaluator creator;

		private WorkingPair(IGoal goal, AbstractEvaluator parent) {
			this.goal = goal;
			this.creator = parent;
		}

	}

	private static class GoalEvaluationState {
		public AbstractEvaluator creator;
		public GoalState state;
		
		@Override
		public String toString() {
			return "Evaluation State: " + state;
		}
	}

	public GoalEngine(IEvaluatorFactory evaluatorFactory) {
		this(evaluatorFactory, new GoalEngineNullLogger());
	}

	public GoalEngine(IEvaluatorFactory evaluatorFactory, IGoalEngineLogger logger) {
		this.evaluatorFactory = evaluatorFactory;
		this.logger = logger;
	}

	private void storeGoal(IGoal goal, GoalState state, AbstractEvaluator creator) {
		GoalEvaluationState es = new GoalEvaluationState();
		es.state = state;
		es.creator = creator;
		
		goalStates.put(goal, es);
	}

	private void notifyEvaluator(AbstractEvaluator evaluator, IGoal subGoal) {
		GoalEvaluationState subGoalState = goalStates.get(subGoal);
		GoalState state = subGoalState.state;

		if (state == GoalState.WAITING) {
			state = GoalState.RECURSIVE;
		}

		List<IGoal> newGoals = evaluator.subgoalDone(subGoal, state);
		assert newGoals != null : "please return IGoal.NO_GOALS if there are no goals";

		for (IGoal newGoal : newGoals) {
			workingQueue.add(new WorkingPair(newGoal, evaluator));
		}
		
		EvaluatorState ev = evaluatorStates.get(evaluator);
		ev.subgoalsLeft--;
		ev.subgoalsLeft += newGoals.size();
		ev.totalSubgoals += newGoals.size();
		ev.subgoals.addAll(newGoals);
		if (state == GoalState.DONE) {
			ev.successfulSubgoals++;
		}
		if (ev.subgoalsLeft == 0) {
			evaluator.finish();
			logger.goalFinished(evaluator.getGoal(), evaluator);
			GoalEvaluationState st = goalStates.get(evaluator.getGoal());
			assert st != null;
			st.state = GoalState.DONE;
			if (st.creator != null) {
				notifyEvaluator(st.creator, evaluator.getGoal());
			}
		}
	}

	public IType evaluateGoal(AbstractTypeGoal rootGoal, IPruner pruner) {
		logger.evaluationStarted(rootGoal);
		
		reset();
		if (pruner != null) {
			pruner.init();
		}
		workingQueue.add(new WorkingPair(rootGoal, null));

		WorkingPair firstPostponed = null;

		while (!workingQueue.isEmpty()) {
			WorkingPair pair = workingQueue.removeFirst();
			GoalEvaluationState state = goalStates.get(pair.goal);
			
			if (state != null && pair.creator != null) {
				/*
				 * Previous goal which reapppeared (because its subgoals are not
				 * yet finished)
				 */

				/*
				 * TODO: Think about a better way to handle recursive goals,
				 * maybe with a priority queue.
				 * 
				 * TODO: needs better documentation and better warning printing
				 * 
				 * Check if the list only contains goals which currently are in
				 * the state 'waiting'
				 */
				if (state.state == GoalState.WAITING && pair != firstPostponed) {
					if (firstPostponed == null) {
						firstPostponed = pair;
					}

					workingQueue.addLast(pair);
				} else {
					IGoal oldGoal = fetchOldGOal(pair.goal);
					assert oldGoal != null;
					
					
					firstPostponed = null;
					notifyEvaluator(pair.creator, oldGoal);
				}
			} else {
				/* Goal just entered the loop */ 
				firstPostponed = null;

				AbstractEvaluator evaluator = evaluatorFactory.createEvaluator(pair.goal);
				assert evaluator != null;
				
				logger.goalCreated(pair.goal, pair.creator, evaluator);

				/* Check if there are any cached results */
				boolean isFinished = false;

				if (CACHING_ENABLED && evaluator.checkCache()) {
					isFinished = true;
				} else {
					List<IGoal> newGoals = evaluator.init();

					assert newGoals != null : "please return IGoal.NO_GOALS if there are no goals";

					/* Process Sub goals */
					if (!newGoals.isEmpty()) {
						for (IGoal newGoal : newGoals) {
							workingQueue.add(new WorkingPair(newGoal,
									evaluator));
						}
						EvaluatorState evaluatorState = new EvaluatorState(newGoals.size());
						evaluatorState.subgoals.addAll(newGoals);
						evaluatorStates.put(evaluator, evaluatorState);
						storeGoal(pair.goal, GoalState.WAITING, pair.creator);
					} else {
						/* If there haven't been any sub goals the result is already known */
						isFinished = true;
					}
				}

				if (isFinished) {
					evaluator.finish();
					logger.goalFinished(pair.goal, evaluator);
					storeGoal(pair.goal, GoalState.DONE, pair.creator);
					if (pair.creator != null) {
						notifyEvaluator(pair.creator, pair.goal);
					}
				}
			}
		}
		GoalEvaluationState s = goalStates.get(rootGoal);

		assert s.state == GoalState.DONE;
		
		logger.evaluationFinished(rootGoal);
		
		return rootGoal.resultType;
	}

	/** 
	 * This function is kinda strange (and will probably disappear). When there
	 * are two equal goals it usually means that a given goal was already solved (or
	 * already worked on). So now e need to discard the newer goal and keep the old one.
	 * But for that we also have to fetch it (we have to get the key, that is).
	 * 
	 */
	private IGoal fetchOldGOal(IGoal goal) {
		for (IGoal curGoal : goalStates.keySet()) {
			if (curGoal.equals(goal)) {
				return curGoal;
			}
		}
		throw new RuntimeException("Unable to fetch 'old goal' for " + goal);
	}

	private void reset() {
		workingQueue.clear();
		goalStates.clear();
		evaluatorStates.clear();
	}

	public void shutdown() {
		logger.shutdown();
	}

}
