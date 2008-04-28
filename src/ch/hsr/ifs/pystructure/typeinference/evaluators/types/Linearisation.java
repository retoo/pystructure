package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;

public class Linearisation extends LinkedList<Class> {

	private static final long serialVersionUID = 1L;

	public Linearisation(List<Class> linearization) {
		super(linearization);
	}

	public Linearisation() {
		super();
	}

	public boolean inTail(Class obj) {
		if (this.size() > 1) {
			/* check if the lement is in the tail */
			for (int i = 1; i < this.size(); i++) {
				if (this.get(i).equals(obj)) {
					return true;
				}
			}
			
			return false;
		} else {
			/* empty or just head= not in tail */
			return false;
		}
	}

	public String getStringRep() {
		StringBuilder sb = new StringBuilder();
		
		boolean first = true;
		for (Class c : this) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(c.getName());
		}
		
		return sb.toString();
	}

}
