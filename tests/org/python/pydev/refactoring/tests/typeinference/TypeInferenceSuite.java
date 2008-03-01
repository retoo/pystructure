/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.python.pydev.refactoring.tests.typeinference;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.python.pydev.refactoring.typeinference.inferencer.PythonTypeInferencer;
import org.python.pydev.refactoring.typeinference.visitors.Workspace;

public class TypeInferenceSuite extends TestSuite {
	
	public TypeInferenceSuite(String testsDirectory) {
		super(testsDirectory);
		File dir = new File(testsDirectory);
		
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.matches(".+\\.py$");
			}
		});
		
		PythonTypeInferencer inferencer = new PythonTypeInferencer();
		
		LinkedList<String> sysPath = new LinkedList<String>();
		Workspace workspace = new Workspace("tests/python/typeinference", sysPath);
		
		for (File file : files) {
			try {
				TestCase test = new TypeInferenceTest(file.getName(), file, inferencer, workspace);
				addTest(test);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
