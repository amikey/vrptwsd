package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class LocalSearchForTesting extends LocalSearchForElementWithTours {

	@Override
	protected void assertEqualsHook() {
		assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(solution));
		assertEquals(true, ElementWithToursUtils.isElementTWFeasible(solution));
		
		if (!MathUtils.equals(solution.getTotalDistanceWithCostFactor(), bestMove.getCost())) { 
			System.out.println("Gibt es in Solution Tour mit Kostenfaktor 2? :" + ((SolutionGot) solution).isExistsTourWithCostFactor2());
			System.out.println(solution.getAsTupel());
			System.out.println("solution.getTotalDistanceWithCostFactor(): " + solution.getTotalDistanceWithCostFactor());
			System.out.println("bestMove.getCost(): " + bestMove.getCost());
			System.out.println("bestMoveClone.getCost(): " + bestMoveClone.getCost());
						
			bestMoveClone.print();
		}
		assertEquals(solution.getTotalDistanceWithCostFactor(),bestMove.getCost(),0.001);
	}
}
