package de.rwth.lofip.cli.got;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import de.rwth.lofip.cli.util.Parameters;
import de.rwth.lofip.cli.util.SystemOutDeactivate;

public class Main {

	public static void main(String... args) throws IOException {    	
    	deactivateSystemOut();
    	Parameters.generateParametersAndSolveProblems();   	   	        
    }
	
	private static void deactivateSystemOut() throws FileNotFoundException {
		PrintStream out = new SystemOutDeactivate();
		System.setOut(out);		
	}
    

}
