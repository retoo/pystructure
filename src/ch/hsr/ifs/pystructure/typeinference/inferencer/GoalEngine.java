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

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.IEvaluatorFactory;

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
	private final Map<GoalEvaluator, EvaluatorState> evaluatorStates = new HashMap<GoalEvaluator, EvaluatorState>();

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

	private static class WorkingPair {
		private IGoal goal;
		private GoalEvaluator creator;

		public WorkingPair(IGoal goal, GoalEvaluator parent) {
			this.goal = goal;
			this.creator = parent;
		}

	}

	private static class GoalEvaluationState {
		public GoalEvaluator creator;
		public GoalState state;
		public Object result;
	}

	public GoalEngine(IEvaluatorFactory evaluatorFactory) {
		this.evaluatorFactory = evaluatorFactory;
	}

	private void storeGoal(IGoal goal, GoalState state, Object result, GoalEvaluator creator) {
		GoalEvaluationState es = new GoalEvaluationState();
		es.result = result;
		es.state = state;
		es.creator = creator;
		goalStates.put(goal, es);
	}

	private EvaluatorState getEvaluatorState(GoalEvaluator evaluator) {
		return evaluatorStates.get(evaluator);
	}

	private void putEvaluatorState(GoalEvaluator evaluator, EvaluatorState state) {
		evaluatorStates.put(evaluator, state);
	}

	private void notifyEvaluator(GoalEvaluator evaluator, IGoal subGoal) {
		GoalEvaluationState subGoalState = goalStates.get(subGoal);
		Object result = subGoalState.result;
		GoalState state = subGoalState.state;

		if (state == GoalState.WAITING) {
			state = GoalState.RECURSIVE;
		}
		
		List<IGoal> newGoals = evaluator.subGoalDone(subGoal, result, state);
		if (newGoals == null) {
			newGoals = IGoal.NO_GOALS;
		}
		for (IGoal newGoal : newGoals) {
			workingQueue.add(new WorkingPair(newGoal, evaluator));
		}
		EvaluatorState ev = getEvaluatorState(evaluator);
		ev.subgoalsLeft--;
		ev.subgoalsLeft += newGoals.size();
		ev.totalSubgoals += newGoals.size();
		ev.subgoals.addAll(newGoals);
		if (state == GoalState.DONE && result != null) {
			ev.successfulSubgoals++;
		}
		if (ev.subgoalsLeft == 0) {
			Object newRes = evaluator.produceResult();
			GoalEvaluationState st = goalStates.get(evaluator.getGoal());
			assert st != null;
			st.state = GoalState.DONE;
			st.result = newRes;
			if (st.creator != null) {
				notifyEvaluator(st.creator, evaluator.getGoal());
			}
		}
	}

	public Object evaluateGoal(IGoal rootGoal, IPruner pruner) {
		reset();
		if (pruner != null) {
			pruner.init();
		}
		workingQueue.add(new WorkingPair(rootGoal, null));
		
		WorkingPair firstPostponed = null;
		
		while (!workingQueue.isEmpty()) {
			WorkingPair pair = workingQueue.removeFirst();
			GoalEvaluationState state = goalStates.get(pair.goal);
			
			System.out.println(pair.goal);
			
			if (state != null && pair.creator != null) {
				/*
				 * TODO: Think about a better way to handle recursive goals,
				 * maybe with a priority queue.
				 * 
				 * TODO: needs better documentation and better warning printing
				 */
				/* Check if the list only contains goals which currently are in the state 'waiting'  */
				if (state.state == GoalState.WAITING && pair != firstPostponed) {
					if (firstPostponed == null) {
						firstPostponed = pair;
					}
					
					workingQueue.addLast(pair);
				} else {
					firstPostponed = null;
					notifyEvaluator(pair.creator, pair.goal);
				}
			} else {
				firstPostponed = null;
				
				boolean prune = false;
				if (pruner != null && pair.creator != null) {
					prune = pruner.prune(pair.goal);
				}
				if (prune) {
					storeGoal(pair.goal, GoalState.PRUNED, null, pair.creator);
					notifyEvaluator(pair.creator, pair.goal);
				} else {
					GoalEvaluator evaluator = evaluatorFactory
							.createEvaluator(pair.goal);
					assert evaluator != null;
					System.out.println(" " + evaluator.getClass().getSimpleName());
					
					/* Check if there are any cached results */
					boolean isFinished = false;
					
					if (evaluator.isCached()) {
						isFinished = true;
					} else {
						List<IGoal> newGoals = evaluator.init();
					
						/* please return IGoal.NO_GOALS if there are no goals */
						assert newGoals != null;
					
						/* Process Sub goals */
						if (!newGoals.isEmpty()) {
							for (IGoal newGoal : newGoals) {
								workingQueue.add(new WorkingPair(newGoal,
										evaluator));
							}
							EvaluatorState evaluatorState = new EvaluatorState(newGoals.size());
							evaluatorState.subgoals.addAll(newGoals);
							putEvaluatorState(evaluator, evaluatorState);
							storeGoal(pair.goal, GoalState.WAITING, null,
									pair.creator);
						} else {
							/* If there haven't been any sub goals the result is already known */
							isFinished = true;
						}
					}
					
					if (isFinished) {
						Object result = evaluator.produceResult();
						storeGoal(pair.goal, GoalState.DONE, result, pair.creator);
						if (pair.creator != null) {
							notifyEvaluator(pair.creator, pair.goal);
						}
					}
				}
			}
		}
		GoalEvaluationState s = goalStates.get(rootGoal);

		assert s.state == GoalState.DONE;
		return s.result;
	}

	private void reset() {
		workingQueue.clear();
		goalStates.clear();
		evaluatorStates.clear();
	}

}
