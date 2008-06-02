/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.util.Stack;

import org.python.pydev.parser.jython.Visitor;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;

/**
 * Visitor which goes over the source code and creates the hierarchy of
 * structure definitions (Class, Function) as they appear in the source code,
 * without knowing under what names they will eventually be accessible at
 * runtime.
 */
public class StructureDefinitionVisitor extends Visitor {

	private Module module;
	private final Stack<StructureDefinition> parents;

	public StructureDefinitionVisitor() {
		this.parents = new Stack<StructureDefinition>();
	}

	public void run(Module module) {
		try {
			this.module = module;
			parents.push(module);
			module.getNode().accept(this);
			parents.pop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = new Class(NodeUtils.getId(node.name), node, module);
		parents.peek().addChild(klass);

		parents.push(klass);
		super.visitClassDef(node);
		parents.pop();
		return null;
	}

	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		StructureDefinition parent = parents.peek();
		StructureDefinition newParent;

		String name = NodeUtils.getId(node.name);
		if (parent instanceof Class) {
			// TODO: What about staticmethod, etc.?
			Class klass = (Class) parent;
			Method method = new Method(module, name, node, klass);
			klass.addMethod(method);
			parent.addChild(method);

			newParent = method;
		} else {
			Function function = new Function(module, name, node);
			parent.addChild(function);

			newParent = function;
		}

		parents.push(newParent);
		super.visitFunctionDef(node);
		parents.pop();
		return null;
	}

}
