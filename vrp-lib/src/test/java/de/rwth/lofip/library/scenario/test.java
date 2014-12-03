//package de.rwth.lofip.library.scenario;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//
//import java.util.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//import org.junit.Test;
//
//import de.rwth.lofip.library.Customer;
//import de.rwth.lofip.library.Depot;
//import de.rwth.lofip.library.Solution;
//import de.rwth.lofip.library.Tour;
//import de.rwth.lofip.library.Vehicle;
//import de.rwth.lofip.library.VrpProblem;
//import de.rwth.lofip.library.solver.repair.RepairSolution;
//
//public class test {
//	
//	@Test
//	public void testList() throws IOException{
//
//		List<String> liste = new ArrayList<String>();
//		
//		liste.add("Test1");
//		liste.add("");
//		liste.add("CUSTOMER-EVENT");
//		liste.add("CUST NO.   DEMAND    Action TIME");
//		liste.add("");
//		liste.add("1          10       13");
//		liste.add("2          10       3");
//		liste.add("3          10       5");
//		liste.add("4          10       25");
//		liste.add("5          10       1");
//		liste.add("6           5       4");
//		
//		
//		DemandScenario dScenario = DemandScenarioUtils
//                .createScenarioFromStringList(liste);
//		
//			
//		// tests whether the events are sorted by time ascending
//		SortedSet<Event> events = dScenario.getEvents();
//		Event event1 = events.first();
//		Event event2 = events.first();
//		assertTrue(event1.getPointInTime() <= event2.getPointInTime());
//		
//		for(Event event : events)
//		{
//			event1 = event2;
//			event2 = event;
//			
//			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
//		}
//		
//	}
//	
//	@Test
//	public void justTest() throws IOException{
//		//set up
//				Customer c11 = new Customer();
//		        c11.setxCoordinate(15);
//		        c11.setyCoordinate(80);
//		        c11.setCustomerNo(11);
//		        c11.setDemand(10);
//		        c11.setTimeWindowOpen(278);
//		        c11.setTimeWindowClose(345);
//		        c11.setServiceTime(90);
//		        
//		        Customer c12 = new Customer();
//		        c12.setCustomerNo(12);
//		        c12.setxCoordinate(20);
//		        c12.setyCoordinate(85);
//		        c12.setDemand(40);
//		        c12.setTimeWindowOpen(475);
//		        c12.setTimeWindowClose(528);
//		        c12.setServiceTime(90);
//
//		        Customer c13 = new Customer();
//		        c13.setCustomerNo(13);
//		        c13.setxCoordinate(25);
//		        c13.setyCoordinate(85);
//		        c13.setDemand(20);
//		        c13.setTimeWindowOpen(625);
//		        c13.setTimeWindowClose(721);
//		        c13.setServiceTime(90);
//		        
//		        Depot depot = new Depot();
//		        depot.setxCoordinate(40);
//		        depot.setyCoordinate(50);
//		        
//		        Vehicle vehicle = new Vehicle(1, 75);
//		        Set<Vehicle> vehicles = new HashSet<Vehicle>();
//		        vehicles.add(vehicle);
//		        
//		        Tour tour = new Tour(depot, vehicle);
//		        tour.addCustomer(c11);
//		        tour.addCustomer(c12);
//		        tour.addCustomer(c13);
//		        
//		        //create vprProblem
//		        VrpProblem vrpProblem = new VrpProblem();
//		        vrpProblem.addCustomer(c11);
//		        vrpProblem.addCustomer(c12);
//		        vrpProblem.addCustomer(c13);
//		        vrpProblem.addDepot(depot);
//		        vrpProblem.setVehicles(vehicles);
//		        vrpProblem.setMaxTime(10000);
//		                
//		        //create solution
//		        Solution solution = new Solution(vrpProblem);
//		        solution.addTour(tour);
//		        
//		        
//		      //create scenario where there is a problem and no insertion in other tour is possible
//		        List<String> liste = new ArrayList<String>();
//				
//				liste.add("Test1");
//				liste.add("");
//				liste.add("CUSTOMER-EVENT");
//				liste.add("CUST NO.   DEMAND    Action TIME");
//				liste.add("");
//				liste.add("11          13       13");
//				liste.add("11          11       3");
//				liste.add("12          45       5");
//				liste.add("13          21       25");
//				liste.add("12          46       1");
//				liste.add("13          17       4");
//				
//				
//				DemandScenario dScenario = DemandScenarioUtils
//		                .createScenarioFromStringList(liste);
//    //End set up
//					        
//	       
//	SortedSet<Event> events = dScenario.getEvents();
//	System.out.println("Scenario 1:"+dScenario.getDescription()+" , Size: "+dScenario.getEvents().size());
//	System.out.println("Events:");
//	System.out.println("CustNO	Time	Demand");
//	System.out.println("----------------------");
//	for (Event event : events) 
//	{
//		System.out.println(event.getCustomerNo()+"	"+event.getPointInTime()+"	"+event.getDemand());
//	}
//	
//	
//	Event eventToDelead = events.first();
//    Solution newSolution = new RepairSolution().repair(solution, eventToDelead).get(0);
//    
//    
//    
//    events.remove(eventToDelead);
//    
//    SortedSet<Event> newEvents = new TreeSet<Event> (events.tailSet(events.first()));
//      
//    DemandScenario newDScenario = new DemandScenario();
//    newDScenario.setDescription(dScenario.getDescription());
//    newDScenario.setEvents(newEvents);
//    
//    System.out.println(" ");
//    System.out.println("Scenario 2:"+dScenario.getDescription()+" , Size: "+dScenario.getEvents().size());
//	System.out.println("Events:");
//	System.out.println("CustNO	Time	Demand");
//	System.out.println("----------------------");
//	for (Event event1 : newEvents) 
//	{
//		System.out.println(event1.getCustomerNo()+"	"+event1.getPointInTime()+"	"+event1.getDemand());
//	}
//    
//	// tests whether the events are sorted by time ascending
//			Event event1 = newEvents.first();
//			Event event2 = newEvents.first();
//			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
//			
//			for(Event eventE : newEvents)
//			{
//				event1 = event2;
//				event2 = eventE;
//				
//				assertTrue(event1.getPointInTime() <= event2.getPointInTime());
//			}
//			
//			System.out.println(" ");
//			System.out.println("new Solution: "+newSolution+" ! ");
//	}
//	
//	
//	
//	
// }
