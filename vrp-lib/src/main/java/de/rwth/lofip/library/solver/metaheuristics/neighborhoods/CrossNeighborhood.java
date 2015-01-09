package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.CustomerInTour;

public class CrossNeighborhood {
	
	private static Tour tour1;
	private static Tour tour2;
	private SolutionGot solution;
	
	private Tour tour1clone;
	private Tour tour2clone;
	
	private int TourCounter1 = 0;
	private int TourCounter2 = 1;
	
	private double costOfMove;
	private boolean firstNeighborhoodStep;

	private static int positionStartOfSegmentTour1 = 0;
	private static int positionEndOfSegmentTour1 = 0;
	private static int positionStartOfSegmentTour2 = 0;
	private static int positionEndOfSegmentTour2 = 0;
	
	private static CustomerInTour startOfSegmentToBeRemovedInTour1;
	private static CustomerInTour endOfSegmentToBeRemovedInTour1;
	private static CustomerInTour startOfSegmentToBeRemovedInTour2;
	private static CustomerInTour endOfSegmentToBeRemovedInTour2;
	
	public CrossNeighborhood(SolutionGot solution) {
		this.solution = solution;
		initialise();
	}
	
	public void resetNeighborhood() {
		initialise();
	}
	
	private void initialise() {
		firstNeighborhoodStep = true;
		this.tour1 = solution.getTour(0);
		this.tour2 = solution.getTour(1);
		positionStartOfSegmentTour1 = 0;
		positionEndOfSegmentTour1 = 0;
		positionStartOfSegmentTour2 = 0;
		positionEndOfSegmentTour2 = 0;
		TourCounter1 = 0;
		TourCounter2 = 1;
		startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionStartOfSegmentTour1);
		endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionEndOfSegmentTour1);
		startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionStartOfSegmentTour2);
		endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionEndOfSegmentTour2);
	}
	
	
	public CrossNeighborhoodMove returnBestCrossMove() {
		List<CrossNeighborhoodMove> moves = new LinkedList<CrossNeighborhoodMove>();
		generateMoves(moves);
		CrossNeighborhoodMove bestMove = findbestMove(moves);
		return bestMove;
	}
	
		private void generateMoves(List<CrossNeighborhoodMove> moves) {
			while (hasNextNeighborhoodMove()) {
				generateNextNeigborhoodMove();
				if (isMovePossible()) {
					calculateCost();		
					CrossNeighborhoodMove move = getNeigborhoodMove();
					moves.add(move);
				}
			}
		}

		private CrossNeighborhoodMove findbestMove(List<CrossNeighborhoodMove> moves) {
			Collections.sort(moves,
					new Comparator<CrossNeighborhoodMove>() {
	                    @Override
	                    public int compare(CrossNeighborhoodMove o1,
	                            CrossNeighborhoodMove o2) {
	                        if (o1.getCost() < o2.getCost()) {
	                            return -1;
	                        }
	                        if (o1.getCost() > o2.getCost()) {
	                            return 1;
	                        }
	                        return 0;
	                    }
	                }
			);			
			return moves.get(0);
		}

	protected void generateNextNeigborhoodMove() {	
		if (firstNeighborhoodStep)
			//do nothing; first step was already created in initialization
			firstNeighborhoodStep = false;
		else if (segmentInTour2CanBeIncreased()) 
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
		
	public boolean hasNextNeighborhoodMove() {
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
			positionStartOfSegmentTour1 = 0;
			positionEndOfSegmentTour1 = 0;		
			startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionStartOfSegmentTour1);
			endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionEndOfSegmentTour1);
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
			return positionEndOfSegmentTour2 < tour2.length()-1;
		}
		
		private void tour2IncreaseEndOfSegmentToBeRemoved() {
			positionEndOfSegmentTour2++;
			endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionEndOfSegmentTour2);
		}
	
		private boolean startOfSegementToBeRemovedCanBeIncreasedInTour2() {
			return positionStartOfSegmentTour2 < tour2.length()-1;	
		}
		
		private void tour2IncreaseStartOfSegmentToBeRemoved() {
			positionStartOfSegmentTour2++;
			positionEndOfSegmentTour2=positionStartOfSegmentTour2;
			startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionStartOfSegmentTour2);
			endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionStartOfSegmentTour2);
		}
		
		private void resetSegmentInTour2() {
			positionStartOfSegmentTour2 = 0;
			positionEndOfSegmentTour2 = 0;		
			startOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionStartOfSegmentTour2);
			endOfSegmentToBeRemovedInTour2 = tour2.getCustomerAtPosition(positionEndOfSegmentTour2);
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
			return positionEndOfSegmentTour1 < tour1.length()-1;
		}
		
		private void increaseEndOfSegmentToBeRemovedInTour1() {
			positionEndOfSegmentTour1++;
			endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionEndOfSegmentTour1);
		}
		
		private boolean startOfSegementToBeRemovedCanBeIncreasedInTour1() {
			return positionStartOfSegmentTour1 < tour1.length()-1;	
		}
		
		private void increaseStartOfSegmentToBeRemovedInTour1() {
			positionStartOfSegmentTour1++;
			positionEndOfSegmentTour1=positionStartOfSegmentTour1;
			startOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionStartOfSegmentTour1);
			endOfSegmentToBeRemovedInTour1 = tour1.getCustomerAtPosition(positionStartOfSegmentTour1);
		}
	
	
	public boolean isMovePossible() {
		tour1clone = new Tour(tour1);
		List<Customer> customers1 = tour1clone.removeCustomersBetween(positionStartOfSegmentTour1,positionEndOfSegmentTour1);
		tour2clone = new Tour(tour2);
		List<Customer> customers2 = tour2clone.removeCustomersBetween(positionStartOfSegmentTour2, positionEndOfSegmentTour2);
		
		boolean isWasInsertionPossible = true;	
		for (int i = 0 ; i <= customers2.size()-1; i++) {
			Customer customer = customers2.get(i);
			if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour1clone, i + positionStartOfSegmentTour1)) {
				tour1clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour1);
			} else {
				isWasInsertionPossible = false;
				break;
			}
		}		
		for (int i = 0; i <= customers1.size()-1; i++) {
			Customer customer = customers1.get(i);					
			if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour2clone, i + positionStartOfSegmentTour2)) {
				tour2clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour2);				
			} else {
				isWasInsertionPossible = false;
				break;
			}			
		}
		if (isTour1AndTour2AreNotTheSameAsBefore())
			return isWasInsertionPossible;
		else 
			return false;
	}
	
	private boolean isTour1AndTour2AreNotTheSameAsBefore() {
		return !tour1.equals(tour2clone);
	}

	public double calculateCost() {
		double costSolution = solution.getTotalDistance();
		double costTour1 = tour1.getTotalDistance();
		double costTour2 = tour2.getTotalDistance();
		double costTour1Clone = tour1clone.getTotalDistance();
		double costTour2Clone = tour2clone.getTotalDistance();
		costOfMove = costSolution - costTour1 - costTour2 + costTour1Clone + costTour2Clone;
		return costOfMove;
	}
		

	public CrossNeighborhoodMove getNeigborhoodMove() {
		return new CrossNeighborhoodMove(tour1, tour2, 
				positionStartOfSegmentTour1, positionEndOfSegmentTour1, 
				positionStartOfSegmentTour2, positionEndOfSegmentTour2,
				costOfMove);	
	}

	public static void printNeighborhoodStep() {
		System.out.println("Tour1: " + tour1.getId());
		System.out.println("Positionen: " + positionStartOfSegmentTour1 + ", " + positionEndOfSegmentTour1);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour1.getCustomer().getId() + ", " +endOfSegmentToBeRemovedInTour1.getCustomer().getId());
		System.out.println("Tour2: " + tour2.getId());
		System.out.println("Positionen: " + positionStartOfSegmentTour2 + ", " + positionEndOfSegmentTour2);
		System.out.println("Kunden: " + startOfSegmentToBeRemovedInTour2.getCustomer().getId() + ", " +endOfSegmentToBeRemovedInTour2.getCustomer().getId());
		System.out.println();
	}

	public SolutionGot acctuallyApplyMove(CrossNeighborhoodMove bestMove) {
		Tour tour1 = bestMove.getTour1();
		Tour tour2 = bestMove.getTour2();
		
		List<Customer> customers1 = tour1.removeCustomersBetween(bestMove.getStartPositionTour1(),bestMove.getEndPositionTour1());
		List<Customer> customers2 = tour2.removeCustomersBetween(bestMove.getStartPositionTour2(),bestMove.getEndPositionTour2());
		
		tour1.insertCustomersAtPosition(customers2, bestMove.getStartPositionTour1());
		tour2.insertCustomersAtPosition(customers1, bestMove.getStartPositionTour2());
		
		return solution;
	}

}
