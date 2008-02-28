package ch.hsr.ifs.pystructure;

public class PyStructure {
	
	public static void main(String[] args) {
		System.out.println("Hello World");
		
		PyStructure pyStructure = new PyStructure();
		
		System.out.println(pyStructure.add(1, 2));
	}

	public int add(int i, int j) {
		return i + j;
	}
	
}
