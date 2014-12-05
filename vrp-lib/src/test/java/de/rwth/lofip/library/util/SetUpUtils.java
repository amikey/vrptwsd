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
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.initialSolver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

public class SetUpUtils {
	
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
	
	
	public static Solution SetUpR201Solution() throws Exception {
				
		VrpProblem vrpProblem = SetUpR201Problem();
		
		//--------------Solution--------------------------------------
		long startTime = System.nanoTime();
		Solution solution = createSolution(vrpProblem);
		long endTime = System.nanoTime();
		long timeTaken = (endTime - startTime);
		solution.setTimeNeeded(timeTaken);	

		return solution;
	}
	
	public static List<Solution> SetUpAllSolutions() throws Exception {
		List<Solution> solutions = new LinkedList<Solution>();
		List<VrpProblem> vrps = createVrpProblems();
		for (VrpProblem vrpp : vrps) {
			Solution solution = createSolution(vrpp);
			solutions.add(solution);	
		}			
		return solutions;
	}
	
	public static Tour SetUpFeasibleTour() {

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
        
        DeterministicTour tour = new DeterministicTour(depot, vehicle);
        tour.addCustomer(c11);
        tour.addCustomer(c12);
        tour.addCustomer(c13);
        tour.addCustomer(c14);
        
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
        DeterministicTour tour = new DeterministicTour(depot, vehicle, costFactor);
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
	    
	    DeterministicTour tour = new DeterministicTour(depot, vehicle);
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
        
        DeterministicTour tour = new DeterministicTour(depot, vehicle);
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
        DeterministicTour tour = new DeterministicTour(depot, vehicle, costFactor);
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
	

	//----------Utilities-----------
	
	private static Solution createSolution(VrpProblem problem) throws Exception
	{
		DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
		Solution solution = initialSolver.solve(problem);
		Solution improvedSolution = new	DeterministicLeiEtAlHeuristic().improve(solution,
				new VrpConfiguration());			
		return improvedSolution;               
	}
	
	
	private static List<VrpProblem> createVrpProblems() {					
		List<VrpProblem> vrpProblems = new LinkedList<VrpProblem>();
    	Iterator<File> problemFiles = FileUtils.iterateFiles(new File("solomon-problems"),
                new String[] { "txt" }, false);
    	while (problemFiles.hasNext()) 
        {
            File file = problemFiles.next();
            if ( !file.getName().contains("scenario") && !file.getName().contains("n"))
            {	           
	            FileInputStream openInputStream = null;
	            try {
	            	openInputStream = FileUtils.openInputStream(file);
	                List<String> lines = IOUtils.readLines(openInputStream);
	                VrpProblem vrpProblem = VrpUtils
	                        .createProblemFromStringList(lines);
	                vrpProblems.add(vrpProblem);
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	         }
        }
		return vrpProblems;
	}

}
