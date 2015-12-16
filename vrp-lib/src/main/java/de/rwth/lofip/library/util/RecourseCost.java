package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.solver.util.TourUtils;

public class RecourseCost {

	//Recourse Cost for several Tours is accumulated cost (see constructor)
	double recourseCost = 0;
	double numberOfAdditionalTours = 0;
	int numberOfRouteFailures = 0;
	//number of different recourse actions for several gots is the mean number of different recourse actions for all gots
	double numberOfDifferentRecourseActions = 0;
	//first entry: number of vehicles needed to serve these customers
	//second entry: number of customers that are served by this number of vehicles
	HashMap<Integer, Integer> toursNeededToServeNumberOfCustomers = new HashMap<Integer, Integer>();
	
	private List<Integer> tourIndizes = new ArrayList<Integer>();
	
	public RecourseCost(double overallRecourseCost,
			double numberOfDifferentRecourseActions2, int additionalNumberOfTours2, int numberOfRouteFailures2) {
		this.recourseCost = overallRecourseCost;
		this.numberOfAdditionalTours = additionalNumberOfTours2;
		this.numberOfRouteFailures = numberOfRouteFailures2;
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions2;
	}
	
	public RecourseCost(GroupOfTours got) {
		double overallRecourseCost = 0;
		ArrayList<GroupOfTours> listOfRecourseActions = new ArrayList<GroupOfTours>();
		int numberOfInfeasibleScenarios = 0;
		//this is needed to calculate which number of customers is served by which number of vehicles
		//first entry: customerNo
		//second entry: set of numbers of tours that customer is served by
		HashMap<Long, HashSet<Integer>> customersServedByTours = new HashMap<Long, HashSet<Integer>>();
		
		SimulationUtils.resetSeed();		
		
		//initialisiere customersServedByTours
		for (int i = 0; i < got.getNumberOfTours(); i++) {
			for (Customer c : got.getTour(i).getCustomers()) {
				HashSet<Integer> tempHashSet = new HashSet<Integer>();
				tempHashSet.add(i);
				
				System.out.println("tempHashSet nach initialisierung f�r Tour " + i + " und Kunde " + c.getCustomerNo() + ": (" );
				for (int k : tempHashSet)
					System.out.print(k + ", ");
				System.out.println(")" );
				
				customersServedByTours.put(c.getCustomerNo(), tempHashSet);
			}
		}
		
		for (int i = 1; i <= Parameters.getNumberOfDemandScenarioRuns(); i++) {					
			GroupOfTours gotClone = got.cloneWithCopyOfTourAndCustomers();
			SimulationUtils.setDemandForCustomersWithDeviation(gotClone, Parameters.getFluctuationOfDemandInPercentage());
			    			
			SimulationUtils.setCapacityOfVehiclesToOriginalCapacity(gotClone);
			if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) {
				numberOfInfeasibleScenarios++;
				System.out.println("Solution is infeasible after altering demands: " + numberOfInfeasibleScenarios);
				
				//calculateNumberOfRouteFailures
				for (Tour tour : gotClone.getTours())
					if (!TourUtils.isTourFeasibleWrtDemandCheckWithRef(tour))
						numberOfRouteFailures++;
				
				//IMPORTANT_TODO: Hier zuerst die L�sungen auf feasibility �berpr�fen, die schon entstanden sind
//				//first try recourse actions that have already been created
//				//RUNTIME_TODO: will ich hier wirklich sortieren?
//				Comparator<GroupOfTours> gotByCostComparator = (e1,e2) -> Double.compare(e1.getTotalDistanceWithCostFactor(),e2.getTotalDistanceWithCostFactor());		
//				Collections.sort(listOfRecourseActions, gotByCostComparator);
//				if 
				
				//first make solution feasible again
				LocalSearchForElementWithTours ls = new LocalSearchForElementWithTours(); //DESIGN_TODO: Hier auch Tabu Search testen
				//create Solution from gotClone for processing with local search    	    				
				ls.improve(gotClone);
				assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots());
				if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) { 
					//got is still demand infeasible -> no feasible solution could be found for demand
					gotClone.addEmptyTour();
					ls.improve(gotClone);    					
				}
				
