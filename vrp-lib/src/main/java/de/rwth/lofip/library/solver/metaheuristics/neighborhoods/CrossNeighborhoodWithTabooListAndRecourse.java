package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.RecourseCost;

public class CrossNeighborhoodWithTabooListAndRecourse extends CrossNeighborhoodWithTabooList {

	public CrossNeighborhoodWithTabooListAndRecourse(ElementWithTours solution) {
		super(solution);
	}

	private List<AbstractNeighborhoodMove> listOfNonTabooMoves = new ArrayList<AbstractNeighborhoodMove>();
	static int numberOfMovesThatRecourseCostAreCalculatedFor = 10;

	@Override
	protected void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
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

	protected void takeFirstXNumberOfMoves() {
		listOfNonTabooMoves = listOfNonTabooMoves.subList(0, Math.min(listOfNonTabooMoves.size(),numberOfMovesThatRecourseCostAreCalculatedFor));		
	}

	private void calculateRecourseCostForMoves() {
		for (AbstractNeighborhoodMove move : listOfNonTabooMoves) {			
			AbstractNeighborhoodMove copyOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
			applyMoveToUnderlyingGots(copyOfMove);
			RecourseCost recourseCost = calculateRecourseCostOfMove(copyOfMove);
		}
		
	}

	private void applyMoveToUnderlyingGots(AbstractNeighborhoodMove copyOfMove) {
		SolutionGot solution = new SolutionGot();
		solution.addGot(copyOfMove.getTour1().getParentGot());
		if (!copyOfMove.isParentGotOfTour2IsSameAsParentGotOfTour1())
			solution.addGot(copyOfMove.getTour2().getParentGot());
		CrossNeighborhood newNeighborhood = new CrossNeighborhood(solution);
		newNeighborhood.acctuallyApplyMove(copyOfMove);		
	}

	private RecourseCost calculateRecourseCostOfMove(
			AbstractNeighborhoodMove copyOfMove) {
//		Double recourseCost = copyOfMove.getTour1().getParentGot().getExpectedRecourseCost();
	}

	private void sortMovesWrtToStochasticCost() {
		// TODO Auto-generated method stub
		
	}

	private void setBestNonTabooMoveToBestCalculatedMove() {
		// TODO Auto-generated method stub
		
	}

	
	//Utilities
	
	public List<AbstractNeighborhoodMove> getListOfNonTabooMoves() {
		return listOfNonTabooMoves;
	}
}
