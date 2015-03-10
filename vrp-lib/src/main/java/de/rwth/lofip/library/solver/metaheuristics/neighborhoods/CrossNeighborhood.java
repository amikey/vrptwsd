package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

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
	
	private Tour tour1;
	private Tour tour2;
	private SolutionGot solution;
	
	private int tourCounter1 = 0;
	private int tourCounter2 = 1;
	
	private double costOfCompleteSolutionThatResultsFromMove;
	private boolean firstNeighborhoodStep;

	//mit den Positionen sind jeweils die Einfügepositionen gemeint. 
	//D.h. bei einer Tour mit vier Kunden gibt es die Positionen 0,1,2,3,4
	private int positionStartOfSegmentTour1 = 0;
	private int positionEndOfSegmentTour1 = 0;
	private int positionStartOfSegmentTour2 = 0;
	private int positionEndOfSegmentTour2 = 0;
	
	private ResourceExtensionFunction RefSegment1 = new ResourceExtensionFunction();
	private ResourceExtensionFunction RefSegment2 = new ResourceExtensionFunction();
		
	private ResourceExtensionFunction currentRef;
	private Tour currentTour;
	private int currentPositionEndOfSegment;
	
	protected AbstractNeighborhoodMove bestMove = null;
	
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
		bestMove = null;
	}
	
	public AbstractNeighborhoodMove returnBestMove() throws Exception {
		int iteration = Integer.MAX_VALUE;
		return returnBestMove(iteration);
	}
	
	public AbstractNeighborhoodMove returnBestMove(int iteration) throws Exception {
		initialise();		
		generateMovesUsingRefs(iteration);
		if (bestMove == null)
			throw new Exception("No feasible move found.");
		return bestMove;
	}
		
		private void generateMovesUsingRefs(int iteration) {
			while (isExistsNextCombinationOfSegments()) {
				generateNextCombinationOfSegements();
				if (!segmentsToBeSwapedAreNotInNeighborhoodRefPositions()) {
//					FOR DEBUGGING:
					if (tour1.getId() == 35 && tour2.getId() == 35 &&
							positionStartOfSegmentTour1 == 4 && positionEndOfSegmentTour1 == 5 &&
							positionStartOfSegmentTour2 == 3 && positionEndOfSegmentTour2 == 3 &&
							iteration == 89)
						System.out.println("DEBUGGING!");
					if (isMoveFeasibleCheckWithRef()) {						
						calculateCostUsingRefs();		
						AbstractNeighborhoodMove move = getNeigborhoodMove();
//						System.out.println("feasible move found. Kosten: " + move.getCost());
						if (isMoveNewBestMove(move)) {
							System.out.println("move ist neuer bester move.");
							if (!isMoveTaboo(move, iteration)) {
								System.out.println("move ist NICHT tabu");
								bestMove = move;
							} else 
								System.out.println("move ist tabu");
						}
					}
				}
			}
		}

		private boolean isMoveNewBestMove(AbstractNeighborhoodMove move) {	
			if (bestMove == null) 
				return true;
			if (move.getCost() <= bestMove.getCost() || //hier weiß man nicht, ob die Anzahl an Fahrzeugen verringert wird 
					(move.reducesNumberOfVehicles() && !bestMove.reducesNumberOfVehicles())) // so werden moves bevorzugt, die die Fahrzeuganzahl verringern
				return true;
			else 
				return false;
		}

		protected boolean isMoveTaboo(AbstractNeighborhoodMove move, int iteration) { 
			return false;
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
			return TourUtils.isInsertionOfRefPossibleInnerTourMove(tour1,positionStartOfSegmentTour1,positionEndOfSegmentTour1,positionStartOfSegmentTour2);					
		} else {		
			//move between two tours
			if (!TourUtils.isInsertionOfRefPossible(tour1,RefSegment2,positionStartOfSegmentTour1,positionEndOfSegmentTour1))
				isWasInsertionPossible = false;
			else if (!TourUtils.isInsertionOfRefPossible(tour2,RefSegment1,positionStartOfSegmentTour2,positionEndOfSegmentTour2))
				isWasInsertionPossible = false;
		}
		return isWasInsertionPossible;
	}
	
	private boolean isInnerTourMove() { 
		return tourCounter1 == tourCounter2;
		//bei inner Tour moves wird immer aus tour 1 entfernt und in tour zwei eingefügt
	}
		
	public boolean segmentsToBeSwapedAreNotInNeighborhoodRefPositions() {
		if (positionStartOfSegmentTour1 == positionEndOfSegmentTour1 && 
				positionStartOfSegmentTour2 == positionEndOfSegmentTour2)
			return true;
		if (positionStartOfSegmentTour1 == 0 && positionEndOfSegmentTour1 == tour1.getCustomerSize() &&
				positionStartOfSegmentTour2 == 0 && positionEndOfSegmentTour2 == tour2.getCustomerSize())
			return true;
		if (tourCounter1 == tourCounter2) {
			if (positionStartOfSegmentTour1 == positionEndOfSegmentTour1)
				return true;
			if (positionStartOfSegmentTour2 != positionEndOfSegmentTour2)
				return true;
			if (!((positionStartOfSegmentTour1 < positionStartOfSegmentTour2 && positionEndOfSegmentTour1 < positionStartOfSegmentTour2) ||
					(positionStartOfSegmentTour1 > positionStartOfSegmentTour2 && positionEndOfSegmentTour1 > positionStartOfSegmentTour2)))
				return true;
		}
		return false;
	}
	
	public double calculateCostUsingRefs() {
		double costSolution = solution.getTotalDistance();			
		if (isOnlyOneSegmentIsSwapped()) {			
			//also calculates inner tour moves
			
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
			
			return costOfCompleteSolutionThatResultsFromMove = costSolution + costTour1 + costTour2;
		} else {
			//two segments are swapped
			//TODO: ich glaube, das ist redundant mit oben (oben werden zwei segmente auch schon behandelt)
			
			double costTour1 = 0;
			//remove edge before segment in tour 1
			costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1), tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength();
			//remove edge after segment in tour 1
			costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1), tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength();
			
			//add edge between tour 1 and start of segment from tour 2
			costTour1 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength();
			//add edge between tour 1 and end of segment from tour 2
			costTour1 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength();
			
			double costTour2 = 0;
			//remove edge before segment in tour 2
			costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength();
			//remove edge after segment in tour 2
			costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength();
			
			//add edge between tour 2 and start of segment from tour 1
			costTour2 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1), tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength();
			//add edge between tour 2 and end of segment from tour 1
			costTour2 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1), tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength();
			
			return costOfCompleteSolutionThatResultsFromMove = costSolution + costTour1 + costTour2;
		}
			
	}
		
	private boolean isOnlyOneSegmentIsSwapped() {		
		boolean isBothSegmentsAreSwapped = isSegmentRemovedFromTour1() && isSegmentRemovedFromTour2();
		return !isBothSegmentsAreSwapped;
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

	public void printNeighborhoodStep() {
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

	public SolutionGot acctuallyApplyMove() {
		AbstractNeighborhoodMove move = this.getNeigborhoodMove();
		return acctuallyApplyMove(move);		
	}
	
	public SolutionGot acctuallyApplyMove(AbstractNeighborhoodMove bestMove) {
		if (bestMove.isInnerTourMove()) {
			Tour tour1 = bestMove.getTour1();
			if (isInsertedBeforePositionWhereSegmentIsRemoved(bestMove)) {
				List<Customer> customers = tour1.removeCustomersBetween(bestMove.getStartPositionTour1(),bestMove.getEndPositionTour1());
				tour1.insertCustomersAtPosition(customers, bestMove.getStartPositionTour2());
			} else {
				List<Customer> customers = tour1.removeCustomersBetween(bestMove.getStartPositionTour1(),bestMove.getEndPositionTour1());
				tour1.insertCustomersAtPosition(customers, bestMove.getStartPositionTour2()-customers.size());
			}
		} else {
			//move between tours
			Tour tour1 = bestMove.getTour1();
			Tour tour2 = bestMove.getTour2();		
			List<Customer> customers1 = tour1.removeCustomersBetween(bestMove.getStartPositionTour1(),bestMove.getEndPositionTour1());
			List<Customer> customers2 = tour2.removeCustomersBetween(bestMove.getStartPositionTour2(),bestMove.getEndPositionTour2());		
			tour1.insertCustomersAtPosition(customers2, bestMove.getStartPositionTour1());
			tour2.insertCustomersAtPosition(customers1, bestMove.getStartPositionTour2());
			solution.removeEmptyTours();
		}					
		resetNeighborhood();
		//TODO: Update Tabu list;
		return solution;		
	}
	
	private boolean isInsertedBeforePositionWhereSegmentIsRemoved(AbstractNeighborhoodMove bestMove) {		
		return bestMove.getStartPositionTour2() < bestMove.getStartPositionTour1();
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
