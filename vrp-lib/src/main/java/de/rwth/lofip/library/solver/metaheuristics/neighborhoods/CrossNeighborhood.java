package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.List;

import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.NeighborhoodInterface;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.solver.util.TourUtils;

public class CrossNeighborhood implements NeighborhoodInterface {
	
	private Tour tour1;
	private Tour tour2;
	private ElementWithTours elementWithTours;
	
	private int tourCounter1 = 0;
	private int tourCounter2 = 0;
	
	private double costOfCompleteSolutionThatResultsFromMove;
	private boolean firstNeighborhoodStep;

	//mit den Positionen sind jeweils die Einfügepositionen gemeint. 
	//D.h. bei einer Tour mit vier Kunden gibt es die Positionen 0,1,2,3,4
	private int positionStartOfSegmentTour1 = 0;
	private int positionEndOfSegmentTour1 = 0;
	private int positionStartOfSegmentTour2 = 0;
	private int positionEndOfSegmentTour2 = 0;
	
	private ResourceExtensionFunction RefSegment1;
	private ResourceExtensionFunction RefSegment2;
		
	//auxiliary fields
	private ResourceExtensionFunction currentRef;
	private Tour currentTour;
	private int currentPositionEndOfSegment;
	private boolean isCurrentSegmentInTour2ViolatesTWorCapacityConstraint = false;
	
	protected AbstractNeighborhoodMove bestNonTabooMove = null;
	private AbstractNeighborhoodMove bestMoveThatMightBeTaboo = null;
	
	public CrossNeighborhood(ElementWithTours solution) {
		this.elementWithTours = solution;
		resetNeighborhood();
	}
	
	public void resetNeighborhood() {
		firstNeighborhoodStep = true;
		tourCounter1 = 0;
		tourCounter2 = 0;
		this.tour1 = elementWithTours.getTour(tourCounter1);		
		this.tour2 = elementWithTours.getTour(tourCounter2);
		positionStartOfSegmentTour1 = 0;
		positionEndOfSegmentTour1 = 0;
		positionStartOfSegmentTour2 = 0;
		positionEndOfSegmentTour2 = 0;
		RefSegment1 = new ResourceExtensionFunction();
		RefSegment2 = new ResourceExtensionFunction();
		bestNonTabooMove = null;
		bestMoveThatMightBeTaboo = null;
	}
		
	public AbstractNeighborhoodMove returnBestMove() throws Exception {
		int iteration = Integer.MAX_VALUE;
		return returnBestMove(iteration);
	}
	
	public AbstractNeighborhoodMove returnBestMove(int iteration) throws Exception {
		resetNeighborhood();		
		generateMovesUsingRefs(iteration);
		if (bestNonTabooMove == null) {
			if (bestMoveThatMightBeTaboo == null)
				throw new NoSolutionExistsException("No feasible move found because of feasibility constraints");
			else 
				throw new NoSolutionExistsException("No move found because all feasible moves are taboo");
		}			
		return bestNonTabooMove;
	}
		
