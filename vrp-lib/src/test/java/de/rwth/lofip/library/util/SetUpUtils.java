package de.rwth.lofip.library.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class SetUpUtils {
	
	static Customer c1;
	static Customer c2;
	static Customer c3;
	static Customer c4; 
	static Depot depot;
	static Vehicle vehicle;
	static Set<Vehicle> vehicles;
	static VrpProblem vrpProblem;
	static SolutionGot solution;
	
	private static void setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution() {
		c1 = new Customer();
	    c1.setCustomerNo(1);
	    c1.setxCoordinate(10);
	    c1.setyCoordinate(80);        
	    c1.setDemand(20);
	    c1.setOriginalDemand(20);
	    c1.setTimeWindowOpen(278);
	    c1.setTimeWindowClose(345);
	    c1.setServiceTime(90);
	    
	    c2 = new Customer();
	    c2.setCustomerNo(2);
	    c2.setxCoordinate(20);
	    c2.setyCoordinate(85);
	    c2.setDemand(20);
	    c2.setOriginalDemand(20);
	    c2.setTimeWindowOpen(475);
	    c2.setTimeWindowClose(528);
	    c2.setServiceTime(90);
	
	    c3 = new Customer();
	    c3.setCustomerNo(3);
	    c3.setxCoordinate(25);
	    c3.setyCoordinate(85);
	    c3.setDemand(20);
	    c3.setOriginalDemand(20);
	    c3.setTimeWindowOpen(425);
	    c3.setTimeWindowClose(721);
	    c3.setServiceTime(90);
	    
	    c4 = new Customer();
	    c4.setCustomerNo(4);
	    c4.setxCoordinate(35);
	    c4.setyCoordinate(70);
	    c4.setDemand(20);
	    c4.setOriginalDemand(20);
	    c4.setTimeWindowOpen(873);
	    c4.setTimeWindowClose(921);
	    c4.setServiceTime(90);
	    
        depot = new Depot();
        depot.setxCoordinate(40);
        depot.setyCoordinate(50);
        depot.setDemand(0);
        depot.setTimeWindowOpen(0);
        depot.setTimeWindowClose(2000);
        depot.setServiceTime(0);
        
        vehicle = new Vehicle(1, 85);
        Set<Vehicle> vehicles = new HashSet<Vehicle>();
        vehicles.add(vehicle);
        
        //create vprProblem
	    vrpProblem = new VrpProblem();
	    vrpProblem.addCustomer(c1);
	    vrpProblem.addCustomer(c2);
	    vrpProblem.addCustomer(c3);
	    vrpProblem.addCustomer(c4);
	    vrpProblem.addDepot(depot);
	    vrpProblem.setVehicles(vehicles);
	    vrpProblem.setOriginalCapacity(vehicle.getCapacity());
	    vrpProblem.setMaxTime(10000);
	    
	    solution = new SolutionGot(vrpProblem);
	}
		
	public static Customer getC1() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		return c1;
	}
	
	public static Customer getC3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		return c3;
	}

	public static Customer getC4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		return c4;
	}
	
	public static Customer getC2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		return c2;
	}
	
	public static Customer getC2WithEarlierTimeWindowOpening() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		c2.setTimeWindowOpen(250);
		return c2;
	}
	
	public static Depot getDepot() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		return depot;
	}
	
	
	public static Tour getTourWithFourCustomers() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	private static Tour getTourWithFourCustomersC3BeforeC2() {
	setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	public static Tour getTourWithCustomers1And2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        
        return tour;
	}
	
	public static Tour getTourWithCustomer4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c4);        
        return tour;
	}
	
	public static Tour getTourWithCustomer2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        return tour;	
	}
	
	public static Tour getTourWithCustomer3And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c4);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        return tour;	
	}
	
	public static Tour getTourWithCustomer2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c2);        
        return tour;
	}

	public static Tour getTourWithCustomer3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c3);        
        return tour;	
	}
	
	public static Tour getTourWithCustomer1() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);        
        return tour;	
	}

	public static Tour getTourWithCustomer1And2And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        return tour;		
    }
	
	private static Tour getTourWithCustomer3And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle); 
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        return tour;
	}	
	
	public static Tour getEmptyTour() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
		return tour;
	}
	
	public static Tour getTourWithCustomers1And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle); 
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        return tour;
	}
	
	private static Tour getTourWithCustomersC1C3C2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours(null));
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;
	}
	
	public static Tour getTourWithCustomer_42_44_fromRC101() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC101();
		Tour tour = new Tour(problem.getDepot(), problem.getNewVehicle());
		GroupOfTours got = new GroupOfTours(null);
		got.addTour(tour);
		tour.addCustomer(problem.getCustomerWithCustomerNo(42));
		tour.addCustomer(problem.getCustomerWithCustomerNo(44));
		return tour;
	}
	
	public static SolutionGot getSolutionWithThreeToursAndTwoCustomersEach() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
                  
        Tour tour1 = new Tour(depot, vehicle);
        GroupOfTours got1 = new GroupOfTours(solution);
        got1.addTour(tour1);
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);                
        
        Tour tour2 = new Tour(depot, vehicle);
        GroupOfTours got2 = new GroupOfTours(solution);
        got2.addTour(tour2);
        tour2.addCustomer(c3);
        tour2.addCustomer(c4);        
        
        Tour tour3 = new Tour(depot, vehicle);
        GroupOfTours got3 = new GroupOfTours(solution);
        got3.addTour(tour3);
        tour3.addCustomer(c1);
        tour3.addCustomer(c2);        
        	            
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);
	    solution.addGot(got3);
	    
	    return solution;      
	}
	
	public static SolutionGot getSolutionWithTwoToursAndTwoCustomersEach() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
                  
        Tour tour1 = new Tour(depot, vehicle);
        tour1.setParentGot(new GroupOfTours(solution));
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);
        GroupOfTours got1 = new GroupOfTours(solution);
        got1.addTour(tour1);
        
        Tour tour2 = new Tour(depot, vehicle);
        tour2.setParentGot(new GroupOfTours(solution));
        tour2.addCustomer(c3);
        tour2.addCustomer(c4);
        GroupOfTours got2 = new GroupOfTours(solution);
        got2.addTour(tour2);             
	            
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);	    
	    return solution;      
	}
	
	public static SolutionGot getSolutionWithTwoToursWithOneAndThreeCustomersRespectively() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour1 = getTourWithCustomers1And2And3();
        GroupOfTours got1 = new GroupOfTours(solution);
        got1.addTour(tour1);
        
        Tour tour2 = getTourWithCustomer4();
        GroupOfTours got2 = new GroupOfTours(solution);
        got2.addTour(tour2);             
                    
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);	    
	    return solution;      
	}
	
	public static SolutionGot getSolutionWithTwoToursWithOneAndThreeCustomersOtherWayRound() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomer4());
		tours.add(getTourWithCustomers1And2And3());
		
		return getSolutionWithEachTourInOneGot(tours);     
	}
	
	public static SolutionGot getSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomer1());
		tours.add(getTourWithCustomer2());
		tours.add(getTourWithCustomer3And4());
		
		return getSolutionWithEachTourInOneGot(tours); 
	}
	
	public static SolutionGot getSolutionWithOneTourWithCustomersC1C2C3C4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithFourCustomers());
		
		return getSolutionWithEachTourInOneGot(tours); 
	}
	
	public static SolutionGot getSolutionWithOneTourWithCustomersC1C2C3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomers1And2And3());
		
		return getSolutionWithEachTourInOneGot(tours);
	}
	
	public static Object getSolutionWithOneTourWithCustomersC1C3C2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomersC1C3C2());
		
		return getSolutionWithEachTourInOneGot(tours);
	}

	public static SolutionGot getSolutionWithOneTourWithCustomersC1C3C2C4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithFourCustomersC3BeforeC2());
		
		return getSolutionWithEachTourInOneGot(tours); 
	}


	private static SolutionGot getSolutionWithEachTourInOneGot(List<Tour> tours) {
		for (Tour tour : tours) {
			GroupOfTours got = new GroupOfTours(solution);
			got.addTour(tour);
			solution.addGot(got);
		}      	   
	    return solution; 
	}

	public static SolutionGot getSolutionWithOneTourWithCustomer2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();		
		tours.add(getTourWithCustomer2And3());		
		return getSolutionWithEachTourInOneGot(tours); 
	}

	public static GroupOfTours getGotWithCustomer1And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		GroupOfTours got = new GroupOfTours(getSomeRandomDummySolution());
		got.addTour(getTourWithCustomer1And2());
		return got;
	}
	
	public static GroupOfTours getGotWithCustomer3And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		GroupOfTours got = new GroupOfTours(getSomeRandomDummySolution());
		got.addTour(getTourWithCustomer3And4());
		return got;
	}

	public static GroupOfTours getSomeExampleGotFromRC103() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC103();			
		
		//create first tour for got
		Vehicle vehicle = problem.getNewVehicle();
		System.out.println(vehicle.getCapacity());
		Tour tour3 = new Tour(problem.getDepot(), vehicle);
		SolutionGot solution2 = new SolutionGot(problem);
		GroupOfTours got2 = new GroupOfTours(solution2);
		got2.addTour(tour3);
		tour3.addCustomer(problem.getCustomerWithCustomerNo(39));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(88));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(60));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(55));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(68));
		tour3.addCustomer(problem.getCustomerWithCustomerNo(70));
		solution2.addGot(got2);
				
		//create tour two for got
		Tour tour4 = new Tour(problem.getDepot(), problem.getNewVehicle());		
		got2.addTour(tour4);
		tour4.addCustomer(problem.getCustomerWithCustomerNo(69));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(53));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(78));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(73));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(17));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(47));
		tour4.addCustomer(problem.getCustomerWithCustomerNo(98));
		
		return got2;
	}

	public static SolutionGot getSomeSolutionFromRC104Problem() throws IOException {
		SolutionGot solutionRC104 = SetUpSolutionFromString.SetUpSolution("( ( 66 82 11 15 9 12 14 47 78 88 98 ) ) ( ( 60 6 7 79 8 46 4 45 3 1 100 ) ) ( ( 73 17 16 53 13 58 ) ) ( ( 99 55 ) ) ( ( 61 68 69 10 87 86 52 64 56 91 ) ) ( ( 70 2 5 40 39 41 71 96 ) ) ( ( 92 95 62 33 32 30 26 29 31 34 50 ) ) ( ( 23 75 97 59 74 ) ) ( ( 90 65 83 57 20 49 18 48 21 25 77 ) ) ( ( 81 54 42 44 43 38 36 35 37 72 93 94 ) ) ( ( 63 76 89 28 27 ) ) ( ( 80 67 84 85 51 19 22 24 ) )" 
				, ReadAndWriteUtils.readSolomonProblemRC104AsList().get(0));
		solutionRC104.printSolutionAsTupel();
		return solutionRC104;
	}
	
	public static SolutionGot getSomeRandomDummySolution() {
		return getSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer();		
	}
	
	public static SolutionGot getSomeSolutionForC101Problem() throws IOException {
		List<VrpProblem> problems = ReadAndWriteUtils.readSolomonProblemC101();
		RandomI1Solver i1 = new RandomI1Solver(); 
		return i1.solve(problems.get(0));
	}
	
}
