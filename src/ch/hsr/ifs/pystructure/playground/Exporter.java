package ch.hsr.ifs.pystructure.playground;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.playground.representation.EClass;
import ch.hsr.ifs.pystructure.playground.representation.EDependency;
import ch.hsr.ifs.pystructure.playground.representation.EMethod;
import ch.hsr.ifs.pystructure.playground.representation.EModule;
import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.results.types.AbstractType;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class Exporter {

	public static final String FLAVOR = "ch.hsr.ifs.pystructure";

	private XMLOutputter outputter;
	private final OutputStream outputStream;

	public Exporter(OutputStream outputStream) {
		this.outputStream = outputStream;

		outputter = new XMLOutputter(Format.getPrettyFormat());
	}

	public void export(String path) throws IOException {
		Workspace workspace = new Workspace(new File(path));
		
		Document document = new Document();

		Element root = new Element("data");
		root.setAttribute("flavor", FLAVOR);
		document.addContent(root);

		Element modules = new Element("modules");
		Element dependencies = new Element("dependencies");

		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger(false));

		for (Module module : workspace.getModules()) {
			Spider spider = new Spider();
			spider.run(module);


			/* Iterate over all methods/classes/functions in a particular module */
			for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
				StructureDefinition definition = entry.getKey();
				List<exprType> expressions = entry.getValue();

				/* Iterate over all expressions in a method/function/class */
				for (exprType node : expressions) {
					/* evaluate type */
					CombinedType types = inferencer.evaluateType(workspace, module, node);

					
					/* create a dependency for each type */
					for (IType type : types) {
						if (type instanceof AbstractType) {
							AbstractType abstractType = (AbstractType) type;
							
							/* determine if is referenced by a attribute */
							if (abstractType.location instanceof Attribute) {
								Attribute attribute = (Attribute) abstractType.location;
								dependencies.addContent(new EDependency(definition, attribute));
							}
						}

						EDependency dependency = new EDependency(definition, type);
						if (dependency.isValid()) {
							dependencies.addContent(dependency);
						}
					}
				}
			}
		}

		inferencer.shutdown();

		/* Now export the structure of the program (modules, classes, methods*/

		for (Module module : workspace.getModules()) {
			EModule emodule = new EModule(module.getNamePath().toString(), module.getUniqueIdentifier());

			modules.addContent(emodule);

			for (Class klass : module.getClasses()) {
				EClass eclass = new EClass(klass.getName(), klass.getUniqueIdentifier()); 
				emodule.addContent(eclass);

				for (Method method : klass.getMethods()) {
					EMethod emethod = new EMethod(method.getName(), method.getUniqueIdentifier());
					eclass.addContent(emethod);
				}

				for (Map.Entry<String, Attribute> entry : klass.getAttributes().entrySet()) {
					String name = entry.getKey();
					Attribute attribute = entry.getValue();
					CombinedType types = attribute.type;

					String attributeId = attribute.getUniqueIdentifier();
					EAttribute eattribute = new EAttribute(name, attributeId);
					eclass.addContent(eattribute);

					for (IType type : types) {
						EDependency dependency = new EDependency(attributeId, type);

						if (dependency.isValid()) {
							dependencies.addContent(dependency);
						}
					}
				}
			}
		}
		
		root.addContent(modules);
		root.addContent(dependencies);

		outputter.output(document, outputStream);
	}


	public static void main(String[] args) throws IOException {
//		String path = "s101g	/examples/simple";
		String path = "s101g/examples/pydoku";
//		String path = "/Users/reto/Downloads/simple_go/";
		

		FileOutputStream stream = new FileOutputStream("s101g/examples/test.xml", false);

		Exporter exporter = new Exporter(stream);
		exporter.export(path);
	}
}
