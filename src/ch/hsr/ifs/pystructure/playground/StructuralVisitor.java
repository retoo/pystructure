package ch.hsr.ifs.pystructure.playground;

import java.util.HashMap;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.ParentVisitor;

/**
 * Base class for intelligently visiting a hierarchy consisting of nested
 * {@link StructureDefinition} elements.
 * 
 * Intelligently means that instead of the normal depth-first visiting, the
 * whole body of a class is visited before descending into the bodies of
 * methods. This means that visit methods like visitFunction are executed twice.
 * 
 * The first time, it is because the parent {@link StructureDefinition} "visits
 * over" the node (therefore the implementation of the sub class should not
 * visit the body).
 * 
 * The second time, it is called to be descended and the sub class should visit
 * the body.
 * 
 * The sub class can distinguish between these two cases using isFirstVisit.
 */
public abstract class StructuralVisitor extends ParentVisitor {

	private StructureDefinition currentlyVisiting;
	private Map<SimpleNode, StructureDefinition> definitionForNode;

	public StructuralVisitor() {
		this.definitionForNode = new HashMap<SimpleNode, StructureDefinition>();
	}
	
	protected void run(Module module) {
		try {
			visitChild(module);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This should be called from within a visit method and is for finding out
	 * whether this is the first time a definition is visited or not (i.e. skip
	 * or descend).
	 */
	protected boolean isFirstVisit(StructureDefinition definition) {
		return definition != currentlyVisiting;
	}

	/**
	 * Get the Class definition corresponding to the node.
	 */
	protected Class getDefinitionFor(ClassDef node) {
		return (Class) definitionForNode.get(node);
	}
	
	/**
	 * Get the Function definition corresponding to the node.
	 */
	protected Function getDefinitionFor(FunctionDef node) {
		return (Function) definitionForNode.get(node);
	}

	/**
	 * This should be called from within a visit method the second time a
	 * definition is visited to descend into it.
	 */
	protected void visitChildren(StructureDefinition definition)
			throws Exception {
		for (StructureDefinition child : definition.getChildren()) {
			visitChild(child);
		}
	}

	private void visitChild(StructureDefinition child) throws Exception {
		for (StructureDefinition def : child.getChildren()) {
			definitionForNode.put(def.getNode(), def);
		}
		currentlyVisiting = child;
		child.getNode().accept(this);
	}

}
