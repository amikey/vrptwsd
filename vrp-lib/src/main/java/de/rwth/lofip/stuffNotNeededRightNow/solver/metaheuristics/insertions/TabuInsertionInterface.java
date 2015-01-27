package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

import java.util.Set;

import de.rwth.lofip.library.solver.util.TabuTourPosition;

/**
 * Interface which all insertion heuristics, used by the
 * {@code TabuSearchHeuristic}, need to implement.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface TabuInsertionInterface extends InsertionInterface {

	public void setTabuPositions(Set<TabuTourPosition> tabuPositions);
    
}
