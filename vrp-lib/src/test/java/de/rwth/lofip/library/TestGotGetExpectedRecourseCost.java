package de.rwth.lofip.library;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestGotGetExpectedRecourseCost {

	GroupOfTours got;
	
	@Test
	public void testGetExpectedRecourseCostInGot() throws IOException {
		got = SetUpUtils.getSomeExampleGotFromRC103();		
		
		System.out.println(got.getTour(0).getDemandOnTour());
		System.out.println(got.getTour(1).getDemandOnTour());
		got.getTour(0).getVehicle().setCapacity(106.0);
		got.getTour(1).getVehicle().setCapacity(103.0);
		
		thenCaluclationOfRecourseCostInGotShouldBeCorrect();
	}

	private void thenCaluclationOfRecourseCostInGotShouldBeCorrect() {
		RecourseCost rc = got.getExpectedRecourseCost();
		rc.print();
	}
	
}
