package ch.hsr.ifs.pystructure.typeinference.goals.base;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class Location {

	public final Module module;
	public final SimpleNode node;

	public Location(ModuleContext context, Definition def) {
		this(context, def.getNode());
	}

	public Location(ModuleContext context, SimpleNode node) {
		this.module = context.getModule();
		this.node = node;
	}

	public int getLineNr() {
		return this.node.beginLine;
	}
	
}
