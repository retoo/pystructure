package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class MetaClassTypeNew extends AbstractType {
	
	private final Module module;
	private final Class klass;

	public MetaClassTypeNew(Module module, Class klass) {
		super("metaclass");
		this.module = module;
		this.klass = klass;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Class getKlass() {
		return klass;
	}
	
}
