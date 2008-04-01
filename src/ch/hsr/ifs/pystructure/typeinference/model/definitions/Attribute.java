package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

public class Attribute extends Definition {
	
	private final String name;
	private final NamePath namePath;
	private final Class klass;
	private final CombinedType type;

	public Attribute(String name, Class klass, CombinedType type) {
		super();
		this.name = name;
		this.namePath = new NamePath(new NamePath(klass.getFullName()), name);
		this.klass = klass;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public NamePath getNamePath() {
		return namePath;
	}
	
	public CombinedType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((klass == null) ? 0 : klass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Attribute other = (Attribute) obj;
		if (klass == null) {
			if (other.klass != null)
				return false;
		} else if (!klass.equals(other.klass))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
