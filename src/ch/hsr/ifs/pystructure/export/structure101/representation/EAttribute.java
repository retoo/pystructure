package ch.hsr.ifs.pystructure.export.structure101.representation;

import org.jdom.Element;

public class EAttribute extends Element {

	private static final long serialVersionUID = 1L;

	public EAttribute(String name, String id) {
		super("submodule");
		
		this.setAttribute("type", "attribute");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}

}
