package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public interface NeighborhoodInterface {

    public AbstractNeighborhoodMove returnBestMove();
    
}
