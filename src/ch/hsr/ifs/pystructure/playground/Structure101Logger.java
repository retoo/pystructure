package ch.hsr.ifs.pystructure.playground;

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

	public Structure101Logger() {
		outputter = new XMLOutputter(Format.getPrettyFormat());
		Document document = new Document();
		
		Element root = new Element("data");
		root.setAttribute("flavor", FLAVOR);
		document.addContent(root);
		
		Element modules = new Element("modules");
		root.addContent(modules);
		
//		outputter.output(document, System.out);
	}

	public void evaluationStarted(IGoal rootGoal) {
		// TODO Auto-generated method stub

	}

	public void evaluationFinished(IGoal rootGoal) {
		// TODO Auto-generated method stub

	}

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
		System.out.println(goal);
	}

	public void goalFinished(IGoal goal) {
		// TODO Auto-generated method stub

	}

}
