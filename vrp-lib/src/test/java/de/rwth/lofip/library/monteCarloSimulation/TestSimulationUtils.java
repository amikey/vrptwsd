package de.rwth.lofip.library.monteCarloSimulation;

import static org.junit.Assert.assertEquals;

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
			assertEquals(true, c.getDemand() >= SetUpUtils.getC1().getDemand() * 0.8);
			assertEquals(true, c.getDemand() <= SetUpUtils.getC1().getDemand() * 1.2);
		}
		
	}

}
