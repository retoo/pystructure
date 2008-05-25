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

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.ListComp;

import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for evaluating the type of list comprehensions:
 * 
 * l = [x for x in range(4)]
 * l[0] ## type int
 */
public class ListCompTypeEvaluator extends DisplayTypeEvaluator {

	private final ListComp listComp;

	public ListCompTypeEvaluator(ExpressionTypeGoal goal, ListComp listComp) {
		super(goal);
		this.listComp = listComp;
	}

	@Override
	public List<IGoal> init() {
		createClassType("list", listComp);
		
		Workspace workspace = getGoal().getContext().getWorkspace();
		Module module = getGoal().getContext().getModule();
		
		Call call = NodeUtils.createMethodCall(listComp, "append", listComp.elt);
		AttributeReference r = new AttributeReference("append", resultType, call.func, module);
		workspace.addPossibleAttributeReference(r);
		
		return IGoal.NO_GOALS;
	}

}
