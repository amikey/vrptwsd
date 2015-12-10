package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodCostCalculationTest {
	
	@Test
	public void testCostCalculationOfCrossNeighborhoodDoubleImprecision() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();
		Tour tour = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got = new GroupOfTours();
		got.addTour(tour);
		tour.addCustomer(problem.getCustomerWithCustomerNo(94));
		tour.addCustomer(problem.getCustomerWithCustomerNo(27));
		SolutionGot solution = new SolutionGot(problem);
		solution.addGot(got);;
		
		Edge edge1 = new Edge(problem.getDepot(),problem.getCustomerWithCustomerNo(94));
		Edge edge2 = new Edge(problem.getCustomerWithCustomerNo(94),problem.getCustomerWithCustomerNo(27));
		Edge edge3 = new Edge(problem.getCustomerWithCustomerNo(27),problem.getDepot());
		double costTour9427 = edge1.getLength() + edge2.getLength() + edge3.getLength();
		System.out.println(costTour9427);
		
		Edge edge4 = new Edge(problem.getDepot(),problem.getCustomerWithCustomerNo(27));
		Edge edge5 = new Edge(problem.getCustomerWithCustomerNo(27),problem.getCustomerWithCustomerNo(94));
		Edge edge6 = new Edge(problem.getCustomerWithCustomerNo(94),problem.getDepot());
		double costTour2794 = edge4.getLength() + edge5.getLength() + edge6.getLength();
		System.out.println(costTour2794);
		
		assertEquals(edge2.getLength(),edge5.getLength(),0.000000000000000000000000000000001);
		assertEquals(edge1.getLength(),edge6.getLength(),0.000000000000000000000000000000001);
		assertEquals(edge3.getLength(),edge4.getLength(),0.000000000000000000000000000000001);
		
		TabuSearchForElementWithTours ts = new TabuSearchForElementWithTours();
		ts.tryToImproveNewBestSolutionWithIntensificationPhase(solution);
		
		throw new RuntimeException("Irgendjemanden fragen, wie das mit der imprecision kommt und warum das in LS passiert, aber nicht, wenn ich in diesem UnitTest auf Gleichheit prüfe");
	}
	
	@Test 
	public void testInnerTourMoveCostCalculation() throws Exception {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();
		GroupOfTours got = getGotWithTour39_88_55_68_70FromRC103();
		SolutionGot solution = new SolutionGot(problem);
		solution.addGot(got);
		
		CrossNeighborhood cn  = new CrossNeighborhood(solution);
		AbstractNeighborhoodMove nm = cn.returnBestMove();
		nm.print();
		
		//create tour that results from other tour when applying best move
		Tour tour2 = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got2 = new GroupOfTours();
		got2.addTour(tour2);
		tour2.addCustomer(problem.getCustomerWithCustomerNo(39));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(88));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(68));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(55));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(70));
		
		System.out.println(got.getFirstTour().getTotalDistanceWithCostFactor()); 
		System.out.println(tour2.getTotalDistanceWithCostFactor());
		assertEquals(tour2.getTotalDistanceWithCostFactor() - got.getFirstTour().getTotalDistanceWithCostFactor(), nm.getCostDifferenceToPreviousSolution(), 0.000001);
		
		//now also test whether applyingMoveToUnderlyingGots results in correct costs
		double difference = -(solution.getTotalDistanceWithCostFactor() - got2.getTotalDistanceWithCostFactor());
		thenCloningNMAndApplyingMoveToUnderlyingGotsShouldResultInCorrectCosts(difference, nm);
	}
	
	private GroupOfTours getGotWithTour39_88_55_68_70FromRC103() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();
		
		//create tour that is going to be optimized
		Tour tour = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got = new GroupOfTours();
		got.addTour(tour);
		tour.addCustomer(problem.getCustomerWithCustomerNo(39));
		tour.addCustomer(problem.getCustomerWithCustomerNo(88));
		tour.addCustomer(problem.getCustomerWithCustomerNo(55));
		tour.addCustomer(problem.getCustomerWithCustomerNo(68));
		tour.addCustomer(problem.getCustomerWithCustomerNo(70));
		return got;
	}

	@Test 
	public void testInnerTourMoveCostCalculation2() throws Exception {
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		CrossNeighborhood cn  = new CrossNeighborhood(solution);
		AbstractNeighborhoodMove nm = cn.returnBestMove();
		nm.print();
		SolutionGot solution2 = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();
		System.out.println(solution.getTotalDistanceWithCostFactor()); 
		System.out.println(solution2.getTotalDistanceWithCostFactor());
		assertEquals(solution2.getTotalDistanceWithCostFactor() - solution.getTotalDistanceWithCostFactor(), nm.getCostDifferenceToPreviousSolution(), 0.000001);
		
		//now also test whether applyingMoveToUnderlyingGots results in correct costs
		double difference = -(solution.getTotalDistanceWithCostFactor() - solution2.getTotalDistanceWithCostFactor());
		thenCloningNMAndApplyingMoveToUnderlyingGotsShouldResultInCorrectCosts(difference, nm);
	}
	
	@Test
	public void testOneSegmentSwappedCostCalculation() throws Exception {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();
		GroupOfTours got = getGotWithTour39_88_55_68_70FromRC103();
		SolutionGot solution = new SolutionGot(problem);
		solution.addGot(got);
		
		//create tour two that is optimized
		Tour tour2 = new Tour(problem.getDepot(), problem.getVehicle());		
		got.addTour(tour2);
		tour2.addCustomer(problem.getCustomerWithCustomerNo(69));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(60));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(53));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(78));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(73));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(17));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(47));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(98));
		
		CrossNeighborhood cn  = new CrossNeighborhood(solution);
		AbstractNeighborhoodMove nm = cn.returnBestMove();
