package ch.hsr.ifs.pystructure.tests.utils;

import org.python.pydev.parser.jython.ParseException;
import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.Module;

import ch.hsr.ifs.pystructure.parser.Parser;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import junit.framework.TestCase;

public class NodeUtilsTest extends TestCase {
	
	public void testGetId() throws ParseException {
		Module module = Parser.parse("module.func");
		Expr expr = (Expr) module.body[0];
		Attribute attribute = (Attribute) expr.value;
		assertEquals("func", NodeUtils.getId(attribute.attr));
	}
	
	public void testGetCallForFunc() throws ParseException {
		Module module = Parser.parse("module.func(arg)");
		Expr expr = (Expr) module.body[0];
		Call call = (Call) expr.value;
		call.func.parent = call;
		
		assertEquals(call, NodeUtils.getCallForFunc(call.func));
	}
	
	public void testGetPrettyPrinted() throws ParseException {
		Module module = Parser.parse("x = 42");
		Assign assign = (Assign) module.body[0];
		String expected = "Assign\n"
						+ "|   targets = \n"
						+ "|   |   Name\n"
						+ "|   |   |   id = x,\n"
						+ "|   |   |   ctx = Store\n"
						+ "|   |   \n"
						+ "|   ,\n"
						+ "|   value = Num\n"
						+ "|   |   n = 42,\n"
						+ "|   |   type = Int,\n"
						+ "|   |   num = 42\n"
						+ "|   \n"
						+ "";
		assertEquals(expected, NodeUtils.getPrettyPrinted(assign));
	}
	
}
