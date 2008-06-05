package ch.hsr.ifs.pystructure.playground.fibonacci;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class FibonacciGoal implements IGoal {

	private int index;
	public int result;

	public FibonacciGoal(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

	public ModuleContext getContext() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		final FibonacciGoal other = (FibonacciGoal) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}

}
