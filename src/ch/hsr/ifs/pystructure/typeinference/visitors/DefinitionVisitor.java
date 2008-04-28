/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.For;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Global;
import org.python.pydev.parser.jython.ast.If;
import org.python.pydev.parser.jython.ast.Import;
import org.python.pydev.parser.jython.ast.ImportFrom;
import org.python.pydev.parser.jython.ast.Lambda;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTokType;
import org.python.pydev.parser.jython.ast.TryExcept;
import org.python.pydev.parser.jython.ast.TryFinally;
import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.While;
import org.python.pydev.parser.jython.ast.aliasType;
import org.python.pydev.parser.jython.ast.argumentsType;
import org.python.pydev.parser.jython.ast.excepthandlerType;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.stmtType;
import org.python.pydev.parser.jython.ast.suiteType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AttributeUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ExceptDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Value;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Block;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.BuiltInScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.ModuleScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Scope;

public class DefinitionVisitor extends StructuralVisitor {

	private final Module module;
	
	private final ModuleScope moduleScope;
	private final Stack<Block> blocks;
	
	private final List<Use> uses;

	public DefinitionVisitor(Module module) {
		this.module = module;
		
		this.moduleScope = new ModuleScope(new BuiltInScope());
		this.blocks = new Stack<Block>();
		this.uses = module.getContainedUses();
	}

	public void run() {
		try {
			super.run(module);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object visitModule(org.python.pydev.parser.jython.ast.Module node) throws Exception {
		blocks.push(moduleScope);
		super.visitModule(node);
		visitChildren(module);
		blocks.pop();

		moduleScope.connectGlobals();
		module.getDefinitions().addAll(moduleScope.getCurrentDefinitions());

		return null;
	}

	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = getDefinitionFor(node);
		
		if (isFirstVisit(klass)) {
			addDefinition(klass);
		} else {
			blocks.push(new Scope(getBlock()));
			super.visitClassDef(node);
			visitChildren(klass);
			blocks.pop();
		}

		return null;
	}

	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		Function function = getDefinitionFor(node);
		
		if (isFirstVisit(function)) {
			/*
			 * TODO: What about this?
			 * 
			 * global func
			 * def func():
			 *     print "I'm global"
			 */
			addDefinition(function);
		} else {
			Scope functionScope = new Scope(getBlock());
			blocks.push(functionScope);
			addArgumentDefinitions(node.args, function);
			super.visitFunctionDef(node);
			visitChildren(function);
			blocks.pop();
		}

		return null;
	}
	
	@Override
	public Object visitLambda(Lambda node) throws Exception {
		// TODO: Implement lambda properly
		return null;
	}

	private void addArgumentDefinitions(argumentsType args,
			Function function) {
		int firstDefault = args.args.length - args.defaults.length;
		for (int position = 0; position < args.args.length; ++position) {
			exprType argument = args.args[position];
			if (argument instanceof Name) {
				String name = ((Name) argument).id;
				exprType defaultValue = null;
				if (position >= firstDefault) {
					defaultValue = args.defaults[position - firstDefault];
				}
				addDefinition(new Argument(module, name, argument,
						position, defaultValue, function));
			}
		}
	}

	@Override
	public Object visitAssign(Assign node) throws Exception {
		node.value.accept(this);
		node.value.parent = node;
		for (exprType target : node.targets) {
			Map<String, Value> values = getValues(target, node.value);
			for (Entry<String, Value> entry : values.entrySet()) {
				String name = entry.getKey();
				Definition d = new AssignDefinition(module, name, node, entry.getValue());
				addDefinition(d);
			}
			target.accept(this);
			target.parent = node;
		}
		return null;
	}

	// TODO: Move this out of DefinitionVisitor
	private Map<String, Value> getValues(exprType target, exprType value) {
		Map<String, Value> values = new HashMap<String, Value>();
		LinkedList<Integer> indexes = new LinkedList<Integer>();
		getValues(values, indexes, target, value);
		return values;
	}
	
