package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class MethodResolutionOrder extends LinkedList<Class> {

	private static final long serialVersionUID = 1L;

	public static MethodResolutionOrder merge(Class klass, List<MethodResolutionOrder> toMerge) {
		MethodResolutionOrder linearisation = new MethodResolutionOrder();
		linearisation.add(klass);


		for (;;) {
			boolean changedSomething = false;

			if (toMerge.isEmpty()) {
				/* yay finished */
				break; 
			}

			for (Iterator<MethodResolutionOrder> i = toMerge.iterator(); i.hasNext();) {
				MethodResolutionOrder chain = i.next();

				/* empty chain detected */
				if (chain.isEmpty()) {
					i.remove();
					changedSomething = true;
					continue;
				}

				Class head = chain.getFirst();

				if (isInNoTail(head, toMerge)) {
					/* great we found a header which is in no tail */
					linearisation.add(head);

					/* remove the other occurrences of that particular class*/
					for (MethodResolutionOrder toClean : toMerge) {
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

		return linearisation;
	}

	private static boolean isInNoTail(Class head, List<MethodResolutionOrder> toMerge) {
		for (MethodResolutionOrder otherChains : toMerge) {
			if (otherChains.isInTail(head)) {
				return false;
			}
		}

		return true;
	}

	public MethodResolutionOrder(List<Class> linearization) {
		super(linearization);
	}

	public MethodResolutionOrder() {
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
