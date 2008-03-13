/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Value;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Evaluator for assign nodes. For determining the type of the left hand side,
 * the type of the right hand side has to be found.
 */
public class AssignTypeEvaluator extends DefinitionTypeEvaluator  {

	private final AssignDefinition assignDefinition;
	
	public AssignTypeEvaluator(DefinitionTypeGoal goal, AssignDefinition assignDefinition) {
		super(goal, assignDefinition);
		this.assignDefinition = assignDefinition;
	}

	@Override
	public List<IGoal> init() {
		Value value = assignDefinition.getValue();
		if (value instanceof TupleElement) {
			return wrap(new TupleElementTypeGoal(getGoal().getContext(), (TupleElement) value));
		} else {
			return wrap(new ExpressionTypeGoal(getGoal().getContext(), assignDefinition.getValue().getExpression()));
		}
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (result instanceof CombinedType) {
			this.resultType = (CombinedType) result;
		} else if (result instanceof IEvaluatedType) {
			this.resultType.appendType((IEvaluatedType) result);
		} else {
			throw new RuntimeException("Upps, thought result is just combined type, check how else this can be done");
		}
		return IGoal.NO_GOALS;
	}

}
