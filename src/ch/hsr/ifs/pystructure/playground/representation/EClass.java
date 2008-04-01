package ch.hsr.ifs.pystructure.playground.representation;

import org.jdom.Element;

public class EClass extends Element {

	private static final long serialVersionUID = 1L;

	public EClass(String name, String id) {
		super("module");
		
		this.setAttribute("type", "class");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}

}
