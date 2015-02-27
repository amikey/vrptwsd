package de.rwth.lofip.library.solver.metaheuristics.interfaces;

import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public interface NeighborhoodInterface {

	AbstractNeighborhoodMove returnBestMove() throws Exception;

}
