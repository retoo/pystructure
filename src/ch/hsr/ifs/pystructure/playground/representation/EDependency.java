package ch.hsr.ifs.pystructure.playground.representation;

import org.jdom.Element;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;

public class EDependency extends Element {

	private static final long serialVersionUID = 1L;
	
	private boolean valid;

	private EDependency() {
		super("dependency");
	
		this.valid = true;
		
		this.setAttribute("type", "references");
	}
	
	public EDependency(String from, IType type) {
		this();
		this.setFrom(from);
		this.setTo(type);
	}

	public EDependency(StructureDefinition from, IType to) {
		this();
		
		this.setFrom(from);
		this.setTo(to);
	}
	
	public EDependency(StructureDefinition from, Definition to) {
		this();
		
		this.setFrom(from);
		this.setTo(to);
	}

	private void setTo(IType type) {
		setTo(getTypeIdentifier(type));
	}
	
	private void setTo(Definition to) {
		if (to == null) {
			this.valid = false;
		} else {
			this.setAttribute("to", to.getUniqueIdentifier());
		}
	}

	private void setFrom(StructureDefinition definition) {
		this.setFrom(definition.getUniqueIdentifier());
	}
	
	private void setFrom(String from) {
		this.setAttribute("from", from);
	}

	private StructureDefinition  getTypeIdentifier(IType type) {
		if (type instanceof ClassType) {
			ClassType classType = (ClassType) type;
			return classType.getKlass();
		} else if (type instanceof MetaclassType) {
			MetaclassType classType = (MetaclassType) type;
			return classType.getKlass();
		} else if (type instanceof FunctionType) {
			FunctionType functionType = (FunctionType) type;
			return functionType.getFunction();
		} else {
			throw new RuntimeException("Unknown type" + type);
		}
	}

	public boolean isValid() {
		return valid;
	}

}
