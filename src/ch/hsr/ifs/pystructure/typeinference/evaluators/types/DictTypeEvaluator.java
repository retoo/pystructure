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
import org.python.pydev.parser.jython.ast.Dict;

import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for evaluating the type of dict displays.
 */
public class DictTypeEvaluator extends DisplayTypeEvaluator {

	private final Dict dict;

	public DictTypeEvaluator(ExpressionTypeGoal goal, Dict dict) {
		super(goal);
		this.dict = dict;
	}

	@Override
	public List<IGoal> init() {
		createClassType("dict");
		
		Workspace workspace = getGoal().getContext().getWorkspace();
		Module module = getGoal().getContext().getModule();
		
		for (int i = 0; i < dict.keys.length; i++) {
			Call call = NodeUtils.createMethodCall(dict, "__setitem__", dict.keys[i], dict.values[i]);
			AttributeReference r = new AttributeReference("__setitem__", resultType, call.func, module);
			workspace.addPossibleAttributeReference(r);
		}
		
		return IGoal.NO_GOALS;
	}

}
