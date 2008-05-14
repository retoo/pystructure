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

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.For;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Global;
import org.python.pydev.parser.jython.ast.If;
import org.python.pydev.parser.jython.ast.Import;
import org.python.pydev.parser.jython.ast.ImportFrom;
import org.python.pydev.parser.jython.ast.Index;
import org.python.pydev.parser.jython.ast.Lambda;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTokType;
import org.python.pydev.parser.jython.ast.Subscript;
import org.python.pydev.parser.jython.ast.TryExcept;
import org.python.pydev.parser.jython.ast.TryFinally;
import org.python.pydev.parser.jython.ast.While;
import org.python.pydev.parser.jython.ast.aliasType;
import org.python.pydev.parser.jython.ast.argumentsType;
import org.python.pydev.parser.jython.ast.excepthandlerType;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.sliceType;
import org.python.pydev.parser.jython.ast.stmtType;
import org.python.pydev.parser.jython.ast.suiteType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AliasedImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AttributeUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ExceptDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Block;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.ExternalScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.ModuleScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Scope;

public class DefinitionVisitor extends StructuralVisitor {

	private final Module module;
	
	private final ExternalScope externalScope;
	private final ModuleScope moduleScope;
	private final Stack<Block> blocks;
	
	private final List<Use> uses;


	public DefinitionVisitor(Module module) {
		this.module = module;
		
		this.externalScope = new ExternalScope(module);
		this.moduleScope = new ModuleScope(externalScope);
		module.setModuleScope(moduleScope);
		
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

	/*
	 * Assign statements like these:
	 * 
	 * a = 1
	 * b, c = 2, 3
	 * d, e = function_returning_tuple()
	 */
	@Override
	public Object visitAssign(Assign node) throws Exception {
		node.value.accept(this);
		node.value.parent = node;
		for (exprType target : node.targets) {
			Map<exprType, exprType> values = NodeUtils.createTupleElementAssignments(target, node.value);
			for (Entry<exprType, exprType> entry : values.entrySet()) {
				exprType targetPart = entry.getKey();
				exprType value = entry.getValue();
				if (targetPart instanceof Name) {
					String name = ((Name) targetPart).id;
					Definition d = new AssignDefinition(module, name, node, value);
					addDefinition(d);
				} else if (targetPart instanceof Subscript) {
					processSubscriptAssignment(targetPart, value);
				}
			}
			target.accept(this);
			target.parent = node;
		}
		return null;
	}

	/**
	 * d["key"] = 42  →  d.__setitem__("key", 42)
	 */
	private void processSubscriptAssignment(exprType target, exprType value) throws Exception {
		Subscript subscript = (Subscript) target;
		exprType receiver = subscript.value;
		sliceType slice = subscript.slice;
		if (slice instanceof Index) {
			exprType key = ((Index) slice).value;
			exprType[] arguments = new exprType[] {key, value};
			Call setitemCall = NodeUtils.createMethodCall(receiver, "__setitem__", arguments);
			setitemCall.accept(this);
		}
	}

	/*
	 * Node for bare names (variables), for example the "instance" of
	 * "instance.method" but not the "method" (which is an attribute).
	 */
	@Override
	public Object visitName(Name node) throws Exception {
		addNameUse(node);
		super.visitName(node);
		return null;
	}

	/*
	 * Examples:
	 * 
	 * module.function
	 * instance.method
	 */
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
			Definition loopVariable = new LoopVariableDefinition(module, name, node, node.iter);
			bodyBlock.setDefinition(loopVariable);
			parent.addDefinition(loopVariable);
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

	private void addNameUse(Name node) {
		NameUse nameUse = new NameUse(node.id, node, module);
		String name = nameUse.getName();
		List<Definition> definitions = getBlock().getCurrentDefinitions(name);
		
		/* Check if this nameuse is alised by the import definieren (from module import Class as Alias). If 
		 * this is the case we register another NameUse using the 'Alias' as name. This enables the 
		 * PotentialReferences Evaluator to find the name, even when it is aliased.  
		 */
		for (Definition def : definitions) {
			if (def instanceof AliasedImportDefinition) {
				AliasedImportDefinition id = (AliasedImportDefinition) def;
				
				NameUse aliasedNameUse = new NameUse(id.getElement(), node, module);
				aliasedNameUse.addDefinition(id);	
				
				uses.add(aliasedNameUse);
			}
		}
		
		nameUse.addDefinitions(definitions);
		if (getScope() == moduleScope || getScope().isGlobal(name)) {
			moduleScope.addGlobalNameUse(nameUse);
		}
		uses.add(nameUse);
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
				ImportPath importPath = new ImportPath(module, name, 0);
				definition = new ImportDefinition(module, node, importPath, null, name.toString());
			} else {
				/* import package.module as alias  # alias -> package.module */
				
				String alias = NodeUtils.getId(entry.asname);
				NamePath path = new NamePath(NodeUtils.getId(entry.name));
				ImportPath importPath = new ImportPath(module, path, 0);
				definition = new ImportDefinition(module, node, importPath, null, alias);
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
	 */
	@Override
	public Object visitImportFrom(ImportFrom node) throws Exception {
		NamePath path = new NamePath(NodeUtils.getId(node.module));
		ImportPath importPath = new ImportPath(module, path, node.level);
		
		if (node.names.length == 0) {
			/* This is a "star" import (from module import *) */
			externalScope.addImportStarPath(importPath);
			
		} else {
			for (aliasType entry : node.names) {
				String element = NodeUtils.getId(entry.name);
				
				ImportDefinition definition;
				if (entry.asname != null) {
					definition = new AliasedImportDefinition(module, node, importPath, element, NodeUtils.getId(entry.asname));
				} else {
					definition = new ImportDefinition(module, node, importPath, element, element);
				}
				
				addDefinition(definition);
			}
		}
		
		return null;
	}

}
