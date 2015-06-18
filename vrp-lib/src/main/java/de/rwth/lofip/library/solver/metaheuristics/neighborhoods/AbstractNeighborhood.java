package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.math.MathUtils;

public abstract class AbstractNeighborhood {
	
	protected AbstractNeighborhoodMove bestMoveThatMightBeTaboo = null;
	protected AbstractNeighborhoodMove bestNonTabooMove = null;

	protected boolean isMoveNewBestMove(AbstractNeighborhoodMove move) {	
		if (bestNonTabooMove == null) 
			return true;
		if (bestNonTabooMove.makesInfeasibleToursFeasible()) {
			if (move.makesInfeasibleToursFeasible() && moveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCost(move))
				return true;
		} else // !bestNonTabooMove.makesInfeasibleToursFeasible()
			if (move.makesInfeasibleToursFeasible() || moveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCost(move))
				return true;
		return false;			
	}
	
	protected boolean moveReducesNumberOfVehiclesOrShortensShortestTourOrReducesCost(AbstractNeighborhoodMove move) {
		//IMPORTANT_TODO: für die stochastische Variante müssen die Akzeptanzkriterien geändert werden.
		//dabei sollte nicht so sehr die Tourenreduktion im Vordergrund stehen, sondern die Kosten
		
		// hier werden moves hierarchisch geordnet:
		// zuerst werden moves bevorzugt, die die Anzahl der Fahrzeuge reduzieren
		// dann werden solche Moves bevorzugt, die die Anzahl an Kunden in einer Tour am meisten reduzieren
		// unter diesen moves wird der günstigste Move bevorzugt
		if (bestNonTabooMove.reducesNumberOfVehicles()) {
			if (!move.reducesNumberOfVehicles())
				return false;
			else // beide moves reduzieren Fahrzeuganzahl
				return isMoveInducesLessCostThanBestNonTabooMove(move);
		} else { // bestNonTabooMove reduziert Fahrzeuganzahl nicht
			if (move.reducesNumberOfVehicles())
				return true;
			else { // beide moves reduzieren Fahrzeuganzahl nicht
				if (bestNonTabooMove.shortensShorterTour()) {
					if (!move.shortensShorterTour())
						return false;
					else { // beide moves reduzieren kürzere Tour
						if (bestNonTabooMove.shorterTourResultsInNumberOfCustomers() < move.shorterTourResultsInNumberOfCustomers())
							return false;
						if (bestNonTabooMove.shorterTourResultsInNumberOfCustomers() > move.shorterTourResultsInNumberOfCustomers())
							return true;
						// case: (bestNonTabooMove.shorterTourResultsInNumberOfCustomers() == move.shorterTourResultsInNumberOfCustomers())
						return isMoveInducesLessCostThanBestNonTabooMove(move);
					}	
				} else // bestNonTabooMove reduziert kürzere Tour nicht
					if (move.shortensShorterTour())
						return true;
					else 
						return isMoveInducesLessCostThanBestNonTabooMove(move);
						
			}
		}				
	}
	
	private boolean isMoveInducesLessCostThanBestNonTabooMove(AbstractNeighborhoodMove move) {
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

	protected void setBestNonTabooMove() {
		//nothing to do here; hook is needed for Recourse calculation
	}
}
