
package ch.hsr.ifs.pystructure.tests.typeannotator;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.playground.TypeAnnotator;
import ch.hsr.ifs.pystructure.utils.FileUtils;

public class AnnotatorTest extends TestCase {
	
	public void testExporter() {
		String outFolder = System.getProperty("output.typeannotator", "tmp/typeannotator-test/");
		System.err.print(System.getProperties().toString());
		FileUtils.mkdir(new File(outFolder));
		
		try {
			TypeAnnotator ta = new TypeAnnotator(new File("s101g/examples/pydoku"), outFolder, null);
			ta.generateReport();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
