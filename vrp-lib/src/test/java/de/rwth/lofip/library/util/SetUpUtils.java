package de.rwth.lofip.library.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.VrpConfiguration;
import de.rwth.lofip.stuffNotNeededRightNow.solver.initialSolver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.DeterministicLeiEtAlHeuristic;

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
	
	public static VrpProblem SetUpR201Problem() throws IOException {
		//setUp();
		//---------VRP------------------------------------------------
		File datei1 = new File("solomon-problems/rc201t.txt");
		BufferedReader br = new BufferedReader(new FileReader(datei1));
		String zeile = "";
		List<String> liste = new ArrayList<String>(0);
		while ((zeile = br.readLine()) != null) {
			liste.add(zeile);
		}
		
		VrpProblem vrpProblem = VrpUtils
                .createProblemFromStringList(liste);
		br.close();
		
		return vrpProblem;
	}
	
	public static Tour getTourWithFourCustomers() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	private static Tour getTourWithFourCustomersC3BeforeC2() {
	setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        
        return tour;
	}
	
	public static Tour getTourWithCustomers1And2And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        
        Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        
        return tour;
	}
	
	static Tour getTourWithCustomer4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
        Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c4);        
        return tour;
	}
	
	public static SolutionGot getSolutionWithThreeToursAndTwoCustomersEach() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
                  
        Tour tour1 = new Tour(depot, vehicle);
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);
        GroupOfTours got1 = new GroupOfTours();
        got1.addTour(tour1);
        
        Tour tour2 = new Tour(depot, vehicle);
        tour2.addCustomer(c3);
        tour2.addCustomer(c4);
        GroupOfTours got2 = new GroupOfTours();
        got2.addTour(tour2);
        
        Tour tour3 = new Tour(depot, vehicle);
        tour3.addCustomer(c1);
        tour3.addCustomer(c2);
        GroupOfTours got3 = new GroupOfTours();
        got3.addTour(tour3);
        	            
	    //create solution
	    solution.addGot(got1);
	    solution.addGot(got2);
	    solution.addGot(got3);
	    
	    return solution;      
	}
	
	public static SolutionGot getSolutionWithTwoToursAndTwoCustomersEach() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
                  
        Tour tour1 = new Tour(depot, vehicle);
        tour1.addCustomer(c1);
        tour1.addCustomer(c2);
        GroupOfTours got1 = new GroupOfTours();
        got1.addTour(tour1);
        
        Tour tour2 = new Tour(depot, vehicle);
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
        tour.addCustomer(c2);
        tour.addCustomer(c3);
        return tour;	
	}
	
	public static Tour getTourWithCustomer3And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c4);
        return tour;	
	}
	
	public static Tour getTourWithCustomer1And2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        return tour;	
	}
	
	static Tour getTourWithCustomer2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c2);        
        return tour;
	}

	public static Tour getTourWithCustomer3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c3);        
        return tour;	
	}
	
	public static Tour getTourWithCustomer1() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);        
        return tour;	
	}

	public static Tour getTourWithCustomer1And2And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);
        tour.addCustomer(c1);
        tour.addCustomer(c2);
        tour.addCustomer(c4);
        return tour;		
    }
	
	private static Tour getTourWithCustomer3And4() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);       
        tour.addCustomer(c3);
        tour.addCustomer(c4);
        return tour;
	}	
	
	public static Tour getEmptyTour() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();
		Tour tour = new Tour(depot, vehicle);
		return tour;
	}
	
	public static Tour getTourWithCustomers1And3() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);       
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        return tour;
	}
	
	private static Tour getTourWithCustomersC1C3C2() {
		setCustomersAndDepotAndVehiclesAndVrpProblemAndSolution();		
		Tour tour = new Tour(depot, vehicle);       
        tour.addCustomer(c1);
        tour.addCustomer(c3);
        tour.addCustomer(c2);
        return tour;
	}

	
	public static Tour SetUpTourThatIsExecutedTheNextDay() {
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
        
        Customer c14 = new Customer();
        c14.setCustomerNo(14);
        c14.setxCoordinate(35);
        c14.setyCoordinate(70);
        c14.setDemand(20);
        c14.setTimeWindowOpen(873);
        c14.setTimeWindowClose(921);
        c14.setServiceTime(90);
        
        Depot depot = new Depot();
        depot.setxCoordinate(40);
        depot.setyCoordinate(50);
        
        Vehicle vehicle = new Vehicle(1, 85);
        Set<Vehicle> vehicles = new HashSet<Vehicle>();
        vehicles.add(vehicle);
        
        double costFactor = 2;
        Tour tour = new Tour(depot, vehicle, costFactor);
        tour.addCustomer(c11);
        tour.addCustomer(c12);
        tour.addCustomer(c13);
        tour.addCustomer(c14);
        
        return tour;
	}
	
	public static Solution SetUpFeasibleSolution() {
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
	    
	    Customer c14 = new Customer();
	    c14.setCustomerNo(14);
	    c14.setxCoordinate(35);
	    c14.setyCoordinate(70);
	    c14.setDemand(20);
	    c14.setTimeWindowOpen(873);
	    c14.setTimeWindowClose(921);
	    c14.setServiceTime(90);
	    
	    Depot depot = new Depot();
	    depot.setxCoordinate(40);
	    depot.setyCoordinate(50);
	    
	    Vehicle vehicle = new Vehicle(1, 85);
	    Set<Vehicle> vehicles = new HashSet<Vehicle>();
	    vehicles.add(vehicle);
	    
	    Tour tour = new Tour(depot, vehicle);
	    tour.addCustomer(c11);
	    tour.addCustomer(c12);
	    tour.addCustomer(c13);
	    tour.addCustomer(c14);
	    
	    //create vprProblem
	    VrpProblem vrpProblem = new VrpProblem();
	    vrpProblem.addCustomer(c11);
	    vrpProblem.addCustomer(c12);
	    vrpProblem.addCustomer(c13);
	    vrpProblem.addCustomer(c14);
	    vrpProblem.addDepot(depot);
	    vrpProblem.setVehicles(vehicles);
	    vrpProblem.setMaxTime(10000);
	            
	    //create solution
	    Solution solution = new Solution(vrpProblem);
	    solution.addTour(tour);
	    
	    return solution;
	}	    
	
	public static Solution SetUpInfeasibleSolution() {    	
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
        
        //create vprProblem
        VrpProblem vrpProblem = new VrpProblem();
        vrpProblem.addCustomer(c11);
        vrpProblem.addCustomer(c12);
        vrpProblem.addCustomer(c13);
        vrpProblem.addDepot(depot);
        vrpProblem.setVehicles(vehicles);
        vrpProblem.setMaxTime(10000);
                
        //create solution
        Solution solution = new Solution(vrpProblem);
        solution.addTour(tour);
        
        return solution;
	}
	
	public static Solution SetUpFeasibleSolutionWhereSingleTourIsExecutedTheSameDay() {
		return SetUpFeasibleSolution();
	}
	
	public static Solution SetUpFeasibleSolutionWhereSingleTourIsExecutedTheNextDay() {    	
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
        
        Vehicle vehicle = new Vehicle(1, 85);
        Set<Vehicle> vehicles = new HashSet<Vehicle>();
        vehicles.add(vehicle);
        
        double costFactor = 2;
        Tour tour = new Tour(depot, vehicle, costFactor);
        tour.addCustomer(c11);
        tour.addCustomer(c12);
        tour.addCustomer(c13);
        
        //create vprProblem
        VrpProblem vrpProblem = new VrpProblem();
        vrpProblem.addCustomer(c11);
        vrpProblem.addCustomer(c12);
        vrpProblem.addCustomer(c13);
        vrpProblem.addDepot(depot);
        vrpProblem.setVehicles(vehicles);
        vrpProblem.setMaxTime(10000);
                
        //create solution
        Solution solution = new Solution(vrpProblem);
        solution.addTour(tour);
        
        return solution;
	}



	
	
	
	
