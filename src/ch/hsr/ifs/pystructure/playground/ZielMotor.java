package ch.hsr.ifs.pystructure.playground;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.playground.WorkUnit.State;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.PythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.GoalEngineNullLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

public class ZielMotor {
	
	private static final boolean CACHING_ENABLED = true;

	private final PythonEvaluatorFactory factory;
	private final IGoalEngineLogger logger;
	
	private LinkedList<WorkUnit> queue;
	private Map<IGoal, WorkUnit> workUnits;

	public ZielMotor() {
		this(new GoalEngineNullLogger());
	}

	public ZielMotor(IGoalEngineLogger logger) {
		this.logger = logger;
		this.factory = new PythonEvaluatorFactory();
	}

	public void evaluateGoal(AbstractTypeGoal rootGoal) {
		logger.evaluationStarted(rootGoal);
		
		queue = new LinkedList<WorkUnit>();
		workUnits = new HashMap<IGoal, WorkUnit>();

		registerWorkUnits(rootGoal, null);

		while (!queue.isEmpty()) {
			WorkUnit current = queue.poll();

			if (current.isNew()) {
				if (CACHING_ENABLED && current.evaluator.checkCache()) {
					finishGoal(current);
				} else {
					List<IGoal> subgoals = current.init();
					registerWorkUnits(subgoals, current);
				}
			}
			
			if (current.isInitialized() && current.areAllSubgoalsDone()) {
				finishGoal(current);
			}
			
			if (current.isFinished()) {
				logger.goalFinished(current.goal, current.evaluator);
				// Don't add to the queue again because it's done.
			} else {
				queue.add(current);
			}
		}
		
		queue = null;
		workUnits = null;
		
		logger.evaluationFinished(rootGoal);
	}

	private void finishGoal(WorkUnit workUnit) {
		workUnit.evaluator.finish();
		workUnit.state = State.FINISHED;
		for (WorkUnit parent : workUnit.parents) {
			List<IGoal> subgoals = parent.subGoalDone(workUnit.goal);
			registerWorkUnits(subgoals, parent);
			if (parent.areAllSubgoalsDone()) {
				finishGoal(parent);
			}
		}
	}

	private void registerWorkUnits(List<IGoal> goals, WorkUnit parent) {
		for (IGoal goal : goals) {
			registerWorkUnits(goal, parent);
		}
	}

	private void registerWorkUnits(IGoal goal, WorkUnit parent) {
		WorkUnit workUnit = workUnits.get(goal);
		if (workUnit == null) {
			// Didn't exist yet, so create it
			workUnit = createWorkUnit(goal, parent);
			queue.add(workUnit);
		} else {
			if (workUnit.isFinished()) {
				// Reuse the goal's result.
				List<IGoal> subgoals = parent.subGoalDone(workUnit.goal);
				registerWorkUnits(subgoals, parent);
			} else {
				// The same goal existed before, so check for cycles.
				if (workUnit.isInParentsOf(parent)) {
					// TODO: Maybe add an event (cyclicGoalCreated) to the logger interface?
					List<IGoal> newGoals = parent.subGoalDone(workUnit.goal, GoalState.RECURSIVE);
					registerWorkUnits(newGoals, parent);
				} else {
					workUnit.addParent(parent);
					queue.add(workUnit);
				}
			}
		}
	}

	private WorkUnit createWorkUnit(IGoal goal, WorkUnit parent) {
		AbstractEvaluator evaluator = factory.createEvaluator(goal);
		
		AbstractEvaluator creator = (parent != null ? parent.evaluator : null);
		logger.goalCreated(goal, creator, evaluator);
		
		WorkUnit workUnit = new WorkUnit(goal, evaluator, parent);
		workUnits.put(goal, workUnit);
		return workUnit;
	}
	
	public void shutdown() {
		logger.shutdown();
	}

}
