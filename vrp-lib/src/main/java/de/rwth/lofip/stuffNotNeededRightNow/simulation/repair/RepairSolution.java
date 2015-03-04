/**
 * 
 */
package de.rwth.lofip.stuffNotNeededRightNow.simulation.repair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.util.CustomerInTour;
//import static org.junit.Assert.assertSame;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.SimilarityRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.util.RemovedCustomer;


/**
 * This class contains all methods to repair a solution that
 * (potentially) is no longer feasible due to changed customer demands
 * 
 * @author Andreas Braun 
 * 
 */
public class RepairSolution {
	 
	private Tour tourWithProblem;
	private Solution oldSolutionBeforeUpdateBecauseOfEvent;
	private Solution oldSolutionAfterUpdateBecauseOfEvent;
	
	/**
	 * Checks whether a solution is no longer feasible after a demand change event
	 * (where demand has increased) has occurred and repairs solution if necessary 
	 * (moves customers between tours if possible; otherwise adds a new tour)
	 * 
	 * @param solution
	 * @param event (customerNo, demand, pointInTime) 
	 *
	 * @return repairedSolution
	 * @throws IOException 
	 * @throws Exception 
	 */
	public List<RepairedSolution> repair(Solution solution, Event event) {
		
		long startTime = System.nanoTime();
		oldSolutionBeforeUpdateBecauseOfEvent = solution.cloneCompletelySeperateCopy();			
		
		//update vrpProblem; necessary so that all customers have the correct demand
		//and the algorithm uses new demands
		updateVrpProblemAttachedToSolution(solution, event);
		oldSolutionAfterUpdateBecauseOfEvent = solution;
		
		List<RepairedSolution> solutionList = new LinkedList<RepairedSolution>();
		
		//detect whether there exists a problem
		if (detectProblem(solution, event) )
		{
			System.err.println("\n Problem");			
			
			//-OB - fix points that have already been passed because event occurs after visiting time on tours
			fixCustomerInSolution(event, solution);
			//solve problem
			solutionList = solveProblemAccordingToDistance(solution, event); 
			System.err.println("solution feasible nach reparatur? " + solutionList.get(0).getNewSolution().isSolutionFeasibleWrtDemand());
												
			solutionList = checkForDominatedSolutions(solutionList);
			
			RepairedSolution solutionNewVehicle = createSolutionWithNewVehicle(solution, event);
			solutionList.add(solutionNewVehicle);
			
			solutionList = checkForEqualSolutions(solutionList);
			//TODO: sort solutions wrt some criteria
		}
		else
		{
			//if there is no problem return the unmodified solution		
			long endTime = System.nanoTime();
	    	long timeTaken = (endTime - startTime);
			RepairedSolution repairedSolution = new RepairedSolution(solution, event, timeTaken);
			solutionList.add(repairedSolution);
		}
		
		return solutionList;
	}
	
	
	private RepairedSolution createSolutionWithNewVehicle(Solution solution, Event event) {
		//es wird immer der letzte Kunde der Tour entfernt		
		long startTime = System.nanoTime();
		Solution temporarySolution = solution.clone();
		Tour tourWithProblemInTemporarySolution = identifyTourWithProblemInTemporarySolution(temporarySolution);
		Customer c = tourWithProblemInTemporarySolution.removeCustomerAtPosition(tourWithProblemInTemporarySolution.getCustomerSize()-1);
		//create new tour for customer with problem
		double costFactorForNewTour = 2;
		Vehicle newVehicle = new Vehicle(new Random().nextInt(), temporarySolution.getTours().get(0).getVehicle().getCapacity());
		DeterministicTour tour = new DeterministicTour(temporarySolution.getVrpProblem().getDepot(), newVehicle, costFactorForNewTour);
		tour.addCustomer(c);
		temporarySolution.addTour(tour);
		
		long endTime = System.nanoTime();
    	long timeTaken = (endTime - startTime);
		RepairedSolution repairedSolution = new RepairedSolution(solution, temporarySolution, event, timeTaken);
		return repairedSolution;
	}
	
	
	
