package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

public class AliasedImportDefinition extends ImportDefinition {

	public AliasedImportDefinition(Module module, SimpleNode node, ImportPath path, String element, String alias) {
		super(module, node, path, element, alias);
	}

}
