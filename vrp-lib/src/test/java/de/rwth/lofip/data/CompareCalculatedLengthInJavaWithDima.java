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
	
	@Test
	public void compareCalculatedLengthGaussKruegerInJavaWithDimaTest() {	
		Customer zsid920366 = new Customer(4232051.706, 5824214.417);
		Customer zsid148 = new Customer(4195871.246, 5787985.093);

		double length = new Edge(zsid920366, zsid148).getLength();
		System.out.println("Java: " + new Edge(zsid920366, zsid148).getLength() + "in Meter");
		System.out.println("in Kilometer: " + length/1000);
		double timeInHours = length/1000/60;
		System.out.println("Stunden bei 60 km/h " + timeInHours);
		double timeInSeconds = timeInHours * 3600;
		System.out.println("in Sekunden: " + timeInSeconds);
		double timeInSecondsIncludingUmwegefaktor = timeInSeconds * (1.414213562); 
		System.out.println("in Sekunden inklusive Umwegefaktor: " + timeInSecondsIncludingUmwegefaktor);
		System.out.println("Dima: 4140 seconds");
	}

}