	/**
	 * repairs Ist-Situation
	 */
	//TODO: OB: wenn die Klasse NewCreatedTourInRepair() rausgenommen wird muss diese Methode den mit dem costFactor gesetzt werden
	public List<RepairedSolution> repairIstSituation(Solution solution, Event event) {
		
		long startTime = System.nanoTime();
    
		//update vrpProblem; necessary so that all customers have the correct demand
		//and the algorithm uses new demands
		updateVrpProblemAttachedToSolution(solution, event);
		
		//detect whether there exists a problem
		if (detectProblem(solution, event) )
		{
			Solution oldSolution = solution.clone();
			//find position of customer in solution
			CustomerInTour custInTour = null;
			for (CustomerInTour cit : tourWithProblem.getCustomersInTour())
			{
				if (cit.getCustomer().getCustomerNo() == event.getCustomerNo())
				{
					custInTour = cit;
					break;
				}	        					
			}
			//remove customer from tour
			tourWithProblem.removeCustomerAtPosition(custInTour.getPosition());
			//create new tour for customer with problem
			double costFactorForNewTour = 2;
			DeterministicTour tour = new DeterministicTour(solution.getVrpProblem().getDepot(), solution.getTours().get(0).getVehicle(), costFactorForNewTour);
			tour.addCustomer(custInTour.getCustomer());
			solution.addTour(tour);
			
			long endTime = System.nanoTime();
	    	long timeTaken = (endTime - startTime);
			
			List<RepairedSolution> solutionList = new LinkedList<RepairedSolution>();
			RepairedSolution repairedSolution = new RepairedSolution(oldSolution, solution, event, timeTaken);
			solutionList.add(repairedSolution);
			return solutionList;			
			
		}
		//if there is no problem return the unmodified solution
		List<RepairedSolution> solutionList = new LinkedList<RepairedSolution>();
		long endTime = System.nanoTime();
    	long timeTaken = (endTime - startTime);
		RepairedSolution repairedSolution = new RepairedSolution(solution, event, timeTaken);
		solutionList.add(repairedSolution);
		return solutionList;
	}
	
	
	
	/**
	 * This method updates the vrp problem that is attached to the solution
	 * according to the information in event.
	 * 
	 * @param solution
	 * @param event 
	 * @return solutionWithUpdatedVrpProblem
	 *
	 * @author Andreas Braun
	 */
	//it might be necessary to check which types of customers 
	//(CustomerInTour - yes, because information that belongs to tour does not change)
	//(RemovedCustomer - yes)
	//(CustomerWithCost - yes)
	//the algorithms use and whether they are
	//updated when a "normal" customer is updated
	public Solution updateVrpProblemAttachedToSolution(Solution solution, Event event) 
	{
		System.out.println("updating customers.");
		VrpProblem problem = solution.getVrpProblem();
		Set<Customer> oldCustomers = problem.getCustomers();
		Set<Customer> newCustomers = new HashSet<Customer>();
				
		for (Customer c: oldCustomers) 
		{
			if (c.getCustomerNo()==event.getCustomerNo() )
			{
				System.out.println("Kunde gefunden. Alter Bedarf ist " + c.getDemand() + "; neuer Bedarf ist " + event.getDemand());
				//update customer demand
				c.setDemand(event.getDemand());
			}
			newCustomers.add(c);
		}
		problem.setCustomers(newCustomers);
		solution.setVrpProblem(problem);
		
		//AB: update solution
		for (Tour t : solution.getTours())
		{
			for (Customer c : t.getCustomers())
			{
				if (c.getCustomerNo()==event.getCustomerNo() )
				{					
					c.setDemand(event.getDemand());
				}
			}
		}
		
		System.out.println("So sieht die ursprüngliche Lösung aus:");
		System.out.println(solution.getSolutionAsStringWithCustomer());	
		
		return solution;
	}
	
