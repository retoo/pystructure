/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import ch.hsr.ifs.pystructure.playground.Structure101Logger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CombinedLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class TypeInferenceSuite extends TestSuite {

	private PythonTypeInferencer inferencer;

	private class PythonFilenameFilter implements FilenameFilter {
		public boolean accept(File file, String name) {
			return name.endsWith(".py");
		}
	}

	public static Test suite() {
		return new TypeInferenceSuite("tests/python/typeinference/");
	}

	public TypeInferenceSuite(String testsDirectory) {
		super(testsDirectory);
		File dir = new File(testsDirectory);
		List<File> files = Arrays.asList(dir.listFiles(new PythonFilenameFilter()));
		
		/*
		 * Shuffle test files using a seed so that a test failure caused by the
		 * run order can be reproduced.
		 */
		long seed = System.currentTimeMillis();
		System.out.println("Seed used for shuffling test files: " + seed);
		Collections.shuffle(files, new Random(seed));
		
		Structure101Logger s101log = new Structure101Logger();
		IGoalEngineLogger logger = new CombinedLogger(s101log);
		inferencer = new PythonTypeInferencer(logger);
		
		Workspace workspace = new Workspace(new File(testsDirectory));
		
		for (File file : files) {
			try {
				TestCase test = new TypeInferenceTest(file.getName(), file, workspace, inferencer, s101log);
				addTest(test);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void run(TestResult result) {
		super.run(result);
		inferencer.shutdown();
	}

}
