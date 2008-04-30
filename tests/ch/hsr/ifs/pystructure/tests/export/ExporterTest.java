package ch.hsr.ifs.pystructure.tests.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.export.structure101.Exporter;

public class ExporterTest extends TestCase {
	
	public void testExporter() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Exporter exporter = new Exporter(out);
		
		exporter.deactivateStats();
		
		try {
			exporter.export("s101g/examples/pydoku");
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
		/* Stupid simple test */
		assertTrue(out.size() > 9900); 
	}

}
