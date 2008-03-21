package ch.hsr.ifs.pystructure.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class LineIterator implements Iterable<String>, Iterator<String> {

	private BufferedReader bufferedReader;
	private String nextLine;

	public LineIterator(Reader reader) {
		bufferedReader = new BufferedReader(reader);
		
		readNextLine();
	}

	private void readNextLine() {
		try {
			nextLine = bufferedReader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Iterator<String> iterator() {
		return this;
	}

	public boolean hasNext() {
		return nextLine != null;
	}

	public String next() {
		String currentLine = nextLine;
		readNextLine();
		return currentLine;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
