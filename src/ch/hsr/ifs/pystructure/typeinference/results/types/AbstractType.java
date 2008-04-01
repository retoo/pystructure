package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;

public abstract class AbstractType implements IType {
	
	// TODO: Ugly ugly ugly
	public Attribute location;

	protected final String typeName;
	
	public AbstractType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

}

