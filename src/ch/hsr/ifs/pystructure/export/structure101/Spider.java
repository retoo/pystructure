/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.export.structure101;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.Yield;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.StructuralVisitor;

public class Spider extends StructuralVisitor {

	private static final Set<java.lang.Class<? extends SimpleNode>> IGNORE = new HashSet<java.lang.Class<? extends SimpleNode>>();

	static {
		IGNORE.add(Tuple.class);
		IGNORE.add(Yield.class);
	}

	private final Map<StructureDefinition, List<exprType>> typables;

	public Spider() {
		typables = new HashMap<StructureDefinition, List<exprType>>();
	}

	public void run(Module module) {
		super.run(module);
	}

	public Map<StructureDefinition, List<exprType>> getTypables() {
		return typables;
	}

	@Override
	public Object visitModule(org.python.pydev.parser.jython.ast.Module node)
	throws Exception {
		Module module = getDefinitionFor(node);
		super.visitModule(node);
		visitChildren(module);
		return null;
	}

	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = getDefinitionFor(node);
		if (isFirstVisit(klass)) {
			return null;
		}
		super.visitClassDef(node);
		visitChildren(klass);
		return null;
	}

	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		Function function = getDefinitionFor(node);
		if (isFirstVisit(function)) {
			return null;
		}
		super.visitFunctionDef(node);
		visitChildren(function);
		return null;
	}

	@Override
	public void traverse(SimpleNode node) throws Exception {
		if (node instanceof exprType) {

			if (IGNORE.contains(node.getClass())) {
				// Ignore it.
			} else {
				exprType expression = (exprType) node;

				StructureDefinition definition = getCurrentStructureDefinition();

				List<exprType> list = typables.get(definition);

				if (list == null) {
					list = new ArrayList<exprType>();
					typables.put(definition, list);
				}

				list.add(expression);
			}
		}
		node.traverse(this);
	}
}