	private void getValues(Map<String, Value> values, LinkedList<Integer> indexes, exprType target, exprType value) {
		if (target instanceof Tuple) {
			Tuple tuple = (Tuple) target;
			for (int i = 0; i < tuple.elts.length; i++) {
				exprType element = tuple.elts[i];
				indexes.addLast(i);
				getValues(values, indexes, element, value);
				indexes.removeLast();
			}
		} else if (target instanceof Name) {
			String name = ((Name) target).id;
			Value v;
			if (indexes.size() == 0) {
				v = new Value(value);
			} else {
				v = new TupleElement(value, indexes);
			}
			values.put(name, v);
		}
	}
	
	@Override

	public Object visitName(Name node) throws Exception {
		NameUse use = new NameUse(node.id, node, module);
		addNameUse(use);
		super.visitName(node);
		return null;
	}

	@Override
	public Object visitAttribute(Attribute node) throws Exception {
		uses.add(new AttributeUse(node, module));
		node.parent = stack.peek();
		stack.push(node);
		// Don't visit node.attr, we don't want a NameUse for it.
		node.attr.parent = node;
		node.value.accept(this);
		stack.pop();
		return null;
	}

	@Override
	public Object visitGlobal(Global node) throws Exception {
		for (NameTokType nameTok : node.names) {
			String name = NodeUtils.getId(nameTok);
			getScope().setGlobal(name);
			nameTok.accept(this);
		}
		return null;
	}

	@Override
	public Object visitIf(If node) throws Exception {
		Block parent = getBlock();

		node.test.accept(this);

		Block bodyBlock = new Block(parent);
		Block orelseBlock = new Block(parent);
		visitBlock(bodyBlock, node.body);
		visitBlock(orelseBlock, node.orelse);

		parent.addCurrentDefinitions(bodyBlock);
		parent.addCurrentDefinitions(orelseBlock);

		return null;
	}

	@Override
	public Object visitFor(For node) throws Exception {
		Block parent = getBlock();

		node.iter.accept(this);

		Block bodyBlock = new Block(parent);

		// TODO: What about tuples?
		if (node.target instanceof Name) {
			String name = ((Name) node.target).id;
			Definition loopVariable = new LoopVariableDefinition(module, name, node);
			bodyBlock.setDefinition(loopVariable);
			parent.addDefinition(loopVariable);
			
			node.target.accept(this);
		}

		visitBlock(bodyBlock, node.body);
		Block orelseBlock = new Block(parent);
		// Definitions may flow from the body to the else block.
		orelseBlock.addCurrentDefinitions(bodyBlock);
		visitBlock(orelseBlock, node.orelse);

		parent.addCurrentDefinitions(bodyBlock);
		parent.addCurrentDefinitions(orelseBlock);

		return null;
	}

	@Override
	public Object visitWhile(While node) throws Exception {
		Block parent = getBlock();

		node.test.accept(this);

		Block bodyBlock = new Block(parent);
		Block orelseBlock = new Block(parent);
		visitBlock(bodyBlock, node.body);
		orelseBlock.addCurrentDefinitions(bodyBlock);
		visitBlock(orelseBlock, node.orelse);

		parent.addCurrentDefinitions(bodyBlock);
		parent.addCurrentDefinitions(orelseBlock);

		return null;
	}

	@Override
	public Object visitTryExcept(TryExcept node) throws Exception {
		Block parent = getBlock();

		Block bodyBlock = new Block(parent);
		Block orelseBlock = new Block(parent);

		visitBlock(bodyBlock, node.body);

		for (excepthandlerType handler : node.handlers) {
			Block handlerBlock = new Block(parent);
			handlerBlock.addBlockDefinitions(bodyBlock);

			// TODO: What about tuples?
			if (handler.name instanceof Name) {
				String name = ((Name) handler.name).id;
				ExceptDefinition definition = new ExceptDefinition(module, name, handler);
				handlerBlock.setDefinition(definition);
			}

			visitBlock(handlerBlock, handler.body);
			parent.addCurrentDefinitions(handlerBlock);
		}

		orelseBlock.addBlockDefinitions(bodyBlock);
		visitBlock(orelseBlock, node.orelse);

		// We need to add all definitions of the block, because we don't know
		// where the exception was thrown, it could be between two definitions
		// of the same name.
		parent.addBlockDefinitions(bodyBlock);
		parent.addCurrentDefinitions(orelseBlock);

		return null;
	}

