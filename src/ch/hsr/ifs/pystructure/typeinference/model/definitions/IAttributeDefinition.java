/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

/**
 * Interface for a definition that is an attribute of another definition, for
 * example methods, module functions and classes (of modules).
 * 
 * It's used for finding attribute references.
 */
public interface IAttributeDefinition {

	Definition getAttributeParent();

}
