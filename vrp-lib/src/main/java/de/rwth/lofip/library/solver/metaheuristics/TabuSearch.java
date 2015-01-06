package de.rwth.lofip.library.solver.metaheuristics;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.IntermediateSolutionGot;

public class TabuSearch implements MetaSolverInterfaceGot {
	
	private Tour tour1;
	private Tour tour2;


	private static int positionTour1Level1 = 0;
	private static int positionTour1Level2 = 0;
	private static int positionTour2Level1 = 0;
	private static int positionTour2Level2 = 0;
	
	private static CustomerInTour startOfSegmentToBeRemovedInTour1;
	private static CustomerInTour endOfSegmentToBeRemovedInTour1;
	private static CustomerInTour startOfSegmentToBeRemovedInTour2;
	private static CustomerInTour endOfSegmentToBeRemovedInTour2;
	
	public TabuSearch(Tour tour1, Tour tour2) {
		this.tour1 = tour1;
		this.tour2 = tour2;
		positionTour1Level1 = 0;
		positionTour1Level2 = 0;
		positionTour2Level1 = 0;
		positionTour2Level2 = 0;
		startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level1);
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level2);
		startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level1);
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level2);
	}

	protected void generateNextNeigborhoodStep() {		
		if (segmentInTour2CanBeIncreased()) 
			increaseSegmentInTour2();	
		else if (segmentInTour1CanBeIncreased()) {
				resetSegmentInTour2();
				increaseSegmentInTour1();
		} else throw new RuntimeException("generateNextNeigborhoodStep was called although no next step exists");
	}
	
	private boolean segmentInTour2CanBeIncreased() {
		if (startOfSegementToBeRemovedCanBeIncreasedInTour2() || endOfSegmentToBeRemovedCanBeIncreasedInTour2())
			return true;
		else 
			return false;
	}

	private void increaseSegmentInTour2() {
		if (endOfSegmentToBeRemovedCanBeIncreasedInTour2())
			tour2IncreaseEndOfSegmentToBeRemoved();
		else if (startOfSegementToBeRemovedCanBeIncreasedInTour2())
			tour2IncreaseStartOfSegmentToBeRemoved();
		else throw new RuntimeException("generateNextNeigborhoodStep() was called although no remaining NeigborhoodStep exists");
	}

	private boolean endOfSegmentToBeRemovedCanBeIncreasedInTour2() {
		return positionTour2Level2 < tour2.length()-1;
	}
	
	private void tour2IncreaseEndOfSegmentToBeRemoved() {
		positionTour2Level2++;
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level2);
	}

	private boolean startOfSegementToBeRemovedCanBeIncreasedInTour2() {
		return positionTour2Level1 < tour2.length()-1;	
	}
	
	private void tour2IncreaseStartOfSegmentToBeRemoved() {
		positionTour2Level1++;
		positionTour2Level2=positionTour2Level1;
		startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level1);
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level1);
	}
	
	private void resetSegmentInTour2() {
		positionTour2Level1 = 0;
		positionTour2Level2 = 0;		
		startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level1);
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level2);
	}
	
	private boolean segmentInTour1CanBeIncreased() {
		if (startOfSegementToBeRemovedCanBeIncreasedInTour1() || endOfSegmentToBeRemovedCanBeIncreasedInTour1())
			return true;
		else 
			return false;
	}

	private void increaseSegmentInTour1() {
		if (endOfSegmentToBeRemovedCanBeIncreasedInTour1())
			increaseEndOfSegmentToBeRemovedInTour1();
		else if (startOfSegementToBeRemovedCanBeIncreasedInTour1())
			increaseStartOfSegmentToBeRemovedInTour1();
		else throw new RuntimeException("generateNextNeigborhoodStep() was called although no remaining NeigborhoodStep exists");	
	}
	
	private boolean endOfSegmentToBeRemovedCanBeIncreasedInTour1() {
		return positionTour1Level2 < tour1.length()-1;
	}
	
	private void increaseEndOfSegmentToBeRemovedInTour1() {
		positionTour1Level2++;
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level2);
	}
	
	private boolean startOfSegementToBeRemovedCanBeIncreasedInTour1() {
		return positionTour1Level1 < tour1.length()-1;	
	}
	
	private void increaseStartOfSegmentToBeRemovedInTour1() {
		positionTour1Level1++;
		positionTour1Level2=positionTour1Level1;
		startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level1);
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level1);
	}
	
	
	

	public Collection<SolutionElement> getNeigborhood() {
		Collection<SolutionElement> list = new LinkedList<SolutionElement>();
		list.add((SolutionElement) startOfSegmentToBeRemovedInTour1);
		list.add((SolutionElement) endOfSegmentToBeRemovedInTour1);
		list.add((SolutionElement) startOfSegmentToBeRemovedInTour2);
		list.add((SolutionElement) endOfSegmentToBeRemovedInTour2);
		return list;
	}

	public boolean HasNextNeighborhoodStep() {
		if (startOfSegementToBeRemovedCanBeIncreasedInTour2() || endOfSegmentToBeRemovedCanBeIncreasedInTour2()
				|| startOfSegementToBeRemovedCanBeIncreasedInTour1() || endOfSegmentToBeRemovedCanBeIncreasedInTour1())
			return true;
		else 
			return false;
	}

	public static void printNeighborhoodStep() {
		System.out.println("Tour1:");
		System.out.println("Positionen: " + positionTour1Level1 + ", " + positionTour1Level2);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour1 + ", " +endOfSegmentToBeRemovedInTour1);
		System.out.println("Tour2:");
		System.out.println("Positionen: " + positionTour2Level1 + ", " + positionTour2Level2);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour2 + ", " +endOfSegmentToBeRemovedInTour2);	
	}
	

}
