package ch.hsr.ifs.pystructure.playground;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
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
		modules.addContent(element);
	}

	public static void main(String[] args) throws IOException {
		LinkedList<String> sysPath = new LinkedList<String>();

		String path = "s101g/examples/pydoku";
		Workspace workspace = new Workspace(path, sysPath);
		
		Exporter exporter = new Exporter(System.out);
		exporter.export(workspace);
	}
}
