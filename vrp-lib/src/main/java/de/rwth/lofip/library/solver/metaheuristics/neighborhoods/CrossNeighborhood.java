package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.NeighborhoodInterface;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.solver.util.TourUtils;

public class CrossNeighborhood implements NeighborhoodInterface {
	
	private static Tour tour1;
	private static Tour tour2;
	private SolutionGot solution;
	
	private Tour tour1clone;
	private Tour tour2clone;
	
	private int tourCounter1 = 0;
	private int tourCounter2 = 1;
	
	private double costOfCompleteSolutionThatResultsFromMove;
	private boolean firstNeighborhoodStep;

	//mit den Positionen sind jeweils die Einfügepositionen gemeint. 
	//D.h. bei einer Tour mit vier Kunden gibt es die Positionen 0,1,2,3,4
	private static int positionStartOfSegmentTour1 = 0;
	private static int positionEndOfSegmentTour1 = 0;
	private static int positionStartOfSegmentTour2 = 0;
	private static int positionEndOfSegmentTour2 = 0;
	
	private ResourceExtensionFunction RefSegment1 = new ResourceExtensionFunction();
	private ResourceExtensionFunction RefSegment2 = new ResourceExtensionFunction();
	
	List<AbstractNeighborhoodMove> moves = new LinkedList<AbstractNeighborhoodMove>();
	private ResourceExtensionFunction currentRef;
	private Tour currentTour;
	private int currentPositionEndOfSegment;
	
	public CrossNeighborhood(SolutionGot solution) {
		this.solution = solution;
		initialise();
	}
	
	public void resetNeighborhood() {
		initialise();
	}
	
	private void initialise() {
		firstNeighborhoodStep = true;
		tourCounter1 = 0;
		tourCounter2 = 0;
		this.tour1 = solution.getTour(tourCounter1);		
		this.tour2 = solution.getTour(tourCounter2);
		positionStartOfSegmentTour1 = 0;
		positionEndOfSegmentTour1 = 0;
		positionStartOfSegmentTour2 = 0;
		positionEndOfSegmentTour2 = 0;
		RefSegment1 = new ResourceExtensionFunction();
		RefSegment2 = new ResourceExtensionFunction();
	}
	
	@Override
	public AbstractNeighborhoodMove returnBestMove() {	
		moves.clear();
		generateMoves();
		AbstractNeighborhoodMove bestMove = findBestMove();
		return bestMove;
	}
	
	public AbstractNeighborhoodMove returnBestMoveUsingRefs(int iteration) {
		initialise();
		moves.clear();
		generateMovesUsingRefs(iteration);
		AbstractNeighborhoodMove bestMove = findBestMove();
		return bestMove;
	}
	
		private void generateMoves() {
			while (isExistsNextCombinationOfSegments()) {
				generateNextCombinationOfSegements();
				if (!segmentsToBeSwapedAreNotInNeighborhood()) {
//					printNeighborhoodStep();
//					assertEquals(isMoveFeasible(),isMoveFeasibleCheckWithRef());
					if (isMoveFeasible()) {
						calculateCost();		
						AbstractNeighborhoodMove move = getNeigborhoodMove();
						if (isMoveNotTaboo(move))
							moves.add(move);
					}
				}
			}
		}
		
		private void generateMovesUsingRefs(int iteration) {
			while (isExistsNextCombinationOfSegments()) {
				generateNextCombinationOfSegements();
				if (!segmentsToBeSwapedAreNotInNeighborhood()) {
//					FOR DEBUGGING:
					if (tour1.getId() == 9 && tour2.getId() == 25 &&
							positionStartOfSegmentTour1 == 0 && positionEndOfSegmentTour1 == 0 &&
							positionStartOfSegmentTour2 == 0 && positionEndOfSegmentTour2 == 1 &&
							iteration == 17)
						System.out.println("MOVE!");
					if (isMoveFeasibleCheckWithRef()) {
						calculateCostUsingRefs();		
						AbstractNeighborhoodMove move = getNeigborhoodMove();
						if (isMoveNotTaboo(move))
							moves.add(move);
					}
				}
			}
		}

