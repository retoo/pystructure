package ch.hsr.ifs.pystructure.typeinference.goals.references;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class CalculateTypeHierarchyGoal extends AbstractGoal {	
	
	private Workspace workspace;

	public CalculateTypeHierarchyGoal(ModuleContext moduleContext) {
		super(moduleContext);
		this.workspace = moduleContext.getWorkspace();
	}

	public Workspace getWorkspace() {
		return workspace;
	}

}
