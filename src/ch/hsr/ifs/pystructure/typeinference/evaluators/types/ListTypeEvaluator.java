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
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for evaluating the type of list displays:
 * 
 * l = [42, 3.14]
 * l[0] ## type float|int
 * 
 * The special thing here is that the elements (42, 3.14) in the literal
 * constructor need to be made available so they will be found.
 */
public class ListTypeEvaluator extends DisplayTypeEvaluator {

	private final org.python.pydev.parser.jython.ast.List list;

	public ListTypeEvaluator(ExpressionTypeGoal goal, org.python.pydev.parser.jython.ast.List list) {
		super(goal);
		this.list = list;
	}

	@Override
	public List<IGoal> init() {
		createClassType("list", list);
		
		Workspace workspace = getGoal().getContext().getWorkspace();
		Module module = getGoal().getContext().getModule();
		
		for (exprType element : list.elts) {
			Call call = NodeUtils.createMethodCall(list, "append", element);
			AttributeReference r = new AttributeReference("append", resultType, call.func, module);
			workspace.addPossibleAttributeReference(r);
		}
		
		return IGoal.NO_GOALS;
	}

}
