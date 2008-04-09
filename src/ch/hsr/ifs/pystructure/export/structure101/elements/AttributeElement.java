package ch.hsr.ifs.pystructure.export.structure101.elements;

import org.jdom.Element;

public class AttributeElement extends Element {

	private static final long serialVersionUID = 1L;

	public AttributeElement(String name, String id) {
		super("submodule");
		
		this.setAttribute("type", "attribute");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}

}
