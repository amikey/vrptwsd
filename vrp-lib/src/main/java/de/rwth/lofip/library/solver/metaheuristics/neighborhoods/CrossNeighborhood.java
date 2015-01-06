package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

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

public class CrossNeighborhood implements MetaSolverInterfaceGot {
	
	private static Tour tour1;
	private static Tour tour2;
	private SolutionGot solution;
	
	private int TourCounter1 = 0;
	private int TourCounter2 = 1;

	private static int positionTour1Level1 = 0;
	private static int positionTour1Level2 = 0;
	private static int positionTour2Level1 = 0;
	private static int positionTour2Level2 = 0;
	
	private static CustomerInTour startOfSegmentToBeRemovedInTour1;
	private static CustomerInTour endOfSegmentToBeRemovedInTour1;
	private static CustomerInTour startOfSegmentToBeRemovedInTour2;
	private static CustomerInTour endOfSegmentToBeRemovedInTour2;
	
	public CrossNeighborhood(SolutionGot solution) {
		this.solution = solution;
		initialise();
	}
	
	private void initialise() {
		this.tour1 = solution.getTour(0);
		this.tour2 = solution.getTour(1);
		positionTour1Level1 = 0;
		positionTour1Level2 = 0;
		positionTour2Level1 = 0;
		positionTour2Level2 = 0;
		TourCounter1 = 0;
		TourCounter2 = 1;
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
		} else if (tour2CanBeIncreased()) {
				increaseTour2();
				resetSegmentInTour2();
				resetSegmentInTour1();
		} else if (tour1CanBeIncreased()) {
				increaseTour1();
				setTour2Accordingly();				
				resetSegmentInTour2();
				resetSegmentInTour1();
		} else throw new RuntimeException("generateNextNeigborhoodStep was called although no next step exists");
	}
		
	public boolean HasNextNeighborhoodStep() {
		if (segmentInTour2CanBeIncreased())
			return true;
		if (segmentInTour1CanBeIncreased())
			return true;
		if (tour2CanBeIncreased())
			return true;
		if (tour1CanBeIncreased())
			return true;
		return false;
	}

	private boolean tour2CanBeIncreased() {
		return TourCounter2 < solution.getNumberOfTours()-1;
	}

	private void increaseTour2() {
		TourCounter2++;
		tour2 = solution.getTour(TourCounter2);
	}

	private boolean tour1CanBeIncreased() {
		return TourCounter1 < solution.getNumberOfTours()-2; //letzte Tour wird nicht berücksichtigt, da diese nur mit sich selbst getauscht werden kann.
	}

	private void increaseTour1() {
		TourCounter1++;
		tour1 = solution.getTour(TourCounter1);
	}
	
	private void setTour2Accordingly() {
		TourCounter2 = TourCounter1 + 1;
		tour2 = solution.getTour(TourCounter2);
	}

	private void resetSegmentInTour1() {
		positionTour1Level1 = 0;
		positionTour1Level2 = 0;		
		startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level1);
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level2);
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

	public static void printNeighborhoodStep() {
		System.out.println("Tour1: " + tour1.getId());
		System.out.println("Positionen: " + positionTour1Level1 + ", " + positionTour1Level2);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour1.getCustomer().getId() + ", " +endOfSegmentToBeRemovedInTour1.getCustomer().getId());
		System.out.println("Tour2: " + tour2.getId());
		System.out.println("Positionen: " + positionTour2Level1 + ", " + positionTour2Level2);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour2.getCustomer().getId() + ", " +endOfSegmentToBeRemovedInTour2.getCustomer().getId());
		System.out.println();
	}
	

}