		protected boolean isMoveNotTaboo(AbstractNeighborhoodMove move) { 
			return true;
		}

		private AbstractNeighborhoodMove findBestMove() {
			Collections.sort(moves,
					new Comparator<AbstractNeighborhoodMove>() {
	                    @Override
	                    public int compare(AbstractNeighborhoodMove o1,
	                            AbstractNeighborhoodMove o2) {
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

	protected void generateNextCombinationOfSegements() {	
		if (firstNeighborhoodStep) {
			//do nothing; first step was already created in initialization
			firstNeighborhoodStep = false;
		} else if (segmentInTour2CanBeIncreased()) { 
				increaseSegmentInTour2();	
		} else if (segmentInTour1CanBeIncreased()) {
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
		
	public boolean isExistsNextCombinationOfSegments() {
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
			return tourCounter2 < solution.getNumberOfTours()-1;
		}
	
		private void increaseTour2() {
			tourCounter2++;
			tour2 = solution.getTour(tourCounter2);
		}
	
		private boolean tour1CanBeIncreased() {
			return tourCounter1 < solution.getNumberOfTours()-1; 
		}
	
		private void increaseTour1() {
			tourCounter1++;
			tour1 = solution.getTour(tourCounter1);
		}
		
		private void setTour2Accordingly() {
			tourCounter2 = tourCounter1;
			tour2 = solution.getTour(tourCounter2);
		}
	
		private void resetSegmentInTour1() {
			positionStartOfSegmentTour1 = 0;
			positionEndOfSegmentTour1 = 0;		
		}
		
		private boolean segmentInTour2CanBeIncreased() {
			return (startOfSegementToBeRemovedCanBeIncreasedInTour2() || endOfSegmentToBeRemovedCanBeIncreasedInTour2());
		}
	
		private void increaseSegmentInTour2() {
			if (endOfSegmentToBeRemovedCanBeIncreasedInTour2())
				tour2IncreaseEndOfSegmentToBeRemoved();
			else if (startOfSegementToBeRemovedCanBeIncreasedInTour2())
				increaseStartOfSegmentToBeRemovedInTour2();
			else throw new RuntimeException("increaseSegmentInTour2 was called although cannot be executed");
		}
	
		private boolean startOfSegementToBeRemovedCanBeIncreasedInTour2() {
			return positionStartOfSegmentTour2 < tour2.length();	
		}
		
		private boolean endOfSegmentToBeRemovedCanBeIncreasedInTour2() {
			return positionEndOfSegmentTour2 < tour2.length();
		}
		
		private void tour2IncreaseEndOfSegmentToBeRemoved() {
			positionEndOfSegmentTour2++;
			updateRefSegment2();
		}
				
		private void updateRefSegment2() {
			currentRef = RefSegment2;
			currentTour = tour2;
			currentPositionEndOfSegment = positionEndOfSegmentTour2;
			updateRefSegmentX();
		}

		private void updateRefSegmentX() {
			Customer newCustomerInSegment = currentTour.getCustomerAtPosition(currentPositionEndOfSegment - 1).getCustomer();
			currentRef.updateWithSubsequentCustomer(newCustomerInSegment);				
		}
		
		private void increaseStartOfSegmentToBeRemovedInTour2() {
			positionStartOfSegmentTour2++;
			positionEndOfSegmentTour2=positionStartOfSegmentTour2;
			resetRefSegment2();
		}
		
		private void resetRefSegment2() {
			RefSegment2.reset();
		}

		private void resetSegmentInTour2() {
			positionStartOfSegmentTour2 = 0;
			positionEndOfSegmentTour2 = 0;		
		}
		
		private boolean segmentInTour1CanBeIncreased() {
			return (startOfSegementToBeRemovedCanBeIncreasedInTour1() || endOfSegmentToBeRemovedCanBeIncreasedInTour1());
		}
	
		private void increaseSegmentInTour1() {
			if (endOfSegmentToBeRemovedCanBeIncreasedInTour1())
				increaseEndOfSegmentToBeRemovedInTour1();
			else if (startOfSegementToBeRemovedCanBeIncreasedInTour1())
				increaseStartOfSegmentToBeRemovedInTour1();
			else throw new RuntimeException("generateNextNeigborhoodStep() was called although no remaining NeigborhoodStep exists");	
		}
		
		private boolean endOfSegmentToBeRemovedCanBeIncreasedInTour1() {
			return positionEndOfSegmentTour1 < tour1.length();
		}
		
		private void increaseEndOfSegmentToBeRemovedInTour1() {
			positionEndOfSegmentTour1++;
			updateRefSegment1();
		}
		
		private void updateRefSegment1() {
			currentRef = RefSegment1;
			currentTour = tour1;
			currentPositionEndOfSegment = positionEndOfSegmentTour1;
			updateRefSegmentX();
		}
		
		private boolean startOfSegementToBeRemovedCanBeIncreasedInTour1() {
			return positionStartOfSegmentTour1 < tour1.length();	
		}
		
		private void increaseStartOfSegmentToBeRemovedInTour1() {
			positionStartOfSegmentTour1++;
			positionEndOfSegmentTour1=positionStartOfSegmentTour1;
			resetRefSegment1();
		}

	private void resetRefSegment1() {
			RefSegment1.reset();
		}

	public boolean isMoveFeasibleCheckWithRef() {		
		boolean isWasInsertionPossible = true;
		if (isInnerTourMove()) {
			//TODO: Ändern -> Im Moment sind inner tour moves nicht erlaubt
			return false;
//			return isMoveFeasible();
			
//			if (positionEndOfSegmentTour1 < positionEndOfSegmentTour2) {
//				ResourceExtensionFunction newRef = calculateNewRefForFirstPartOfTour();
//				return TourUtils.isInsertionOfRefPossible(newRef, RefSegment1, positionStartOfSegmentTour2);
//			} else {
//				ResourceExtensionFunction newRef = calculateNewRefForLastPartOfTour();
//				return TourUtils.isInsertionOfRefPossible(newRef, RefSegment1, positionStartOfSegmentTour2);
//			}
		} else {		
			//move between two tours
			if (!TourUtils.isInsertionOfRefPossible(tour1,RefSegment2,positionStartOfSegmentTour1,positionEndOfSegmentTour1))
				isWasInsertionPossible = false;
			if (!TourUtils.isInsertionOfRefPossible(tour2,RefSegment1,positionStartOfSegmentTour2,positionEndOfSegmentTour2))
				isWasInsertionPossible = false;
		}
		return isWasInsertionPossible;
	}
	
	private boolean isInnerTourMove() { 
		return tourCounter1 == tourCounter2;
	}
	
	public boolean isMoveFeasible() {			
		
//		printNeighborhoodStep();
		
		if (tourCounter1 == tourCounter2) {			
			//inner-tour move
			tour1clone = new Tour(tour1);
			tour2clone = tour1clone;
		} else {
			//move between two tours
			tour1clone = new Tour(tour1);
			tour2clone = new Tour(tour2);
		}
		
		List<Customer> customers1 = tour1clone.removeCustomersBetween(positionStartOfSegmentTour1,positionEndOfSegmentTour1);		
		List<Customer> customers2 = tour2clone.removeCustomersBetween(positionStartOfSegmentTour2, positionEndOfSegmentTour2);
		
		//check demand and tw feasibility
		boolean isWasInsertionPossible = true;	
		for (int i = 0 ; i < positionEndOfSegmentTour2 - positionStartOfSegmentTour2; i++) {
			Customer customer = customers2.get(i);
			if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour1clone, i+positionStartOfSegmentTour1)) {
				tour1clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour1);
			} else {
				isWasInsertionPossible = false;
				break;
			}
		}		
		for (int i = 0; i < positionEndOfSegmentTour1 - positionStartOfSegmentTour1; i++) {
			Customer customer = customers1.get(i);			
			if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour2clone, i + positionStartOfSegmentTour2)) {
				tour2clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour2);				
			} else {
				isWasInsertionPossible = false;
				break;
			}			
		}
		return isWasInsertionPossible;
	}
	
