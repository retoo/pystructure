package ch.hsr.ifs.pystructure;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.export.structure101.Exporter;

public final class CLI {

	private CLI() {
	}
	
	public static void main(String[] args) {
		List<File> sourceDirs = new LinkedList<File>();
		List<String> ignores = new LinkedList<String>();
		boolean showHelp = (args.length == 0);
		for (String arg : args) {
			if (arg.equals("-h") || arg.equals("--help")) {
				showHelp = true;
				break;
			}
			File file = new File(arg);
			if (file.isDirectory()) {
				sourceDirs.add(file);
			} else {
				ignores.add(arg);
			}
		}
		
		if (showHelp) {
			System.out.println("Usage: " + "pystructure" + " SOURCEDIR [SOURCEDIR]...");
			return;
		}
		
		for (String ignore : ignores) {
			System.err.println("Argument is no directory, ignoring: " + ignore);
		}
		
		Exporter exporter = new Exporter(System.out);
		try {
			exporter.export(sourceDirs);
		} catch (IOException e) {
			System.err.println("IOException occurred during export.");
		}
	}

}
