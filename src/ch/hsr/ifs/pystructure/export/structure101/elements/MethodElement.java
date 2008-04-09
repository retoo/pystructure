package ch.hsr.ifs.pystructure.export.structure101.elements;

import org.jdom.Element;

public class MethodElement extends Element {

	private static final long serialVersionUID = 1L;

	public MethodElement(String name, String id) {
		super("submodule");
		
		this.setAttribute("type", "method");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}
	
}
