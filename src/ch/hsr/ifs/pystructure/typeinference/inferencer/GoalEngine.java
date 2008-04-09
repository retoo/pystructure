package ch.hsr.ifs.pystructure.typeinference.inferencer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.GoalNode.State;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.PythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.GoalEngineNullLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

/**
 * The goal engine is the place where a root goal and all of its subgoals are
 * evaluated.
 */
public class GoalEngine {
	
	private static final boolean CACHING_ENABLED = true;

	private final PythonEvaluatorFactory factory;
	private final IGoalEngineLogger logger;
	
	private LinkedList<GoalNode> queue;
	private Map<IGoal, GoalNode> goalNodes;

	public GoalEngine() {
		this(new GoalEngineNullLogger());
	}

	public GoalEngine(IGoalEngineLogger logger) {
		this.logger = logger;
		this.factory = new PythonEvaluatorFactory();
	}

	public void evaluateGoal(AbstractTypeGoal rootGoal) {
		logger.evaluationStarted(rootGoal);
		
		queue = new LinkedList<GoalNode>();
		goalNodes = new HashMap<IGoal, GoalNode>();

		registerGoalNode(rootGoal, null);

		while (!queue.isEmpty()) {
			GoalNode current = queue.poll();

			if (current.isNew()) {
				if (CACHING_ENABLED && current.evaluator.checkCache()) {
					finishGoalNode(current);
				} else {
					List<IGoal> subgoals = current.init();
					registerGoalNode(subgoals, current);
				}
			}
			
			if (current.isInitialized() && current.areAllSubgoalsDone()) {
				finishGoalNode(current);
			}
			
			if (current.isFinished()) {
				logger.goalFinished(current.goal, current.evaluator);
				// Don't add to the queue again because it's done.
			} else {
				queue.add(current);
			}
		}
		
		queue = null;
		goalNodes = null;
		
		logger.evaluationFinished(rootGoal);
	}

	private void finishGoalNode(GoalNode goalNode) {
		goalNode.evaluator.finish();
		goalNode.state = State.FINISHED;
		for (GoalNode parent : goalNode.parents) {
			List<IGoal> subgoals = parent.subGoalDone(goalNode.goal);
			registerGoalNode(subgoals, parent);
			if (parent.areAllSubgoalsDone()) {
				finishGoalNode(parent);
			}
		}
	}

	private void registerGoalNode(List<IGoal> goals, GoalNode parent) {
		for (IGoal goal : goals) {
			registerGoalNode(goal, parent);
		}
	}

	private void registerGoalNode(IGoal goal, GoalNode parent) {
		GoalNode goalNode = goalNodes.get(goal);
		if (goalNode == null) {
			// Didn't exist yet, so create it
			goalNode = createGoalNode(goal, parent);
			queue.add(goalNode);
		} else {
			if (goalNode.isFinished()) {
				// Reuse the goal's result.
				List<IGoal> subgoals = parent.subGoalDone(goalNode.goal);
				registerGoalNode(subgoals, parent);
			} else {
				// The same goal existed before, so check for cycles.
				if (goalNode.isInParentsOf(parent)) {
					// TODO: Maybe add an event (cyclicGoalCreated) to the logger interface?
					List<IGoal> newGoals = parent.subGoalDone(goalNode.goal, GoalState.RECURSIVE);
					registerGoalNode(newGoals, parent);
				} else {
					goalNode.addParent(parent);
					queue.add(goalNode);
				}
			}
		}
	}

	private GoalNode createGoalNode(IGoal goal, GoalNode parent) {
		AbstractEvaluator evaluator = factory.createEvaluator(goal);
		
		AbstractEvaluator creator = (parent != null ? parent.evaluator : null);
		logger.goalCreated(goal, creator, evaluator);
		
		GoalNode goalNode = new GoalNode(goal, evaluator, parent);
		goalNodes.put(goal, goalNode);
		return goalNode;
	}
	
	public void shutdown() {
		logger.shutdown();
	}

}
