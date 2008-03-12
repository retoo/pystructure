/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.inferencer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.FieldReferencesGoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.IGoalEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.MethodCallsGoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.NullGoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.FieldReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.MethodCallsGoal;

/**
 * <p>
 * Default DLTK type inferencing implementation, that uses ideas of
 * demand-driven analysis with a subgoal pruning (see GoalEngine class).Type
 * evaluation becomes a root goal for a GoalEngine.
 *
 * <p>
 * Cause this class is common, it doesn't provide lots of evaluators. Only
 * FieldReferencesGoalEvaluator and MethodCallsGoalEvaluator registered. Please,
 * look for their javadocs for more info.
 *
 * <p>
 * User can register evaluators via registerEvaluator() method. Also user are
 * able to provide custom evaluators factory, it will have higher priority, than
 * evaluators, registered via registerEvaluator() method.
 *
 */
public class DefaultTypeInferencer implements ITypeInferencer {

	private class MapBasedEvaluatorFactory implements IGoalEvaluatorFactory {

		public GoalEvaluator createEvaluator(IGoal goal) {
			Object evaluator = null;
			if (userFactory != null) {
				evaluator = userFactory.createEvaluator(goal);
				if (evaluator != null) {
					return (GoalEvaluator) evaluator;
				}
			}

			Class goalClass = goal.getClass();
			evaluator = evaluators.get(goalClass);
			if (evaluator == null || (!(evaluator instanceof Class))) {
				// throw new RuntimeException("No evaluator registered for "
				// + goalClass.getName() + " : " + goal);
//				String className = goalClass.getName();
//				if (DLTKCore.DEBUG) {
//					System.err.println("No evaluator registered for "
//							+ className.substring(className.lastIndexOf('.'))
//							+ ": " + goal + " - using NullGoalEvaluator");
//				}
				return new NullGoalEvaluator(goal);
			}
			Class evalClass = (Class) evaluator;
			GoalEvaluator newInstance;

			try {
				newInstance = (GoalEvaluator) evalClass.getConstructor(
						new Class[] { IGoal.class }).newInstance(
						new Object[] { goal });
				return newInstance;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	private Map evaluators = new HashMap();

	private final GoalEngine engine;
	private final IGoalEvaluatorFactory userFactory;

	private void initStdGoals() {
		registerEvaluator(FieldReferencesGoal.class,
				FieldReferencesGoalEvaluator.class);
		registerEvaluator(MethodCallsGoal.class, MethodCallsGoalEvaluator.class);
	}

	public DefaultTypeInferencer(IGoalEvaluatorFactory userFactory) {
		engine = new GoalEngine(new MapBasedEvaluatorFactory());
		this.userFactory = userFactory;
		initStdGoals();
	}

	public void registerEvaluator(Class goalClass, Class evaluatorClass) {
		assert((IGoal.class.isAssignableFrom(goalClass)));
		assert(GoalEvaluator.class.isAssignableFrom(evaluatorClass));
		evaluators.put(goalClass, evaluatorClass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.dltk.ti.ITypeInferencer#evaluateType(org.eclipse.dltk.ti.AbstractTypeGoal,
	 *      long)
	 */
	public IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit) {
		Object result = this.evaluateType(goal, new TimelimitPruner(timeLimit));
		return (IEvaluatedType) result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.dltk.ti.ITypeInferencer#evaluateType(org.eclipse.dltk.ti.AbstractTypeGoal)
	 */
	public IEvaluatedType evaluateType(AbstractTypeGoal goal, IPruner pruner) {
		return (IEvaluatedType) engine.evaluateGoal(goal, pruner);
	}

	protected Object evaluateGoal(IGoal goal, IPruner pruner) {
		return engine.evaluateGoal(goal, pruner);
	}

	public IEvaluatedType evaluateType(AbstractTypeGoal goal) {
		return evaluateType(goal, null);
	}

}
