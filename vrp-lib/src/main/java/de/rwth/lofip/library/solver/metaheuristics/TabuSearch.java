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

	private int position = 0;
	private int position2 = 0;
	
	private CustomerInTour startOfSegmentToBeRemoved;
	private CustomerInTour endOfSegmentToBeRemoved;
	
	public TabuSearch(Tour tour) {
		this.tour = tour;		
		startOfSegmentToBeRemoved = tour.getCustomerAtPosition(position);
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(position2);
	}

	protected void generateNextNeigborhoodStep() {	
		if (customer2CanBeIncreased())
			IncreaseCustomer2();
		else if (customer1CanBeIncreased())
			IncreaseCustomer1();		
	}

	private boolean customer2CanBeIncreased() {
		return position2 < tour.length();
	}
	
	private void IncreaseCustomer2() {
		position2++;
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(position2);
	}

	private boolean customer1CanBeIncreased() {
		return position < tour.length();	
	}
	
	private void IncreaseCustomer1() {
		position++;
		startOfSegmentToBeRemoved = tour.getCustomerAtPosition(position);
		endOfSegmentToBeRemoved = tour.getCustomerAtPosition(position);
	}

	public Collection<SolutionElement> getNeigborhood() {
		Collection<SolutionElement> list = new LinkedList<SolutionElement>();
		list.add((SolutionElement) startOfSegmentToBeRemoved);
		list.add((SolutionElement) endOfSegmentToBeRemoved);
		return list;
	}
	

}
