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

package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

/**
 * Generic interface for loggers of the goal engine. The logger is being used
 * during the whole lifetime of the type inferencer.
 * 
 * For a goal tree aware logger see {@link GoalTreeLogger}.
 */
public interface IGoalEngineLogger {

	/**
	 * Hook which gets called when a new evaluation has been started. This 
	 * is the root goal (quite often an ExpressionTypeGoal) which then causes
	 * all the subgoals to be created. 
	 * 
	 * @param goal the initial root goal submitted by the user of the library
	 */
	void evaluationStarted(IGoal goal);
	
	
	/**
	 * Hook which gets called when an evaluation has been finished. This 
	 * 
	 * @param goal the initial goal which is now solved
	 */
	void evaluationFinished(IGoal goal);

	/**
	 * Hook which gets called when a subgoal was been created by one of the evaluators.
	 * 
	 * @param goal The newly created goal 
	 * @param creator The original evaluator which created that particular goal
	 * @param evaluator The evaluator which is being used to process this goal
	 */
	void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator);
	/**
	 * Hook which gets called when a subgoal has been finished (successfully or not)
	 * 
	 * @param goal Goal which got finished (successfully or not)
	 * @param evaluator Evaluator which was responsible for the processing of this goal
	 */
	void goalFinished(IGoal goal, AbstractEvaluator evaluator);

	/**
	 * Hook when the inferencer is about to shutdown.
	 */
	void shutdown();

}
