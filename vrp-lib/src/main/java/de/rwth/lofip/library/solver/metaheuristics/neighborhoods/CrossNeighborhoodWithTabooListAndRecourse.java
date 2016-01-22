package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.math.MathUtils;

public class CrossNeighborhoodWithTabooListAndRecourse extends CrossNeighborhoodWithTabooList {

	public CrossNeighborhoodWithTabooListAndRecourse(ElementWithTours solution) {
		super(solution);
	}
	
	private List<SolutionGot> listOfParetoOptimalSolutions = new ArrayList<SolutionGot>();
	private List<AbstractNeighborhoodMove> listOfNonTabooMoves = new ArrayList<AbstractNeighborhoodMove>();

	@Override
	//here the moves have to be added to a list instead of just remembering the best move
	public void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
		assertEquals(true, move.isMoveValidWrtPositions());
		listOfNonTabooMoves.add(move);
	}
	
	@Override
	//this is just an empty hook in CrossNeighborhood
	protected void setBestNonTabooMoveHook() {
		sortMovesWrtDeterministicCost();
		takeFirstXNumberOfMoves();
		calculateRecourseCostForMoves();
		sortMovesWrtToStochasticAspectsAndSetBestMove();
	}
	
	public void sortMovesWrtDeterministicCost() {
		Comparator<AbstractNeighborhoodMove> byDeterministicCost = (e1,e2) -> Double.compare(e1.getCost(),e2.getCost());		
		Collections.sort(listOfNonTabooMoves, byDeterministicCost);				
	}
	
	//this exists only for testing
	public void sortMovesWrtCostDifference() {
		Comparator<AbstractNeighborhoodMove> byCostDifference = (e1,e2) -> Double.compare(e1.getCostDifferenceToPreviousSolution(),e2.getCostDifferenceToPreviousSolution());		
		Collections.sort(listOfNonTabooMoves, byCostDifference);
	}

	public void takeFirstXNumberOfMoves() {
		listOfNonTabooMoves = listOfNonTabooMoves.subList(0, Math.min(listOfNonTabooMoves.size(),Parameters.getNumberOfMovesThatStochasticCostIsCalculatedFor()));		
	}

	public void calculateRecourseCostForMoves() {
		for (AbstractNeighborhoodMove move : listOfNonTabooMoves) {
			RecourseCost oldRecourseCost = calculateRecourseCostOfGotsThatAreAffectedByMove(move);
			move.setRecourseCostForGotsThatAreAffectedByMoveForOldSolutionBeforeMoveIsApplied(oldRecourseCost);
			
			AbstractNeighborhoodMove copyOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
			applyMoveToUnderlyingGots(copyOfMove);
			
			RecourseCost newRecourseCost = calculateRecourseCostOfGotsThatAreAffectedByMove(copyOfMove);
			move.setRecourseCostForGotsThatAreAffectedByMoveForSolutionAfterMoveIsApplied(newRecourseCost);
		}		
	}

	public static void applyMoveToUnderlyingGots(AbstractNeighborhoodMove copyOfMove) {
		CrossNeighborhood.actuallyApplyMove(copyOfMove);		
	}

	private RecourseCost calculateRecourseCostOfGotsThatAreAffectedByMove(AbstractNeighborhoodMove move) {
		RecourseCost recourseCost = new RecourseCost(move.getGots());		
		return recourseCost;	
	}

	private void sortMovesWrtToStochasticAspectsAndSetBestMove() {
		sortMovesWrtToStochasticAspect();
		setBestNonTabooMoveToBestCalculatedMove();
		
//		throw new RuntimeException("impelement"); //s. Code unten
		
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
		Comparator<AbstractNeighborhoodMove> byDeterministicAndStochasticCost = (e1,e2) -> Double.compare(e1.getDeterministicAndStochasticCostDifference(),e2.getDeterministicAndStochasticCostDifference());		
		Collections.sort(listOfNonTabooMoves, byDeterministicAndStochasticCost);
	}
	
	private void setBestNonTabooMoveToBestCalculatedMove() {
		bestNonTabooMove = listOfNonTabooMoves.get(0);
	}

//	@Override 
//	//dieser  Override ist dazu da, das Akzeptanzkriterium gegenüber der deterministischen Suche zu verändern
//	protected boolean moveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCost(AbstractNeighborhoodMove move) {
//		//IMPORTANT_TODO: für die stochastische Variante müssen die Akzeptanzkriterien geändert werden.
//		//dabei sollte nicht so sehr die Tourenreduktion im Vordergrund stehen, sondern die Kosten
//		
//		//oben gesagtes ist hier schon teilweise umgesetz:
//		if (MathUtils.lessThan(move.getCost(), bestNonTabooMove.getCost()) || //hier weiß man nicht, ob die Anzahl an Fahrzeugen verringert wird 
//			(move.reducesNumberOfVehicles() && !bestNonTabooMove.reducesNumberOfVehicles())) // so werden moves bevorzugt, die die Fahrzeuganzahl verringern
//				return true;
//		return false;
//	}
	
	//Utilities
	
	public List<AbstractNeighborhoodMove> getListOfNonTabooMoves() {
		return listOfNonTabooMoves;
	}

	//this exists only for testing
	public AbstractNeighborhoodMove getFirstMoveInMoveList() {
		return listOfNonTabooMoves.get(0);
	}


}
