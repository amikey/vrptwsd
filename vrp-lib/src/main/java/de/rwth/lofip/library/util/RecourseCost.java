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
import de.rwth.lofip.library.SolutionGot;
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
	//Recourse Kosten, die nur Sonderfahrten enthalten
	private double recourseCostOnlyAdditionalTours = 0;
	double numberOfAdditionalTours = 0;
	int numberOfRouteFailures = 0;
	//numberDifferentRecourseActions is always total number
	double numberOfDifferentRecourseActions = 0;
	//numberOfDifferentToursOfBasicVehicles
	double numberOfDifferentToursForBasicVehicles; //number of different tours for all basic vehicles
	
	//first entry: number of vehicles needed to serve these customers //second entry: number of customers that are served by this number of vehicles
	HashMap<Integer, Integer> toursNeededToServeNumberOfCustomers = new HashMap<Integer, Integer>();
	//what does this do?
	private List<Integer> tourIndizes = new ArrayList<Integer>();	
	
	public RecourseCost(List<GroupOfTours> gots) {		
		for (GroupOfTours got : gots) {
			recourseCost += got.getExpectedRecourse().getRecourseCost();
			recourseCostOnlyAdditionalTours += got.getExpectedRecourse().getRecourseCostOnlyAdditionalTours();
			numberOfAdditionalTours += got.getExpectedRecourse().getNumberOfAdditionalTours();
			numberOfRouteFailures += got.getExpectedRecourse().getNumberOfRouteFailures();
			numberOfDifferentRecourseActions += got.getExpectedRecourse().getNumberOfDifferentRecourseActions();
			numberOfDifferentToursForBasicVehicles += got.getExpectedRecourse().getNumberOfDifferentToursForBasicVehicles();
			calculateToursNeededToServeNumberOfCustomers(got);
		}		
	}

	public RecourseCost(GroupOfTours got) {
		double overallRecourseCost = 0;
		double overallRecourseCostOnlyAdditionalTours = 0;
		ArrayList<GroupOfTours> listOfRecourseActions = new ArrayList<GroupOfTours>();
		ArrayList<Tour> listOfDifferentTours = new ArrayList<Tour>(got.getTours());
		numberOfDifferentToursForBasicVehicles = listOfDifferentTours.size();
		int numberOfInfeasibleScenarios = 0;
		//this is needed to calculate which number of customers is served by which number of vehicles
		//first entry: customerNo; second entry: set of numbers of tours that customer is served by
		HashMap<Long, HashSet<Integer>> customersServedByTours = new HashMap<Long, HashSet<Integer>>();
		
		SimulationUtils.resetSeed();		
		initialiseCustomersServedByTours(customersServedByTours, got);		

		//run scenarios
		for (int i = 1; i <= Parameters.getNumberOfDemandScenarioRuns(); i++) {		
			GroupOfTours gotClone = got.cloneWithCopyOfTourAndCustomers();		
			SimulationUtils.setCapacityOfVehiclesToOriginalCapacity(gotClone);	
			SimulationUtils.generateDemandsForNextScenario();
			SimulationUtils.setDemandForCustomersWithDeviation(gotClone);

			if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) {
				numberOfInfeasibleScenarios++;
//				System.out.println("Solution is infeasible after altering demands: " + numberOfInfeasibleScenarios);
				
				calculateNumberOfRouteFailures(gotClone);
				//RUNTIME_TODO: entfernen, braucht O(n) zeit
				assertThatGotContainsNoEmptyTours(gotClone);
				makeSolutionFeasibleAgain(gotClone, listOfRecourseActions);
				//RUNTIME_TODO: entfernen
				assertThatSolutionIsFeasible(gotClone,got,i);				
				overallRecourseCost += calculateRecourseCostOfFeasibleSolution(got, gotClone);
				overallRecourseCostOnlyAdditionalTours += calculateRecourseCostAdditionalToursOfFeasibleSolution(gotClone);
				calculateAdditionalNumberOfTours(gotClone);
				calculateNumberOfDifferentRecourseTours(listOfDifferentTours, gotClone);
				calculateNumberOfDifferentRecourseActionsAndUpdateCustomersServedByTours(listOfRecourseActions, gotClone, customersServedByTours);				
			} 		
		}
		recourseCost = overallRecourseCost / Parameters.getNumberOfDemandScenarioRuns();	
		recourseCostOnlyAdditionalTours = overallRecourseCostOnlyAdditionalTours / Parameters.getNumberOfDemandScenarioRuns();
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
			if (ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotTemp)) {
				gotClone = gotTemp;
				break;
			}
		}
	}
	
	public GroupOfTours makeSolutionFeasibleAgainWithCostMinimationPublic(GroupOfTours gotClone) {
		makeSolutionFeasibleAgainWithCostMinimization(gotClone);
		return gotClone;
	}
	
	private void makeSolutionFeasibleAgainWithCostMinimization(GroupOfTours gotClone) {
		LocalSearchForElementWithTours ls = new LocalSearchForElementWithTours(); //DESIGN_TODO: Hier auch Tabu Search testen
		//create Solution from gotClone for processing with local search    
		ls.improve(gotClone);
		assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots());
		int iteration = 0;
		while (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) {
			gotClone.addEmptyTour();
			ls.improve(gotClone);
			
			iteration++;
			if(iteration >= 10)
				throw new RuntimeException("Mehr als 10 zus�tzliche Touren n�tig, um Got feasible zu machen. Sehr sehr sehr unwahrscheinlich.");
		}
		