	protected boolean segmentsToBeSwapedAreNotInNeighborhood() {
		if (positionStartOfSegmentTour1 == positionEndOfSegmentTour1 && 
				positionStartOfSegmentTour2 == positionEndOfSegmentTour2)
			return true;
		if (positionStartOfSegmentTour1 == 0 && positionEndOfSegmentTour1 == tour1.getCustomerSize() &&
				positionStartOfSegmentTour2 == 0 && positionEndOfSegmentTour2 == tour2.getCustomerSize())
			return true;
		if (tourCounter1 == tourCounter2) {
			if (positionStartOfSegmentTour2 != positionEndOfSegmentTour2)
				return true;			
			if (positionStartOfSegmentTour2 > tour2.getCustomerSize() - (positionEndOfSegmentTour1 - positionStartOfSegmentTour1))
				return true;
			if (positionStartOfSegmentTour2 == positionStartOfSegmentTour1)
				return true;
		}
		return false;
	}

	public double calculateCost() {
		//TODO: tourClones entfernen, da nicht mehr benutzt werden, wenn isMoveFeasibleCheckWithRef() aufgerufen wird und isMoveFeasible() nicht mehr		
		double costSolution = solution.getTotalDistance();
		double costTour1 = tour1.getTotalDistance();
		double costTour2 = tour2.getTotalDistance();
		double costTour1Clone = tour1clone.getTotalDistance();
		double costTour2Clone = tour2clone.getTotalDistance();
		if (tourCounter1 == tourCounter2)
			costOfCompleteSolutionThatResultsFromMove = costSolution - costTour1 + costTour1Clone;
		else
			costOfCompleteSolutionThatResultsFromMove = costSolution - costTour1 - costTour2 + costTour1Clone + costTour2Clone;
		return costOfCompleteSolutionThatResultsFromMove;
	}
	
