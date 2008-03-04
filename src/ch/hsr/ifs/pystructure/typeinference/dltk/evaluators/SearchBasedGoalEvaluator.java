/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.evaluators;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.ItemReference;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.PossiblePosition;

public abstract class SearchBasedGoalEvaluator extends GoalEvaluator {

//	private List possiblePositionsGoals = new ArrayList();
	private List references = new ArrayList();

//	private SearchRequestor requestor = new SearchRequestor() {
//
//		public void acceptSearchMatch(SearchMatch match) throws CoreException {
//			ASTNode node = null;
//			if (match instanceof FieldReferenceMatch) {
//				FieldReferenceMatch match2 = (FieldReferenceMatch) match;
//				node = match2.getNode();
//			}
//			if (match instanceof MethodReferenceMatch) {
//				MethodReferenceMatch match2 = (MethodReferenceMatch) match;
//				node = match2.getNode();
//			}
//			PossiblePosition pos = new PossiblePosition(match.getResource(),
//					match.getOffset(), match.getLength(), node);
//			possiblePositionsGoals.add(createVerificationGoal(pos));
//		}
//
//	};

	public SearchBasedGoalEvaluator(IGoal goal) {
		super(goal);
	}

	public List<IGoal> init() {
		return IGoal.NO_GOALS;
	}
//	public IGoal[] init() {
//		IGoal goal = getGoal();
//		IScriptProject project = null;
//		IContext context = goal.getContext();
//		if (context instanceof ISourceModuleContext) {
//			ISourceModuleContext basicContext = (ISourceModuleContext) goal
//					.getContext();
//			project = basicContext.getSourceModule().getScriptProject();
//		}
//		if (project == null) {
//			return null;
//		}
//		IDLTKSearchScope scope = SearchEngine
//				.createSearchScope(new IModelElement[] { project });
//		SearchPattern pattern = createSearchPattern();
//		SearchEngine engine = new SearchEngine();
//
//		try {
//			engine.search(pattern, new SearchParticipant[] { SearchEngine
//					.getDefaultSearchParticipant() }, scope, requestor, null);
//		} catch (CoreException e) {
//			e.printStackTrace();
//			return IGoal.NO_GOALS;
//		}
//
//		return (IGoal[]) possiblePositionsGoals
//				.toArray(new IGoal[possiblePositionsGoals.size()]);
//	}

	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (result != null && result instanceof ItemReference) {
			references.add(result);
		}
		return IGoal.NO_GOALS;
	}

	public Object produceResult() {
		return references.toArray(new ItemReference[references.size()]);
	}

//	protected abstract SearchPattern createSearchPattern();

	protected abstract IGoal createVerificationGoal(PossiblePosition pos);

}
