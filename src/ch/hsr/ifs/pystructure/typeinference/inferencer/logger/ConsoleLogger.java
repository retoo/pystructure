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

import java.io.PrintStream;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class ConsoleLogger extends GoalTreeLogger {
	static final PrintStream OUT = System.out;
	
	public ConsoleLogger() {
	}
	
	public void evaluationFinished(IGoal rootGoal) {
		super.evaluationFinished(rootGoal);
		OUT.println("Evaluation finished of " + rootGoal);
	}

	public void evaluationStarted(IGoal rootGoal) {
		super.evaluationStarted(rootGoal);
		OUT.println("Evaluation started of " + rootGoal);		
	}
	
	@Override
	public void goalCreated(IGoal goal, AbstractEvaluator creator,
			AbstractEvaluator evaluator) {
		super.goalCreated(goal, creator, evaluator);
		
		say(creator, evaluator, "Created " + evaluator.getClass().getSimpleName() + " "  + goal);

	}
	
	@Override
	protected void goalFinished(IGoal goal, AbstractEvaluator creator,
			AbstractEvaluator evaluator) {
		super.goalFinished(goal, creator, evaluator);
		
		say(creator, evaluator, "Finished " + goal);
	}
	
	
	private void say(AbstractEvaluator creator, AbstractEvaluator evaluator, String text) {
		int level = level(evaluator);
		int idParent = getNumber(creator);
		int id = getNumber(evaluator);
		
		OUT.print(StringUtils.multiply(level, "|   ")
				+ idParent
				+ " " + id
				+ " " + text
				+ "\n");		
	}
	
	
	public void shutdown() {
		OUT.println("Engine finished");
	}

}
