package de.rwth.lofip.library.util;

public class RecourseCost {

	double recourseCost;
	int numberOfDifferentRecourseActions;
	
	public RecourseCost(double overallRecourseCost,
			int numberOfDifferentRecourseActions2) {
		this.recourseCost = overallRecourseCost;
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions2;
	}

	public double getRecourseCost() {
		return recourseCost;
	}
	
	public void setRecourseCost(double recourseCost) {
		this.recourseCost = recourseCost;
	}
	
	public int getNumberOfDifferentRecourseActions() {
		return numberOfDifferentRecourseActions;
	}
	
	public void setNumberOfDifferentRecourseActions(
			int numberOfDifferentRecourseActions) {
		this.numberOfDifferentRecourseActions = numberOfDifferentRecourseActions;
	}
		
}
