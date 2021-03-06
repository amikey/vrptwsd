package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.util.List;

import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.solver.util.TourUtils;

public class CrossNeighborhood extends AbstractNeighborhood {
	
	protected Tour tour1;
	protected Tour tour2;
	
	private int tourCounter1 = 0;
	private int tourCounter2 = 0;
	
	protected double costOfCompleteSolutionThatResultsFromMove;
	protected double costDifferenceToPreviousSolution;
	private boolean firstNeighborhoodStep;

	//mit den Positionen sind jeweils die Einfügepositionen gemeint. 
	//D.h. bei einer Tour mit vier Kunden gibt es die Positionen 0,1,2,3,4
	protected int positionStartOfSegmentTour1 = 0;
	protected int positionEndOfSegmentTour1 = 0;
	protected int positionStartOfSegmentTour2 = 0;
	protected int positionEndOfSegmentTour2 = 0;
	
	private ResourceExtensionFunction RefSegment1;
	private ResourceExtensionFunction RefSegment2;
		
	//auxiliary fields
	private ResourceExtensionFunction currentRef;
	private Tour currentTour;
	private int currentPositionEndOfSegment;
	private boolean isCurrentSegmentInTour2ViolatesTWorCapacityConstraint = false;
	
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
		//bestMoveThatMightBeTaboo existiert nur, um unterscheiden zu können, ob es keine zulässingen Moves gibt, oder ob alle Moves tabu sind.
		bestMoveThatMightBeTaboo = null;
		ResetListOfNonTabooMovesHook();
	}
		
	protected void ResetListOfNonTabooMovesHook() {
		//nothing to do here; hook exists for stochastic version
	}

	public AbstractNeighborhoodMove returnBestMove() throws Exception {
		int iteration = Integer.MAX_VALUE;
		return returnBestMove(iteration);
	}
	
	public AbstractNeighborhoodMove returnBestMove(int iteration) throws Exception {
//		System.out.println("Solution in CrossNeighborhood bei Start von returnBestMove: " + elementWithTours.getTotalDistanceWithCostFactor());
		
		resetNeighborhood();
		//RUNTIME_TODO: entfernen
		if (Parameters.isTestingMode())
			assertThatSolutionContainsNoEmptyTours();
		generateMovesUsingRefs(iteration);
		if (bestNonTabooMove == null) {
			if (bestMoveThatMightBeTaboo == null)
				throw new NoSolutionExistsException("No feasible move found because of feasibility constraints");
			else 
				throw new NoSolutionExistsException("No move found because all feasible moves are taboo");
		}			
		return bestNonTabooMove;
	}
	
		private void assertThatSolutionContainsNoEmptyTours() {
			assertEquals(false, elementWithTours.isHasEmptyToursThatAreNotForRecourse());	
		}

		private void generateMovesUsingRefs(int iteration) {
			while (isExistsNextCombinationOfSegments()) {
				generateNextCombinationOfSegments();
				if (!segmentsToBeSwapedAreNotInNeighborhoodRefPositions()) {															
					//for testing
					if (Parameters.isTestingMode())
						assertEquals(isMoveFeasibleCheckNaiv(),isMoveFeasibleCheckWithRef());
					
					if (isMoveFeasibleCheckWithRef()) {
						calculateCostUsingRefs();
						AbstractNeighborhoodMove move = getNeigborhoodMove();
						
						//for testing
						if (Parameters.isTestingMode())
							assertEquals(true, move.isMoveValidWrtPositions());
						
						if (isMoveNewBestMove(move)) {							
							// bestMoveThatMightBeTaboo existiert nur, um unterscheiden zu können, ob es keine zulässingen Moves gibt, oder ob alle Moves tabu sind.
							bestMoveThatMightBeTaboo = move;
							if (!isMoveTaboo(move, iteration)) {								
								setRespAddBestNonTabooMove(move);
							}
						}
					}
				}
			}
			setBestNonTabooMoveHook();
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
			return (startOfSegmentToBeRemovedCanBeIncreasedInTour2() || endOfSegmentToBeRemovedCanBeIncreasedInTour2());
		}
								
			private boolean startOfSegmentToBeRemovedCanBeIncreasedInTour2() {
				return positionStartOfSegmentTour2 < tour2.length();	
			}
			
			boolean endOfSegmentToBeRemovedCanBeIncreasedInTour2() {
				if (isCurrentSegmentLeadsToViolationOfTWorCapacityConstraint())
					return false;
				return positionEndOfSegmentTour2 < Math.min((positionStartOfSegmentTour2 + Parameters.getMaximalLengthOfSegmentInMove()),tour2.length());
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
				return positionEndOfSegmentTour1 < Math.min(positionStartOfSegmentTour1 + Parameters.getMaximalLengthOfSegmentInMove(),tour1.length());
			}
		
		boolean tour2CanBeIncreased() {
			return tourCounter2 < elementWithTours.getNumberOfTours()-1;
		}
		
		private boolean tour1CanBeIncreased() {
			return tourCounter1 < elementWithTours.getNumberOfTours()-1; 
		}
		
		
	protected void generateNextCombinationOfSegments() {	
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
			else if (startOfSegmentToBeRemovedCanBeIncreasedInTour2())
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
			if(!TourUtils.isInsertionOfRefPossibleWrtTWAndDemandInnerTourMove(tour1,positionStartOfSegmentTour1,positionEndOfSegmentTour1,positionStartOfSegmentTour2))
				isWasInsertionPossible = false;			
		} else {					
			//move between two tours
			//try inserting segment 2 in tour 1
			if (!TourUtils.isInsertionOfRefPossible(tour1,RefSegment2,positionStartOfSegmentTour1,positionEndOfSegmentTour1)) { 
				isWasInsertionPossible = false;				
				isCurrentSegmentInTour2ViolatesTWorCapacityConstraint = true;
			}		
			//try inserting segment1 in tour 2
			//both items need to be feasible in order to apply move. If first wasn't feasible, then second doesn't need to be checked. 		
			else if (!TourUtils.isInsertionOfRefPossible(tour2,RefSegment1,positionStartOfSegmentTour2,positionEndOfSegmentTour2)) {
				isWasInsertionPossible = false;				
			}		
			//check whether tour 1 is feasible 			
		}
		return isWasInsertionPossible;
	}
	
		private boolean isInnerTourMove() { 
			return tourCounter1 == tourCounter2;
			//bei inner Tour moves wird immer aus tour 1 entfernt und in tour zwei eingefügt
		}
		
	public boolean isMoveFeasibleCheckNaiv() {			
			
			printNeighborhoodStep();
			
			Tour tour1clone;
			Tour tour2clone;
			
			if (isInnerTourMove()) {							
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
			
			//first insert customers from tour 2 in tour 1
			int numberOfCustomersThatWasRemovedFromTour2 = positionEndOfSegmentTour2 - positionStartOfSegmentTour2; 
			for (int i = 0 ; i < numberOfCustomersThatWasRemovedFromTour2; i++) {
				Customer customer = customers2.get(i);
				if (TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour1clone, i+positionStartOfSegmentTour1)) {
					tour1clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour1);
				} else {
					isWasInsertionPossible = false;
					break;
				}
			}		
			
			//then insert customers from tour 1 in tour 2
			int numberOfCustomersThatWasRemovedFromTour1 = positionEndOfSegmentTour1 - positionStartOfSegmentTour1; 
			if (!isInnerTourMove()) 
				for (int i = 0; i < numberOfCustomersThatWasRemovedFromTour1; i++) {
					Customer customer = customers1.get(i);			
					if (TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour2clone, i + positionStartOfSegmentTour2)) {
						tour2clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour2);				
					} else {
						isWasInsertionPossible = false;
						break;
					}			
				}
			
			if (isInnerTourMove())
				for (int i = 0; i < numberOfCustomersThatWasRemovedFromTour1; i++) {
					Customer customer = customers1.get(i);			
					if (isInsertingPositionAfterRemovedSegment()) {
						if (TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour2clone, i + positionStartOfSegmentTour2 - numberOfCustomersThatWasRemovedFromTour1)) {
							tour2clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour2);				
						} else {
							isWasInsertionPossible = false;
							break;
						}
					} else {
						if (TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour2clone, i + positionStartOfSegmentTour2)) {
							tour2clone.insertCustomerAtPosition(customer, i + positionStartOfSegmentTour2);				
						} else {
							isWasInsertionPossible = false;
							break;
						}
					}
				}
				
			return isWasInsertionPossible;
		}
	
		
	
		private boolean isInsertingPositionAfterRemovedSegment() {
			return !(positionStartOfSegmentTour2 < positionStartOfSegmentTour1);
		}

		public double calculateCostUsingRefs() {					
			
			double costSolution = elementWithTours.getTotalDistanceWithCostFactor();
			double costTour1 = 0;
			double costTour2 = 0;			
								
			//cost calculation caters for all cases: innerTour moves, moves where one segment is swapped
			//and moves where two segments are swapped as tested in Unit tests in CrossNeighborhoodCostCalculationTest
			
			//calculate cost for tour1
			if (isSegmentRemovedFromTour1()) {
				//remove edge between last customer before segment and first customer in segment from tour 1			
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength() * tour1.getCostFactor();
				//remove edge between last customer in segment and first customer after segment from tour 1		
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength() * tour1.getCostFactor();
				//remove edges from segment (might be important because of cost factor of tour)
				costTour1 -= tour1.getDistanceForSegmentBetweenPositions(positionStartOfSegmentTour1,positionEndOfSegmentTour1) * tour1.getCostFactor();
			} else
				//remove edge between last customer before inserting position and first customer after inserting position
				costTour1 -= new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength() * tour1.getCostFactor();
				
			if (isSegmentRemovedFromTour2()) {
				//add edge between last customer before segment from tour 1 and first customer in segment from tour 2				
				costTour1 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1), tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength() * tour1.getCostFactor();
				//add edge between last customer in segment from tour 2 and first customer after inserting position
				costTour1 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength() * tour1.getCostFactor();
				//add edges from inserted segment (might be important because of cost factor of tour)
				costTour1 += tour2.getDistanceForSegmentBetweenPositions(positionStartOfSegmentTour2,positionEndOfSegmentTour2) * tour1.getCostFactor();
			} else
				//add edge between last customer before segment and first customer after segment
				costTour1 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1-1),tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1)).getLength() * tour1.getCostFactor();
								
			//calculate cost for tour2
			if (isSegmentRemovedFromTour2()) {
				//remove edge between last customer before segment and first customer in segment from tour 2
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength() * tour2.getCostFactor();
				//remove edge between last customer in segment and first customer after segment from tour 2
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength() * tour2.getCostFactor();
				//remove edges from inserted segment (might be important because of cost factor of tour)
				costTour2 -= tour2.getDistanceForSegmentBetweenPositions(positionStartOfSegmentTour2,positionEndOfSegmentTour2) * tour2.getCostFactor();
			} else
				//remove edge between last customer before inserting position and first customer after inserting position
				costTour2 -= new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2)).getLength() * tour2.getCostFactor();
			
			if (isSegmentRemovedFromTour1()) {
				//add edge between last customer before segment from tour 2 and first customer in segment from tour 1
				costTour2 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1), tour1.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour1)).getLength() * tour2.getCostFactor();
				//add edge between last customer in segment from tour 1 and first customer after inserting position
				costTour2 += new Edge(tour1.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour1-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength() * tour2.getCostFactor();
				//add edges from inserted segment from tour 1 (might be important because of cost factor of tour)
				costTour2 += tour1.getDistanceForSegmentBetweenPositions(positionStartOfSegmentTour1,positionEndOfSegmentTour1) * tour2.getCostFactor();
			} else 
				//add edge between last customer before segment and first customer after segment
				costTour2 += new Edge(tour2.getCustomerAtPositionIncludingDepot(positionStartOfSegmentTour2-1),tour2.getCustomerAtPositionIncludingDepot(positionEndOfSegmentTour2)).getLength() * tour2.getCostFactor();
			
			costOfCompleteSolutionThatResultsFromMove = costSolution + costTour1 + costTour2;
			costDifferenceToPreviousSolution = costTour1 + costTour2;

			//return wird durchgeführt, damit Methode auch von Tests verwendet werden kann.
			return costOfCompleteSolutionThatResultsFromMove;
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
					costOfCompleteSolutionThatResultsFromMove, costDifferenceToPreviousSolution);	
		}

		
	//********************	
		
	public ElementWithTours actuallyApplyMoveAndMaintainNeighborhood(AbstractNeighborhoodMove bestMove) {
		actuallyApplyMove(bestMove);
		elementWithTours.removeEmptyToursAndGots();
		resetNeighborhood();
		return elementWithTours;	
	}
	
		private static boolean isInsertedBeforePositionWhereSegmentIsRemoved(AbstractNeighborhoodMove bestMove) {		
			return bestMove.getStartPositionTour2() < bestMove.getStartPositionTour1();
		}
		
	public static void actuallyApplyMove(AbstractNeighborhoodMove bestMove) {
		if (!bestMove.isMoveValidWrtPositions())
			throw new RuntimeException("Move that should be applied is not feasible");
		
		if (bestMove.isInnerTourMove()) {
			//inner tour moves always happen in tour 1
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
			
			//remove empty tours
			for(GroupOfTours got : bestMove.getGots())
				got.removeEmptyToursAndGots();
		}		
	}
		

	
	//Utilities

	//this exists only for testing
	public ElementWithTours actuallyApplyMoveAndMaintainNeighborhood() {
		AbstractNeighborhoodMove move = this.getNeigborhoodMove();
		return actuallyApplyMoveAndMaintainNeighborhood(move);		
	}
	
	public String getNeighborhoodStepAsString() {
		String s = "Move " + tour1.getId() + ": (";		
		for (int i = positionStartOfSegmentTour1; i < positionEndOfSegmentTour1; i++) {
			s += tour1.getCustomerAtPosition(i).getCustomer().getCustomerNo() + " ";
		}
		s += ")[" + positionStartOfSegmentTour1 + "] " +
				tour2.getId() + "(";
		for (int i = positionStartOfSegmentTour2; i < positionEndOfSegmentTour2; i++)
			s+= tour2.getCustomerAtPosition(i).getCustomer().getCustomerNo() + " ";
		s+= ")[" + positionStartOfSegmentTour2 + "] ";
		return s;
	}

	public void printNeighborhoodStep() {
		System.out.print("Move " + tour1.getId() + ": (");		
		for (int i = positionStartOfSegmentTour1; i < positionEndOfSegmentTour1; i++) {
			System.out.print(tour1.getCustomerAtPosition(i).getCustomer().getCustomerNo() + " ");
		}
		System.out.print(")[" + positionStartOfSegmentTour1 + "] " +
				tour2.getId() + "(");
		for (int i = positionStartOfSegmentTour2; i < positionEndOfSegmentTour2; i++)
			System.out.print(tour2.getCustomerAtPosition(i).getCustomer().getCustomerNo() + " ");
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