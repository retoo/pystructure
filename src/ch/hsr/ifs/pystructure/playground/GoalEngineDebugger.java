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

package ch.hsr.ifs.pystructure.playground;

import java.io.File;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CombinedLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.ConsoleLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public final class GoalEngineDebugger {
	
	private GoalEngineDebugger() {
	}
	
	public static void main(String[] args) {
		Structure101Logger s101 = new Structure101Logger();
		s101.testStarted("simple", "print x", 12);
		IGoalEngineLogger logger = new CombinedLogger(new ConsoleLogger(), new StatsLogger(), s101);
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(logger);
		File path = new File("s101g/examples/simple/");
		Workspace workspace = new Workspace(path);
		Module module = workspace.getModule("simple");
		
		int[] lines = {13};
		
		for (int line : lines) {
			Expr expression = getExpressionAtLine(module, line);
			IType type = inferencer.evaluateType(workspace, module, expression.value);
			
			System.out.println("Type is: " + type);
		}
		
		inferencer.shutdown();
	}

	private static Expr getExpressionAtLine(Module module, int line) {
		SimpleNode rootNode = module.getNode();
		
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		
		try {
			visitor.traverse(rootNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Expr expression = visitor.getExpression();

		if (expression == null) {
			throw new RuntimeException("Unable to find expression on line " + line);
		}
		return expression;
	}

}