	public double calculateCostUsingRefs() {
		double costSolution = solution.getTotalDistance();
//		if (tourCounter1 == tourCounter2) {}
//			//inner-tour move
//		else { 
			//move between tours
			double costTour1 = 0;
			//calculate cost for tour1
			if (isSegmentRemovedFromTour1()) {
				//remove edge between last customer before segment and first customer in segment from tour 1			
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength();
				//remove edge between last customer in segment and first customer after segment from tour 1		
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength();
			} else
				//remove edge between last customer before inserting position and first customer after inserting position
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength();
			
			if (isSegmentRemovedFromTour2()) {
				//add edge between last customer before segment from tour 1 and first customer in segment from tour 2				
				costTour1 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1), tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength();
				//add edge between last customer in segment from tour 2 and first customer after inserting position
				costTour1 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength(); 				
			} else
				//add edge between last customer before segment and first customer after segment
				costTour1 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength();
				
			double costTour2 = 0;
			//calculate cost for tour2
			if (isSegmentRemovedFromTour2()) {
				//remove edge between last customer before segment and first customer in segment from tour 2
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength();
				//remove edge between last customer in segment and first customer after segment from tour 2
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength();
			} else
				//remove edge between last customer before inserting position and first customer after inserting position
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength();
			
			if (isSegmentRemovedFromTour1()) {
				//add edge between last customer before segment from tour 2 and first customer in segment from tour 1
				costTour2 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1), tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength();
				//add edge between last customer in segment from tour 1 and first customer after inserting position
				costTour2 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength(); 				
			} else 
				//add edge between last customer before segment and first customer after segment
				costTour2 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength();