//	public static Solution SetUpR201Solution() throws Exception {
//		
//		VrpProblem vrpProblem = SetUpR201Problem();
//		
//		//--------------Solution--------------------------------------
//		long startTime = System.nanoTime();
//		Solution solution = createSolution(vrpProblem);
//		long endTime = System.nanoTime();
//		long timeTaken = (endTime - startTime);
//		solution.setTimeNeeded(timeTaken);	
//
//		return solution;
//	}
//	
//	public static List<Solution> SetUpAllSolutions() throws Exception {
//		List<Solution> solutions = new LinkedList<Solution>();
//		List<VrpProblem> vrps = createVrpProblems();
//		for (VrpProblem vrpp : vrps) {
//			Solution solution = createSolution(vrpp);
//			solutions.add(solution);	
//		}			
//		return solutions;
//	}
//	
//	
//	private static Solution createSolution(VrpProblem problem) throws Exception
//	{
//		DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
//		Solution solution = initialSolver.solve(problem);
//		Solution improvedSolution = new	DeterministicLeiEtAlHeuristic().improve(solution,
//				new VrpConfiguration());			
//		return improvedSolution;               
//	}
//	
//	
//	private static List<VrpProblem> createVrpProblems() {					
//		List<VrpProblem> vrpProblems = new LinkedList<VrpProblem>();
//    	Iterator<File> problemFiles = FileUtils.iterateFiles(new File("solomon-problems"),
//                new String[] { "txt" }, false);
//    	while (problemFiles.hasNext()) 
//        {
//            File file = problemFiles.next();
//            if ( !file.getName().contains("scenario") && !file.getName().contains("n"))
//            {	           
//	            FileInputStream openInputStream = null;
//	            try {
//	            	openInputStream = FileUtils.openInputStream(file);
//	                List<String> lines = IOUtils.readLines(openInputStream);
//	                VrpProblem vrpProblem = VrpUtils
//	                        .createProblemFromStringList(lines);
//	                vrpProblems.add(vrpProblem);
//	            }
//	            catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	         }
//        }
//		return vrpProblems;
//	}














}
