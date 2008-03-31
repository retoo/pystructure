package ch.hsr.ifs.pystructure.playground;

import java.io.File;

import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.StructuralVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.StructureDefinitionVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class TestStructuralVisitor extends StructuralVisitor {

	private final Module module;

	public TestStructuralVisitor(Module module) {
		this.module = module;
	}
	
	public void run() {
		super.run(module);
	}
	
	@Override
	public Object visitModule(org.python.pydev.parser.jython.ast.Module node)
			throws Exception {
		
		super.visitModule(node);
		visitChildren(module);
		
		return null;
	}
	
	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = getDefinitionFor(node);
		if (isFirstVisit(klass)) {
			System.out.println("definition of " + klass.getName());
		} else {
			System.out.println("visiting body of " + klass.getName());
			super.visitClassDef(node);
			visitChildren(klass);
		}
		
		return null;
	}
	
	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		Function function = getDefinitionFor(node);
		if (isFirstVisit(function)) {
			System.out.println("definition of " + function.getName());
		} else {
			System.out.println("visiting body of " + function.getName());
			super.visitFunctionDef(node);
			visitChildren(function);
		}
		
		return null;
	}

	public static void main(String[] args) {
		File path = new File("tests/python/typeinference/structure/");
		Workspace workspace = new Workspace(path);
		
		Module module = workspace.getModule("module");
		
		StructureDefinitionVisitor structureVisitor = new StructureDefinitionVisitor();
		structureVisitor.run(module);
		
		TestStructuralVisitor visitor = new TestStructuralVisitor(module);
		visitor.run();
	}

}
