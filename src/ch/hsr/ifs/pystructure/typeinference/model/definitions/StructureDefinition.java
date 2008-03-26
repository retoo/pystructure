package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public abstract class StructureDefinition extends Definition {
	
	private final List<StructureDefinition> children;
	
	public StructureDefinition() {
		children = new ArrayList<StructureDefinition>();
	}
	
	public StructureDefinition(IModule module, NameAdapter name, SimpleNode node) {
		super(module, name, node);
		children = new ArrayList<StructureDefinition>();
	}
	
	public void addChild(StructureDefinition child) {
		children.add(child);
	}
	
	public List<StructureDefinition> getChildren() {
		return children;
	}

}
