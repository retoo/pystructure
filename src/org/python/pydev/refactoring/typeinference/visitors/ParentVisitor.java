/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.visitors;

import java.util.Stack;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.VisitorBase;

public class ParentVisitor extends VisitorBase {
	protected Stack<SimpleNode> stack = new Stack<SimpleNode>();
	
	public ParentVisitor() {
		stack.push(null);
	}
	
	@Override
	public void traverse(SimpleNode node) throws Exception {
		node.parent = stack.peek();
		stack.push(node);
		node.traverse(this);
		stack.pop();
	}

	@Override
	protected Object unhandled_node(SimpleNode node) throws Exception {
		return null;
	}
}
