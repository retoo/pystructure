package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.PythonEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public abstract class DefinitionTypeEvaluator extends PythonEvaluator {

	private Definition<?> definition;
	protected CombinedType resultType;
	
	public DefinitionTypeEvaluator(DefinitionTypeGoal goal, Definition<?> definition) {
		super(goal);
		this.definition = definition;
		this.resultType = new CombinedType();
	}
	
	public boolean isCached() {
		
		if (definition.type != null) {
			this.resultType = definition.type;
			
			return true;
		} else {
			return false;
		}
	}
	
	public Object produceResult() {
		if (definition.type == null) {
			definition.type = resultType;
		}
		
		return resultType;
	}
	
}