	@Override
	public Object visitTryFinally(TryFinally node) throws Exception {
		Block parent = getBlock();

		Block bodyBlock = new Block(parent);
		Block finallyBlock = new Block(parent);

		visitBlock(bodyBlock, node.body);
		finallyBlock.addBlockDefinitions(bodyBlock);
		visitBlock(finallyBlock, node.finalbody);

		parent.addBlockDefinitions(bodyBlock);
		// Overwrite the current definitions, because we don't know whether an
		// exception occurred or not.
		for (Definition definition : finallyBlock.getCurrentDefinitions()) {
			parent.setDefinition(definition);
		}

		return null;
	}

	private void visitBlock(Block block, stmtType[] body) throws Exception {
		if (body == null) {
			return;
		}

		blocks.push(block);
		for (stmtType statement : body) {
			statement.accept(this);
		}
		blocks.pop();
	}

	private void visitBlock(Block block, suiteType suite) throws Exception {
		if (suite != null) {
			visitBlock(block, suite.body);
		}
	}

	private void addDefinition(Definition definition) {
		if (getScope().isGlobal(definition.getName())) {
			moduleScope.addGlobalDefinition(definition);
		} else {
			getBlock().setDefinition(definition);
		}
	}

	private void addNameUse(NameUse use) {
		List<Definition> definitions = getBlock().getCurrentDefinitions(use.getName());
		use.addDefinitions(definitions);
		if (getScope() == moduleScope || getScope().isGlobal(use.getName())) {
			moduleScope.addGlobalNameUse(use);
		}
		uses.add(use);
	}

	private Block getBlock() {
		return blocks.peek();
	}

	private Scope getScope() {
		Block scope = getBlock();
		while (!(scope instanceof Scope)) {
			scope = scope.getParent();
		}
		return (Scope) scope;
	}

	public List<Use> getUses() {
		return uses;
	}
	
	/* import stuff */

	/*
	 * Examples:
	 * 
	 * import pkg.module
	 * import pkg.module as module
	 * import pkg.module, pkg2.module2
	 * import pkg
	 * 
	 * Doesn't work:
	 * 
	 * import pkg.module.Class
	 */
	@Override
	public Object visitImport(Import node) throws Exception {
		for (aliasType entry : node.names) {
			ImportDefinition definition;
			
			if (entry.asname == null) {
				/* import package.module  # package -> package */
				
				NamePath fullPath = new NamePath(NodeUtils.getId(entry.name));
				NamePath name = new NamePath(fullPath.getFirstPart());
				definition = new ImportDefinition(module, node, name, null, name.toString());
			} else {
				/* import package.module as alias  # alias -> package.module */
				
				String alias = NodeUtils.getId(entry.asname);
				NamePath path = new NamePath(NodeUtils.getId(entry.name));
				definition = new ImportDefinition(module, node, path, null, alias);
			}
			
			addDefinition(definition);
		}
		
		return null;
	}

	/*
	 * Examples:
	 * 
	 * from pkg.module import Class
	 * from pkg.module import Class as C1, ClassTwo as C2
	 * from pkg.module import *  # grrr
	 * from pkg import module
	 * from pkg import subpkg
	 * from pkg import *  # doesn't import all modules, but everything in __init__.py
	 * from .module import Class
	 * from . import module
	 * 
	 * TODO: Support wildcard imports
	 */
	@Override
	public Object visitImportFrom(ImportFrom node) throws Exception {
		NamePath path = new NamePath(NodeUtils.getId(node.module));
		
		for (aliasType entry : node.names) {
			String element = NodeUtils.getId(entry.name);
			String alias = NodeUtils.getId(entry.asname == null ? entry.name : entry.asname);
			
			ImportDefinition definition = new ImportDefinition(module, node, path, element, alias, node.level);
			addDefinition(definition);
		}
		
		return null;
	}

}
