package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;

public class AbstractNeighborhoodMove implements Serializable {
	
	//helper class to store a cross move including its cost
	private static final long serialVersionUID = 1L;
	
	private Tour tour1;
	private Tour tour2;
	private int positionStartOfSegmentTour1;
	private int positionEndOfSegmentTour1;
	private int positionStartOfSegmentTour2;
	private int positionEndOfSegmentTour2;
	private double costOfCompleteSolutionThatResultsFromMove;
	private double costDifferenceToPreviousSolution;
	
	public AbstractNeighborhoodMove(Tour tour1, Tour tour2, int posStart1, int posEnd1, int posStart2, int posEnd2, double cost, double costDifferenceToPreviousSolution) {
		this.tour1 = tour1;
		this.tour2 = tour2;
		this.positionStartOfSegmentTour1 = posStart1;
		this.positionEndOfSegmentTour1 = posEnd1;
		this.positionStartOfSegmentTour2 = posStart2;
		this.positionEndOfSegmentTour2 = posEnd2;
		this.costOfCompleteSolutionThatResultsFromMove = cost;
		this.costDifferenceToPreviousSolution = costDifferenceToPreviousSolution;
	}
	
	public AbstractNeighborhoodMove(double cost) {
		this.costOfCompleteSolutionThatResultsFromMove = cost;
	}

	public double getCost() {
		return costOfCompleteSolutionThatResultsFromMove;
	}

	public Tour getTour1() {
		return tour1;
	}
	
	private void setTour1(Tour tour) {
		tour1 = tour;		
	}

	public Tour getTour2() {
		return tour2;
	}
	
	private void setTour2(Tour tour) {
		tour2 = tour;		
	}

	public int getStartPositionTour1() {
		return positionStartOfSegmentTour1;
	}

	public int getEndPositionTour1() {
		return positionEndOfSegmentTour1;
	}

	public int getStartPositionTour2() {
		return positionStartOfSegmentTour2;	
	}

	public int getEndPositionTour2() {
		return positionEndOfSegmentTour2;
	}
	
	public double getCostDifferenceToPreviousSolution() {
		return costDifferenceToPreviousSolution;
	}

	public void setCostDifferenceToPreviousSolution(
			double costDifferenceToPreviousSolution) {
		this.costDifferenceToPreviousSolution = costDifferenceToPreviousSolution;
	}

	public void print() {
		System.out.println("Tour1: " + tour1.getId() + "; " + tour1.getTourAsTupel());
		System.out.println("Positionen: " + positionStartOfSegmentTour1 + ", " + positionEndOfSegmentTour1);
		System.out.println("Tour2: " + tour2.getId()  + "; " + tour2.getTourAsTupel());;
		System.out.println("Positionen: " + positionStartOfSegmentTour2 + ", " + positionEndOfSegmentTour2);	
		System.out.println("CostDifference = " + costDifferenceToPreviousSolution);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNeighborhoodMove other = (AbstractNeighborhoodMove) obj;
		if (Double.doubleToLongBits(costOfCompleteSolutionThatResultsFromMove) != Double
				.doubleToLongBits(other.costOfCompleteSolutionThatResultsFromMove))
			return false;
		if (Double.doubleToLongBits(costDifferenceToPreviousSolution) != Double
				.doubleToLongBits(other.costDifferenceToPreviousSolution))
			return false; 
		if (positionEndOfSegmentTour1 != other.positionEndOfSegmentTour1)
			return false;
		if (positionEndOfSegmentTour2 != other.positionEndOfSegmentTour2)
			return false;
		if (positionStartOfSegmentTour1 != other.positionStartOfSegmentTour1)
			return false;
		if (positionStartOfSegmentTour2 != other.positionStartOfSegmentTour2)
			return false;
		if (tour1 == null) {
			if (other.tour1 != null)
				return false;
		} else if (!tour1.equals(other.tour1))
			return false;
		if (tour2 == null) {
			if (other.tour2 != null)
				return false;
		} else if (!tour2.equals(other.tour2))
			return false;
		return true;
	}

	public boolean isInnerTourMove() {
		return tour1.equals(tour2);
	}

	public boolean reducesNumberOfVehicles() {
		if (positionStartOfSegmentTour1 == 0 && positionEndOfSegmentTour1 == tour1.getCustomerSize()
				&& positionStartOfSegmentTour2 == positionEndOfSegmentTour2)
			return true;
		if (positionStartOfSegmentTour2 == 0 && positionEndOfSegmentTour2 == tour2.getCustomerSize()
				&& positionStartOfSegmentTour1 == positionEndOfSegmentTour1)
			return true;
		return false;
	}
	
	public AbstractNeighborhoodMove cloneWithCopyOfToursAndGotsAndCustomers() {
		AbstractNeighborhoodMove newMove = new AbstractNeighborhoodMove(tour1, tour2, positionStartOfSegmentTour1, positionEndOfSegmentTour1, positionStartOfSegmentTour2, positionEndOfSegmentTour2, costOfCompleteSolutionThatResultsFromMove, costDifferenceToPreviousSolution);
		//Achtung, Touren sind noch nicht geklont
		
		GroupOfTours got1 = tour1.getParentGot().cloneWithCopyOfTourAndCustomers();
		Tour tour1InGot1 = got1.getTourThatIsEqualTo(tour1);
		//Setze Pointer in newMove auf geklonte Tour  
		newMove.setTour1(tour1InGot1);

		if (isParentGotOfTour2IsSameAsParentGotOfTour1()) {
			//no need to clone got because it's the same
			Tour tour2InGot2 = got1.getTourThatIsEqualTo(tour2);
			//Setze Pointer in newMove auf geklonte Tour  
			newMove.setTour2(tour2InGot2);
		} else { //parent gots are different			
			GroupOfTours got2 = tour2.getParentGot().cloneWithCopyOfTourAndCustomers();
			Tour tour2InGot2 = got2.getTourThatIsEqualTo(tour2);
			//Setze Pointer in newMove auf geklonte Tour  
			newMove.setTour2(tour2InGot2);
		}
		return newMove;
	}

	public boolean isParentGotOfTour2IsSameAsParentGotOfTour1() {
		return tour1.getParentGot().cloneWithCopyOfTourAndCustomers().equals(tour2.getParentGot().cloneWithCopyOfTourAndCustomers());
	}

	public boolean isSwapsTwoSegments() {
		boolean isBothSegmentsAreSwapped = isSegmentRemovedFromTour1() && isSegmentRemovedFromTour2();
		return isBothSegmentsAreSwapped;
	}
	
	private boolean isSegmentRemovedFromTour1() {
		return positionStartOfSegmentTour1 != positionEndOfSegmentTour1;
	}
	
	private boolean isSegmentRemovedFromTour2() {
		return positionStartOfSegmentTour2 != positionEndOfSegmentTour2;
	}

	public List<GroupOfTours> getGots() {
		List<GroupOfTours> list = new ArrayList<GroupOfTours>();
		if (isParentGotOfTour2IsSameAsParentGotOfTour1())
			list.add(tour1.getParentGot());
		else {
			list.add(tour1.getParentGot());
			list.add(tour2.getParentGot());
		}
		return list;
			
	}


}
