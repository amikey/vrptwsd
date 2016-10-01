package de.rwth.lofip.library.monteCarloSimulation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestSimulationUtils {
	
	private GroupOfTours got1;

	@Test
	public void testSetDemandForCustomersWithDeviation() {
		givenOneGot();
		whenDemandForCustomersIsSetWithDeviation();
		thenDemandForCustomersShouldBeWithinSpecifiedRange();
		
		Parameters.setAllParametersToDefaultValues();
	}

	private void givenOneGot() {
		got1 = SetUpUtils.getGotWithCustomer1And2();
	}

	private void whenDemandForCustomersIsSetWithDeviation() {
		Parameters.setFluctuationOfDemandTo(0.2);
		SimulationUtils.generateDemandsForNextScenario();
		SimulationUtils.setDemandForCustomersWithDeviation(got1);
	}

	private void thenDemandForCustomersShouldBeWithinSpecifiedRange() {
		for (Customer c : got1.getCustomers()) {
			System.out.println(c.getDemand());
			//C1.getDemand() equals C2.getDemand() (=20)
			assertEquals(true, c.getDemand() >= SetUpUtils.getC1().getDemand() * 0.8);
			assertEquals(true, c.getDemand() <= SetUpUtils.getC1().getDemand() * 1.2);
		}
	}
	
	@Test
	public void testDemandDeviationForCorrelatedCustomers() throws IOException {
		givenSomeGot();
		Parameters.setNumberOfCustomersInCorrelatedGroup(5);
		whenDemandForCustomersIsSetWithDeviation();
		
		thenAllCustomersInCorrelatedGroupShouldHaveTheSameRelativeStandardDeviation();
		
	}

	private void givenSomeGot() throws IOException {
		got1 = SetUpUtils.getSomeExampleGotFromRC103();
	}
	
	private void thenAllCustomersInCorrelatedGroupShouldHaveTheSameRelativeStandardDeviation() {
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.addAll(got1.getCustomers());
		sortListWrtToCustomerNo(customerList);
		
		System.out.println(customerList.size());
		printCustomerRSD(customerList);		
		
		int numberOfCustomersInGroup = Parameters.getNumberOfCustomersInCorrelatedGroup();
		List<Customer> customerGroupList = customerList.subList(0, numberOfCustomersInGroup);
		assertThatAllCustomersInGroupHaveTheSameStandardDeviation(customerGroupList);
		
	}

	private static void sortListWrtToCustomerNo(List<Customer> customerList) {
		Comparator<Customer> customerByNoComparator = (e1,e2) -> Double.compare(e1.getCustomerNo(), e2.getCustomerNo());
		Collections.sort(customerList, customerByNoComparator);
	}
	
	private void printCustomerRSD(List<Customer> customerList) {
		for (Customer c1: customerList) {
			double rsdC1 = (((double) c1.getDemand()) / ((double) c1.getOriginalDemand()));
			System.out.println(rsdC1 + ": " + c1.getDemand() + "/" + c1.getOriginalDemand());
		}	
	}
	
	private void assertThatAllCustomersInGroupHaveTheSameStandardDeviation(List<Customer> customerGroupList) {
		Customer c = customerGroupList.get(0);
		double relativeStandardDeviation = (c.getDemand() / c.getOriginalDemand());
		for (Customer c1 : customerGroupList) {
			double rsdC1 = (c1.getDemand() / c1.getOriginalDemand());
			assertEquals(relativeStandardDeviation,rsdC1,0.001);
		}
	}

}
