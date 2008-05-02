package ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public interface IEvaluatorFactory {

	AbstractEvaluator createEvaluator(IGoal goal);

}
