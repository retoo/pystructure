package ch.hsr.ifs.pystructure.export.structure101.representation;

import org.jdom.Element;

public class EMethod extends Element {

	private static final long serialVersionUID = 1L;

	public EMethod(String name, String id) {
		super("submodule");
		
		this.setAttribute("type", "method");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}
	
}
