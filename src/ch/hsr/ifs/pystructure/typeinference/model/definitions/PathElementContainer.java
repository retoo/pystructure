package ch.hsr.ifs.pystructure.typeinference.model.definitions;

public interface PathElementContainer {

	void addChild(PathElement child);
	PathElement getChild(String name);
	PathElementContainer getParent();

}
