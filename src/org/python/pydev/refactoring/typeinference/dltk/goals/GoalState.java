/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.goals;

public abstract class GoalState {
	public static final GoalState DONE = new GoalState() {
		public String toString() {
			return "DONE";
		}
	};

	public static final GoalState WAITING = new GoalState() {
		public String toString() {
			return "WAITING";
		}
	};

	public static final GoalState PRUNED = new GoalState() {
		public String toString() {
			return "PRUNED";
		}
	};

	public static final GoalState RECURSIVE = new GoalState() {
		public String toString() {
			return "RECURSIVE";
		}
	};
}
