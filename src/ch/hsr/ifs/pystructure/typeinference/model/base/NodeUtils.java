package ch.hsr.ifs.pystructure.typeinference.model.base;

import org.python.pydev.parser.jython.SimpleNode;

public final class NodeUtils {

	private NodeUtils() {
	}

	public static String nodePosition(SimpleNode node) {
		return 
			node != null
				? "(L" + node.beginLine + " C" + node.beginColumn + ")"
				: "";
	}
	
}
