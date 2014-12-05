package de.rwth.lofip.library;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.RepairSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;

public class SolutionTest {
	Solution solution;

	@Test
	public void testThatFeasibleSolutionIsFeasible() {
		givenFeasibleSolution();

		thenSolutionShouldBeFeasible();
	}

	@Test
	public void testThatInfeasibleSolutionIsInfeasible() {
		givenInfeasibleSolution();

		thenSolutionShouldBeInfeasible();
	}

	@Test
	public void testPercentageOfToursExecutedTheSameDay() {
		givenInfeasibleSolutionWithOneTour();

		whenSolutionIsRepaired();

		thenPercentageOfToursExecutedTheSameDayShouldBe(0.5);
	}

	@Test
	public void testPercentageOfCustomersServedTheSameDay() {
		givenInfeasibleSolutionWithOneTour();

		whenSolutionIsRepaired();

		thenPercentageOfCustomersServedTheSameDayShouldBe(2 / 3);
	}

	@Test
	public void testPercentageOfParcelsCollectedTheSameDay() {
		givenInfeasibleSolutionWithOneTour();

		whenSolutionIsRepaired();

		thenPercentageOfParcelsCollectedTheSameDayShouldBe(2 / 3);
	}

	private void thenPercentageOfParcelsCollectedTheSameDayShouldBe(double d) {
		assertSame(solution.percentageOfParcelsCollectedTheSameDay(), d);
	}

	private void thenPercentageOfCustomersServedTheSameDayShouldBe(double d) {
		assertSame(solution.percentageOfCustomersServedTheSameDay(), d);
	}

	private void thenPercentageOfToursExecutedTheSameDayShouldBe(double d) {
		assertSame(solution.percentageOfToursExecutedTheSameDay(), d);
	}

	private void givenInfeasibleSolutionWithOneTour() {
		givenInfeasibleSolution();
	}

	private void whenSolutionIsRepaired() {
		try {
			repairSolution();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void repairSolution() throws Exception {
		Event e = createEventThatDoesNotChangeAnything();
		solution = new RepairSolution().repair(solution, e).get(0)
				.getNewSolution();
	}

	private Event createEventThatDoesNotChangeAnything() {
		return new Event(11, 50, 10);
	}

	private void givenFeasibleSolution() {
		// set up
		Customer c11 = new Customer();
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(11);
		c11.setDemand(50);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		Customer c12 = new Customer();
		c12.setCustomerNo(12);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(40);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

		Customer c13 = new Customer();
		c13.setCustomerNo(13);
		c13.setxCoordinate(25);
		c13.setyCoordinate(85);
		c13.setDemand(20);
		c13.setTimeWindowOpen(625);
		c13.setTimeWindowClose(721);
		c13.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new Tour(depot, vehicle);
		tour.addCustomer(c11);
		tour.addCustomer(c12);
		tour.addCustomer(c13);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c11);
		vrpProblem.addCustomer(c12);
		vrpProblem.addCustomer(c13);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		solution = new Solution(vrpProblem);
		solution.addTour(tour);
	}

	@Test
	public void testXmlSolution() throws JAXBException, IOException, SAXException {

		givenFeasibleSolution();

		JAXBContext ctx = JAXBContext
				.newInstance(new Class[] { Solution.class });

		SchemaCreatingSchemaOutput out = new SchemaCreatingSchemaOutput();
		ctx.generateSchema(out);
		Schema imlicitSchema = out.createSchema();
		System.out.println(out.out.toString());

		Marshaller marsh = ctx.createMarshaller();
//		marsh.setSchema(imlicitSchema);
		File file = new File("testSolution.xml");
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marsh.marshal(solution, file);
		marsh.marshal(solution, System.out);

//		Unmarshaller unmarsh = ctx.createUnmarshaller();
//		unmarsh.setSchema(imlicitSchema);
//		Object o = unmarsh.unmarshal(file);
//		assertEquals(solution.getClass(), o.getClass());

//		file.delete();

	}

	private void givenInfeasibleSolution() {
		// set up
		Customer c11 = new Customer();
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(11);
		c11.setDemand(20);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		Customer c12 = new Customer();
		c12.setCustomerNo(12);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(20);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

		Customer c13 = new Customer();
		c13.setCustomerNo(13);
		c13.setxCoordinate(25);
		c13.setyCoordinate(85);
		c13.setDemand(20);
		c13.setTimeWindowOpen(625);
		c13.setTimeWindowClose(721);
		c13.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 65);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new Tour(depot, vehicle);
		tour.addCustomer(c11);
		tour.addCustomer(c12);
		tour.addCustomer(c13);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c11);
		vrpProblem.addCustomer(c12);
		vrpProblem.addCustomer(c13);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		solution = new Solution(vrpProblem);
		solution.addTour(tour);
	}

	private void thenSolutionShouldBeFeasible() {
		assertTrue(solution.isSolutionFeasibleWrtDemand());
	}

	private void thenSolutionShouldBeInfeasible() {
		assertFalse(solution.isSolutionFeasibleWrtDemand());
	}

}
