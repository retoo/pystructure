/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.playground;

import java.io.File;

import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public final class SimpleExample {

	private SimpleExample() {
	}
	
	public static void main(String[] args) {
		PythonTypeInferencer inferencer = new PythonTypeInferencer();
		
		File sourceDir = new File("s101g/examples/simple/");
		Workspace workspace = new Workspace(sourceDir);
		Module module = workspace.getModule("simple");
		
		// Try 10 or 14, the other lines are no expressions, they're statements.
		int line = 13;
		
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		Expr expression = visitor.run(module.getNode());
		
		if (expression == null) {
			System.out.println("No expression at line " + line);
		} else {
			// The next line is where the *real work* happens :)
			IType type = inferencer.evaluateType(workspace, module, expression.value);
			System.out.println("Type of expression at line " + line + " is: " + type);
		}
	}

}
