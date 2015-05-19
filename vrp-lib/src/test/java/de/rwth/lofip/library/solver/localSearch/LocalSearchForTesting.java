package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;

public class LocalSearchForTesting extends LocalSearchForElementWithTours {

	@Override
	protected void assertEqualsHook() {
		assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(solution));
		assertEquals(true, ElementWithToursUtils.isElementTWFeasible(solution));
		assertEquals(solution.getTotalDistance(),bestMove.getCost(),0.001);
	}
}
