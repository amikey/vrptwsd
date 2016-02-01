package de.rwth.lofip.library.solver.insertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.PrintUtils;

public class RandomnessInGreedyInsertionTest {

	@Test
	 public void testRandomnessInGreedyInsertion() throws IOException {
		 VrpProblem problem = ReadAndWriteUtils.readSolomonProblemC101().get(0);
		 Customer c1 = problem.getCustomerWithCustomerNo(18);
		 Customer c2 = problem.getCustomerWithCustomerNo(28);
		 ArrayList<Customer> list = new ArrayList<Customer>();
		 list.add(c1);
		 list.add(c2);
		
		 
		 Random rnd = new Random(1);
		 for (int i = 0; i < 10000; i+=1) {
			 Collections.shuffle(list,rnd);
			 PrintUtils.printListOfCustomers(list);
		 }		 
	}

}
