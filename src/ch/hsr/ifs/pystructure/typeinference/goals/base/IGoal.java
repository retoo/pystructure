package ch.hsr.ifs.pystructure.typeinference.goals.base;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public interface IGoal {

	List<IGoal> NO_GOALS = new ArrayList<IGoal>();

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
