package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchWithTourEliminationTest {

	private SolutionGot solution;

	@Test
	public void testTSWithTourEliminationOnSomeSolutionForR101() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfNonImprovingIterationsInTS(20);
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setNumberOfInsertionTries(1);
		Parameters.setMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood(4);
		Parameters.setPublishSolutionValueProgress(true);
		
		GivenSolutionForR101();
		ThenTSSWithTourEliminationShouldNotThrowAnyErrors();
	}

	private void GivenSolutionForR101() throws IOException {
		solution = SetUpUtils.getSomeSolutionForRC104Problem(); 
		solution.printSolutionCost();
	}
	
	private void ThenTSSWithTourEliminationShouldNotThrowAnyErrors() throws IOException {
		TabuSearchForElementWithTours tabuSearch = new TSWithTourEliminationNeighborhood();
		tabuSearch.improve(solution);
	}
	
}
