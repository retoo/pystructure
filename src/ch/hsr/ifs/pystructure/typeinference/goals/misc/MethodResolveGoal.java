package ch.hsr.ifs.pystructure.typeinference.goals.misc;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MethodType;

public class MethodResolveGoal extends AbstractGoal {

	private final String attributeName;
	private final ClassType classType;
	public final List<MethodType> methodTypes;

	public MethodResolveGoal(ModuleContext context, ClassType classType, String attributeName) {
		super(context);
		this.classType = classType;
		this.attributeName = attributeName;
		this.methodTypes = new LinkedList<MethodType>();
	}

	public ClassType getClassType() {
		return classType;
	}

	public String getAttributeName() {
		return attributeName;
	}
	
	@Override
	public String toString() {
		return fillToString(attributeName + " of " + classType.getKlass());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classType == null) ? 0 : classType.hashCode());
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
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
		final MethodResolveGoal other = (MethodResolveGoal) obj;
		if (classType == null) {
			if (other.classType != null) {
				return false;
			}
		} else if (!classType.equals(other.classType)) {
			return false;
		}
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		return true;
	}

}
