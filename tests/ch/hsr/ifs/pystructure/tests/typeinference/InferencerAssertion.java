package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;

import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

public abstract class InferencerAssertion extends Assert {
	
	public class InferencerAssertionError extends AssertionFailedError {

		private static final long serialVersionUID = 1L;
		
		public InferencerAssertionError(String type, String expected,
				String actual, String expression, String filename, int line) {
			
			super(type + " of <" + expression + ">: expected <" + expected 
					+ "> but was <" + actual 
					+ "> (" + filename + ":" + line + ")");
		}

	}

	public final String filename;
	public final String expression;
	public final int line;


	public InferencerAssertion(String fileName, String expression, int line) {
		this.filename = fileName;
		this.expression = expression;
		assertNotNull(expression);
		this.line = line;
	}

	public void check(File file, PythonTypeInferencer inferencer, Workspace workspace) {
		Module module = workspace.getModule(file);
		
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		Expr expression = visitor.run(module.getNode());
		
		if (expression == null) {
			throw new RuntimeException("Unable to find node for expresssion '" + this.expression + "'");
		}
		
		doIt(inferencer, workspace, module, expression);
	}

	protected abstract void doIt(PythonTypeInferencer inferencer, Workspace workspace,
			Module module, Expr expression);

}
