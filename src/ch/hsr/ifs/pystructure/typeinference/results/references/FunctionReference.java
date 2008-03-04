/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.keywordType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;

public class FunctionReference extends Reference {
	
	public FunctionReference(Function definition, SimpleNode node) {
		super(definition, node);
	}
	
	public exprType getArgumentExpression(Argument argument) {
		return getArgumentExpression(argument, false);
	}
	
	/**
	 * Given a Call node, extract the expression which is assigned to this argument.
	 * @param call
	 * @return argument expression
	 */
	protected exprType getArgumentExpression(Argument argument, boolean firstArgumentIsImplicit) {
		if (!(getNode().parent instanceof Call)) {
			return null;
		}
		Call call = (Call) getNode().parent;
		if (call.func != getNode()) {
			return null;
		}

		int position = argument.getPosition();
		if (firstArgumentIsImplicit) {
			// The first argument (self) does not occur in the call argument
			// list, so adjust the position.
			position -= 1;
		}

		// Is it passed as a positional argument?
		if (position < call.args.length) {
			return call.args[position];
		}

		// Is it passed as a keyword argument?
		for (keywordType keyword : call.keywords) {
			NameAdapter keywordName = new NameAdapter(keyword.arg);
			if (keywordName.equals(argument.getName())) {
				return keyword.value;
			}
		}

		// Return default value, may also return null.
		return argument.getDefaultValue();
	}
}
