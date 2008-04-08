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

	private final PythonEvaluatorFactory factory;
	private final IGoalEngineLogger logger;
	
	private Map<IGoal, WorkUnit> workUnits;

	public ZielMotor() {
		this(new GoalEngineNullLogger());
	}

	public ZielMotor(IGoalEngineLogger logger) {
		this.logger = logger;
		System.out.println(this.logger);
		this.factory = new PythonEvaluatorFactory();
	}

	public void evaluateGoal(AbstractTypeGoal goal) {
		this.workUnits = new HashMap<IGoal, WorkUnit>();
		LinkedList<WorkUnit> queue = new LinkedList<WorkUnit>();

		registerWorkUnits(queue, goal, null);

		while (!queue.isEmpty()) {
			WorkUnit current = queue.poll();

			if (current.isNew()) {
				List<IGoal> subgoals = current.init();
				registerWorkUnits(queue, subgoals, current);
			}
			
			if (current.isInitialized() && current.areAllSubgoalsDone()) {
				goalDone(queue, current);
//				current.evaluator.finish();
//				current.state = State.DONE;
//				for (WorkUnit parent : current.parents) {
//					if (parent != null) {
//						List<IGoal> newGoals = parent.subGoalDone(current.goal);
//						registerWorkUnits(queue, newGoals, parent);
//					}
//				}
			}
			
			if (current.isDone()) {
				// Don't add to the queue again because it's done.
			} else {
				queue.add(current);
			}
		}
	}

	private void goalDone(List<WorkUnit> queue, WorkUnit workUnit) {
		workUnit.evaluator.finish();
		workUnit.state = State.DONE;
		for (WorkUnit parent : workUnit.parents) {
			List<IGoal> subgoals = parent.subGoalDone(workUnit.goal);
			registerWorkUnits(queue, subgoals, parent);
			if (parent.areAllSubgoalsDone()) {
				goalDone(queue, parent);
			}
		}
	}

	private void registerWorkUnits(List<WorkUnit> queue, List<IGoal> goals, WorkUnit parent) {
		for (IGoal goal : goals) {
			registerWorkUnits(queue, goal, parent);
		}
	}

	private void registerWorkUnits(List<WorkUnit> queue, IGoal goal, WorkUnit parent) {
		WorkUnit workUnit = workUnits.get(goal);
		if (workUnit == null) {
			// Didn't exist yet, so create it
			AbstractEvaluator evaluator = factory.createEvaluator(goal);
			workUnit = new WorkUnit(goal, evaluator, parent);
			workUnits.put(goal, workUnit);
			queue.add(workUnit);
		} else {
			if (workUnit.isDone()) {
				List<IGoal> subgoals = parent.subGoalDone(workUnit.goal);
				registerWorkUnits(queue, subgoals, parent);
			} else {
				// The same goal existed before, so check for cycles.
				if (isCyclic(workUnit, parent)) {
					System.out.println("Cyclic, old goal: " + workUnit.goal);
					System.out.println("        new goal: " + goal);
					
					List<IGoal> newGoals = parent.subGoalDone(workUnit.goal, GoalState.RECURSIVE);
					registerWorkUnits(queue, newGoals, parent);
				} else {
					workUnit.addParent(parent);
					queue.add(workUnit);
				}
			}
		}
	}
	
	private boolean isCyclic(WorkUnit start, WorkUnit end) {
		if (start == end) {
			return true;
		}
		for (WorkUnit parent : end.parents) {
			if (isCyclic(start, parent)) {
				return true;
			}
		}
		return false;
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