	//-OB -fix customers that have already been passed because event occures after visiting time on tour  
	/** 
	 * Verifies and sets the customer in tour attribute {@code isFixedCustomer} 
	 * to fixed customer, which according to the time when the event occures should 
	 * not be changed within the tours of the solution.
	 * 
	 * @param event
	 * @param solution
	 */
	public void fixCustomerInSolution(Event event, Solution solution)
	{
		
		List<Tour> tours = solution.getTours();
		
		for (Tour tour : tours)
		{
			List<CustomerInTour> customersInTour = tour.getCustomersInTour();
			
			// change the attribute for the customer in the Tours of the Solution 			
			for (CustomerInTour customerInTour : customersInTour)  
			{
				if (isCustomerNeedToBeFixed(customerInTour, event))
					customerInTour.setFixCustomerInTour(true);		
			}	
		}	
	}
	
	
	//-OB -verifies if the customers in tours need to be fixed 
	private static boolean isCustomerNeedToBeFixed(CustomerInTour customerInTour, Event event){
		
		if (customerInTour.getArrivalTime() <= event.getPointInTime()){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether an event causes a problem, i.e. 
	 * the demand on the tour that the customer belongs to exceeds the vehicle capacity
	 * 
	 * @param solution
	 * @param event (customerNo, demand, pointInTime) 
	 * @param tourWithCustomer3And2 (used to return the tour with problem)
	 * @return boolean (true if there exists a problem; false otherwise)
	 * 
	 * @author Andreas Braun
	 */
	public boolean detectProblem(Solution solution, Event event) 
	{
		System.out.println("Überprüfe, ob es ein Problem gibt.");	
		
//		System.out.println("So sieht die Lösung aus:");
//		System.out.println(solution.getSolutionAsStringWithCustomer());
		
		//first determine which tour the changed customer belongs to
		List<CustomerInTour> oldCustomers = solution.getCustomersInTours();
			
		for (CustomerInTour c: oldCustomers) 
		{
			if (c.getCustomer().getCustomerNo()==event.getCustomerNo() )
			{
				//determine tour that customer belongs to
				this.tourWithProblem = c.getTour();
				break;
			}
		}
		
		//now determine whether there is a problem in this tour.
    	long totalDemand = 0;
        for (Customer c : tourWithProblem.getCustomers()) 
        {
            totalDemand += c.getDemand();            
        }
        
        double tourCapacity = new Double(tourWithProblem.getVehicle().getCapacity());       

        if (tourCapacity < totalDemand) 
        {
        	// problem in tour; return true
        	System.out.println("PROBLEM AUF TOUR \n");
        	return true;
        }
        // no problem; return false
        System.out.println("KEIN PROBLEM AUF TOUR \n");
        return false;
	}
	
	
	/**
	 * Repairs a solution such that the additional traveled distance 
	 * of all vehicles is as small as possible
	 * 
	 * @param solution
	 * @param event (only needed so it can be added to repairedSolution)
	 * @return repairedSolution
	 * 
	 * @author Andreas Braun
	 * @throws Exception 
	 */
	private List<RepairedSolution> solveProblemAccordingToDistance(Solution solution, Event event)
	{
		
		List<RepairedSolution> bestSolutionList = new LinkedList<RepairedSolution>();
		long startTime = System.nanoTime();
		/**
		 * deletion part
		 */
		//first delete a customer from the tourWithProblem
		//and remove additional customers from solution using similarity removal
			
		//first step, delete a random customer from tour tourWithProblem			
		Set<Solution> solutionSet = new HashSet<Solution>();
		
//		CustomerInTour cit = solution.getCustomersInTours()
//                .get(new Random(42).nextInt(customerCountInTour));
//		
//		Tour tourWithProblemClone = tourWithProblem.clone();
//		List<CustomerInTour> citClone = tourWithProblemClone.getCustomersInTour();
		
		//set maximal number of removed customers
		int maximalNumberOfRemovedCustomers = 5;
		//assert that maximal number of removed customers is not greater than actual number of customers in problem
		if (solution.getVrpProblem().getCustomers().size() < maximalNumberOfRemovedCustomers)
			maximalNumberOfRemovedCustomers = solution.getVrpProblem().getCustomers().size();
		
		
		int numberOfCustomersToBeRemoved = 3;
		//assert that number of customers to be removed is not greater than the number of customers that can be removed from tours.
		//first iterate over number of removed customers
//		for ( int numberOfCustomersToBeRemoved = 1; numberOfCustomersToBeRemoved <= maximalNumberOfRemovedCustomers; numberOfCustomersToBeRemoved++ ) 
//		{
		    numberOfCustomersToBeRemoved = assertThatNumberIsNotGreaterThanNumberOfCustomersThatCanBeRemoved(numberOfCustomersToBeRemoved, solution);					
			
			//überprüfe, ob Kunde angefahren wird, bevor Event eintritt
			if (tourWithProblem.getUnfixedCustomersInTour().size() == 0)
			{
//				//überprüfe, ob Überspringen des Kunden möglich ist.
//				Tour newTour = tourWithProblem.clone();
//				newTour.removeCustomerAtPosition(newTour.getCustomerWithId(event.getCustomerNo()).getPosition());
//				if (TourUtils.isTourFeasible(newTour)){
					//hebe Fixierung dieses Kunden auf
					tourWithProblem.getCustomerWithId(event.getCustomerNo()).setFixCustomerInTour(false);
			}
			
			System.err.println("Anzahl unfixierter Kunden in tourWithProblem: " + tourWithProblem.getUnfixedCustomersInTour().size());
			
			//then iterate over all customers in tourWithProblem
			for (CustomerInTour cit : tourWithProblem.getUnfixedCustomersInTour())//-OB -es wird nur über nicht fixierte iteriert
			{	
				//System.out.println("	START ITERATION MIT " + numberOfCustomersToBeRemoved + " ZU ENTFERNENDEN KUNDEN, ERSTER GELÖSTER KUNDE IST " + cit.getCustomer().getCustomerNo());							
				
				Solution temporarySolution = solution.clone();
				
				System.err.println("originale Solution: " + solution.getSolutionAsString());
				System.err.println("geklonte Solution: " + temporarySolution.getSolutionAsString());				
				
				Tour tourWithProblemInTemporarySolution = identifyTourWithProblemInTemporarySolution(temporarySolution); 
										
				System.err.println("originale tourWithProblem: " + tourWithProblem.getTourAsTupel() + "; id: " + tourWithProblem.getId());
				System.err.println("geklonte tourWithProblem: " + tourWithProblemInTemporarySolution.getTourAsTupel() + "; id: " + tourWithProblemInTemporarySolution.getId());
					
				Customer firstRemovedCustomer = tourWithProblemInTemporarySolution.
						removeCustomerAtPosition(cit.getPosition());
				
				RemovedCustomer rc = new RemovedCustomer();
				rc.setCustomer(firstRemovedCustomer);
				rc.setTourId(tourWithProblemInTemporarySolution.getId());
				rc.setPosition(cit.getPosition());
				
				List<RemovedCustomer> removedCustomers = new ArrayList<RemovedCustomer>(
		                numberOfCustomersToBeRemoved);
		        removedCustomers.add(rc);	     
		        
			        //now assert that demand on tour with problem does not exceed vehicle capacity
			        //as long as demand on tour is greater than vehicle capacity, remove customers from tour.
			        //first calculate remaining demand on tour with problem
			        long totalDemand = 0;
			        for (Customer c : tourWithProblemInTemporarySolution.getCustomers()) 
			        {
			            totalDemand += c.getDemand();
			        }
			        //Then delete customers if necessary
			        while (totalDemand > tourWithProblemInTemporarySolution.getVehicle().getCapacity())
			        {		        	
			            System.err.println("		Entfernen eines weiteren Kunden aus der TourWithProblem ist nötig, da totalDemand auf Tour " 
			            		+ totalDemand + " größer ist als die Vehicle Kapazität " + tourWithProblemInTemporarySolution.getVehicle().getCapacity()); 
			        	//remove random customer from tour
			            CustomerInTour NextRandomCustomer;
			            if (tourWithProblemInTemporarySolution.getUnfixedCustomersInTour().size() == 0)
						{
//			            	System.out.println("		event.getCustomerNo() = " + event.getCustomerNo());
//			            	System.out.println("		tourWithProblemClone = " + tourWithProblemClone.getTourAsTupel());
//			            	System.out.println("		tourWithProblemClone.getCustomerWithId(event.getCustomerNo()) = " + tourWithProblemClone.getCustomerWithId(event.getCustomerNo()));
			            	System.err.println("tourWithProblemInRepairedSolution: " + tourWithProblemInTemporarySolution.getTourAsTupel());
			            	System.err.println("event.getCustomerNo(): " + event.getCustomerNo());
			            	System.err.println("So sah die Lösung vor dem Event aus: " + oldSolutionBeforeUpdateBecauseOfEvent.getSolutionAsStringWithCustomer());
			            	System.err.println("So sah die Lösung nach dem Event aus: " + oldSolutionAfterUpdateBecauseOfEvent.getSolutionAsStringWithCustomer());
			            	System.err.println("tourWithProblemInRepairedSolution.getCustomerWithId(event.getCustomerNo()): " + tourWithProblemInTemporarySolution.getCustomerWithId(event.getCustomerNo()));			            	
							tourWithProblemInTemporarySolution.getCustomerWithId(event.getCustomerNo()).setFixCustomerInTour(false);
							NextRandomCustomer = tourWithProblemInTemporarySolution.getCustomerWithId(event.getCustomerNo());
						}
			            else 
			            {
			            	if (tourWithProblemInTemporarySolution.getUnfixedCustomersInTour().size() == 1)			            	
			            		NextRandomCustomer = tourWithProblemInTemporarySolution.getUnfixedCustomersInTour().get(0);			            
			            	else 
				            	NextRandomCustomer = tourWithProblemInTemporarySolution.getUnfixedCustomersInTour().
				            			get(new Random(42).nextInt((tourWithProblemInTemporarySolution.getUnfixedCustomersInTour().size()-1))); //-OB- Auswahl nur aus nicht fixierten Kunden möglich			            	
			            }
			        	//actually remove customer
			        	Customer NextRandomRemovedCustomer = tourWithProblemInTemporarySolution.removeCustomerAtPosition(NextRandomCustomer.getPosition());
			            RemovedCustomer rcNext = new RemovedCustomer();
						rcNext.setCustomer(NextRandomRemovedCustomer);
						rcNext.setTourId(tourWithProblemInTemporarySolution.getId());
						rcNext.setPosition(NextRandomCustomer.getPosition());
						removedCustomers.add(rcNext);
						
//						System.out.println("		Kunde " + NextRandomCustomer.getCustomer().getCustomerNo() + " wird aus Tour entfernt.");
						
						//recalculate demand on tourWithProblem
						totalDemand -= NextRandomCustomer.getCustomer().getDemand();
//						System.out.println("		Neuer Demand auf TourWithProblem ist " + totalDemand);
			        }
			        temporarySolution.removeEmptyTours();
			        
		        //now demand on tour does not exceed vehicle capacity
			    try {
			    	System.out.println("\n ITERIERE ÜBER KUNDEN: Kunde " + firstRemovedCustomer.getCustomerNo() + " wird als erstes aus Tour " + tourWithProblem.getTourAsTupel() + " entfernt.");
			    } catch (Exception e) {
			        System.err.println("Fehler beim Ausgeben von Infos.");
			        if (firstRemovedCustomer == null)
			        	System.err.println("firstRemovedCustomer ist null");
			        if (tourWithProblem == null)
			        	System.err.println("tourWithProblem ist null");
			        e.printStackTrace();
			    } 			    
			    
			    try {
			    	System.out.println("	Alle Kunden, die aus problematischer Tour entfernt wurden:");
			    	for(RemovedCustomer i : removedCustomers)
			    		System.out.println(i.getCustomer().getCustomerNo());
			    } catch (Exception e) {
			    	System.err.println("Fehler beim Ausgeben von Infos.");
			    	e.printStackTrace();
			    }
						 
				//if less customers have been removed than should be removed, remove additional customers
				if (removedCustomers.size() < numberOfCustomersToBeRemoved)
				{
					int temporalNumberOfCustomersToBeRemoved = numberOfCustomersToBeRemoved - removedCustomers.size() + 1;
//					System.out.println("	numberOfCustomersToBeRemoved nach Anpassung: " + temporalNumberOfCustomersToBeRemoved + " (SimilarityRemoval entfernt genau einen Kunden weniger als die Eingabe)"); 
					removedCustomers = new SimilarityRemoval().removeRemainingCustomers(temporarySolution, temporalNumberOfCustomersToBeRemoved, 
						removedCustomers, firstRemovedCustomer);
				}
				
				temporarySolution.removeEmptyTours();				
				
				System.out.println("	Alle weiteren Kunden, die entfernt wurden:");
				for(RemovedCustomer i : removedCustomers)
					System.out.println(i.getCustomer().getCustomerNo());
				
				/**
				 * insertion part
				 */
				//now insert customers again into solution
				
				//VrpConfiguration is not used anywhere but needs to be passed because 
				//GreedyInsertationD inherits from InsertionInterface which dictates the 
				//use of the parameter-interface 
				VrpConfiguration configuration = null;
				int iteration = 1;
				
				//for greedy insertion it is still necessary to decide 
				//whether time windows or only capacity should be checked on insertion
					
//				Solution RepairedSolutionSimVal = RepairedSolution.clone();
				
				temporarySolution = new GreedyInsertationD().insertCustomers(temporarySolution, removedCustomers, iteration, configuration);
				solutionSet.add(temporarySolution);
								
//				System.out.println("		Gefundene Lösung mit Greedy: " + RepairedSolution.getSolutionAsTupel());
				
//				RepairedSolutionSimVal = new SimilarityInsertion().insertCustomers(RepairedSolutionSimVal, removedCustomers, iteration, configuration);
//				
//				System.out.println("		Gefundene Lösung mit SimVal Insertion: " + RepairedSolutionSimVal.getSolutionAsString());
				
				//assertSame(RepairedSolution.getTotalCost(), RepairedSolutionSimVal.getTotalCost());
				
				
				
			//end iteration over customers in tourWithProblem
			} 		
			//select best solution
			double minCost = 1000000000;
			Solution bestSolution = null;
			for (Solution s : solutionSet)
				if (s.getTotalDistanceOfAllTours() < minCost)
				{
					bestSolution = s;
					minCost = s.getTotalDistanceOfAllTours();
				};		
				
			long endTime = System.nanoTime();
	    	long timeTaken = (endTime - startTime);
	    	
			RepairedSolution repairedSolution = new RepairedSolution(solution, bestSolution, event, timeTaken);
			bestSolutionList.add(repairedSolution);
			
//		end iteration over numberOfRemovedCustomers
//		} 
	    //bestSolutionList enthält für jede Anzahl an gelösten Kunden die jeweils beste Lösung, 
		//beginnend mit Lösung für 1 gelösten Kunden, 2 gelöste Kunden, etc.
		return bestSolutionList;
	}



	private int assertThatNumberIsNotGreaterThanNumberOfCustomersThatCanBeRemoved(
			int numberOfCustomersToBeRemoved, Solution solution) {
		if (solution.getUnfixedCustomersInTours().size() < numberOfCustomersToBeRemoved)
			numberOfCustomersToBeRemoved = solution.getUnfixedCustomersInTours().size();
		return numberOfCustomersToBeRemoved;
	}



	private Tour identifyTourWithProblemInTemporarySolution(
			Solution temporarySolution) {
		long tourId = tourWithProblem.getId();
		int numberOfToursFound = 0;
		Tour tourWithProblemInTemporarySolution = null;
		for (Tour t : temporarySolution.getTours())
			if (t.getId() == tourId) {			
				tourWithProblemInTemporarySolution = t;
				numberOfToursFound++;
			};									
		if (tourWithProblemInTemporarySolution == null)
			throw new RuntimeException("Die tourWithProblem konnte nicht in der geklonten Solution gefunden werden.");
		else if (numberOfToursFound > 1) 
			throw new RuntimeException("Es wurden mehrere Touren mit der gleichen ID gefunden.");
		else 
			return tourWithProblemInTemporarySolution;
	}
	
	
	
	/**	
	 * @author Andreas Braun
	 * 
	 * checks whether repaired solutions are dominated according to the following three criteria:
	 * - cost
	 * - distance (-> environment)
	 * - numberOfAffectedTours (-> communication overhead)
	 * @throws IOException 
	 */
	 public List<RepairedSolution> checkForDominatedSolutions(List<RepairedSolution> repairedSolutionList)
	 {
		 int numberOfDominatedElements = 0;
		 List<RepairedSolution> newList = new LinkedList<RepairedSolution>();
		 for (RepairedSolution repairedSolution : repairedSolutionList)
		 {
			 boolean isElementNotDominated = true;
			 for (RepairedSolution repairedSolutionToBeComparedWith : repairedSolutionList)
			 {
			     //check whether RepairedSolutionToBeCheckedWith dominates RepairedSolution wrt additional cost 
				 if (repairedSolutionToBeComparedWith.getAdditionalCost() < repairedSolution.getAdditionalCost() &&
					 repairedSolutionToBeComparedWith.getAdditionalDistance() <= repairedSolution.getAdditionalDistance() &&
					 repairedSolutionToBeComparedWith.getNumberOfAffectedTours() <= repairedSolution.getNumberOfAffectedTours())
					 	isElementNotDominated = false;
				 //check whether RepairedSolutionToBeCheckedWith dominates RepairedSolution wrt additional distance
				 if (repairedSolutionToBeComparedWith.getAdditionalCost() <= repairedSolution.getAdditionalCost() &&					 
					 repairedSolutionToBeComparedWith.getAdditionalDistance() < repairedSolution.getAdditionalDistance() &&
					 repairedSolutionToBeComparedWith.getNumberOfAffectedTours() <= repairedSolution.getNumberOfAffectedTours())
					 	isElementNotDominated = false;				 
				 //check whether RepairedSolutionToBeCheckedWith dominates RepairedSolution wrt number of affected tours 
				 if (repairedSolutionToBeComparedWith.getAdditionalCost() <= repairedSolution.getAdditionalCost() &&					 
					 repairedSolutionToBeComparedWith.getAdditionalDistance() <= repairedSolution.getAdditionalDistance() &&
					 repairedSolutionToBeComparedWith.getNumberOfAffectedTours() < repairedSolution.getNumberOfAffectedTours())
					 	isElementNotDominated = false;			
			 }
			 if (isElementNotDominated) {
				 newList.add(repairedSolution);
			 } else {
				 numberOfDominatedElements += 1;
			 }
		 }
		 System.out.println(numberOfDominatedElements + " Elements were removed because they were dominated.");
		 return newList;
	 }	
	
	 
	 public List<RepairedSolution> checkForEqualSolutions(List<RepairedSolution> repairedSolutionList) {
		 List<RepairedSolution> newRepairedSolutionList = new LinkedList<RepairedSolution>();
		 for (RepairedSolution rs : repairedSolutionList)
			 if (!isElementAlreadyInList(rs, newRepairedSolutionList))
				 newRepairedSolutionList.add(rs);
		return newRepairedSolutionList;
	 }

	private boolean isElementAlreadyInList(RepairedSolution rs,
			List<RepairedSolution> newRepairedSolutionList) {
		boolean isElementAlreadyInList = false;
		for (RepairedSolution nrs : newRepairedSolutionList)
			if (nrs.equals(rs))
				isElementAlreadyInList = true;
		return isElementAlreadyInList;
	}
	 
	
}