package ch.hsr.ifs.pystructure.playground;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.StructuralVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import ch.hsr.ifs.pystructure.utils.LineIterator;

public class Spider extends StructuralVisitor {
	
	private Map<StructureDefinition, List<exprType>> typables;

	public Spider() {
		typables = new HashMap<StructureDefinition, List<exprType>>();
	}
	
	protected void run(Module module) {
		super.run(module);
	}
	
	public Map<StructureDefinition, List<exprType>> getTypables() {
		return typables;
	}
	
	@Override
	public Object visitModule(org.python.pydev.parser.jython.ast.Module node)
			throws Exception {
		Module module = getDefinitionFor(node);
		super.visitModule(node);
		visitChildren(module);
		return null;
	}
	
	@Override
	public Object visitClassDef(ClassDef node) throws Exception {
		Class klass = getDefinitionFor(node);
		if (isFirstVisit(klass)) {
			return null;
		}
		super.visitClassDef(node);
		visitChildren(klass);
		return null;
	}
	
	@Override
	public Object visitFunctionDef(FunctionDef node) throws Exception {
		Function function = getDefinitionFor(node);
		if (isFirstVisit(function)) {
			return null;
		}
		super.visitFunctionDef(node);
		visitChildren(function);
		return null;
	}
	
	@Override
	public void traverse(SimpleNode node) throws Exception {
		if (node instanceof exprType) {
			if (!typables.containsKey(getCurrentStructureDefinition())) {
				typables.put(getCurrentStructureDefinition(), new ArrayList<exprType>());
			}
			typables.get(getCurrentStructureDefinition()).add((exprType) node);
		}
		node.traverse(this);
	}

	private static final class Result implements Comparable<Result> {

		public final StructureDefinition definition;
		public final SimpleNode node;
		public final IType type;

		public Result(StructureDefinition definition, SimpleNode node, IType type) {
			this.definition = definition;
			this.node = node;
			this.type = type;
		}

		public int compareTo(Result o) {
			if (this.node.beginColumn < o.node.beginColumn) {
				return -1;
			} else if (this.node.beginColumn > o.node.beginColumn) {
				return +1;
			} else {
				return 0;
			}
		}
		
		@Override
		public String toString() {
			return definition.toString() + " " + this.type.toString();
		}
		
	}

	public static void main(String[] args) throws Exception {
		String path = "s101g/examples/pydoku/";
		LinkedList<String> sysPath = new LinkedList<String>();
		Workspace workspace = new Workspace(path, sysPath);
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger(false));
		
		for (Module module : workspace.getModules()) {
			HashMap<Integer, List<Result>> types = new HashMap<Integer, List<Result>>();
			
			Spider spider = new Spider();
			spider.run(module);
			
			for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
				StructureDefinition definition = entry.getKey();
				List<exprType> expressions = entry.getValue();
				
				for (exprType node : expressions) {
//					System.out.println(node);
					
					IType type = inferencer.evaluateType(workspace, module, node);
//					System.out.println(" T: "  + type);
					
					List<Result> l = types.get(node.beginLine);
					if (l == null) {
						l = new LinkedList<Result>();
						types.put(node.beginLine, l);
					}
					
					l.add(new Result(definition, node, type));
				}
			}
			
			/* print out source */
			String source = module.getSource();
			
			int i = 1;
			for (String line : new LineIterator(new StringReader(source))) {
				System.out.print(line);
				
				List<Result> lineType = types.get(i);

				if (lineType != null) {
					Collections.sort(lineType);
					System.out.println(" # " + lineType);
				} else {
					System.out.println();
				}
				i++;
			}
		}
		
		inferencer.shutdown();
		
		
	}

}
