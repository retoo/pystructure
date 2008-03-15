package ch.hsr.ifs.pystructure.playground;

import java.io.IOException;
import java.util.IdentityHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.IGoalEngineLogger;

public class Structure101Logger implements IGoalEngineLogger {

	public static final String FLAVOR = "ch.hsr.ifs.pystructure.goalengine";
	
	private final XMLOutputter outputter;
	private Document document;
	private Element modules;
	private Element dependencies;

	private IdentityHashMap<GoalEvaluator, Integer> ids;

	public Structure101Logger() {
		outputter = new XMLOutputter(Format.getPrettyFormat());
	}

	public void evaluationStarted(IGoal rootGoal) {
		document = new Document();
		ids = new IdentityHashMap<GoalEvaluator, Integer>();
		
		Element root = new Element("data");
		root.setAttribute("flavor", FLAVOR);
		document.addContent(root);
		
		modules = new Element("modules");
		dependencies = new Element("dependencies");
	}

	public void evaluationFinished(IGoal rootGoal) {
		Element root = document.getRootElement();
		root.addContent(modules);
		root.addContent(dependencies);
		
		try {
			outputter.output(document, System.out);
		} catch (IOException e) {
			// Ignore
		}
	}

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
		registerEvaluator(creator);
		registerEvaluator(evaluator);
		
		Element dependency = new Element("dependency");
		dependency.setAttribute("from", id(creator));
		dependency.setAttribute("to", id(evaluator));
		dependency.setAttribute("type", type(goal));
		dependencies.addContent(dependency);
	}

	public void goalFinished(IGoal goal, GoalEvaluator evaluator) {
		// ignore
	}

	private String type(IGoal goal) {
		return goal.getClass().getSimpleName().replaceAll("Goal$", "");
	}

	private void registerEvaluator(GoalEvaluator creator) {
		if (!ids.containsKey(creator)) {
			Element module = new Element("module");
			module.setAttribute("name", name(creator));
			module.setAttribute("type", "class");
			module.setAttribute("id", id(creator));
			modules.addContent(module);
		}
	}
	
	private static String name(GoalEvaluator evaluator) {
		if (evaluator == null) {
			return "start";
		} else {
			return evaluator.getClass().getSimpleName().replaceAll("Evaluator$", "");
		}
	}
	
	private String id(GoalEvaluator evaluator) {
		if (ids.containsKey(evaluator)) {
			return Integer.toString(ids.get(evaluator));
		} else {
			int id = ids.size();
			ids.put(evaluator, id);
			return Integer.toString(id);
		}
	}
}