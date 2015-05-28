package de.rwth.lofip.library.util;

import java.util.List;

import de.rwth.lofip.library.GroupOfTours;

public class RecourseCost {

	double recourseCost;
	double numberOfDifferentRecourseActions;
	
	public RecourseCost(double overallRecourseCost,
			double numberOfDifferentRecourseActions2) {
		this.recourseCost = overallRecourseCost;
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions2;
	}
	
	public RecourseCost(List<GroupOfTours> gots) {
		double cost = 0;
		for (GroupOfTours got : gots)
			cost += got.getExpectedRecourseCost().getRecourseCost();		
		double numberOfDifferentRecourseActionsTemp = 0;
		for (GroupOfTours got : gots)
			numberOfDifferentRecourseActionsTemp += got.getExpectedRecourseCost().getNumberOfDifferentRecourseActions();
		numberOfDifferentRecourseActionsTemp = numberOfDifferentRecourseActionsTemp / gots.size();
		recourseCost = cost;
		numberOfDifferentRecourseActions =  numberOfDifferentRecourseActionsTemp;		
	}
	
	public double getRecourseCost() {
		return recourseCost;
	}
	
	public void setRecourseCost(double recourseCost) {
		this.recourseCost = recourseCost;
	}
	
	public double getNumberOfDifferentRecourseActions() {
		return numberOfDifferentRecourseActions;
	}
	
	public void setNumberOfDifferentRecourseActions(
			int numberOfDifferentRecourseActions) {
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions;
	}

	public void print() {
		System.out.println("Cost: " + recourseCost + "; NumberOfDifferentRecourseActions: " + numberOfDifferentRecourseActions);
		
	}
		
}
