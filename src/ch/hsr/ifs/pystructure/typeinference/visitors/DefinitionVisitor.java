/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;
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

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AttributeUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ExceptDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
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
	
	private ModuleScope moduleScope;
	private Stack<Block> blocks;
	
	private List<Use> uses;

	public DefinitionVisitor(Module module) {
		this.module = module;
		
		this.moduleScope = new ModuleScope(new BuiltInScope(), module);
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
		module.getDefinitions().addAll(moduleScope.getCurrentBlockDefinitions());

		return null;
	}

	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = getDefinitionFor(node);
		
		if (isFirstVisit(klass)) {
			addDefinition(klass);
		} else {
			blocks.push(new Scope(getBlock(), klass));
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
			if (function instanceof Method) {
				Class klass = (Class) getScope().getDefinition();
				klass.addMethod((Method) function);
			}
			/*
			 * TODO: What about this?
			 * 
			 * global func
			 * def func():
			 *     print "I'm global"
			 */
			addDefinition(function);
		} else {
			Scope functionScope = new Scope(getBlock(), function);
			blocks.push(functionScope);
			addArgumentDefinitions(node.args, function);
			super.visitFunctionDef(node);
			visitChildren(function);
			blocks.pop();
		}

		return null;
	}

	private void addArgumentDefinitions(argumentsType args,
			Function function) {
		int firstDefault = args.args.length - args.defaults.length;
		for (int position = 0; position < args.args.length; ++position) {
			exprType argument = args.args[position];
			if (argument instanceof Name) {
				NameAdapter name = new NameAdapter((Name) argument);
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
			Map<NameAdapter, Value> values = getValues(target, node.value);
			for (Entry<NameAdapter, Value> entry : values.entrySet()) {
				NameAdapter name = entry.getKey();
				Definition d = new AssignDefinition(module, name, node, entry.getValue());
				addDefinition(d);
			}
			target.accept(this);
			target.parent = node;
		}
		return null;
	}

	// TODO: Move this out of DefinitionVisitor
	private Map<NameAdapter, Value> getValues(exprType target, exprType value) {
		Map<NameAdapter, Value> values = new HashMap<NameAdapter, Value>();
		LinkedList<Integer> indexes = new LinkedList<Integer>();
		getValues(values, indexes, target, value);
		return values;
	}
	
	private void getValues(Map<NameAdapter, Value> values, LinkedList<Integer> indexes, exprType target, exprType value) {
		if (target instanceof Tuple) {
			Tuple tuple = (Tuple) target;
			for (int i = 0; i < tuple.elts.length; i++) {
				exprType element = tuple.elts[i];
				indexes.addLast(i);
				getValues(values, indexes, element, value);
				indexes.removeLast();
			}
		} else if (target instanceof Name) {
			NameAdapter name = new NameAdapter((Name) target);
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
		NameUse use = new NameUse(new NameAdapter(node), module);
		addNameUse(use);
		super.visitName(node);
		return null;
	}

	@Override
	public Object visitNameTok(NameTok node) throws Exception {
		NameUse use = new NameUse(new NameAdapter(node), module);
		addNameUse(use);
		super.visitNameTok(node);
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
		for (NameTokType name : node.names) {
			getScope().setGlobal(new NameAdapter(name));
			name.accept(this);
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

		parent.addToCurrentDefinitions(bodyBlock.getCurrentBlockDefinitions());
		parent.addToCurrentDefinitions(orelseBlock.getCurrentBlockDefinitions());

		return null;
	}

	@Override
	public Object visitFor(For node) throws Exception {
		Block parent = getBlock();

		node.iter.accept(this);

		Block bodyBlock = new Block(parent);

		// TODO: What about tuples?
		if (node.target instanceof Name) {
			NameAdapter name = new NameAdapter((Name) node.target);
			Definition definition = new LoopVariableDefinition(module, name, node);
			bodyBlock.setCurrentDefinition(definition);
			parent.addToCurrentDefinitions(definition);
		}

		node.target.accept(this);

		visitBlock(bodyBlock, node.body);
		Block orelseBlock = new Block(parent);
		// Definitions may flow from the body to the else block.
		orelseBlock.addToCurrentDefinitions(bodyBlock.getCurrentBlockDefinitions());
		visitBlock(orelseBlock, node.orelse);

		parent.addToCurrentDefinitions(bodyBlock.getCurrentBlockDefinitions());
		parent.addToCurrentDefinitions(orelseBlock.getCurrentBlockDefinitions());

		return null;
	}

	@Override
	public Object visitWhile(While node) throws Exception {
		Block parent = getBlock();

		node.test.accept(this);

		Block bodyBlock = new Block(parent);
		Block orelseBlock = new Block(parent);
		visitBlock(bodyBlock, node.body);
		orelseBlock.addToCurrentDefinitions(bodyBlock.getCurrentBlockDefinitions());
		visitBlock(orelseBlock, node.orelse);

		parent.addToCurrentDefinitions(bodyBlock.getCurrentBlockDefinitions());
		parent.addToCurrentDefinitions(orelseBlock.getCurrentBlockDefinitions());

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
			handlerBlock.addToCurrentDefinitions(bodyBlock.getBlockDefinitions());

			// TODO: What about tuples?
			if (handler.name instanceof Name) {
				NameAdapter name = new NameAdapter((Name) handler.name);
				ExceptDefinition definition = new ExceptDefinition(module, name, handler);
				handlerBlock.setCurrentDefinition(definition);
			}

			visitBlock(handlerBlock, handler.body);
			parent.addToCurrentDefinitions(handlerBlock.getCurrentBlockDefinitions());
		}

		orelseBlock.addToCurrentDefinitions(bodyBlock.getBlockDefinitions());
		visitBlock(orelseBlock, node.orelse);

		// We need to add all definitions of the block, because we don't know
		// where the exception was thrown, it could be between two definitions
		// of the same name.
		parent.addToCurrentDefinitions(bodyBlock.getBlockDefinitions());
		parent.addToCurrentDefinitions(orelseBlock.getCurrentBlockDefinitions());

		return null;
	}

	@Override
	public Object visitTryFinally(TryFinally node) throws Exception {
		Block parent = getBlock();

		Block bodyBlock = new Block(parent);
		Block finallyBlock = new Block(parent);

		visitBlock(bodyBlock, node.body);
		finallyBlock.addToCurrentDefinitions(bodyBlock.getBlockDefinitions());
		visitBlock(finallyBlock, node.finalbody);

		parent.addToCurrentDefinitions(bodyBlock.getBlockDefinitions());
		// Overwrite the current definitions, because whether an exception
		// occurred or not.
		parent.setCurrentDefinition(finallyBlock.getCurrentBlockDefinitions());

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
			getBlock().setCurrentDefinition(definition);
		}
	}

	private void addNameUse(NameUse use) {
		List<Definition> definitions = getBlock().getDefinitions(use.getName());
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
				
				// TODO: Create ModulePath class or something like that
				NameAdapter path = new NameAdapter(entry.name);
				NameAdapter name = new NameAdapter(path.getId().split("\\.", 2)[0]);
				definition = new ImportDefinition(module, node, name, null, name);
			} else {
				/* import package.module as alias  # alias -> package.module */
				
				NameAdapter alias = new NameAdapter(entry.asname);
				NameAdapter path = new NameAdapter(entry.name);
				definition = new ImportDefinition(module, node, path, null, alias);
			}
			
			addDefinition(definition);
		}
		
		return super.visitImport(node);
	}

	/*
	 * Examples:
	 * 
	 * from pkg.module import Class
	 * from pkg.module import Class as C1, ClassTwo as C2
	 * from pkg.module import *  # grrr
	 * from pkg import module
	 * from pkg import *  # doesn't import all modules, but everything in __init__.py
	 * from .module import Class
	 * from . import module
	 * 
	 * TODO: Support wildcard imports
	 */
	@Override
	public Object visitImportFrom(ImportFrom node) throws Exception {
		NameAdapter path = new NameAdapter(node.module);
		
		for (aliasType entry : node.names) {
			NameAdapter element = new NameAdapter(entry.name);
			NameAdapter alias = new NameAdapter(entry.asname == null ? entry.name : entry.asname);
			
			ImportDefinition definition = new ImportDefinition(module, node, path, element, alias, node.level);
			addDefinition(definition);
			
			if (entry.asname != null) {
				/*
				 * In the following case, we have to add a name use for Class,
				 * because otherwise it can't be found by reference finders:
				 * 
				 * from module import Class as Alias
				 */
				NameUse use = new NameUse(element, this.module);
				use.addDefinition(definition);
				addNameUse(use);
			}
		}
		
		return super.visitImportFrom(node);
	}

}
