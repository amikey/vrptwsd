package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.NeighborhoodInterface;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.math.MathUtils;

public abstract class AbstractNeighborhood implements NeighborhoodInterface {
	
	protected ElementWithTours elementWithTours;
	
	protected AbstractNeighborhoodMove bestMoveThatMightBeTaboo = null;
	protected AbstractNeighborhoodMove bestNonTabooMove = null;
	
	protected boolean isMoveNewBestMove(AbstractNeighborhoodMove move) {
		return isSecondMoveBetterThanFirstMove(bestNonTabooMove, move);
	}

	public static boolean isSecondMoveBetterThanFirstMove(AbstractNeighborhoodMove bestNonTabooMove, AbstractNeighborhoodMove move) {	
		if (bestNonTabooMove == null) 
			return true;
		if (bestNonTabooMove.makesInfeasibleToursFeasible()) {
			if (move.makesInfeasibleToursFeasible() && secondMoveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCostMoreThanFirstMove(bestNonTabooMove, move))
				return true;
		} else // !bestNonTabooMove.makesInfeasibleToursFeasible()
			if (move.makesInfeasibleToursFeasible() || secondMoveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCostMoreThanFirstMove(bestNonTabooMove, move))
				return true;
		return false;			
	}
	
	protected static boolean secondMoveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCostMoreThanFirstMove(AbstractNeighborhoodMove bestNonTabooMove, AbstractNeighborhoodMove move) {	
		// hier werden moves hierarchisch geordnet:
		// zuerst werden moves bevorzugt, die die Anzahl der Fahrzeuge reduzieren
		// dann werden solche Moves bevorzugt, die die Anzahl an Kunden in einer Tour am meisten reduzieren
		// unter diesen moves wird der günstigste Move bevorzugt
		if (Parameters.shallTourNumberBeMinimized()) {		
			if (bestNonTabooMove.reducesNumberOfVehicles()) {
				if (!move.reducesNumberOfVehicles())
					return false;
				else // beide moves reduzieren Fahrzeuganzahl
					return isSecondMoveInducesLessCostThanFirstMove(bestNonTabooMove, move);
			} else { // bestNonTabooMove reduziert Fahrzeuganzahl nicht
				if (move.reducesNumberOfVehicles())
					return true;
				else { // beide moves reduzieren Fahrzeuganzahl nicht
					if (bestNonTabooMove.shortensShorterRespShortestTour()) {
						if (!move.shortensShorterRespShortestTour())
							return false;
						else { // beide moves reduzieren kürzere Tour
							if (bestNonTabooMove.shorterRespShortestTourResultsInNumberOfCustomers() < move.shorterRespShortestTourResultsInNumberOfCustomers())
								return false;
							if (bestNonTabooMove.shorterRespShortestTourResultsInNumberOfCustomers() > move.shorterRespShortestTourResultsInNumberOfCustomers())
								return true;
							// case: (bestNonTabooMove.shorterTourResultsInNumberOfCustomers() == move.shorterTourResultsInNumberOfCustomers())
							return isSecondMoveInducesLessCostThanFirstMove(bestNonTabooMove, move);
						}	
					} else // bestNonTabooMove reduziert kürzere Tour nicht
						if (move.shortensShorterRespShortestTour())
							return true;
						else 
							return isSecondMoveInducesLessCostThanFirstMove(bestNonTabooMove, move);
							
				}
			} 
		}else { // keine explizite Reduzierung der Tourenanzahl (bzw. weniger als oben)
			if (bestNonTabooMove.reducesNumberOfVehicles())
				if (!move.reducesNumberOfVehicles())
					return false;
				else // beide moves reduzieren Fahrzeuganzahl
					if (MathUtils.lessThan(move.getCost(), bestNonTabooMove.getCost()))
						return true;
					else // move reduziert Kosten nicht
						return false;
				else // bestNonTabooMove reduziert Fahrzeuganzahl nicht
					if (move.reducesNumberOfVehicles())
						return true;
					else // beide moves reduzieren Fahrzeuganzahl nicht
						if (MathUtils.lessThan(move.getCost(), bestNonTabooMove.getCost()))
							return true;
						else // move reduziert Kosten nicht
							return false;		
		}
	}
	
	private static boolean isSecondMoveInducesLessCostThanFirstMove(AbstractNeighborhoodMove bestNonTabooMove, AbstractNeighborhoodMove move) {
		if (MathUtils.lessThan(move.getCost(), bestNonTabooMove.getCost()))
			return true;
		else // move reduziert Kosten nicht
			return false;
	}
	
	protected boolean isMoveTaboo(AbstractNeighborhoodMove move, int iteration) { 
		return false;
	}
	
	protected void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
		bestNonTabooMove = move;
	}

	protected void setBestNonTabooMoveHook() {
		//nothing to do here; hook is needed for Recourse calculation
	}
	
	public void updateNeighborhoodWithSolution(ElementWithTours solution) {
		elementWithTours = solution;
	}
}
