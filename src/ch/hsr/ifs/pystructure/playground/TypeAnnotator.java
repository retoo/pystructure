/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.playground;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.export.structure101.Spider;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import ch.hsr.ifs.pystructure.utils.LineIterator;

public final class TypeAnnotator {
	
	private TypeAnnotator() {
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
		
		/* hashcode and equals have to implemented if Result objects are stored in certain
		 * data structures. The Object implementations can't be used as they are not consitent
		 * with our compareTo impelmentation.
		 */
		@Override
		public boolean equals(Object obj) {
			throw new RuntimeException("not impelemnted");
		}
		
		@Override
		public int hashCode() {
			throw new RuntimeException("not implemented");
		}
		
		@Override
		public String toString() {
			return definition.toString() + " " + this.type.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		File path = new File("s101g/examples/pydoku/");
		Workspace workspace = new Workspace(path);
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger(false));
		
		for (Module module : workspace.getModules()) {
			
			List<Result> results = new ArrayList<Result>();
			
			Spider spider = new Spider();
			spider.run(module);
			
			for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
				StructureDefinition definition = entry.getKey();
				List<exprType> expressions = entry.getValue();
				
				for (exprType node : expressions) {
					IType type = inferencer.evaluateType(workspace, module, node);
					results.add(new Result(definition, node, type));
				}
			}
			
			
			System.out.println(generateDebugSourceOutput(module, results));
		}
		inferencer.shutdown();
	}

	private static String generateDebugSourceOutput(Module module, List<Result> results) {
		StringBuilder sb = new StringBuilder();
		
		/* group results by line nr */
		HashMap<Integer, List<Result>> types = new HashMap<Integer, List<Result>>();
		for (Result result : results) {
			List<Result> l = types.get(result.node.beginLine);
			if (l == null) {
				l = new LinkedList<Result>();
				types.put(result.node.beginLine, l);
			}
			l.add(result);
		}
		
		
		/* print out source */
		int i = 1;
		for (String line : new LineIterator(new StringReader(module.getSource()))) {
			sb.append(line);
			
			List<Result> lineType = types.get(i);

			if (lineType != null) {
				Collections.sort(lineType);
				sb.append(" # " + lineType + "\n");
			} else {
				sb.append("\n");
			}
			i++;
		}
		
		return sb.toString();
	}

}
