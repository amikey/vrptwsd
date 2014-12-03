//package de.rwth.lofip.library.solver.repair;
//
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import de.rwth.lofip.library.Customer;
//import de.rwth.lofip.library.Depot;
//import de.rwth.lofip.library.Solution;
//import de.rwth.lofip.library.Tour;
//import de.rwth.lofip.library.Vehicle;
//import de.rwth.lofip.library.VrpProblem;
//import de.rwth.lofip.library.scenario.Event;
//import de.rwth.lofip.library.util.CustomerInTour;
//
///**
// * Test class which contains tests for the class RepairSolution
// *
// * @author Andreas Braun
// */	
//public class RepairSolution_TEST {
//	
//	Customer c11 = new Customer();
//	Customer c12 = new Customer();
//	Customer c13 = new Customer();
//    List<Solution> solutionList = new LinkedList<Solution>();
//    Solution solution;
//	
//	@Before
//	public void setUp()
//	{
//		//set up
//        c11.setxCoordinate(15);
//        c11.setyCoordinate(80);
//        c11.setCustomerNo(11);
//        c11.setDemand(10);
//        c11.setTimeWindowOpen(278);
//        c11.setTimeWindowClose(345);
//        c11.setServiceTime(90);
//        
//        c12.setCustomerNo(12);
//        c12.setxCoordinate(20);
//        c12.setyCoordinate(85);
//        c12.setDemand(40);
//        c12.setTimeWindowOpen(475);
//        c12.setTimeWindowClose(528);
//        c12.setServiceTime(90);
//
//        c13.setCustomerNo(13);
//        c13.setxCoordinate(25);
//        c13.setyCoordinate(85);
//        c13.setDemand(20);
//        c13.setTimeWindowOpen(625);
//        c13.setTimeWindowClose(721);
//        c13.setServiceTime(90);
//        
//        Depot depot = new Depot();
//        depot.setxCoordinate(40);
//        depot.setyCoordinate(50);
//        
//        Vehicle vehicle = new Vehicle(1, 75);
//        Set<Vehicle> vehicles = new HashSet<Vehicle>();
//        vehicles.add(vehicle);
//        
//        Tour tour = new Tour(depot, vehicle);
//        tour.addCustomer(c11);
//        tour.addCustomer(c12);
//        tour.addCustomer(c13);
//        
//        //create vprProblem
//        VrpProblem vrpProblem = new VrpProblem();
//        vrpProblem.addCustomer(c11);
//        vrpProblem.addCustomer(c12);
//        vrpProblem.addCustomer(c13);
//        vrpProblem.addDepot(depot);
//        vrpProblem.setVehicles(vehicles);
//        vrpProblem.setMaxTime(10000);
//                
//        //create solution
//        solution = new Solution(vrpProblem);
//        solution.addTour(tour);
//        //End set up
//	}
//
//	@Test
//	public void testEventWithNoProblem()
//	{
//        //create scenario where there is no problem
//        //time is ignored so far
//        int newDemand = 15;
//        Event event = new Event(13, newDemand, 517);
//        
//        System.out.println("1.er Test startet.");
//        
//        solutionList = new RepairSolution().repair(solution, event);
//        
////        System.out.println("Number of Tours is " + solutionList.get(0).getTours().size() + "; should be 1.");
////        assertEquals( "Number of Tours is not 2 but should be 1. ", 1, solutionList.get(0).getTours().size());
//        
//        System.out.println("TEST BESTANDEN.");
////        System.out.println("calculated solution: " + solutionList.get(0).getSolutionAsString());       
//        
//        //solutionList.addAll(modifiedSolutionSet);
//        
//        System.out.println("beste Lösungen (für 1 bis n gelöste Kunden):");
//        for (int i = 0; i < solutionList.size(); i++ ) 
//        	System.out.println(solutionList.get(i).getSolutionAsString()); 
//	}
//	
//	
//	@Test public void testEventWhereNewTourHasToBeCreated()
//	{
//        //create scenario where there is a problem
//        //time is ignored so far
//        int newDemand = 30;
//        Event event = new Event(13, newDemand, 517);
//
//        System.out.println("2.er Test startet.");
//        
//        solutionList = new RepairSolution().repair(solution, event);
//        
//        //solutionList.addAll(modifiedSolutionSet);
////        System.out.println("Number of Tours is " + solutionList.get(0).getTours().size() + "; should be 2.");
////        assertEquals( "Number of Tours is not 2 but should be 2. ", 2, solutionList.get(0).getTours().size());
//        System.out.println("TEST BESTANDEN.");
////        System.out.println("calculated solution: " + solutionList.get(0).getSolutionAsString());
//        
////        System.out.println("Inhalt des ersten Elements von solutionList: " + solutionList.get(0)); 
////        System.out.println("Inhalt der NewSolution des ersten Elements von solutionList: " + solutionList.get(0).getNewSolution());
////        
//        System.out.println("beste Lösungen (für 1 bis n gelöste Kunden):");
//        for (int i = 0; i < solutionList.size(); i++ ) 
//        	System.out.println(solutionList.get(i).getSolutionAsString());
//        	//System.out.println("calculated solution for " + (i+1) + " removed customers: " + solutionList.get(i).getNewSolution().getSolutionAsString());
//	}
//	
//	
//	@Test public void testEventWhereMoveBetweenToursIsPossible()
//	{        
//        //create scenario where there is a problem
//        //time is ignored so far
//        int newDemand = 30;
//        Event event = new Event(13, newDemand, 517);
//       
//        //create scenario where there is a problem and insertion in another tour is possible
//        Customer c21 = new Customer();
//        c21.setxCoordinate(40);
//        c21.setyCoordinate(90);
//        c21.setCustomerNo(21);
//        c21.setDemand(20);
//        c21.setTimeWindowOpen(701);
//        c21.setTimeWindowClose(886);
//        c21.setServiceTime(90);
//        
//        Customer c22 = new Customer();
//        c22.setxCoordinate(50);
//        c22.setyCoordinate(80);
//        c22.setCustomerNo(22);
//        c22.setDemand(20);
//        c22.setTimeWindowOpen(799);
//        c22.setTimeWindowClose(945);
//        c22.setServiceTime(90);
//        
//        Vehicle vehicle2 = new Vehicle(1, 75);
//        solution.getVrpProblem().getVehicles().add(vehicle2);
//        
//        Tour tour2 = new Tour(solution.getVrpProblem().getDepot(), vehicle2);
//        tour2.addCustomer(c21);
//        tour2.addCustomer(c22);
//        
//        //update vprProblem
//        solution.getVrpProblem().addCustomer(c21);
//        solution.getVrpProblem().addCustomer(c22);
//                
//        //update solution
//        solution.addTour(tour2);
//        
//        //System.out.println("ID Tour 1: " + solution.getTour.getId());
//        //System.out.println("ID Tour 2: " + tour2.getId());
//        
//        System.out.println("3.er Test startet.");
//           
//        System.out.println("Lösung vor Änderung: " + solution.getSolutionAsString());
//                
//        solutionList = new RepairSolution().repair(solution, event);
//        //solutionList.addAll(modifiedSolutionSet3);
//        
////        System.out.println("Number of Tours is " + solution3.getTours().size() + "; should be 2.");
////        //assertEquals( "Number of Tours is not 2 but should be 2. ", 2, solution3.getTours().size());
//        System.out.println("TEST BESTANDEN.");
////        System.out.println("");       
////        System.out.println("Solution nach Änderung: " +solution3.getSolutionAsString());
//        
//        System.out.println("beste Lösungen (für 1 bis n gelöste Kunden):");
//        for (int i = 0; i < solutionList.size(); i++ ) 
//        	System.out.println(solutionList.get(i).getSolutionAsString()); 
//
//	}
//	
//	//this test tests whether a sufficient amount of customers is removed from tour with problem
//	//such that the capacity is not exceeded any more.
//	@Test 
//	public void testRemoveSufficientCustomersFromTour()
//	{		
//        int newDemand = 40;
//        Event event = new Event(13, newDemand, 517);
//
//        System.out.println("Test, ob genügend Kunden aus Tour entfernt werden, startet.");
//        
//        solutionList = new RepairSolution().repair(solution, event);
//        
//        System.out.println("TEST BESTANDEN (Zumindest läuft das Programm durch; ob wirklich die Kapazität überall eingehalten wird, muss noch gecheckt werden.");
//  
//        System.out.println("beste Lösungen (für 1 bis n gelöste Kunden):");
//        for (int i = 0; i < solutionList.size(); i++ ) 
//        	System.out.println(solutionList.get(i).getSolutionAsString());
//   	}
//	
//	@Test 
//	public void testFixCustomerInSolution(){
//		
//		Event event = new Event(11, 30, 100);
//		System.out.println("Event Ankunft: "+ event.getPointInTime());
//		
//		new RepairSolution().fixCustomerInSolution(event, solution);
//		
//		//List<CustomerInTour> customersInTour = solution.getUnfixedCustomersInTours();
//		for (CustomerInTour customerInTour : solution.getCustomersInTours()){
//		
//		System.out.println("Customer "+customerInTour.getCustomer().getCustomerNo()+", arival at "+customerInTour.getArrivalTime()
//				+" , fixed: "+customerInTour.isCustomerInTourFixed());
//		}
//		
//		solutionList = new RepairSolution().repair(solution, event);
//			} 
//	
//	
//	
//    @Test
//	public void TestDoGetNodesFixed()
//	// werden Knoten fixiert?
//	{
//		
//	Customer c3 = new Customer();
//	Customer c4 = new Customer();
//	Customer c5 = new Customer();
//	
//	Customer c6 = new Customer();
//	Customer c7 = new Customer();
//	
//    Solution solution;
//		//set up
//        c3.setxCoordinate(30);
//        c3.setyCoordinate(40);
//        c3.setCustomerNo(3);
//        c3.setDemand(10);
//        c3.setTimeWindowOpen(15);
//        c3.setTimeWindowClose(45);
//        c3.setServiceTime(10);
//        
//        c4.setCustomerNo(4);
//        c4.setxCoordinate(38);
//        c4.setyCoordinate(45);
//        c4.setDemand(20);
//        c4.setTimeWindowOpen(100);
//        c4.setTimeWindowClose(140);
//        c4.setServiceTime(15);
//
//        c5.setCustomerNo(5);
//        c5.setxCoordinate(42);
//        c5.setyCoordinate(40);
//        c5.setDemand(30);
//        c5.setTimeWindowOpen(325);
//        c5.setTimeWindowClose(500);
//        c5.setServiceTime(90);
//        
//        c6.setxCoordinate(50);
//        c6.setyCoordinate(40);
//        c6.setCustomerNo(6);
//        c6.setDemand(15);
//        c6.setTimeWindowOpen(90);
//        c6.setTimeWindowClose(125);
//        c6.setServiceTime(30);
//        
//        c7.setCustomerNo(7);
//        c7.setxCoordinate(50);
//        c7.setyCoordinate(30);
//        c7.setDemand(30);
//        c7.setTimeWindowOpen(10);
//        c7.setTimeWindowClose(40);
//        c7.setServiceTime(15);
//        
//        Depot depot = new Depot();
//        depot.setxCoordinate(40);
//        depot.setyCoordinate(10);
//        
//        Vehicle vehicle1 = new Vehicle(1, 75);
//        Vehicle vehicle2 = new Vehicle(2, 75);
//        Set<Vehicle> vehicles = new HashSet<Vehicle>();
//        vehicles.add(vehicle1);
//        vehicles.add(vehicle2);
//        
//        Tour tour1 = new Tour(depot, vehicle1);
//        tour1.addCustomer(c3);
//        tour1.addCustomer(c4);
//        tour1.addCustomer(c5);
//        
//        Tour tour2 = new Tour(depot, vehicle1);
//        tour2.addCustomer(c7);
//        tour2.addCustomer(c6);
//        
//        //create vprProblem
//        VrpProblem vrpProblem = new VrpProblem();
//        vrpProblem.addCustomer(c3);
//        vrpProblem.addCustomer(c4);
//        vrpProblem.addCustomer(c5);
//        vrpProblem.addCustomer(c6);
//        vrpProblem.addCustomer(c7);
//        
//        vrpProblem.addDepot(depot);
//        vrpProblem.setVehicles(vehicles);
//        vrpProblem.setMaxTime(10000);
//                
//        //create solution
//        solution = new Solution(vrpProblem);
//        solution.addTour(tour1);
//        solution.addTour(tour2);
//        
//        //event
//        Event event = new Event(4, 38, 100);
//        //End set up
//        
//        System.out.println("Event Ankunft: "+ event.getPointInTime());
//		
//		new RepairSolution().fixCustomerInSolution(event, solution);
//		
//		for (CustomerInTour customerInTour : solution.getCustomersInTours()){
//		
//		System.out.println("Customer "+customerInTour.getCustomer().getCustomerNo()+", arival at "+customerInTour.getArrivalTime()
//				+" , fixed: "+customerInTour.isCustomerInTourFixed());
//		}
//		
//		solutionList = new RepairSolution().repair(solution, event);	}
//	
//	
//	
//	
// }