		private void generateMovesUsingRefs(int iteration) {
			while (isExistsNextCombinationOfSegments()) {
				generateNextCombinationOfSegements();
				if (!segmentsToBeSwapedAreNotInNeighborhoodRefPositions()) {
//					FOR DEBUGGING:
//					if (tour1.getId() == 35 && tour2.getId() == 35 &&
//							positionStartOfSegmentTour1 == 4 && positionEndOfSegmentTour1 == 5 &&
//							positionStartOfSegmentTour2 == 3 && positionEndOfSegmentTour2 == 3 &&
//							iteration == 89)
//						System.out.println("DEBUGGING!");
					if (isMoveFeasibleCheckWithRef()) {						
						calculateCostUsingRefs();		
						AbstractNeighborhoodMove move = getNeigborhoodMove();
//						System.out.println("feasible move found. Kosten: " + move.getCost());
						if (isMoveNewBestMove(move)) {
							bestMoveThatMightBeTaboo = move;
//							System.out.println("move ist neuer bester move.");
							if (!isMoveTaboo(move, iteration)) {
//								System.out.println("move ist NICHT tabu");
								setRespAddBestNonTabooMove(move);								
							}
//							} else 
//								System.out.println("move ist tabu");
						}
					}
				}
			}
			setBestNonTabooMove();
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
	
		boolean segmentInTour2CanBeIncreased() {			
			return (startOfSegementToBeRemovedCanBeIncreasedInTour2() || endOfSegmentToBeRemovedCanBeIncreasedInTour2());
		}
								
			private boolean startOfSegementToBeRemovedCanBeIncreasedInTour2() {
				return positionStartOfSegmentTour2 < tour2.length();	
			}
			
			boolean endOfSegmentToBeRemovedCanBeIncreasedInTour2() {
				if (isCurrentSegmentLeadsToViolationOfTWorCapacityConstraint())
					return false;
				return positionEndOfSegmentTour2 < tour2.length();
			}
	
			private boolean isCurrentSegmentLeadsToViolationOfTWorCapacityConstraint() {
				return isCurrentSegmentInTour2ViolatesTWorCapacityConstraint;
			}

		private boolean segmentInTour1CanBeIncreased() {
			return (startOfSegementToBeRemovedCanBeIncreasedInTour1() || endOfSegmentToBeRemovedCanBeIncreasedInTour1());
		}
		
			private boolean startOfSegementToBeRemovedCanBeIncreasedInTour1() {
				return positionStartOfSegmentTour1 < tour1.length();	
			}
		
			private boolean endOfSegmentToBeRemovedCanBeIncreasedInTour1() {
				return positionEndOfSegmentTour1 < tour1.length();
			}
		
		boolean tour2CanBeIncreased() {
			return tourCounter2 < elementWithTours.getNumberOfTours()-1;
		}
		
		private boolean tour1CanBeIncreased() {
			return tourCounter1 < elementWithTours.getNumberOfTours()-1; 
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
		
		private void increaseSegmentInTour2() {
			if (endOfSegmentToBeRemovedCanBeIncreasedInTour2())
				tour2IncreaseEndOfSegmentToBeRemoved();
			else if (startOfSegementToBeRemovedCanBeIncreasedInTour2())
				increaseStartOfSegmentToBeRemovedInTour2();
			else throw new RuntimeException("increaseSegmentInTour2 was called although cannot be executed");
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
				isCurrentSegmentInTour2ViolatesTWorCapacityConstraint = false;
			}			
			
				private void resetRefSegment2() {
					RefSegment2.reset();
				}
	
		private void resetSegmentInTour2() {
			positionStartOfSegmentTour2 = 0;
			positionEndOfSegmentTour2 = 0;		
		}
	
		private void increaseSegmentInTour1() {
			if (endOfSegmentToBeRemovedCanBeIncreasedInTour1())
				increaseEndOfSegmentToBeRemovedInTour1();
			else if (startOfSegementToBeRemovedCanBeIncreasedInTour1())
				increaseStartOfSegmentToBeRemovedInTour1();
			else throw new RuntimeException("generateNextNeigborhoodStep() was called although no remaining NeigborhoodStep exists");	
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
			
			private void increaseStartOfSegmentToBeRemovedInTour1() {
				positionStartOfSegmentTour1++;
				positionEndOfSegmentTour1=positionStartOfSegmentTour1;
				resetRefSegment1();
			}
			
			private void resetRefSegment1() {
				RefSegment1.reset();
			}
			
		private void increaseTour2() {
			tourCounter2++;
			tour2 = elementWithTours.getTour(tourCounter2);
		}
		
		private void resetSegmentInTour1() {
			positionStartOfSegmentTour1 = 0;
			positionEndOfSegmentTour1 = 0;		
		}
		
		private void increaseTour1() {
			tourCounter1++;
			tour1 = elementWithTours.getTour(tourCounter1);
		}
	
		private void setTour2Accordingly() {
			tourCounter2 = tourCounter1;
			tour2 = elementWithTours.getTour(tourCounter2);
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
		
	
	public boolean isMoveFeasibleCheckWithRef() {		
		boolean isWasInsertionPossible = true;
		if (isInnerTourMove()) { 			
			return TourUtils.isInsertionOfRefPossibleInnerTourMove(tour1,positionStartOfSegmentTour1,positionEndOfSegmentTour1,positionStartOfSegmentTour2);					
		} else {		
			//move between two tours
			//try inserting segment 2 in tour 1
			if (!TourUtils.isInsertionOfRefPossible(tour1,RefSegment2,positionStartOfSegmentTour1,positionEndOfSegmentTour1)) { 
				isWasInsertionPossible = false;				
				isCurrentSegmentInTour2ViolatesTWorCapacityConstraint = true;
			}
			//try inserting segment1 in tour 2
			else if (!TourUtils.isInsertionOfRefPossible(tour2,RefSegment1,positionStartOfSegmentTour2,positionEndOfSegmentTour2))
				isWasInsertionPossible = false;
		}
		return isWasInsertionPossible;
	}
	
		private boolean isInnerTourMove() { 
			return tourCounter1 == tourCounter2;
			//bei inner Tour moves wird immer aus tour 1 entfernt und in tour zwei eingefügt
		}
		
	
		public double calculateCostUsingRefs() {
			double costSolution = elementWithTours.getTotalDistance();			
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
		
		
		private boolean isMoveNewBestMove(AbstractNeighborhoodMove move) {	
			if (bestNonTabooMove == null) 
				return true;
			if (move.getCost() <= bestNonTabooMove.getCost() || //hier weiß man nicht, ob die Anzahl an Fahrzeugen verringert wird 
					(move.reducesNumberOfVehicles() && !bestNonTabooMove.reducesNumberOfVehicles())) // so werden moves bevorzugt, die die Fahrzeuganzahl verringern
				return true;
			else 
				return false;
		}

		protected boolean isMoveTaboo(AbstractNeighborhoodMove move, int iteration) { 
			return false;
		}
		
		protected void setRespAddBestNonTabooMove(AbstractNeighborhoodMove move) {
			bestNonTabooMove = move;
		}
		
		protected void setBestNonTabooMove() {
			//nothing to do here; hook is needed for Recourse calculation
		}
		
	//********************	
	public ElementWithTours acctuallyApplyMove(AbstractNeighborhoodMove bestMove) {
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
			elementWithTours.removeEmptyTours();
		}					
		resetNeighborhood();
		return elementWithTours;		
	}
	
		private boolean isInsertedBeforePositionWhereSegmentIsRemoved(AbstractNeighborhoodMove bestMove) {		
			return bestMove.getStartPositionTour2() < bestMove.getStartPositionTour1();
		}
		

	
	//Utilities

	public ElementWithTours acctuallyApplyMove() {
		AbstractNeighborhoodMove move = this.getNeigborhoodMove();
		return acctuallyApplyMove(move);		
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

	protected ResourceExtensionFunction getRefSegment1() {
		return RefSegment1;		
	}

	public ResourceExtensionFunction getRefSegment2() {
		return RefSegment2;		
	}
	
	public Tour getTour1() {
		return tour1;	
	}
	
	public boolean isNeighborhoodStepEquals(int i1, int i2, int i3, int i4) {
		boolean returnValue = positionStartOfSegmentTour1 == i1 && positionEndOfSegmentTour1 == i2 &&
				positionStartOfSegmentTour2 == i3 && positionEndOfSegmentTour2 == i4;
		if (returnValue == false)
			printNeighborhoodStep();
		return returnValue;		
	}


}