				//asserts for code validation; es k�nnte auch der pathologische Fall auftreten, dass vier Touren ben�tigt werden, um alle Kunden bedienen zu k�nnen; das wird unten �berpr�ft
				//RUNTIME_TODO: remove assert
				assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots()+1);
				assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(gotClone));

				//calculate recourseCost
    			//IMPORTANT_TODO: Will ich hier auch zus�tzliche Tour mit doppelten Kosten bestrafen? Eigentlich schon, oder?
    			double recourseCostTemp = -got.getTotalDistanceWithCostFactor();
    			double costOfGotClone = gotClone.getTotalDistanceWithCostFactor(); 
    			recourseCostTemp += costOfGotClone;
    			overallRecourseCost += recourseCostTemp;
    			System.out.println("Cost of Solution " + got.getTotalDistanceWithCostFactor());
    			System.out.println("Recourse Cost: " + recourseCostTemp);
    			
    			//calculate additional number of tours
    			if (gotClone.getNumberOfTours() >= Parameters.getMaximalNumberOfToursInGots())
    				numberOfAdditionalTours += (gotClone.getNumberOfTours() - Parameters.getMaximalNumberOfToursInGots());
    			
    			//calculate number of different recourse actions    		
    			if (!isGotAlreadyExistsInRecourseActions(gotClone, listOfRecourseActions)) {
    				System.out.println("NEW SOLUTION THAT HAS NOT BEEN SEEN: ");
    				gotClone.print();
    				numberOfDifferentRecourseActions++;
    				listOfRecourseActions.add(gotClone);
    				
    				//update customersServedByTours
    				for (int j = 0; j < gotClone.getNumberOfTours(); j++) {
    					for (Customer c : gotClone.getTour(j).getCustomers()) {
    						HashSet<Integer> tempHashSet = customersServedByTours.get(c.getCustomerNo());
    						
    						System.out.println("tempHashSet f�r Kunde " + c.getCustomerNo() + " bevor Tour " + j + " hinzugef�gt wird: ");
    						for (int k : tempHashSet)
    							System.out.print(k + ", ");
    						System.out.println(")" );
    						
    						tempHashSet.add(j);
    						
    						System.out.println("tempHashSet f�r Kunde " + c.getCustomerNo() + " nachdem Tour " + j + " hinzugef�gt wird:" );
    						for (int k : tempHashSet)
    							System.out.print(k + ", ");
    						System.out.println(")" );
    						
    						customersServedByTours.put(c.getCustomerNo(), tempHashSet);
    					}
    				}    				
    			}
			} // else 
