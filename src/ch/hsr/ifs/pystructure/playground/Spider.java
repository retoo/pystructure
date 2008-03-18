package ch.hsr.ifs.pystructure.playground;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.VisitorBase;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class Spider extends VisitorBase {
	
	private List<SimpleNode> typables;

	public Spider() {
		typables = new ArrayList<SimpleNode>();
	}
	
	public List<SimpleNode> getTypables() {
		return typables;
	}
	
	@Override
	public void traverse(SimpleNode node) throws Exception {
		if (node instanceof exprType) {
			typables.add(node);
		}
		node.traverse(this);
	}

	@Override
	protected Object unhandled_node(SimpleNode node) throws Exception {
		return null;
	}

	public static void main(String[] args) throws Exception {
		String path = "s101g/examples/pydoku/";
		LinkedList<String> sysPath = new LinkedList<String>();
		Workspace workspace = new Workspace(path, sysPath);
		
		Module module = workspace.getModule("pydoku");

		Spider spider = new Spider();
		module.getNode().accept(spider);
		
		for (SimpleNode node : spider.getTypables()) {
			System.out.println(node);
		}
	}

}
