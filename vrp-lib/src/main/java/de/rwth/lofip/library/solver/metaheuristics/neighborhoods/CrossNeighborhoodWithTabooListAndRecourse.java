package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.RecourseCost;

public class CrossNeighborhoodWithTabooListAndRecourse extends CrossNeighborhoodWithTabooList {

	public CrossNeighborhoodWithTabooListAndRecourse(ElementWithTours solution) {
		super(solution);
	}
	
	private List<SolutionGot> listOfParetoOptimalSolutions = new ArrayList<SolutionGot>();
	private List<AbstractNeighborhoodMove> listOfNonTabooMoves = new ArrayList<AbstractNeighborhoodMove>();

	@Override
	public void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
		listOfNonTabooMoves.add(move);
	}
	
	@Override
	protected void setBestNonTabooMove() {
		sortMovesWrtDeterministicCost();
		takeFirstXNumberOfMoves();
		calculateRecourseCostForMoves();
		sortMovesWrtToStochasticAspectsAndSetBestMove();
	}

	public void sortMovesWrtDeterministicCost() {
		Comparator<AbstractNeighborhoodMove> byDeterministicCost = (e1,e2) -> Double.compare(e1.getCost(),e2.getCost());		
		Collections.sort(listOfNonTabooMoves, byDeterministicCost);				
	}
	
	public void sortMovesWrtCostDifference() {
		Comparator<AbstractNeighborhoodMove> byCostDifference = (e1,e2) -> Double.compare(e1.getCostDifferenceToPreviousSolution(),e2.getCostDifferenceToPreviousSolution());		
		Collections.sort(listOfNonTabooMoves, byCostDifference);
	}

	public void takeFirstXNumberOfMoves() {
		listOfNonTabooMoves = listOfNonTabooMoves.subList(0, Math.min(listOfNonTabooMoves.size(),Parameters.getNumberOfMovesThatStochasticCostIsCalculatedFor()));		
	}

	private void calculateRecourseCostForMoves() {
		for (AbstractNeighborhoodMove move : listOfNonTabooMoves) {
			move.setOldRecourseCost(calculateRecourseCostOfMove(move));
			AbstractNeighborhoodMove copyOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
			applyMoveToUnderlyingGots(copyOfMove);
			move.setNewRecourseCost(calculateRecourseCostOfMove(copyOfMove));
		}		
	}

	public static void applyMoveToUnderlyingGots(AbstractNeighborhoodMove copyOfMove) {
		SolutionGot solution = new SolutionGot();
		solution.addGot(copyOfMove.getTour1().getParentGot());
		if (!copyOfMove.isParentGotOfTour2IsSameAsParentGotOfTour1())
			solution.addGot(copyOfMove.getTour2().getParentGot());
		CrossNeighborhood newNeighborhood = new CrossNeighborhood(solution);
		newNeighborhood.acctuallyApplyMove(copyOfMove);		
	}

	private RecourseCost calculateRecourseCostOfMove(
			AbstractNeighborhoodMove copyOfMove) {
		RecourseCost recourseCost = new RecourseCost(copyOfMove.getGots());		
		return recourseCost;	
	}

	private void sortMovesWrtToStochasticAspectsAndSetBestMove() {
		throw new RuntimeException("impelement"); //s. Code unten
		
//		//if exists element which is dominant in both regards -> done
//		for (AbstractNeighborhoodMove move : listOfNonTabooMoves)
//			if (isMovePareotoOptimalWrtParetoSet(move))
//				bestNonTabooMove = move;			
//		
//		if (bestNonTabooMove == null) {//no move pareto optimal
//			sortMovesWrtToStochasticAspect();
//			setBestNonTabooMoveToBestCalculatedMove();
//		}		
	}

	private void sortMovesWrtToStochasticAspect() {
		throw new RuntimeException("implement");
		
//		//IMPORTANT_TODO: hier zwischen Kosten und #RecourseActions alternieren, oder gucken wie Thomas das gemacht hat
//		Comparator<AbstractNeighborhoodMove> byDeterministicAndStochasticCost = (e1,e2) -> Double.compare(e1.getDeterministicAndStochasticCostDifference(),e2.getDeterministicAndStochasticCostDifference());		
//		Collections.sort(listOfNonTabooMoves, byDeterministicAndStochasticCost);
	}
	
	private void setBestNonTabooMoveToBestCalculatedMove() {
		bestNonTabooMove = listOfNonTabooMoves.get(0);
	}

	
	//Utilities
	
	public List<AbstractNeighborhoodMove> getListOfNonTabooMoves() {
		return listOfNonTabooMoves;
	}


}
