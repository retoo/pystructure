package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

public class ClassAttributeTypeGoal extends AbstractTypeGoal {

	private final ClassType classType;
	private final String attributeName;

	public ClassAttributeTypeGoal(ModuleContext context, ClassType classType, String attributeName) {
		super(context);

		this.classType = classType;
		this.attributeName = attributeName;
	}
	
	public ClassType getClassType() {
		return classType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result
				+ ((classType == null) ? 0 : classType.hashCode());
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
		final ClassAttributeTypeGoal other = (ClassAttributeTypeGoal) obj;
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		if (classType == null) {
			if (other.classType != null) {
				return false;
			}
		} else if (!classType.equals(other.classType)) {
			return false;
		}
		return true;
	}

 
}
