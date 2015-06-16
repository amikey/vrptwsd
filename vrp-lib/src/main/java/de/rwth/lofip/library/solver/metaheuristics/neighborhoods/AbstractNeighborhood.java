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
		if (MathUtils.lessThan(move.getCost(), bestNonTabooMove.getCost()) || //hier weiﬂ man nicht, ob die Anzahl an Fahrzeugen verringert wird 
				(move.reducesNumberOfVehicles() && !bestNonTabooMove.reducesNumberOfVehicles())) // so werden moves bevorzugt, die die Fahrzeuganzahl verringern
			return true;
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
