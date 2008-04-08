package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class ClassTypeNew extends AbstractType {
	
	private final Module module;
	private final Class klass;
	
	public ClassTypeNew(String typeName) {
		super(typeName);
		/* FIXME this smells somehow, shouldn't we use a different class if we don't know
		 * the klass and module ? */
		this.module = null;
		this.klass = null;
	}

	public ClassTypeNew(Module module, Class klass) {
		super(klass.getName());
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
