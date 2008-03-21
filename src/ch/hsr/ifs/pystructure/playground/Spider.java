package ch.hsr.ifs.pystructure.playground;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.IfExp;
import org.python.pydev.parser.jython.ast.VisitorBase;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class Spider extends VisitorBase {
	
	private List<exprType> typables;

	public Spider() {
		typables = new ArrayList<exprType>();
	}
	
	public List<exprType> getTypables() {
		return typables;
	}
	
	@Override
	public void traverse(SimpleNode node) throws Exception {
		if (node instanceof exprType) {
			if (node instanceof IfExp) {
				/* SKIP */
			} else {
				typables.add((exprType) node);
			}
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
		

		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger());
		
		for (Module module : workspace.getModules()) {
			Spider spider = new Spider();
			module.getNode().accept(spider);
			
			for (SimpleNode node : spider.getTypables()) {
				System.out.println(node);
				
				IType type = inferencer.evaluateType(workspace, module, node);
				System.out.println(" T: "  + type);
			}
		}
		
		inferencer.shutdown();
		
		
	}

}
