package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.MethodResolutionOrder;

public class MethodResolutionOrderGoal extends AbstractGoal {

	private final Class klass;
	public MethodResolutionOrder linearization;

	public MethodResolutionOrderGoal(ModuleContext context, Class klass) {
		super(context);
		
		this.klass = klass;
	}

	public Class getKlass() {
		return klass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((klass == null) ? 0 : klass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MethodResolutionOrderGoal other = (MethodResolutionOrderGoal) obj;
		if (klass == null) {
			if (other.klass != null) {
				return false;
			}
		} else if (!klass.equals(other.klass)) {
			return false;
		}
		return true;
	}

}