//				System.out.println("Solution is feasible after altering demands");
		}
		overallRecourseCost = overallRecourseCost / Parameters.getNumberOfDemandScenarioRuns();
		
		recourseCost = overallRecourseCost;
		
		calculateNumberOfCustomersServedByNumberOfDifferentTours(customersServedByTours);
	}
		
    public boolean isGotAlreadyExistsInRecourseActions(GroupOfTours gotClone, List<GroupOfTours> list) {
		boolean exists = false;
		for (GroupOfTours got : list) {
			if (got.equals(gotClone)) {
				exists = true;
				break;
			}			
		}
		return exists;
	}
    
    private void calculateNumberOfCustomersServedByNumberOfDifferentTours(HashMap<Long, HashSet<Integer>> customersServedByTours) {
		Iterator<Entry<Long, HashSet<Integer>>> it = customersServedByTours.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Long, HashSet<Integer>> pair = it.next();
	        // knowing the customer number is not necessary but we would get it this way:
	        // Long currentCustomerNo = pair.getKey();
	        int numberOfVehiclesThatTheCurrentCustomerIsServedBy = pair.getValue().size();
	        int numberOfCustomersThatIsServedByThisNumberOfTours = getNumberOfCustomersThatIsServedByTheFollowingNumberOfVehicles(numberOfVehiclesThatTheCurrentCustomerIsServedBy);	      
	        numberOfCustomersThatIsServedByThisNumberOfTours++;
	        toursNeededToServeNumberOfCustomers.put(numberOfVehiclesThatTheCurrentCustomerIsServedBy, numberOfCustomersThatIsServedByThisNumberOfTours);
	      
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	private int getNumberOfCustomersThatIsServedByTheFollowingNumberOfVehicles(int numberOfVehiclesCustomerIsServedBy) {
		int numberOfCustomersThatIsServedByThisValue;
		try {
        	numberOfCustomersThatIsServedByThisValue = toursNeededToServeNumberOfCustomers.get(numberOfVehiclesCustomerIsServedBy);
        } catch (NullPointerException e) {
        	numberOfCustomersThatIsServedByThisValue = 0;
        }
		return numberOfCustomersThatIsServedByThisValue;
	}

	public RecourseCost(List<GroupOfTours> gots) {		
		for (GroupOfTours got : gots) {
			recourseCost += got.getExpectedRecourse().getRecourseCost();
			numberOfAdditionalTours += got.getExpectedRecourse().getNumberOfAdditionalTours();
			numberOfRouteFailures += got.getExpectedRecourse().getNumberOfRouteFailures();
			numberOfDifferentRecourseActions += got.getExpectedRecourse().getNumberOfDifferentRecourseActions();
			
			calculateToursNeededToServeNumberOfCustomers(got);
		}		
		//IMPORTANT_TODO: implement calculation of customersServedByTours		
		
	}
	
	private void calculateToursNeededToServeNumberOfCustomers(GroupOfTours got) {
		Iterator<Entry<Integer, Integer>> it = got.getExpectedRecourse().getNumberOfCustomersServedByNumberOfDifferentTours().entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pair = it.next();
	        int currentlyExaminedNumberOfVehicles = pair.getKey();
	        int numberOfCustomersSoFarServedByThisNumberOfVehicles = getNumberOfCustomersThatIsServedByTheFollowingNumberOfVehicles(currentlyExaminedNumberOfVehicles);	        
	        int numberOfCustomersNowServedByThisNumberOfVehicles = numberOfCustomersSoFarServedByThisNumberOfVehicles + pair.getValue();
	        toursNeededToServeNumberOfCustomers.put(pair.getKey(), numberOfCustomersNowServedByThisNumberOfVehicles);
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public HashMap<Integer, Integer> getNumberOfCustomersServedByNumberOfDifferentTours() {
		return toursNeededToServeNumberOfCustomers;
	}

	public double getRecourseCost() {
		return recourseCost;
	}
	
	public double getNumberOfAdditionalTours() {
		return numberOfAdditionalTours;
	}


	public int getNumberOfRouteFailures() {
		return numberOfRouteFailures;
	}
	
	public double getNumberOfDifferentRecourseActions() {
		return numberOfDifferentRecourseActions;
	}

	public void addTourIndex(int i) {
		tourIndizes.add(i);
	}

	public List<Integer> getIndizesOfTours() {
		return tourIndizes;
	}
	
	public double getCombinationOfCostAndNumberRecourseActions() {
		//DESIGN_TODO: Hier muss ich mir noch genauer �berlegen, wie ich die Kombination aus recourseCost und #Aushilfsaktionen machen m�chte
		return recourseCost + (recourseCost / Parameters.getNumberOfDemandScenarioRuns() * numberOfDifferentRecourseActions); 
	}
	
	
	// Utilities
	
	public void print() {
		System.out.println("Cost: " + recourseCost + "; NumberOfDifferentRecourseActions: " + numberOfDifferentRecourseActions);
		
	}
		
}
