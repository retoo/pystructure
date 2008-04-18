package ch.hsr.ifs.pystructure.tests.utils;

import org.python.pydev.parser.jython.ParseException;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.Module;

import ch.hsr.ifs.pystructure.parser.Parser;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import junit.framework.TestCase;

public class NodeUtilsTest extends TestCase {
	
	public void testIsCalledFunction() throws ParseException {
		Module module = Parser.parse("module.func(arg)");
		Expr expr = (Expr) module.body[0];
		Call call = (Call) expr.value;
		call.func.parent = call;
		
		assertEquals(call, NodeUtils.getCallForFunc(call.func));
	}
	
}
