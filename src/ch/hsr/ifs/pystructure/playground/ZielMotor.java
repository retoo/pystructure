package ch.hsr.ifs.pystructure.playground;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.playground.WorkUnit.State;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.PythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.GoalEngineNullLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

public class ZielMotor {

	private final PythonEvaluatorFactory factory;
	private final IGoalEngineLogger logger;

	public ZielMotor() {
		this(new GoalEngineNullLogger());
	}

	public ZielMotor(IGoalEngineLogger logger) {
		this.logger = logger;
		System.out.println(this.logger);
		this.factory = new PythonEvaluatorFactory();

	}

	public void evaluateGoal(AbstractTypeGoal goal) {
		LinkedList<WorkUnit> queue = new LinkedList<WorkUnit>();

		registerWorkUnits(queue, goal, null);

		while (!queue.isEmpty()) {
			WorkUnit current = queue.poll();

			if (current.isNew()) {
				List<IGoal> subgoals = current.init();
				registerWorkUnits(queue, subgoals, current);
			}
			
			if (current.isInitialized()) {
				if (current.areAllSubgoalsDone()) {
					current.evaluator.finish();
					current.state = State.DONE;
					List<IGoal> newGoals = current.parent.subGoalDone(current.goal);
					registerWorkUnits(queue, newGoals, current.parent);
				}
			}
			
			if (current.isDone()) {
				
			} else {
				queue.add(current);
			}
		}
	}


	private void registerWorkUnits(List<WorkUnit> queue, List<IGoal> goals, WorkUnit parent) {
		for (IGoal goal : goals) {
			registerWorkUnits(queue, goal, parent);
		}
	}

	private void registerWorkUnits(List<WorkUnit> queue, IGoal goal, WorkUnit parent) {
		AbstractEvaluator evaluator = factory.createEvaluator(goal);
		queue.add(new WorkUnit(goal, evaluator, parent));
	}	


	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
