package de.rwth.lofip.library.solver.metaheuristics;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import log4j2.Log4jTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.IntermediateSolutionGot;

public class TabuSearch implements MetaSolverInterfaceGot {
	
	static Logger logger = LogManager.getLogger(TabuSearch.class);
	
	private int NumberOfNeighborhoodSteps = 0;
	private static Tour tour1;
	private static Tour tour2;
	private GroupOfTours got1;
	private GroupOfTours got2;

	private static int positionTour1Level1 = 0;
	private static int positionTour1Level2 = 0;
	private static int positionTour2Level1 = 0;
	private static int positionTour2Level2 = 0;
	
	private static CustomerInTour startOfSegmentToBeRemovedInTour1;
	private static CustomerInTour endOfSegmentToBeRemovedInTour1;
	private static CustomerInTour startOfSegmentToBeRemovedInTour2;
	private static CustomerInTour endOfSegmentToBeRemovedInTour2;
	
	public TabuSearch(GroupOfTours got1, GroupOfTours got2) {
		this.got1 = got1;
		this.got2 = got2;	
		generateFirstNeighborhoodStep();
	}
	
//	protected void generateNextNeighborhoodStep() {
//		if (NumberOfNeighborhoodSteps == 0) {
//			generateFirstNeighborhoodStep();
//			NumberOfNeighborhoodSteps++; 
//		} else {
//			generateSubsequentNeigborhoodStep();
//			NumberOfNeighborhoodSteps++;
//		}
//	}
	
	private void generateFirstNeighborhoodStep() {
		tour1 = got1.getFirstTour();
		tour2 = got2.getFirstTour();
		if (tour2equalsTour1())
			if (got2.hasNextTour())
				tour2 = got2.getNextTour();
			else throw new RuntimeException("Es kann kein erster Nachbarschaftsschritt generiert werden, da die Touren in den beiden Gots gleich sind und es keine weiteren Touren gibt. Hier muss später ein statt dieser RuntimeException ein anderes Got gewählt werden.");
		positionTour1Level1 = 0;
		positionTour1Level2 = 0;
		positionTour2Level1 = 0;
		positionTour2Level2 = 0;
		startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level1);
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionTour1Level2);
		startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level1);
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionTour2Level2);
	}

	protected void generateNextNeighborhoodStep() {		
		if (segmentInTour2CanBeIncreased()) 
			increaseSegmentInTour2();	
		else if (segmentInTour1CanBeIncreased()) {
				resetSegmentInTour2();
				increaseSegmentInTour1();
		}
		else if (got2.hasNextTour()) {
			tour2 = got2.getNextTour();
			if (tour2equalsTour1())
				if (got2.hasNextTour())
					tour2 = got2.getNextTour();
					//falls das der Fall ist, muss man von hier aus irgendwie in die untere if-Abfrage kommen.
			resetSegmentInTour1();
			resetSegmentInTour2();					
		} else if (got1.hasNextTour()) {
			tour1 = got1.getNextTour();
			resetTour2();			
			if (tour2equalsTour1())
				if (got2.hasNextTour())
					tour2 = got2.getNextTour();
				else throw new RuntimeException("generateNextNeigborhoodStep was called although no next step exists");
			resetSegmentInTour1();
			resetSegmentInTour2();
		}
		else throw new RuntimeException("generateNextNeigborhoodStep was called although no next step exists");
	}
	
	// jetzt ist man bei den schleifen bei einschließlich Zeile 4 (von unten)
	// jetzt ist noch zu testen, ob das auch wirklich funktioniert, wenn die Touren gleich sind => Test, in dem zweimal mit dem gleichen Got initialisiert wird. 	
	
	private void resetTour2() {
		tour2 = got2.getFirstTour();
		got2.setTourPointer(0);
	}

	public boolean HasNextNeighborhoodStep() {		
		if (segmentInTour1CanBeIncreased())
			return true;
		if (segmentInTour2CanBeIncreased())
			return true;
		if (got2.hasNextTour())
			if (!tour2equalsTour1())
				return true;
			else if (got2.hasNextTourButOne())			
					return true;				 	
		if (got1.hasNextTour())
			if (!tour2equalsTour1())
				return true;
			else if (got2.hasNextTourButOne())
				return true;
		return false;
	}
	
	
	


	private boolean tour2equalsTour1() {
		return tour2 == tour1;
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
		System.out.println("");
	}
	

}
