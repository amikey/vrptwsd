package de.rwth.lofip.data;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;

public class CompareCalculatedLengthInJavaWithDima {
	
	@Test
	public void compareCalculatedLengthInJavaWithDimaTest() {	
		Customer zsid920366 = new Customer(8.05337, 52.48518);
		Customer zsid148 = new Customer(7.55502, 52.14164);
		
		System.out.println("Java: " + new Edge(zsid920366, zsid148).getLength());
		System.out.println("Dima: 4140 seconds");
	}

}
