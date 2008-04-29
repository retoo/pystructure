package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;

public class Linearisation extends LinkedList<Class> {

	private static final long serialVersionUID = 1L;
	
	public static Linearisation merge(Class klass, List<Linearisation> toMerge) {
		Linearisation linearization = new Linearisation();
		linearization.add(klass);


		for (;;) {
			boolean changedSomething = false;
			
			if (toMerge.isEmpty()) {
				/* yay finished */
				break; 
			}
			
			for (Iterator<Linearisation> i = toMerge.iterator(); i.hasNext();) {
				Linearisation chain = i.next();

				/* empty chain detected */
				if (chain.isEmpty()) {
					i.remove();
					changedSomething = true;
					continue;
				}
				
				Class head = chain.getFirst();

				if (isInNoTail(head, toMerge)) {
					/* great we found a header which is in no tail */
					linearization.add(head);

					/* remove the other occurrences of that particular class*/
					for (Linearisation toClean : toMerge) {
						toClean.remove(head);
					}

					/* begin again from the beginning */
					changedSomething = true;
					break;
				}
			}

			if (!changedSomething) {
				/* hm, we walked through the whole list without changing anything, thats bad */
				throw new RuntimeException("Invalid linerarisation");
			}
		}

		return linearization;
	}

	private static boolean isInNoTail(Class head, List<Linearisation> toMerge) {
		for (Linearisation otherChains : toMerge) {
			if (otherChains.isInTail(head)) {
				return false;
			}
		}

		return true;
	}

	public Linearisation(List<Class> linearization) {
		super(linearization);
	}

	public Linearisation() {
		super();
	}

	public boolean isInTail(Class obj) {
		if (this.size() > 1) {
			/* check if the element is in the tail */
			for (int i = 1; i < this.size(); i++) {
				if (this.get(i).equals(obj)) {
					return true;
				}
			}
			
			return false;
		} else {
			/* empty or just head -> not in tail */
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
