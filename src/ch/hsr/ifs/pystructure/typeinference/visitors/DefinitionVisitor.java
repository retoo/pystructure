/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import org.python.pydev.parser.jython.SimpleNode;
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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.IPackage;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Path;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Value;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Block;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.BuiltInScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.ModuleScope;
import ch.hsr.ifs.pystructure.typeinference.model.scopes.Scope;

public class DefinitionVisitor extends ParentVisitor {

	private ModuleScope moduleScope;
	private List<Use> uses;

	private Stack<Block> blocks;
	private Stack<List<SimpleNode>> nodesToVisitLater;
	private Map<SimpleNode, Definition> scopeDefinitions;
	private Module module;
	private List<ImportPath> importPaths;

	public DefinitionVisitor(List<ImportPath> importPaths, Module module) {
		this.module = module;
		this.importPaths = importPaths;
		init();
	}

	private void init() {
		uses = module.getContainedUses();
		blocks = new Stack<Block>();
		nodesToVisitLater = new Stack<List<SimpleNode>>();
		scopeDefinitions = new HashMap<SimpleNode, Definition>();
	}

	public void run() {
		try {
			module.getNode().accept(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object visitModule(org.python.pydev.parser.jython.ast.Module node) throws Exception {
		moduleScope = new ModuleScope(new BuiltInScope(), module);

		beginScope(moduleScope);
		super.visitModule(node);
		endScope();

		moduleScope.connectGlobals();
		module.getDefinitions().addAll(moduleScope.getCurrentBlockDefinitions());

		return null;
	}

	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		if (visitLater(node)) {
			NameAdapter name = new NameAdapter(node.name);
			Class klass = new Class(name, node, module);
			addDefinition(klass);
			return null;
		}

		beginScope(new Scope(getBlock(), scopeDefinitions.get(node)));
		super.visitClassDef(node);
		endScope();

		return null;
	}

	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		if (visitLater(node)) {
			NameAdapter name = new NameAdapter(node.name);
			if (getScope().getDefinition() instanceof Class) {
				Class klass = (Class) getScope().getDefinition();
				Method method = new Method(module, name, node, klass);
				klass.addMethod(method);
				addDefinition(method);
			} else {
				Definition parentDefinition = getScopeOf(name).getDefinition();
				addDefinition(new Function(module, name, node, parentDefinition));
			}
			return null;
		}

		Function function = (Function) scopeDefinitions.get(node);

		Scope functionScope = new Scope(getBlock(), function);
		beginScope(functionScope);
		addArgumentDefinitions(node.args, function);
		super.visitFunctionDef(node);
		endScope();

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

	private void beginScope(Scope scope) {
		blocks.push(scope);
		nodesToVisitLater.push(new ArrayList<SimpleNode>());
	}

	private void endScope() throws Exception {
		for (SimpleNode node : nodesToVisitLater.peek()) {
			node.accept(this);
		}
		nodesToVisitLater.pop();
		blocks.pop();
	}

	private boolean visitLater(SimpleNode node) {
		if (nodesToVisitLater.peek().contains(node)) {
			return false;
		} else {
			nodesToVisitLater.peek().add(node);
			return true;
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
		if (definition instanceof Function || definition instanceof Class) {
			scopeDefinitions.put(definition.getNode(), definition);
		}

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

    private Scope getScopeOf(NameAdapter name) {
		if (getScope().isGlobal(name)) {
			return moduleScope;
		} else {
			return getScope();
		}
	}

	public List<Use> getUses() {
		return uses;
	}

	/* module resolution stuff */

	@Override
	public Object visitImport(Import node) throws Exception {
		for (aliasType entry : node.names) {
			NameAdapter moduleName = new NameAdapter(entry.name);

			Path path = resolve(moduleName, 0);
			
			if (path == null) {
				return null;
			}

			if (entry.asname != null) {
				/* import package.module as Alias, registers module as Alias*/
				NameAdapter alias = new NameAdapter(entry.asname);
				PathElement module = path.top();

				if (module instanceof Module) {
					registerPackage(alias, module);
				} else {
					throw new RuntimeException("Import didn't import a module");
				}
			} else {
				/* import package.module registers package as package*/
				PathElement pkg = path.base();

				if (pkg instanceof Package || pkg instanceof Module) {
					registerPackage(pkg.getName(), pkg);
				} else {
					throw new RuntimeException("Import didn't import a package or a module.");
				}
			}
		}
		
		return super.visitImport(node);
	}

	@Override
	public Object visitImportFrom(ImportFrom node) throws Exception {
		NameAdapter moduleName = new NameAdapter(node.module);

		Path path = resolve(moduleName, node.level);
		
		if (path == null) {
			return null;
		}

		for (aliasType entry : node.names) {
			NameAdapter name = new NameAdapter(entry.name);
			NameAdapter alias = new NameAdapter(entry.asname == null ? entry.name : entry.asname);

			PathElement pe = path.top();

			
			/* Are we importing something from a package or a module
			 * import some-module from package
			 *  vs.
			 * import some-class from` package.module
			 * 
			 * TODO: it probably would be nicer and easier if we could handle these 
			 * cases without making a 'difference'
			 */
			if (pe instanceof Module) {
				Module module = (Module) pe;
				/* get the class/global variable in this particular module */
				Definition definition = module.getChild(name);
				
				if (definition == null) {
					throw new RuntimeException("Imported entitiy by a import from didn't exist in the module");
				}
			
				registerDefinition(alias, definition, module);
				
				if (entry.asname != null) {
					/* case: from module import Class as Alias
					 * We have to add a name use for Class, because otherwise it
					 * can't be found by reference finders. */
					NameUse use = new NameUse(name, this.module);
					use.addDefinition(definition);
					addNameUse(use);
				}
			} else if (pe instanceof Package) {
				Package pkg = (Package) pe;
				
				/* this case looks like: from a_package import module_or_package 
				 * the problem is that the module_or_package hasn't been loaded by the 
				 * resolve() call above, so we have to do that now.
				 * we discard the actual output, hopefully something was found, 
				 * otherwise we can't do anything against it anyway */
				pkg.lookFor(name.getId());
				
				PathElement child = pkg.getChild(name);
				registerPackage(alias, child);
			} else {
				throw new RuntimeException("ImportFrom doesn't import from a package or module");
			}
			
		}
		return super.visitImportFrom(node);
	}

	private void registerDefinition(NameAdapter alias, Definition def, Definition parent) {
		ImportDefinition definition = new ImportDefinition(module, alias, def, parent);
		addDefinition(definition);
	}

	private void registerPackage(NameAdapter alias, PathElement child) {
		ImportDefinition definition = new ImportDefinition(module, alias, child);
		addDefinition(definition);
	}
	
	private Path resolve(NameAdapter moduleName, int level) {
		/* first we try to look if it is a relative lookup*/
		IPackage parentPath = module.getPackage(); 

		if (level > 1) {
			for (int i = 1; i < level; i++) {
				parentPath = parentPath.getParent();
			}
		}
		
		Path found = parentPath.lookFor(moduleName.getId());

		if (found != null) {
			return found;
		}

		/* second, if we haven't found anything yet, walk through the sys.path */
		for (ImportPath importPath : importPaths) {
			found = importPath.lookFor(moduleName.getId());

			if (found != null) {
				return found;
			}
		}
		
		// Probably from Python standard library or built-in.
		// System.err.println("Warning: Unable to find " + moduleName + " imported by " + module.getPath());
		return null;
	}

}
