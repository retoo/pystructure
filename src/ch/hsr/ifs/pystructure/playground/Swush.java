package ch.hsr.ifs.pystructure.playground;

import java.util.LinkedList;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CombinedLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.ConsoleLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public final class Swush {
	
	private Swush() {
	}
	
	public static void main(String[] args) {
		LinkedList<String> sysPath = new LinkedList<String>();

		IGoalEngineLogger logger = new CombinedLogger(new ConsoleLogger(), new StatsLogger());
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(logger);
		String path = "s101g/examples/simple/";
		Workspace workspace = new Workspace(path, sysPath);
		Module module = workspace.getModule("simple");
		
		int[] lines = {13, 14, 15, 16};
		
		for (int line : lines) {
			Expr expression = getExpressionAtLine(module, line);
			
			ModuleContext context = new ModuleContext(workspace, module);
			ExpressionTypeGoal goal = new ExpressionTypeGoal(context, expression.value);
			IEvaluatedType type = inferencer.evaluateType(goal, -1);
			
			System.out.println("Type is: " + type);
			
		}
		
		inferencer.shutdown();
		
		
//		for (Definition<?> x : module.getDefinitions()) {
//			if (x instanceof AssignDefinition) {
//				AssignDefinition assign = (AssignDefinition) x;
//				
//				exprType node = assign.getNode().targets[0];
//				
//				ExpressionTypeGoal goal = new ExpressionTypeGoal(context, node);
//				IEvaluatedType type = inferencer.evaluateType(goal, -1);
//				if (type == null) {
//					System.out.println("Type inferencer returned null for " + x + " / " + x.getNode());
//				} else {
//					System.out.println(" type " + type.getTypeName()) ;
//				}
//			}
//		}
		
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
