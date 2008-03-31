package ch.hsr.ifs.pystructure.playground;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class Exporter {
	
	public static final String FLAVOR = "ch.hsr.ifs.pystructure";
	
	private XMLOutputter outputter;
	private final OutputStream outputStream;

	public Exporter(OutputStream outputStream) {
		this.outputStream = outputStream;
		
		outputter = new XMLOutputter(Format.getPrettyFormat());
	}
	
	public void export(Workspace workspace) throws IOException {
		Document document = new Document();
		
		Element root = new Element("data");
		root.setAttribute("flavor", FLAVOR);
		document.addContent(root);
		
		Element modules = new Element("modules");
		root.addContent(modules);
		
		for (Module module : workspace.getModules()) {
			exportModule(modules, module);
		}
		
		outputter.output(document, outputStream);
	}
	
	private void exportModule(Element modules, Module module) {
		for (Class klass : module.getClasses()) {
			exportClass(modules, klass);
		}
	}

	private void exportClass(Element modules, Class klass) {
		Element element = new Element("module");
		element.setAttribute("type", "class");
		String fullName = klass.getModule().getFullName() + "." + klass.getName();
		element.setAttribute("name", fullName);
		element.setAttribute("id", klass.getUniqueIdentifier());
		modules.addContent(element);
		
		exportMethods(element, klass);
		exportAttributes(element, klass);
	}

	private void exportAttributes(Element element, Class klass) {
		for (String name : klass.getAttributes().keySet()) {
			Element attribute = new Element("submodule");
			attribute.setAttribute("type", "attribute");
			attribute.setAttribute("name", name);
			attribute.setAttribute("id", String.valueOf(name.hashCode()));
			element.addContent(attribute);
		}
		
	}

	private void exportMethods(Element element, Class klass) {
		for (Method method : klass.getMethods()) {
			Element submodule = new Element("submodule");
			submodule.setAttribute("type", "method");
			submodule.setAttribute("name", method.getName());
			submodule.setAttribute("id", method.getUniqueIdentifier());
			
			element.addContent(submodule);
		}
	}

	public static void main(String[] args) throws IOException {
		LinkedList<String> sysPath = new LinkedList<String>();

		String path = "s101g/examples/pydoku";
		Workspace workspace = new Workspace(path, sysPath);
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger(false));
		
		for (Module module : workspace.getModules()) {
			
//			List<Result> results = new ArrayList<Result>();
			
			Spider spider = new Spider();
			spider.run(module);
			
			for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
				List<exprType> expressions = entry.getValue();
				
				for (exprType node : expressions) {
					inferencer.evaluateType(workspace, module, node);
				}
			}
			
			
		}
		inferencer.shutdown();

		
		Exporter exporter = new Exporter(System.out);
		exporter.export(workspace);
		
		
	}
}
