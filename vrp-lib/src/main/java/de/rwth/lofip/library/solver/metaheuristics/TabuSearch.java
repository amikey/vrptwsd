package de.rwth.lofip.library.solver.metaheuristics;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.IntermediateSolutionGot;

public class TabuSearch implements MetaSolverInterfaceGot {
	
	Tour tour;

	private static int positionLevel1 = 0;
	private static int positionLevel2 = 0;
	
	private static CustomerInTour startOfSegmentToBeRemoved;
	private static CustomerInTour endOfSegmentToBeRemoved;
	
	public TabuSearch(Tour tour) {
		this.tour = tour;		
		positionLevel1 = 0;
		positionLevel2 = 0;
		startOfSegmentToBeRemoved = tour.getCustomerAtPosition(positionLevel1);
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(positionLevel2);
	}

	protected void generateNextNeigborhoodStep() {	
		if (customer2CanBeIncreased())
			IncreaseCustomer2();
		else if (customer1CanBeIncreased())
			IncreaseCustomer1();
		else throw new RuntimeException("generateNextNeigborhoodStep() was called although no remaining NeigborhoodStep exists");
	}

	private boolean customer2CanBeIncreased() {
		return positionLevel2 < tour.length()-1;
	}
	
	private void IncreaseCustomer2() {
		positionLevel2++;
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(positionLevel2);
	}

	private boolean customer1CanBeIncreased() {
		return positionLevel1 < tour.length()-1;	
	}
	
	private void IncreaseCustomer1() {
		positionLevel1++;
		positionLevel2=positionLevel1;
		startOfSegmentToBeRemoved = tour.getCustomerAtPosition(positionLevel1);
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(positionLevel1);
	}

	public Collection<SolutionElement> getNeigborhood() {
		Collection<SolutionElement> list = new LinkedList<SolutionElement>();
		list.add((SolutionElement) startOfSegmentToBeRemoved);
		list.add((SolutionElement) endOfSegmentToBeRemoved);
		return list;
	}

	public boolean HasNextNeighborhoodStep() {
		if (customer1CanBeIncreased() && customer2CanBeIncreased())
			return true;
		else 
			return false;
	}

	public static void printNeighborhoodStep() {
		System.out.println("Positionen: " + positionLevel1 + ", " + positionLevel2);
		System.out.println("Kunden: " + startOfSegmentToBeRemoved + ", " +endOfSegmentToBeRemoved);	
	}
	

}
