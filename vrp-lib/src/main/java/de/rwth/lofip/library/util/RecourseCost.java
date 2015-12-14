package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
		SimulationUtils.resetSeed();
		int numberOfInfeasibleScenarios = 0;
		
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
				
				//IMPORTANT_TODO: Hier zuerst die Lösungen auf feasibility überprüfen, die schon entstanden sind
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
				
				//asserts for code validation; es könnte auch der pathologische Fall auftreten, dass vier Touren benötigt werden, um alle Kunden bedienen zu können; das wird unten überprüft
				//RUNTIME_TODO: remove assert
				assertEquals(true, gotClone.getNumberOfTours() <= Parameters.getMaximalNumberOfToursInGots()+1);
				assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(gotClone));

				//calculate recourseCost
    			//IMPORTANT_TODO: Will ich hier auch zusätzliche Tour mit doppelten Kosten bestrafen? Eigentlich schon, oder?
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
    			}
			} // else 
//				System.out.println("Solution is feasible after altering demands");
		}
		overallRecourseCost = overallRecourseCost / Parameters.getNumberOfDemandScenarioRuns();
		
		recourseCost = overallRecourseCost;
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

	public RecourseCost(List<GroupOfTours> gots) {		
		double numberOfDifferentRecourseActionsTemp = 0;
		for (GroupOfTours got : gots) {
			recourseCost += got.getExpectedRecourse().getRecourseCost();
			numberOfAdditionalTours += got.getExpectedRecourse().getNumberOfAdditionalTours();
			numberOfRouteFailures += got.getExpectedRecourse().getNumberOfRouteFailures();
			numberOfDifferentRecourseActionsTemp += got.getExpectedRecourse().getNumberOfDifferentRecourseActions();
		}
		numberOfDifferentRecourseActionsTemp = numberOfDifferentRecourseActionsTemp / gots.size();
		numberOfDifferentRecourseActions =  numberOfDifferentRecourseActionsTemp;		
	}
	
	
	
	public double getRecourseCost() {
		return recourseCost;
	}
	
	private double getNumberOfAdditionalTours() {
		return numberOfAdditionalTours;
	}


	private int getNumberOfRouteFailures() {
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
		//DESIGN_TODO: Hier muss ich mir noch genauer überlegen, wie ich die Kombination aus recourseCost und #Aushilfsaktionen machen möchte
		return recourseCost + (recourseCost / Parameters.getNumberOfDemandScenarioRuns() * numberOfDifferentRecourseActions); 
	}
	
	
	// Utilities
	
	public void print() {
		System.out.println("Cost: " + recourseCost + "; NumberOfDifferentRecourseActions: " + numberOfDifferentRecourseActions);
		
	}
		
}
