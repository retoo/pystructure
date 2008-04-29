package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.util.LinkedList;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

/**
 * Records all the necessary details of the goal processing and allows therefore to post process them
 */
public class CustomLogger extends GoalTreeLogger {
	
	public static class Record {

		public final IGoal goal;
		public final int level;
		
		public final int creatorId;
		public final AbstractEvaluator creator;
		
		public final int id;
		public final AbstractEvaluator evaluator;
		
		public final String msg;
		public final long time;

		public Record(IGoal goal, int level, int creatorId,
				AbstractEvaluator creator, int id, AbstractEvaluator evaluator,
				String msg, long time) {
					this.goal = goal;
					this.level = level;
					this.creatorId = creatorId;
					this.creator = creator;
					this.id = id;
					this.evaluator = evaluator;
					this.msg = msg;
					this.time = time;
		}
		
	}

	private LinkedList<Record> currentLog;
	private IGoal currentRootGoal;

	public CustomLogger() {
		currentLog = null;
	}
	
	@Override
	public void evaluationStarted(IGoal rootGoal) {
		super.evaluationStarted(rootGoal);
		
		currentLog = new LinkedList<Record>();
		currentRootGoal = rootGoal;
	}
	
	@Override
	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		super.goalCreated(goal, creator, evaluator);
		
		log(goal, creator, evaluator, "Created", System.currentTimeMillis());
	}
	
	@Override
	protected void goalFinished(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		super.goalFinished(goal, creator, evaluator);
		
		log(goal, creator, evaluator, "Finished", System.currentTimeMillis());
	}
	
	private void log(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator, String msg, long time) {
		int level = level(evaluator);
		int creatorId = getNumber(creator);
		int id = getNumber(evaluator);
		Record record = new Record(goal, level, creatorId, creator, id, evaluator, msg, time);
		currentLog.add(record);
	}
	
	public LinkedList<Record> getLog() {
		return currentLog;
	}

	public IGoal getCurrentRootGoal() {
		return currentRootGoal;
	}

}
