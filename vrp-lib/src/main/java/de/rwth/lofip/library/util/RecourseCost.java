package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.solver.util.TourUtils;

public class RecourseCost {

	//Recourse Cost for several Tours is accumulated cost (see constructor)
	double recourseCost = 0;
	double numberOfAdditionalTours = 0;
	int numberOfRouteFailures = 0;
	//numberDifferentRecourseActions is always total number
	double numberOfDifferentRecourseActions = 0;
	//first entry: number of vehicles needed to serve these customers //second entry: number of customers that are served by this number of vehicles
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
		//first entry: customerNo; second entry: set of numbers of tours that customer is served by
		HashMap<Long, HashSet<Integer>> customersServedByTours = new HashMap<Long, HashSet<Integer>>();
		
		SimulationUtils.resetSeed();		
		initialiseCustomersServedByTours(customersServedByTours, got);		
		
		//run scenarios
		for (int i = 1; i <= Parameters.getNumberOfDemandScenarioRuns(); i++) {					
			GroupOfTours gotClone = got.cloneWithCopyOfTourAndCustomers();
			SimulationUtils.generateDemandsForNextScenario();
			SimulationUtils.setDemandForCustomersWithDeviation(gotClone);
			    			
			SimulationUtils.setCapacityOfVehiclesToOriginalCapacity(gotClone);
			if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) {
				numberOfInfeasibleScenarios++;
//				System.out.println("Solution is infeasible after altering demands: " + numberOfInfeasibleScenarios);
				
				calculateNumberOfRouteFailures(gotClone);
				//RUNTIME_TODO: entfernen, braucht O(n) zeit
				assertThatGotContainsNoEmptyTours(gotClone);
				makeSolutionFeasibleAgain(gotClone, listOfRecourseActions);
				//RUNTIME_TODO: entfernen
				assertThatSolutionIsFeasible(gotClone);
				overallRecourseCost = calculateRecourseCostOfFeasibleSolution(overallRecourseCost, got, gotClone);
				calculateAdditionalNumberOfTours(gotClone);
				calculateNumberOfDifferentRecourseActionsAndUpdateCustomersServedByTours(listOfRecourseActions, gotClone, customersServedByTours);				
			} 		
		}
		recourseCost = overallRecourseCost / Parameters.getNumberOfDemandScenarioRuns();	
		calculateNumberOfCustomersServedByNumberOfDifferentTours(customersServedByTours);
	}

	private void initialiseCustomersServedByTours(HashMap<Long, HashSet<Integer>> customersServedByTours, GroupOfTours got) {
		for (int i = 0; i < got.getNumberOfTours(); i++) {
			for (Customer c : got.getTour(i).getCustomers()) {
				HashSet<Integer> tempHashSet = new HashSet<Integer>();
				tempHashSet.add(i);
				customersServedByTours.put(c.getCustomerNo(), tempHashSet);
			}
		}
	}
	
	private void calculateNumberOfRouteFailures(GroupOfTours gotClone) {
    	//calculateNumberOfRouteFailures
		for (Tour tour : gotClone.getTours())
			if (!TourUtils.isTourFeasibleWrtDemandCheckWithRef(tour))
				numberOfRouteFailures++;		
	}
	
	private void assertThatGotContainsNoEmptyTours(GroupOfTours got) {
		assertEquals(false, got.isHasEmptyToursThatAreNotForRecourse());
	}
	
	private void makeSolutionFeasibleAgain(GroupOfTours gotClone, ArrayList<GroupOfTours> listOfRecourseActions) {
		if (Parameters.isRecourseActionNumberMinimization())
			makeSolutionFeasibleAgainWithRecourseMinimization(gotClone, listOfRecourseActions);
		else
			makeSolutionFeasibleAgainWithCostMinimization(gotClone);	
	}
		
	private void makeSolutionFeasibleAgainWithRecourseMinimization(GroupOfTours gotClone, ArrayList<GroupOfTours> listOfRecourseActions) {
		tryToReuseAlreadySeenRecourseAction(gotClone, listOfRecourseActions);
		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone))
			//in this case no previous Recourse Actions could be used
			makeSolutionFeasibleAgainWithCostMinimization(gotClone);
	}
	
	private void tryToReuseAlreadySeenRecourseAction(GroupOfTours gotClone,	ArrayList<GroupOfTours> listOfRecourseActions) {
		Comparator<GroupOfTours> gotByCostComparator = (e1,e2) -> Double.compare(e1.getTotalDistanceWithCostFactor(),e2.getTotalDistanceWithCostFactor());		
		Collections.sort(listOfRecourseActions, gotByCostComparator);
		for (GroupOfTours gotTemp : listOfRecourseActions) {
			SimulationUtils.setDemandForCustomersWithDeviation(gotTemp);
//			if (Parameters.isTestingMode())
			//RUNTIME_TODO: entfernen
				assertEquals(true, GotUtils.isCustomersInGotsHaveTheSameDemands(gotClone, gotTemp));
			gotClone = gotTemp;
			break;
		}
	}

	private void makeSolutionFeasibleAgainWithCostMinimization(GroupOfTours gotClone) {
		LocalSearchForElementWithTours ls = new LocalSearchForElementWithTours(); //DESIGN_TODO: Hier auch Tabu Search testen
		//create Solution from gotClone for processing with local search    
		ls.improve(gotClone);
		assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots());
		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) { 
			//got is still demand infeasible -> no feasible solution could be found for demand			
			gotClone.addEmptyTour();			
			ls.improve(gotClone);			    				
		}
		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) { 
			//got is still demand infeasible -> drei Touren reichen nicht aus, oder LS findet keine Lösung, in der drei Touren ausreichen			
			gotClone.addEmptyTour();			
			ls.improve(gotClone);			    				
		}		
	}

	private void assertThatSolutionIsFeasible(GroupOfTours gotClone) {
		//asserts for code validation; es könnte auch der pathologische Fall auftreten, dass vier Touren benötigt werden, um alle Kunden bedienen zu können; das wird unten überprüft
		//RUNTIME_TODO: remove assert
		if (gotClone.getNumberOfTours() > Parameters.getMaximalNumberOfToursInGots()+1)
			System.out.println("Anzahl Touren größer als Parameters.getMaximalNumberOfToursInGots()+1. So sieht das got aus : " + gotClone.getAsTupel());
		assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots()+1);
		assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(gotClone));
	}
	
	private double calculateRecourseCostOfFeasibleSolution(double overallRecourseCost, GroupOfTours got, GroupOfTours gotClone) {
		//IMPORTANT_TODO: Will ich hier auch zusätzliche Tour mit doppelten Kosten bestrafen? Eigentlich schon, oder?
		double recourseCostTemp = -got.getTotalDistanceWithCostFactor();
		double costOfGotClone = gotClone.getTotalDistanceWithCostFactor(); 
		recourseCostTemp += costOfGotClone;
		overallRecourseCost += recourseCostTemp;
		return overallRecourseCost;		
	}
	
	private void calculateAdditionalNumberOfTours(GroupOfTours gotClone) {
		if (gotClone.getNumberOfTours() >= Parameters.getMaximalNumberOfToursInGots())
			numberOfAdditionalTours += (gotClone.getNumberOfTours() - Parameters.getMaximalNumberOfToursInGots());
	}
	
	private void calculateNumberOfDifferentRecourseActionsAndUpdateCustomersServedByTours(
			ArrayList<GroupOfTours> listOfRecourseActions, GroupOfTours gotClone, HashMap<Long, HashSet<Integer>> customersServedByTours) {  		
		if (!isGotAlreadyExistsInRecourseActions(gotClone, listOfRecourseActions)) {
//			System.out.println("NEW SOLUTION THAT HAS NOT BEEN SEEN: ");
//			gotClone.print();
			numberOfDifferentRecourseActions++;
			listOfRecourseActions.add(gotClone);
			
			updateCustomersServedByTours(customersServedByTours, gotClone, listOfRecourseActions);
		}
	}

	private void updateCustomersServedByTours(
			HashMap<Long, HashSet<Integer>> customersServedByTours,GroupOfTours gotClone, ArrayList<GroupOfTours> listOfRecourseActions) {
		for (int j = 0; j < gotClone.getNumberOfTours(); j++) {
			for (Customer c : gotClone.getTour(j).getCustomers()) {
				HashSet<Integer> tempHashSet = customersServedByTours.get(c.getCustomerNo());
				tempHashSet.add(j);
				customersServedByTours.put(c.getCustomerNo(), tempHashSet);
			}
		}    				
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
	
	public double getConvexCombinationOfCostAndNumberRecourseActions() {
		//DESIGN_TODO: Hier muss ich mir noch genauer überlegen, wie ich die Kombination aus recourseCost und #Aushilfsaktionen machen möchte
		return recourseCost + (recourseCost / Parameters.getNumberOfDemandScenarioRuns() * numberOfDifferentRecourseActions); 
	}
	
	// Print Utilities
	
	public void print() {
		System.out.println("Cost: " + recourseCost + "; NumberOfDifferentRecourseActions: " + numberOfDifferentRecourseActions);
		
	}
	
	// Test Utils
	
	public GroupOfTours makeSolutionFeasibleAgainWithCostMinimizationForTesting(GroupOfTours got) throws IOException{
		makeSolutionFeasibleAgainWithCostMinimization(got);
		return got;
	}
		
}
