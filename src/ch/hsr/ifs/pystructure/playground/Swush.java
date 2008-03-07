package ch.hsr.ifs.pystructure.playground;

import java.util.LinkedList;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pysucture.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class Swush {
	public static void main(String[] args) {
		LinkedList<String> sysPath = new LinkedList<String>();

		PythonTypeInferencer inferencer = new PythonTypeInferencer();
		String path = "/Users/reto/scripts/pydoku";
		Workspace workspace = new Workspace(path, sysPath);
		
		String name = "pydoku";
		
		Module module = workspace.getModule(name);
		
//		Expr expression = getExpressionAtLine(module, 7);
		
		PythonContext context = new PythonContext(workspace, module);
		
		
		for (Definition<?> x : module.getDefinitions()) {
			if (x instanceof AssignDefinition) {
				AssignDefinition assign = (AssignDefinition) x;
				
				exprType node = assign.getNode().targets[0];
				
				ExpressionTypeGoal goal = new ExpressionTypeGoal(context, node);
				IEvaluatedType type = inferencer.evaluateType(goal, -1);
				if (type == null) {
					System.out.println("Type inferencer returned null for " + x + " / " + x.getNode());
				} else {
					System.out.println(" type " + type.getTypeName()) ;
				}
			}
		}
		
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
			throw new RuntimeException("Unable to find node for expresssion '" + expression + "'");
		}
		return expression;
	}
}
