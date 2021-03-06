package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestTourUtilsIsConcatenationOfSegmentsPossible {
	
	Tour tour;

	@Test
	public void testThatIsConcatenationOfSegmentsWorskCorrectly() {
		givenTourWithCustomer2();
		thenConcatenationShouldBePossibleWhenTryingToInsertCustomer3AtPosition0();
	}

	private void givenTourWithCustomer2() {
		tour = SetUpUtils.getTourWithCustomer2();
	}
	
	private void thenConcatenationShouldBePossibleWhenTryingToInsertCustomer3AtPosition0() {
		ResourceExtensionFunction ref = new ResourceExtensionFunction(SetUpUtils.getC3());
		int position = 0;
		assertEquals(true,TourUtils.isInsertionOfRefPossible(tour, ref, position));
	}
	
	@Test
	public void testThatIsConcatenationOfSegmentsWorskCorrectly2() {
		givenTourWithCustomer4();
		ThenConcatenationShouldBePossibleWhenTryingToInsertCustomer1AtPosition0();
	}

	private void givenTourWithCustomer4() {
		tour = SetUpUtils.getTourWithCustomer4();
	}
	
	private void ThenConcatenationShouldBePossibleWhenTryingToInsertCustomer1AtPosition0() {
		ResourceExtensionFunction ref = new ResourceExtensionFunction(SetUpUtils.getC1());
		int position = 0;
		assertEquals(true,TourUtils.isInsertionOfRefPossible(tour, ref, position));
	}
	
	@Test
	public void testThatIsConcatenationOfSegmentsWorskCorrectlyWhenSimultaniouslyDeletingAnotherSegment() {
		whenGivenTourWithC1C2();
		ThenConcatenationShouldBePossibleWhenTryingToInsertC33AtPos0AndDeletingC1();
	}

	private void whenGivenTourWithC1C2() {
		tour = SetUpUtils.getTourWithCustomer1And2();
	}
	
	private void ThenConcatenationShouldBePossibleWhenTryingToInsertC33AtPos0AndDeletingC1() {
		ResourceExtensionFunction ref = new ResourceExtensionFunction(SetUpUtils.getC3());
		int position = 0;
		int pos2 = 1;
		assertEquals(true,TourUtils.isInsertionOfRefPossible(tour, ref, position, pos2));
	}

}