//		nm.print();
		
		//create solution that results from applying best move
		Tour tour3 = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got2 = new GroupOfTours();
		got2.addTour(tour3);
		tour3.addCustomer(problem.getCustomerWithCustomerNo(39));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(88));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(60));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(55));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(68));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(70));
		SolutionGot solution2 = new SolutionGot(problem);
		solution2.addGot(got2);
		
		//create tour two that is optimized
		Tour tour4 = new Tour(problem.getDepot(), problem.getVehicle());		
		got2.addTour(tour4);
		tour4.addCustomer(problem.getCustomerWithCustomerNo(69));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(53));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(78));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(73));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(17));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(47));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(98));
		
		//test that calculation of costs in CrossNeighborhood is correct
		double difference = -(solution.getTotalDistanceWithCostFactor() - solution2.getTotalDistanceWithCostFactor());
		System.out.println(solution.getTotalDistanceWithCostFactor());
		System.out.println(solution2.getTotalDistanceWithCostFactor());
		assertEquals(difference, nm.getCostDifferenceToPreviousSolution(), 0.00001);
		
		//now also test whether applyingMoveToUnderlyingGots results in correct costs
		thenCloningNMAndApplyingMoveToUnderlyingGotsShouldResultInCorrectCosts(difference, nm);
	}
	
	private void thenCloningNMAndApplyingMoveToUnderlyingGotsShouldResultInCorrectCosts(
			double difference, AbstractNeighborhoodMove nm) {
		AbstractNeighborhoodMove nmClone = nm.cloneWithCopyOfToursAndGotsAndCustomers();
		CrossNeighborhoodWithTabooListAndRecourse.applyMoveToUnderlyingGots(nmClone);
		double originalCost = 0;
		for (GroupOfTours got :  nm.getGots())
			originalCost += got.getTotalDistanceWithCostFactor();				
		double costForNMWithAppliedMove = 0;
		for (GroupOfTours got :  nmClone.getGots())
			costForNMWithAppliedMove += got.getTotalDistanceWithCostFactor();
		double differenceCloned = -(originalCost - costForNMWithAppliedMove);
		assertEquals(difference, differenceCloned, 0.00001);
	}

	@Test
	public void testTwoSegmentSwappedCostCalculation() throws Exception {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();
		
		//create tour that is going to be optimized
		Tour tour = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got = new GroupOfTours();
		got.addTour(tour);
		tour.addCustomer(problem.getCustomerWithCustomerNo(57));
		tour.addCustomer(problem.getCustomerWithCustomerNo(86));
		tour.addCustomer(problem.getCustomerWithCustomerNo(24));
		tour.addCustomer(problem.getCustomerWithCustomerNo(19));
		tour.addCustomer(problem.getCustomerWithCustomerNo(48));
		tour.addCustomer(problem.getCustomerWithCustomerNo(58));
		tour.addCustomer(problem.getCustomerWithCustomerNo(20));
		SolutionGot solution = new SolutionGot(problem);
		solution.addGot(got);
				
		//create tour two that is optimized
		Tour tour2 = new Tour(problem.getDepot(), problem.getVehicle());		
		got.addTour(tour2);
		tour2.addCustomer(problem.getCustomerWithCustomerNo(23));
		tour2.addCustomer(problem.getCustomerWithCustomerNo(75));
		
		CrossNeighborhood cn  = new CrossNeighborhood(solution);
		AbstractNeighborhoodMove nm = cn.returnBestMove();
		nm.print();
		
		//create tour that has been optimized hardcoded
		Tour tour3 = new Tour(problem.getDepot(), problem.getVehicle());
		GroupOfTours got2 = new GroupOfTours();
		got2.addTour(tour3);
		tour3.addCustomer(problem.getCustomerWithCustomerNo(57));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(86));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(75));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(58));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(20));
		SolutionGot solution2 = new SolutionGot(problem);
		solution2.addGot(got2);
						
		//create tour two that has been optimized hardcoded
		Tour tour4 = new Tour(problem.getDepot(), problem.getVehicle());		
		got2.addTour(tour4);
		tour4.addCustomer(problem.getCustomerWithCustomerNo(23));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(24));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(19));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(48));
		
		//test that calculation of costs in CrossNeighborhood is correct
		double difference = -(solution.getTotalDistanceWithCostFactor() - solution2.getTotalDistanceWithCostFactor());
		System.out.println(solution.getTotalDistanceWithCostFactor());
		System.out.println(solution2.getTotalDistanceWithCostFactor());
		assertEquals(difference, nm.getCostDifferenceToPreviousSolution(), 0.00001);
		
		//now also test whether applyingMoveToUnderlyingGots results in correct costs
		thenCloningNMAndApplyingMoveToUnderlyingGotsShouldResultInCorrectCosts(difference, nm);
	}

}
