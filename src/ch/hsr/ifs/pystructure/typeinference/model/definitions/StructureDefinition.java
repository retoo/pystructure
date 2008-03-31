package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;

public abstract class StructureDefinition extends Definition {
	
	private final List<StructureDefinition> children;
	
	public StructureDefinition() {
		children = new ArrayList<StructureDefinition>();
	}
	
	public StructureDefinition(IModule module, String name, SimpleNode node) {
		super(module, name, node);
		children = new ArrayList<StructureDefinition>();
	}
	
	public void addChild(StructureDefinition child) {
		children.add(child);
	}
	
	public List<StructureDefinition> getChildren() {
		return children;
	}
	
	public String getUniqueIdentifier() {
		return String.valueOf(this.hashCode());
	}

}
