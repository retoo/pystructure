package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class ConstructorReference extends MethodReference {

	public ConstructorReference(Method method, exprType classExpression, Module module) {
		super(method, classExpression, module);
	}
	
	@Override
	public exprType getArgumentExpression(Argument argument) {
		if (argument.getPosition() == 0) {
			Call call = NodeUtils.getCallForFunc(getExpression());
			return call;
		} else {
			return super.getArgumentExpression(argument, true);
		}
	}

}
