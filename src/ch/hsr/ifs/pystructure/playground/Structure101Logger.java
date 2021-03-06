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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IdentityHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

public class Structure101Logger implements IGoalEngineLogger {

	public static final String FLAVOR = "ch.hsr.ifs.pystructure.goalengine";
	
	private final XMLOutputter outputter;
	private Document document;
	private Element modules;
	private Element dependencies;
	private String modulePrefix;

	private IdentityHashMap<AbstractEvaluator, Integer> ids;

	public Structure101Logger() {
		outputter = new XMLOutputter(Format.getPrettyFormat());
		
		document = new Document();
		ids = new IdentityHashMap<AbstractEvaluator, Integer>();
		
		Element root = new Element("data");
		root.setAttribute("flavor", FLAVOR);
		document.addContent(root);
		
		modules = new Element("modules");
		dependencies = new Element("dependencies");
	}
	
	public void testStarted(String filename, String expression, int line) {
		modulePrefix = filename + "/" + line + ":" + expression + "/";
	}

	public void evaluationStarted(IGoal rootGoal) {
		// ignore
	}

	public void evaluationFinished(IGoal rootGoal) {
		// ignore
	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		addEvaluator(creator);
		addEvaluator(evaluator);
		
		Element dependency = new Element("dependency");
		dependency.setAttribute("from", id(creator));
		dependency.setAttribute("to", id(evaluator));
		dependency.setAttribute("type", type(goal));
		dependencies.addContent(dependency);
	}

	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
		// ignore
	}

	private void addEvaluator(AbstractEvaluator creator) {
		if (!ids.containsKey(creator)) {
			Element module = new Element("module");
			module.setAttribute("name", modulePrefix + name(creator));
			module.setAttribute("type", "evaluator");
			module.setAttribute("id", id(creator));
			modules.addContent(module);
		}
	}
	
	private static String name(AbstractEvaluator evaluator) {
		// TODO: Add one "start" per test assertion
		if (evaluator == null) {
			return "start";
		} else {
			return evaluator.getClass().getSimpleName().replaceAll("Evaluator$", "");
		}
	}
	
	private String id(AbstractEvaluator evaluator) {
		if (ids.containsKey(evaluator)) {
			return Integer.toString(ids.get(evaluator));
		} else {
			int id = ids.size();
			ids.put(evaluator, id);
			return Integer.toString(id);
		}
	}

	private String type(IGoal goal) {
		return goal.getClass().getSimpleName().replaceAll("Goal$", "");
	}

	public void shutdown() {
		Element root = document.getRootElement();
		root.addContent(modules);
		root.addContent(dependencies);
		
		try {
			String outDir = System.getProperty("output.tests", "tests");
			FileOutputStream out = new FileOutputStream(outDir + "/goalengine.xml");
			outputter.output(document, out);
			out.close();
		} catch (IOException e) {
			// ignore
		}
	}
}
