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

import java.util.Arrays;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class CombinedLogger implements IGoalEngineLogger {
	private final List<IGoalEngineLogger> loggers;

	public CombinedLogger(IGoalEngineLogger... loggers) {
		this.loggers = Arrays.asList(loggers);
	}
	

	public void evaluationFinished(IGoal rootGoal) {
		for (IGoalEngineLogger logger : loggers) {
			logger.evaluationFinished(rootGoal);
		}
	}

	public void evaluationStarted(IGoal rootGoal) {
		for (IGoalEngineLogger logger : loggers) {
			logger.evaluationStarted(rootGoal);
		}
	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		for (IGoalEngineLogger logger : loggers) {
			logger.goalCreated(goal, creator, evaluator);
		}
	}

	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
		for (IGoalEngineLogger logger : loggers) {
			logger.goalFinished(goal, evaluator);
		}
	}

	public void shutdown() {
		for (IGoalEngineLogger logger : loggers) {
			logger.shutdown();
		}
	}

}
