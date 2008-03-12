/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;

public class MethodReference extends FunctionReference {
	private boolean firstArgumentIsImplicit;

	public MethodReference(Method method, SimpleNode node) {
		this(method, node, true);
	}
	
	public MethodReference(Method method, SimpleNode node, boolean firstArgumentIsImplicit) {
		super(method, node);
		this.firstArgumentIsImplicit = firstArgumentIsImplicit;
	}

	@Override
	public exprType getArgumentExpression(Argument argument) {
		return super.getArgumentExpression(argument, firstArgumentIsImplicit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (firstArgumentIsImplicit ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} 
		
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final MethodReference other = (MethodReference) obj;
		if (firstArgumentIsImplicit != other.firstArgumentIsImplicit) {
			return false;
		}
		return true;
	}
	
}