//		for (int i = 1; i <= Parameters.getMaximalNumberOfToursInGots(); i++) {
//			for (int j = 1; j <= i; j++) {
//				gotClone.addEmptyTour();
//			}
//			ls.improve(gotClone);
//			if (ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone))
//				break;
//		}

//		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) { 
//			//got is still demand infeasible -> no feasible solution could be found for demand			
//			gotClone.addEmptyTour();			
//			ls.improve(gotClone);		
//			System.out.println("Got nach improve mit LS: " + gotClone.getAsTupelWithDemand());
//		}
//		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) { 
//			//got is still demand infeasible -> drei Touren reichen nicht aus, oder LS findet keine L�sung, in der drei Touren ausreichen			
//			gotClone.addEmptyTour();
//			System.out.println("Got vor improve mit LS: " + gotClone.getAsTupelWithDemand());
//			ls.improve(gotClone);
//			System.out.println("Got nach improve mit LS: " + gotClone.getAsTupelWithDemand());
//		}		
	}

	private void assertThatSolutionIsFeasible(GroupOfTours gotClone, GroupOfTours got, int i) {
		//asserts for code validation; es k�nnte auch der pathologische Fall auftreten, dass vier Touren ben�tigt werden, um alle Kunden bedienen zu k�nnen; das wird unten �berpr�ft
		//RUNTIME_TODO: remove assert
//		if (gotClone.getNumberOfTours() > Parameters.getMaximalNumberOfToursInGots()+1)
//			System.out.println("Anzahl Touren gr��er als Parameters.getMaximalNumberOfToursInGots()+1. So sieht das got aus : " + gotClone.getAsTupel());
//		assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots()+1);
		if (!ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone)) {
			System.out.println("folgendes Got aus Problem " + got.getParentSolution().getVrpProblem().getDescription() + " ist infeasible in Iteration " + i + " in Simulation: " + got.getAsTupel());
		}
		assertEquals(true, ElementWithToursUtils.isElementDemandFeasibleCheckWithRef(gotClone));
	}
	
	private double calculateRecourseCostOfFeasibleSolution(GroupOfTours got, GroupOfTours gotClone) {
		//IMPORTANT_TODO: Will ich hier auch zus�tzliche Tour mit doppelten Kosten bestrafen? Eigentlich schon, oder?
		double recourseCostTemp = -got.getTotalDistanceWithCostFactor();
		double costOfGotClone = gotClone.getTotalDistanceWithCostFactor(); 
		recourseCostTemp += costOfGotClone;
		return recourseCostTemp;		
	}
	
	private double calculateRecourseCostAdditionalToursOfFeasibleSolution(GroupOfTours gotClone) {
		return gotClone.getTotalDistanceOfAdditionalToursWithCostFactor();		
	}
	
	private void calculateAdditionalNumberOfTours(GroupOfTours gotClone) {
		if (gotClone.getNumberOfTours() >= Parameters.getMaximalNumberOfToursInGots())
			numberOfAdditionalTours += (gotClone.getNumberOfTours() - Parameters.getMaximalNumberOfToursInGots());
	}
	
	private void calculateNumberOfDifferentRecourseTours(ArrayList<Tour> listOfDifferentRecourseTours, GroupOfTours gotClone) {
		for (Tour t : gotClone.getTours()) 
			if (!t.isNewTourForRecourseAction()) 
				if (!isTourAlreadyExistsInListOfTours(t, listOfDifferentRecourseTours)) {
					numberOfDifferentToursForBasicVehicles++;
					listOfDifferentRecourseTours.add(t);
				}
	}
	
	private boolean isTourAlreadyExistsInListOfTours(Tour t, ArrayList<Tour> list) {
		boolean exists = false;
		for (Tour tour : list) {
			if (tour.equals(t)) {
				exists = true;
				break;
			}			
		}
		return exists;
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
	
	private void calculateToursNeededToServeNumberOfCustomers(GroupOfTours got) {
		Iterator<Entry<Integer, Integer>> it = got.getExpectedRecourse().getToursNeededToServeNumberOfCustomers().entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pair = it.next();
	        int currentlyExaminedNumberOfVehicles = pair.getKey();
	        int numberOfCustomersSoFarServedByThisNumberOfVehicles = getNumberOfCustomersThatIsServedByTheFollowingNumberOfVehicles(currentlyExaminedNumberOfVehicles);	        
	        int numberOfCustomersNowServedByThisNumberOfVehicles = numberOfCustomersSoFarServedByThisNumberOfVehicles + pair.getValue();
	        toursNeededToServeNumberOfCustomers.put(pair.getKey(), numberOfCustomersNowServedByThisNumberOfVehicles);	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public HashMap<Integer, Integer> getToursNeededToServeNumberOfCustomers() {
		return toursNeededToServeNumberOfCustomers;
	}

	public double getRecourseCost() {
		return recourseCost;
	}
	
	public double getRecourseCostOnlyAdditionalTours() {
		return recourseCostOnlyAdditionalTours;
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
	
	public double getNumberOfDifferentToursForBasicVehicles() {
		return numberOfDifferentToursForBasicVehicles;
	}

	public void addTourIndex(int i) {
		tourIndizes.add(i);
	}

	public List<Integer> getIndizesOfTours() {
		return tourIndizes;
	}
	
	public double getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(SolutionGot solution) {
		double deterministicCostOfCompleteSolution = solution.getTotalDistanceWithCostFactor();
		double detAndStochCost = deterministicCostOfCompleteSolution + recourseCost;
		double convexcombination = detAndStochCost + detAndStochCost * Parameters.getWeightForConvexcombination() * (numberOfDifferentToursForBasicVehicles / solution.getNumberOfTours());
		double convexcombinationWithoutDetCost = convexcombination - deterministicCostOfCompleteSolution;
		return convexcombinationWithoutDetCost;
		
//		return recourseCost + (recourseCost / Parameters.getNumberOfDemandScenarioRuns() * numberOfDifferentRecourseActions); 
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
	
	public RecourseCost(double overallRecourseCost,	double numberOfDifferentRecourseActions2, int additionalNumberOfTours2, int numberOfRouteFailures2) {
		this.recourseCost = overallRecourseCost;
		this.numberOfAdditionalTours = additionalNumberOfTours2;
		this.numberOfRouteFailures = numberOfRouteFailures2;
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions2;
	}

	public RecourseCost() {
		// TODO Auto-generated constructor stub
	}

		
}
