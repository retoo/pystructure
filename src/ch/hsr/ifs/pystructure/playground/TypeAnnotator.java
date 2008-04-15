/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.playground;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.output.XMLOutputter;
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

public class TypeAnnotator {
	
	private Workspace workspace;
	private File outPath;
	private XMLOutputter escaper;
	private PythonTypeInferencer inferencer;
	private Foo foo;
	public TypeAnnotator(File path, String outPath) {
		this.workspace = new Workspace(path);
		this.outPath = new File(outPath);
		this.escaper = new XMLOutputter();
		this.inferencer = new PythonTypeInferencer(new StatsLogger(false));
	}
	
	public void generateReport() throws IOException {
		
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
			
			String filename = module.getNamePath().toString() + ".html";
			File outFile = new File(outPath, filename);
			System.out.println(outFile);
			
			String out = escaper.escapeAttributeEntities(generateDebugSourceOutput(module, results));
			
			FileWriter writer = new FileWriter(outFile);
			
			writer.write("<pre>");
			writer.write(out);
			writer.write("</pre>");
			
			writer.close();
		}
		inferencer.shutdown();
		
		
	}


	private String generateDebugSourceOutput(Module module, List<Result> results) {
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
			return node.beginColumn - o.node.beginColumn;
		}
		
		/* hashCode and equals have to implemented if Result objects are stored in certain
		 * data structures. The Object implementations can't be used as they are not consistent
		 * with our compareTo implementation.
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
		
		TypeAnnotator ta = new TypeAnnotator(path, "out");
		
		ta.generateReport();
	}

}
