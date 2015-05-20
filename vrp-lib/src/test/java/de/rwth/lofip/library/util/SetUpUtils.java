package de.rwth.lofip.library.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;

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
	    c1.setTimeWindowOpen(278);
	    c1.setTimeWindowClose(345);
	    c1.setServiceTime(90);
	    
	    c2 = new Customer();
	    c2.setCustomerNo(2);
	    c2.setxCoordinate(20);
	    c2.setyCoordinate(85);
	    c2.setDemand(20);
	    c2.setTimeWindowOpen(475);
	    c2.setTimeWindowClose(528);
	    c2.setServiceTime(90);
	
	    c3 = new Customer();
	    c3.setCustomerNo(3);
	    c3.setxCoordinate(25);
	    c3.setyCoordinate(85);
	    c3.setDemand(20);
	    c3.setTimeWindowOpen(425);
	    c3.setTimeWindowClose(721);
	    c3.setServiceTime(90);
	    
	    c4 = new Customer();
	    c4.setCustomerNo(4);
	    c4.setxCoordinate(35);
	    c4.setyCoordinate(70);
	    c4.setDemand(20);
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
        tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	private static Tour getTourWithFourCustomersC3BeforeC2() {
	setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	public static Tour getTourWithCustomers1And2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        
        return tour;
	}
	
	public static Tour getTourWithCustomer4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        Tour tour = new Tour(depot, vehicle);
        tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c4);        
        return tour;
	}
	
	public static SolutionGot getSolutionWithThreeToursAndTwoCustomersEach() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
                  
        Tour tour1 = new Tour(depot, vehicle);
        GroupOfTours got1 = new GroupOfTours();
        got1.addTour(tour1);
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);                
        
        Tour tour2 = new Tour(depot, vehicle);
        GroupOfTours got2 = new GroupOfTours();
        got2.addTour(tour2);
        tour2.addCustomer(c3);
        tour2.addCustomer(c4);        
        
        Tour tour3 = new Tour(depot, vehicle);
        GroupOfTours got3 = new GroupOfTours();
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
        tour1.setParentGot(new GroupOfTours());
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);
        GroupOfTours got1 = new GroupOfTours();
        got1.addTour(tour1);
        
        Tour tour2 = new Tour(depot, vehicle);
        tour2.setParentGot(new GroupOfTours());
        tour2.addCustomer(c3);
        tour2.addCustomer(c4);
        GroupOfTours got2 = new GroupOfTours();
        got2.addTour(tour2);             
	            
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);	    
	    return solution;      
	}
	
	public static SolutionGot SetUpSolutionWithTwoToursWithOneAndThreeCustomersRespectively() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour1 = getTourWithCustomers1And2And3();
        GroupOfTours got1 = new GroupOfTours();
        got1.addTour(tour1);
        
        Tour tour2 = getTourWithCustomer4();
        GroupOfTours got2 = new GroupOfTours();
        got2.addTour(tour2);             
                    
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);	    
	    return solution;      
	}
	
	public static SolutionGot SetUpSolutionWithTwoToursWithOneAndThreeCustomersOtherWayRound() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomer4());
		tours.add(getTourWithCustomers1And2And3());
		
		return createSolutionWithEachTourInOneGot(tours);     
	}
	
	public static SolutionGot getSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomer1());
		tours.add(getTourWithCustomer2());
		tours.add(getTourWithCustomer3And4());
		
		return createSolutionWithEachTourInOneGot(tours); 
	}
	
	public static SolutionGot getSolutionWithOneTourWithCustomersC1C2C3C4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithFourCustomers());
		
		return createSolutionWithEachTourInOneGot(tours); 
	}
	
	public static SolutionGot getSolutionWithOneTourWithCustomersC1C2C3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomers1And2And3());
		
		return createSolutionWithEachTourInOneGot(tours);
	}
	
	public static Object getSolutionWithOneTourWithCustomersC1C3C2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithCustomersC1C3C2());
		
		return createSolutionWithEachTourInOneGot(tours);
	}

	public static SolutionGot getSolutionWithOneTourWithCustomersC1C3C2C4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();
		tours.add(getTourWithFourCustomersC3BeforeC2());
		
		return createSolutionWithEachTourInOneGot(tours); 
	}


	private static SolutionGot createSolutionWithEachTourInOneGot(
			List<Tour> tours) {
		for (Tour tour : tours) {
			GroupOfTours got = new GroupOfTours();
			got.addTour(tour);
			solution.addGot(got);
		}      	   
	    return solution; 
	}

	public static SolutionGot SetUpSolutionWithOneTourWithCustomer2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
		List<Tour> tours = new LinkedList<Tour>();		
		tours.add(getTourWithCustomer2And3());		
		return createSolutionWithEachTourInOneGot(tours); 
	}
	
	public static Tour getTourWithCustomer2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        return tour;	
	}
	
	public static Tour getTourWithCustomer3And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c4);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        return tour;	
	}
	
	public static Tour getTourWithCustomer2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c2);        
        return tour;
	}

	public static Tour getTourWithCustomer3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c3);        
        return tour;	
	}
	
	public static Tour getTourWithCustomer1() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);        
        return tour;	
	}

	public static Tour getTourWithCustomer1And2And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        return tour;		
    }
	
	private static Tour getTourWithCustomer3And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle); 
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        return tour;
	}	
	
	public static Tour getEmptyTour() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
		return tour;
	}
	
	public static Tour getTourWithCustomers1And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle); 
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        return tour;
	}
	
	private static Tour getTourWithCustomersC1C3C2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
		tour.setParentGot(new GroupOfTours());
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;
	}

	public static GroupOfTours getGotWithCustomer1And2() {
		GroupOfTours got = new GroupOfTours();
		got.addTour(getTourWithCustomer1And2());
		return got;
	}
	
	public static GroupOfTours getGotWithCustomer3And4() {
		GroupOfTours got = new GroupOfTours();
		got.addTour(getTourWithCustomer3And4());
		return got;
	}

	
}