//		}
		return costOfCompleteSolutionThatResultsFromMove = costSolution + costTour1 + costTour2;
	}
		

	private boolean isThereIsFirstCustomerAfterSegmentInTour1() {
		return positionEndOfSegmentTour1 > tour1.getCustomerSize();
	}

	private boolean isThereIsLastCustomerBeforeSegmentInTour1() {
		return positionStartOfSegmentTour1 != 0;
	}

	private boolean isSegmentRemovedFromTour1() {
		return positionStartOfSegmentTour1 != positionEndOfSegmentTour1;
	}
	
	private boolean isSegmentRemovedFromTour2() {
		return positionStartOfSegmentTour2 != positionEndOfSegmentTour2;
	}

	public AbstractNeighborhoodMove getNeigborhoodMove() {
		return new AbstractNeighborhoodMove(tour1, tour2, 
				positionStartOfSegmentTour1, positionEndOfSegmentTour1, 
				positionStartOfSegmentTour2, positionEndOfSegmentTour2,
				costOfCompleteSolutionThatResultsFromMove);	
	}

	public static void printNeighborhoodStep() {
		System.out.print("Move " + tour1.getId() + "(");		
		for (int i = positionStartOfSegmentTour1; i < positionEndOfSegmentTour1; i++) {
			System.out.print(tour1.getCustomerAtPosition(i).getCustomer().getCustomerNo());
		}
		System.out.print(")[" + positionStartOfSegmentTour1 + "] " +
				tour2.getId() + "(");
		for (int i = positionStartOfSegmentTour2; i < positionEndOfSegmentTour2; i++)
			System.out.print(tour2.getCustomerAtPosition(i).getCustomer().getCustomerNo());
		System.out.print(")[" + positionStartOfSegmentTour2 + "] ");
		System.out.println();
		System.out.println("Tour1: "); tour1.print(); System.out.println("Tour2: "); tour2.print(); 
	}
	
	public void printSegment1() {
		System.out.print(tour1.getId() + "(");		
		for (int i = positionStartOfSegmentTour1; i < positionEndOfSegmentTour1; i++) {
			System.out.print(tour1.getCustomerAtPosition(i).getCustomer().getCustomerNo());
		}
		System.out.print(")");
	}

	public SolutionGot acctuallyApplyMove(AbstractNeighborhoodMove bestMove) {		
		Tour tour1 = bestMove.getTour1();
		Tour tour2 = bestMove.getTour2();		
		List<Customer> customers1 = tour1.removeCustomersBetween(bestMove.getStartPositionTour1(),bestMove.getEndPositionTour1());
		List<Customer> customers2 = tour2.removeCustomersBetween(bestMove.getStartPositionTour2(),bestMove.getEndPositionTour2());		
		tour1.insertCustomersAtPosition(customers2, bestMove.getStartPositionTour1());
		tour2.insertCustomersAtPosition(customers1, bestMove.getStartPositionTour2());		
		solution.removeEmptyTours();				
		resetNeighborhood();
		//TODO: Update Tabu list;
		return solution;
	}

	public SolutionGot acctuallyApplyMove() {
		List<Customer> customers1 = tour1.removeCustomersBetween(positionStartOfSegmentTour1,positionEndOfSegmentTour1);
		List<Customer> customers2 = tour2.removeCustomersBetween(positionStartOfSegmentTour2,positionEndOfSegmentTour2);		
		tour1.insertCustomersAtPosition(customers2, positionStartOfSegmentTour2);
		tour2.insertCustomersAtPosition(customers1, positionStartOfSegmentTour1);		
		solution.removeEmptyTours();				
		resetNeighborhood();
		return solution;
	}

	protected ResourceExtensionFunction getRefSegment1() {
		return RefSegment1;		
	}

	public Tour getTour1() {
		return tour1;	
	}

	public ResourceExtensionFunction getRefSegment2() {
		return RefSegment2;		
	}



}
