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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.export.structure101.Spider;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.base.ILocatable;
import ch.hsr.ifs.pystructure.typeinference.goals.base.Location;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.CalculateTypeHierarchyGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.MethodResolveGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CombinedLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CustomLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CustomLogger.Record;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import ch.hsr.ifs.pystructure.utils.FileUtils;
import ch.hsr.ifs.pystructure.utils.LineIterator;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class TypeAnnotator extends HtmlOutputter {
	
	private Workspace workspace;
	private File outPath;
	private PythonTypeInferencer inferencer;
	private CustomLogger logger;
	private File goalDir;
	private String pygmentizePath;
	
	public TypeAnnotator(File path, String outPath, String pygmentizePath) {
		this.workspace = new Workspace(path);
		this.outPath = new File(outPath);
		this.pygmentizePath = pygmentizePath;
		this.logger = new CustomLogger();
		
		goalDir = new File(outPath, "goals");
		
		FileUtils.mkdir(goalDir);
		
		this.inferencer = new PythonTypeInferencer(new CombinedLogger(new StatsLogger(false), logger));
	}

	public void generateReport() throws IOException {
		for (Module module : workspace.getModules()) {
			/* parse */
			List<Result> results = parseModule(module);
			
			writeGoalReport(results);
			writeModuleReport(module, results);
		}
		
		inferencer.shutdown();
	}

	private void writeModuleReport(Module module, List<Result> results)
			throws IOException {
		/* Write page */
		Document doc = createDocument(0, module.getName());
		Element root = doc.getRootElement();
		
		Element body = emptyTag("body");
		root.addContent(body);
		
		body.addContent(tag("h2", module.getNamePath().toString()));
		
		Element details = emptyTag("p");
		body.addContent(details);
		
		printDetail(details, "File", module.getFile().toString());
		
		Element table = emptyTag("table");
		body.addContent(table);
		
		Map<Integer, List<Result>> types = groupResultsByLine(results);
		
		/* print out source */
		int i = 1;
		
		String styledSource = style(module.getFile());
		
		for (String line : new LineIterator(new StringReader(styledSource))) {
			Element tr = emptyTag("tr");
			table.addContent(tr);
			
			tr.addContent(td("" + i + ":"));
			
			Element tdLine = emptyTag("td"); 
			tr.addContent(tdLine);
			
			tdLine.addContent(emptyTag("a", "name", "" + i));
			
			/* UGLY */
			tdLine.addContent(new ProcessingInstruction(javax.xml.transform.Result.PI_DISABLE_OUTPUT_ESCAPING, ""));
			tdLine.addContent(tag("span", "&nbsp;" + line, "class", "code"));
			tdLine.addContent(new ProcessingInstruction(javax.xml.transform.Result.PI_ENABLE_OUTPUT_ESCAPING, ""));
			
			List<Result> lineType = types.get(i);
			
			Element tdTypes = emptyTag("td");
			tr.addContent(tdTypes);
		
			if (lineType != null) {
				Collections.sort(lineType);
				boolean first = true;
				for (Result result : lineType) {
					if (first) {
						first = false;
					} else {
						tdTypes.addContent(", ");
					}
					
					String goalFilename = "goals/" + result.uid + ".html";
					
					IType type = result.type;
					String label = type.toString().equals("") ? "?" : type.toString();
					tdTypes.addContent(link(label, goalFilename));
				}
			} else {
				tdTypes.addContent(" ");
			}
			
			i++;
		}
		
		String filename = moduleFilename(module);
		FileOutputStream out = new FileOutputStream(new File(outPath, filename), false);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		
		/* open output file */
		outputter.output(doc, out);
	}

	private Map<Integer, List<Result>> groupResultsByLine(List<Result> results) {
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
		return types;
	}

	private List<Result> parseModule(Module module) {
		List<Result> results = new ArrayList<Result>();
		
		Spider spider = new Spider();
		spider.run(module);
		
		for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
			StructureDefinition definition = entry.getKey();
			List<exprType> expressions = entry.getValue();
			
			for (exprType node : expressions) {
				IType type = inferencer.evaluateType(workspace, module, node);
				LinkedList<Record> log = logger.getLog();
				IGoal rootGoal = logger.getCurrentRootGoal();
				
				Result result = new Result(definition, node, type, rootGoal, log);
				
				results.add(result);
			}
		}
		return results;
	}

	private String style(File sourceFile) throws IOException {
		if (pygmentizePath != null) {
			String[] cmd = {pygmentizePath, "-f", "html", "-l", "python", sourceFile.getPath()};
			
			Process formatter = Runtime.getRuntime().exec(cmd);
			
			formatter.getOutputStream().close();
			
			String formatted = FileUtils.read(formatter.getInputStream());
			
			int exitValue;
			try {
				exitValue = formatter.waitFor();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			if (exitValue != 0) {
				throw new RuntimeException("pygmentize returned witha non-zero" + exitValue);
			}
			/* don't ask, don't tell */
	 		return formatted.replaceFirst("^.*?<pre>", "")
	 						.replaceFirst("</pre></div>$", "");
		} else {
			/* Do not format if pygmentize is null */
			return FileUtils.read(sourceFile);
		}
	}

	private void writeGoalReport(List<Result> results) throws IOException {
		for (Result result : results) {
			Document doc = createDocument(1, "suboals");
			Element root = doc.getRootElement();
			
			Element body = emptyTag("body");
			root.addContent(body);
		
			String filename = String.valueOf(result.uid) + ".html";
			File file = new File(goalDir, filename);
			
			IGoal rootGoal = result.rootGoal;
			
			ModuleContext context = rootGoal.getContext();
			
			Element p = emptyTag("p");
			body.addContent(p);
			printDetail(p, "Root Goal", rootGoal.getClass().getSimpleName());
			printDetail(p, "Context", module(context.getModule()));
			
			Element table = emptyTag("table");
			body.addContent(table);
			
			Element trHeader = emptyTag("tr");
			table.addContent(trHeader);
			
			th(trHeader, "");
			th(trHeader, "Action");
			th(trHeader, "Parent");
			th(trHeader, "ID");
			th(trHeader, "Goal");
			th(trHeader, "Evaluator");
			th(trHeader, "Target");
			
			
			for (Record log : result.log) {
				Element tr = emptyTag("tr");
				table.addContent(tr);
				
				td(tr, StringUtils.multiply(log.level, "|   "), "code");
				td(tr, log.msg);
				td(tr, String.valueOf(log.creatorId));
				td(tr, String.valueOf(log.id));
				td(tr, log.goal.getClass().getSimpleName());
				td(tr, log.evaluator.toString());
				td(tr, goalDetails(log.goal));
			}
			
			FileOutputStream out = new FileOutputStream(file, false);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, out);
		}
	}

	private Content goalDetails(IGoal goal) {
		if (goal instanceof ILocatable) {
			Location location = ((ILocatable) goal).getLocation();
			
			Element span = emptyTag("span");
			
			if (location.node == null) {
				span.addContent("null");
			} else {
				span.addContent(location.node.getClass().getSimpleName() + " ");
				span.addContent(linkTo(location));
			}
			return span;
		} else if (goal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) goal;
			return new Text(g.getName());
		} else if (goal instanceof ClassAttributeTypeGoal) {
			ClassAttributeTypeGoal g = (ClassAttributeTypeGoal) goal;
			return linkTo(g.getContext(), g.getClassType().getKlass(), g.getAttributeName());
		} else if (goal instanceof AttributeReferencesGoal) {
			AttributeReferencesGoal g = (AttributeReferencesGoal) goal;
			Attribute attribute = g.getAttribute();
			return linkTo(g.getContext(), attribute.getKlass(), attribute.getName());
		} else if (goal instanceof PossibleAttributeReferencesGoal) {
			PossibleAttributeReferencesGoal g = (PossibleAttributeReferencesGoal) goal;
			return new Text(g.getName());
		} else if (goal instanceof MethodResolveGoal) {
			MethodResolveGoal g = (MethodResolveGoal) goal;
			return new Text(g.getClassType().getKlass() + " " + g.getAttributeName());
		} else if (goal instanceof MethodResolutionOrderGoal) {
			MethodResolutionOrderGoal g = (MethodResolutionOrderGoal) goal;
			return new Text("MRO for " + g.getKlass());
		} else if (goal instanceof CalculateTypeHierarchyGoal) {
			return new Text("Calculate all Types");
		} else { 
			throw new RuntimeException("Cannot format goal, unknown goal type: " + goal);
		}
		
	}

	private static String moduleFilename(Module module) {
		return module.getNamePath().toString() + ".html";
	}

	private static Element linkTo(ModuleContext context, Class klass,
			String attributeName) {
		Location classLocation = new Location(context, klass.getNode());
		Element span = emptyTag("span");
		span.addContent("Attribute: ");
		span.addContent(linkTo(klass.getName(), classLocation));
		span.addContent("." + attributeName);
		return span;
	}


	private static Element module(Module module) {
		String filename = moduleFilename(module);
		
		return link(module.getNamePath().toString(), "../" + filename);
	}
	
	static Element linkTo(String label, Location location) {
		String filename = moduleFilename(location.module);
		return link(label, "../" + filename + "#" + location.getLineNr());
	}

	protected static Element linkTo(Location location) {
		String label = location.module.getNamePath().toString() + ":" + location.getLineNr();
		return linkTo(label, location);
	}
	
	private static final class Result implements Comparable<Result> {
		private static int currentUid = 0;
		
		public final StructureDefinition definition;
		public final SimpleNode node;
		public final IType type;
		public final LinkedList<Record> log;
		public final int uid;
		public final IGoal rootGoal;

		public Result(StructureDefinition definition, SimpleNode node, IType type, IGoal rootGoal, LinkedList<Record> log) {
			this.definition = definition;
			this.node = node;
			this.type = type;
			this.rootGoal = rootGoal;
			this.log = log;
			this.uid = currentUid;
			currentUid++;
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
			return this.type.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		String pyg = "s101g/examples/pygments/pygmentize";
		
//		new TypeAnnotator(new File("tests/python/typeinference/"), "out/tests/").generateReport();
		new TypeAnnotator(new File("s101g/examples/pydoku/"), "out/pydoku/", pyg).generateReport();
//		new TypeAnnotator(new File("s101g/examples/pygments/"), "out/pygments/").generateReport();
	}

}
