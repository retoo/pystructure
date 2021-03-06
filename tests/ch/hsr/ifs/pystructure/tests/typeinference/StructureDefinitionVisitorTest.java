/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

// TODO: Maybe move this test somewhere else?
public class StructureDefinitionVisitorTest extends TestCase {

	private Module module;
	
	@Override
	protected void setUp() throws Exception {
		File path = new File("tests/python/typeinference/structure");
		Workspace workspace = new Workspace(path);
		
		module = workspace.getModule("module");
	}
	
	public void testChildren() {
		assertEquals(3, module.getChildren().size());
		
		StructureDefinition classA = module.getChildren().get(0);
		assertEquals("A", classA.getName());
		assertEquals(2, classA.getChildren().size());
		
		StructureDefinition methodInit = (StructureDefinition) classA.getChildren().get(0);
		assertEquals("__init__", methodInit.getName());
		assertEquals(0, methodInit.getChildren().size());
		
		StructureDefinition methodFoo = (StructureDefinition) classA.getChildren().get(1);
		assertEquals("foo", methodFoo.getName());
		assertEquals(1, methodFoo.getChildren().size());
		
		StructureDefinition functionBar = (StructureDefinition) methodFoo.getChildren().get(0);
		assertEquals(0, functionBar.getChildren().size());
		
		
		StructureDefinition classB = module.getChildren().get(1);
		assertEquals(1, classB.getChildren().size());
		
		StructureDefinition classC = (StructureDefinition) classB.getChildren().get(0);
		assertEquals(0, classC.getChildren().size());
		
		
		StructureDefinition functionToplevel = module.getChildren().get(2);
		assertEquals(0, functionToplevel.getChildren().size());
	}

}
