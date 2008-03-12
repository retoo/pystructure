package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;

public abstract class AbstractType implements IEvaluatedType {

	protected final String modelKey;
	
	public AbstractType(String modelKey) {
		this.modelKey = modelKey;
	}

	public String getModelKey() {
		return modelKey;
	}

}

