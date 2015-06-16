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
			if (move.makesInfeasibleToursFeasible() && moveReducesCostOrNumberOfVehicles(move))
				return true;
		} else // !bestNonTabooMove.makesInfeasibleToursFeasible()
			if (move.makesInfeasibleToursFeasible() || moveReducesCostOrNumberOfVehicles(move))
				return true;
		return false;			
	}
	
	private boolean moveReducesCostOrNumberOfVehicles(AbstractNeighborhoodMove move) {
		// hier werden moves hierarchisch geordnet:
		// zuerst werden moves bevorzugt, die die Anzahl der Fahrzeuge reduzieren
		// unter diesen moves wird der günstigste Move bevorzugt
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
