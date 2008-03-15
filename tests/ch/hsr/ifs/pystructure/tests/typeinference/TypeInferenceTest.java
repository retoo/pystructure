/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


import ch.hsr.ifs.pystructure.playground.Structure101Logger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.ITypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import ch.hsr.ifs.pystructure.utils.FileUtils;
import ch.hsr.ifs.pystructure.utils.TestUtils;
import ch.hsr.ifs.pystructure.utils.TestUtils.Marker;

public class TypeInferenceTest extends TestCase {

	private File file;
	private String sourceCode;
	private Workspace workspace;
	private ITypeInferencer inferencer;
	private Structure101Logger logger;

	public TypeInferenceTest(String name, File file, Workspace workspace,
			ITypeInferencer inferencer, Structure101Logger logger)
			throws IOException {
		super(name);
		this.file = file;
		this.sourceCode = FileUtils.read(file);
		this.workspace = workspace;
		this.inferencer = inferencer;
		this.logger = logger;
	}
	
	protected void runTest() throws Throwable {
		List<ExpressionTypeAssertion> assertions = extractAssertions();

		if (assertions.size() == 0) {
			return;
		}

		for (ExpressionTypeAssertion assertion : assertions) {
			logger.testStarted(assertion.filename, assertion.expression, assertion.line);
			assertion.check(file, inferencer, workspace);
		}
	}

	private List<ExpressionTypeAssertion> extractAssertions() {
		List<ExpressionTypeAssertion> assertions = new ArrayList<ExpressionTypeAssertion>();
		
		/* Fetch all markers and create a test case for each marker */
		for (Marker marker : TestUtils.getMarkers(sourceCode)) {
			String name = getName();
			
			ExpressionTypeAssertion expressionTypeAssertion = 
				new ExpressionTypeAssertion(
					name, 
					marker.expr, 
					marker.beginLine, 
					marker.type
				);
			
			assertions.add(expressionTypeAssertion);	
		}

		return assertions;
	}
}
