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

package ch.hsr.ifs.pystructure.typeinference.goals.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public interface IGoal {

	/*
	 * we use 'unmodifiableList' to forbid any modofictions to NO_GOALS (not
	 * sure if anybody would be that stupid, but who knows
	 */
	List<IGoal> NO_GOALS = Collections.unmodifiableList(new ArrayList<IGoal>());

	/**
	 * Returns context, in which this goal should be considered. Context
	 * contains, for example, the instance of the class a method is called of,
	 * precalculated scope or something like that.
	 *
	 * @return The context of this goal, or <code>null</code> is there is
	 *         none.
	 */
	ModuleContext getContext();
	
}
