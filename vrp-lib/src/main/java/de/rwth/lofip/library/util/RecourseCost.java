package de.rwth.lofip.library.util;

import java.util.ArrayList;
import java.util.List;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.parameters.Parameters;

public class RecourseCost {

	//Recourse Cost for several Tours is accumulated cost (see constructor)
	double recourseCost;
	//number of different recourse actions for several gots is the mean number of different recourse actions for all gots
	double numberOfDifferentRecourseActions;
	private List<Integer> tourIndizes = new ArrayList<Integer>();
	
	public RecourseCost(double overallRecourseCost,
			double numberOfDifferentRecourseActions2) {
		this.recourseCost = overallRecourseCost;
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions2;
	}
	
	public RecourseCost(List<GroupOfTours> gots) {
		double cost = 0;
		for (GroupOfTours got : gots)
			cost += got.getExpectedRecourse().getRecourseCost();		
		double numberOfDifferentRecourseActionsTemp = 0;
		for (GroupOfTours got : gots)
			numberOfDifferentRecourseActionsTemp += got.getExpectedRecourse().getNumberOfDifferentRecourseActions();
		numberOfDifferentRecourseActionsTemp = numberOfDifferentRecourseActionsTemp / gots.size();
		recourseCost = cost;
		numberOfDifferentRecourseActions =  numberOfDifferentRecourseActionsTemp;		
	}
	
	public double getRecourseCost() {
		return recourseCost;
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
