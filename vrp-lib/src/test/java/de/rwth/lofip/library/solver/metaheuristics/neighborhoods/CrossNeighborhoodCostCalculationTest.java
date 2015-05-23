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

public class CrossNeighborhoodCostCalculationTest {
	
	@Test
	public void testCostCalculationOfCrossNeighborhood() throws IOException {
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
		
		throw new RuntimeException("Irgendjemanden (z.B. Andreas) fragen, wie das mit der imprecision kommt und warum das in LS passiert, aber nicht, wenn ich in diesem UnitTest auf Gleichheit prüfe");
	}

}
