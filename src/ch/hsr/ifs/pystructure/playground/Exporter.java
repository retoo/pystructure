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
		Element dependencies = new Element("dependencies");
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer(new StatsLogger(false));
		
		for (Module module : workspace.getModules()) {
			Spider spider = new Spider();
			spider.run(module);
			
			for (Map.Entry<StructureDefinition, List<exprType>> entry : spider.getTypables().entrySet()) {
				StructureDefinition definition = entry.getKey();
				List<exprType> expressions = entry.getValue();
				
				for (exprType node : expressions) {
					IType type = inferencer.evaluateType(workspace, module, node);
					
					if (type instanceof CombinedType) {
						for (IType t : (CombinedType) type) {
							EDependency dependency = new EDependency(definition, t);
							if (dependency.isValid()) {
								dependencies.addContent(dependency);
							}
						}
					} else {
						throw new RuntimeException("Not combined");
					}
				}
			}
		}
		
		inferencer.shutdown();
		
		root.addContent(modules);
		root.addContent(dependencies);
		
		for (Module module : workspace.getModules()) {
			EModule emodule = new EModule(module.getNamePath().toString(), module.getUniqueIdentifier());

			modules.addContent(emodule);
			
			
			for (Class klass : module.getClasses()) {
				EClass eclass = new EClass(klass.getFullName(), klass.getUniqueIdentifier()); 
				modules.addContent(eclass);
				
				for (Method method : klass.getMethods()) {
					EMethod emethod = new EMethod(method.getName(), method.getUniqueIdentifier());
					eclass.addContent(emethod);
				}
				
				for (Map.Entry<String, CombinedType> attribute : klass.getAttributes().entrySet()) {
					String name = attribute.getKey();
					CombinedType types = attribute.getValue();
					
					String attribId = klass.getFullName() + "." + name;
					EAttribute eattribute = new EAttribute(name, attribId);
					eclass.addContent(eattribute);
					
					for (IType type : types) {
						EDependency dependency = new EDependency(attribId, type);
						
						if (dependency.isValid()) {
							dependencies.addContent(dependency);
						}
					}
				}
			}
		}
		
		outputter.output(document, outputStream);
	}
	

	public static void main(String[] args) throws IOException {
		String path = "s101g/examples/pydoku";
		Workspace workspace = new Workspace(new File(path));
		
		FileOutputStream stream = new FileOutputStream("s101g/examples/test.xml", false);
		
		Exporter exporter = new Exporter(stream);
		exporter.export(workspace);
	}
}
