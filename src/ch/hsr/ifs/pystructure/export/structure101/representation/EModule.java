package ch.hsr.ifs.pystructure.export.structure101.representation;

import org.jdom.Element;

public class EModule extends Element {

	private static final long serialVersionUID = 1L;
	
	public EModule(String name, String id) {
		super("module");
		
		this.setAttribute("type", "module");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}

}
