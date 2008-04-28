/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.playground.Structure101Logger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;
import ch.hsr.ifs.pystructure.utils.FileUtils;
import ch.hsr.ifs.pystructure.utils.TestUtils;
import ch.hsr.ifs.pystructure.utils.TestUtils.Marker;

public class TypeInferenceTest extends TestCase {

	private File file;
	private String sourceCode;
	private Workspace workspace;
	private PythonTypeInferencer inferencer;
	private Structure101Logger logger;

	public TypeInferenceTest(String name, File file, Workspace workspace,
			PythonTypeInferencer inferencer, Structure101Logger logger)
			throws IOException {
		super(name);
		this.file = file;
		this.sourceCode = FileUtils.read(file);
		this.workspace = workspace;
		this.inferencer = inferencer;
		this.logger = logger;
	}
	
	protected void runTest() throws Throwable {
		List<InferencerAssertion> assertions = extractAssertions();

		for (InferencerAssertion assertion : assertions) {
			logger.testStarted(assertion.filename, assertion.expression, assertion.line);
			assertion.check(file, inferencer, workspace);
		}
	}

	private List<InferencerAssertion> extractAssertions() {
		List<InferencerAssertion> assertions = new ArrayList<InferencerAssertion>();
		
		/* Fetch all markers and create a test case for each marker */
		for (Marker marker : TestUtils.getMarkers(sourceCode)) {
			String name = getName();
			
			InferencerAssertion expressionTypeAssertion = 
				marker.markerType == Marker.Type.TYPE 
					? new ExpressionTypeAssertion(
							name, 
							marker.expr, 
							marker.beginLine, 
							marker.type)
					: new MROAssertion(
							name,
							marker.expr,
							marker.beginLine,
							marker.type);
				
			
			
			assertions.add(expressionTypeAssertion);	
		}

		return assertions;
	}
}
