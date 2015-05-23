package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.RecourseCost;

public class CrossNeighborhoodWithTabooListAndRecourse extends CrossNeighborhoodWithTabooList {

	public CrossNeighborhoodWithTabooListAndRecourse(ElementWithTours solution) {
		super(solution);
	}

	private List<AbstractNeighborhoodMove> listOfNonTabooMoves = new ArrayList<AbstractNeighborhoodMove>();
	//CODE_SMELL_TODO: eine Parameterklasse anlegen
	private static int numberOfMovesThatRecourseCostAreCalculatedFor = 10;

	@Override
	public void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
		listOfNonTabooMoves.add(move);
	}
	
	@Override
	protected void setBestNonTabooMove() {
		sortMovesWrtDeterministicCost();
		takeFirstXNumberOfMoves();
		calculateRecourseCostForMoves();
		sortMovesWrtToStochasticCost();
		setBestNonTabooMoveToBestCalculatedMove();
	}

	public void sortMovesWrtDeterministicCost() {
		Comparator<AbstractNeighborhoodMove> byDeterministicCost = (e1,e2) -> Double.compare(e1.getCost(),e2.getCost());		
		Collections.sort(listOfNonTabooMoves, byDeterministicCost);		
	}

	public void takeFirstXNumberOfMoves() {
		listOfNonTabooMoves = listOfNonTabooMoves.subList(0, Math.min(listOfNonTabooMoves.size(),getNumberOfMovesThatRecourseCostAreCalculatedFor()));		
	}

	private void calculateRecourseCostForMoves() {
		for (AbstractNeighborhoodMove move : listOfNonTabooMoves) {			
			AbstractNeighborhoodMove copyOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
			applyMoveToUnderlyingGots(copyOfMove);
			RecourseCost recourseCost = calculateRecourseCostOfMove(copyOfMove);
		}
		
	}

	public static void applyMoveToUnderlyingGots(AbstractNeighborhoodMove copyOfMove) {
		//IMPORTANT_TODO: mit clonen wurden die Kosten nicht richtig berechnet in normaler TS und LS
		SolutionGot solution = new SolutionGot();
		solution.addGot(copyOfMove.getTour1().getParentGot());
		if (!copyOfMove.isParentGotOfTour2IsSameAsParentGotOfTour1())
			solution.addGot(copyOfMove.getTour2().getParentGot());
		CrossNeighborhood newNeighborhood = new CrossNeighborhood(solution);
		newNeighborhood.acctuallyApplyMove(copyOfMove);		
	}

	private RecourseCost calculateRecourseCostOfMove(
			AbstractNeighborhoodMove copyOfMove) {
		throw new RuntimeException("calculateRecourseCostOfMove noch nicht implementiert");
//		Double recourseCost = copyOfMove.getTour1().getParentGot().getExpectedRecourseCost();
	}

	private void sortMovesWrtToStochasticCost() {
		throw new RuntimeException("sortMovesWrtToStochasticCost() noch nicht implementiert");
	}

	private void setBestNonTabooMoveToBestCalculatedMove() {
		throw new RuntimeException("setBestNonTabooMoveToBestCalculatedMove() noch nicht implementiert");
	}

	
	//Utilities
	
	public List<AbstractNeighborhoodMove> getListOfNonTabooMoves() {
		return listOfNonTabooMoves;
	}

	public static int getNumberOfMovesThatRecourseCostAreCalculatedFor() {
		return numberOfMovesThatRecourseCostAreCalculatedFor;
	}

	public static void setNumberOfMovesThatRecourseCostAreCalculatedFor(
			int numberOfMovesThatRecourseCostAreCalculatedFor) {
		CrossNeighborhoodWithTabooListAndRecourse.numberOfMovesThatRecourseCostAreCalculatedFor = numberOfMovesThatRecourseCostAreCalculatedFor;
	}
}
