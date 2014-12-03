package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.Set;

import de.rwth.lofip.library.solver.util.TabuTourPosition;

public interface NeighborhoodInterface extends TabuInsertionInterface {

    public Set<TabuTourPosition> getTabuPositions();
    
}
