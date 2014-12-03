package de.rwth.lofip.library.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;

public class VrpUtils_TEST {	

	/**
	 * Test, dass Daten richtig eingelesen werden.
	 * @throws IOException 
	 */
	@Test
	public void testDassDatenRichtigEingelesenWerden() throws IOException {
		VrpProblem vrpProblem = SetUpR201Problem();
		System.out.println(printVrpProblem(vrpProblem));
	}
	
	private String printVrpProblem(VrpProblem vrpProblem) {
		String s = "";
		s += "Description: " + vrpProblem.getDescription() + "\n";
		s += "Vehicle Number: " + vrpProblem.getVehicleCount() + "\n";	
		s += "Capacity: " + vrpProblem.getVehicles().iterator().next().getCapacity() + "\n";
		s += "\n";
		List<Customer> customers = new LinkedList<Customer>();
		for (Customer c : vrpProblem.getCustomers())
			customers.add(c);
		Collections.sort(customers, new CustomerNoComparator());
		
		for (Customer c : customers)
			s += c.getCompleteDescription() + "\n";
		return s;
	}
	
	private VrpProblem SetUpR201Problem() throws IOException {
		//setUp();
		//---------VRP------------------------------------------------
		File datei1 = new File("original-solomon-problems/rc201.txt");
		BufferedReader br = new BufferedReader(new FileReader(datei1));
		String zeile = "";
		List<String> liste = new ArrayList<String>(0);
		while ((zeile = br.readLine()) != null) {
			liste.add(zeile);
		}
		
		VrpProblem vrpProblem = VrpUtils
                .createProblemFromStringList(liste);
		br.close();
		
		return vrpProblem;
	}

	
	private class CustomerNoComparator implements Comparator<Customer> {

		public int compare(Customer c1, Customer c2) {
			if (c1.getCustomerNo() < c2.getCustomerNo())
				return -1;
			if (c1.getCustomerNo() == c2.getCustomerNo())
				return 0;
			return 1;
		}
	}
	
}
