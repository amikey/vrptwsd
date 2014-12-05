package de.rwth.lofip.stuffNotNeededRightNow.simulation.analyse;

import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.DemandScenario;

/**
 * This class is the central representation of key performance indicators of the repair 
 * algorithm during a DemandScenario (for one solomon instance). 
 * It calculates some basic properties like average time for calculation, number of 
 * existing Problems within a DemandScenario or average of effected tours.
 * 
 * @author Olga Bock
 */
public class KeyPerformanceIndicators extends ComparisonOfSolutions  {
	
	public KeyPerformanceIndicators(DemandScenario demandScenario) {
		super(demandScenario);
	}
	
	public KeyPerformanceIndicators() {
		super();
	}
		
//start getters 
	
	/**
	 * @return the average time for repairing the solution after an event
	 */
	public long getAverageTimeForCalculation() {
		long sum = 0;
		for (RepairedSolution repairedSolution : this.getRepairedSolutions()){
			if (repairedSolution.isExistsProblem())
			sum += repairedSolution.getTimeNeededForCalculationOfSolution(); 
		}
		long averageTime = sum; 
		if (this.getNumberOfExistingProblems() != 0)
			averageTime = sum/this.getNumberOfExistingProblems();
		
		return averageTime ;
	}
	
	public String getAverageTimeForCalculationAsString() {	
		String s = String.format("%.3f", (double) getAverageTimeForCalculation() / 1000 / 1000);
		return s;
	}
	
	/**
	 * @return the minimum time for repairing the solution after an event
	 */
	public long getMinTimeForCalculation() {
		long min = 1000000000;
		for (RepairedSolution repairedSolution : this.getRepairedSolutions()){
			long time = repairedSolution.getTimeNeededForCalculationOfSolution();
			if (repairedSolution.isExistsProblem() && (time < min)){
			 min= time; 
			}
		}
		if (min == 1000000000) 
			min = 0;
		return min ;
	}
	
	public String getMinTimeForCalculationAsString() {	
		String s = String.format("%.3f", (double) getMinTimeForCalculation() / 1000 / 1000);
		return s;
	}
	
	/**
	 * @return the maximum time for repairing the solution after an event
	 */
	public long getMaxTimeForCalculation() {
		long max = 0;
		for (RepairedSolution repairedSolution : this.getRepairedSolutions()){
			long time = repairedSolution.getTimeNeededForCalculationOfSolution();
			if (repairedSolution.isExistsProblem() && (time > max)){
			 max= time; 
			}
		}
		return max ;
	}
	
	public String getMaxTimeForCalculationAsString() {	
		String s = String.format("%.3f", (double) getMaxTimeForCalculation() / 1000 / 1000);
		return s;
	}
	

	/**
	 * @return the average additional distance
	 */
	public double getAverageOfAdditionalDistances() {
		double sum = 0;		
		int count = 0;
		
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.getAdditionalDistance() != 0)
			{
				sum += repairedSolution.getAdditionalDistance();
				count += 1;
			}				
		}
		
		double averageDistance = 0;
		if (count != 0)
		{
			averageDistance = sum / (double) count;
		}		
		return averageDistance;
	}
	
	public String getAverageOfAdditionalDistancesAsString() {
		String averageDistance = String.format("%.3f", getAverageOfAdditionalDistances());
		return averageDistance;
	}
	
	public String getTotalAdditionalDistancesAsString() {
		String totalDistance = String.format("%.3f", getTotalAdditionalDistances());
		return totalDistance;
	}
	
	public String getTotalAdditionalCostInEuroAsString() {
		String cost = String.format("%.3f", getTotalAdditionalCostInEuro());
		return cost;
	}
	
	public String getAverageOfAdditionalCostInEuroAsString() {
		String cost = String.format("%.3f", getAverageOfAdditionalCostInEuro());
		return cost;
	}
	
	public String getAverageOfAdditionalCostInEuroPerInstanceAsString(int numberOfScenarios) {
		return String.format("%.3f",(getTotalAdditionalCostInEuro() / numberOfScenarios));
	}
	
	public double getAverageOfAdditionalCostInEuroPerInstance(int numberOfScenarios) {
		 return (getTotalAdditionalCostInEuro() / numberOfScenarios);
	}
	
	public String getAverageOfAdditionalDistancePerInstanceAsString(int numberOfScenarios) {
		return String.format("%.3f",(getTotalAdditionalDistances() / numberOfScenarios));
	}
	
	/**
	 * @return the average of additional cost InEuro
	 */
	public double getAverageOfAdditionalCostInEuro() {
		double sum = 0;		
		int count = 0;
		
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.getAdditionalCost() != 0)
			{
				sum += repairedSolution.getAdditionalCost();
				count += 1;
			}				
		}
		
		double averageCost = 0;
		if (count != 0)
		{
			averageCost = sum / (double) count;
		}		
		return averageCost;
	}
	
	/**
	 * @return the total additional cost InEuro
	 */
	public double getTotalAdditionalCostInEuro() {
		double sum = 0;		
		
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.getAdditionalCost() != 0)
			{
				sum += repairedSolution.getAdditionalCost();
			}				
		}
		return sum;
	}

	
	/**
	 * @return the total additional distance
	 */
	public double getTotalAdditionalDistances() {
		double sum = 0;		
		
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.getAdditionalDistance() != 0)
			{
				sum += repairedSolution.getAdditionalDistance();
			}				
		}
		return sum;	
	}
	
	
//	/**
//	 * @return the total additional distance
//	 */
//	public double getTotalAdditionalDistances() {
//		Solution oldSolution = this.getRepairedSolutions().get(0).getOldSolution();
//		Solution newSolution = this.getRepairedSolutions().get(this.getRepairedSolutions().size()-1).getNewSolution();
//		double additionalDistance;
//		additionalDistance = newSolution.getTotalDistance() - oldSolution.getTotalDistance();
//		return additionalDistance;
//	}
	
	
	/**
	 * @return the average utilized capacity for repaired solutions
	 */
	public double getAverageUtilizedCapacityFromRepairedSolutions(){
		return getTotalAdditionalDistances()/this.getRepairedSolutions().size();		
	}
	
//	/**
//	 * @return the average utilized capacity for repaired solutions
//	 */
//	public double getAverageUtilizedCapacityFromRepairedSolutions(){
//		double auc= 0;
//		double sum =0;
//		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
//				sum += repairedSolution.getNewSolution().totalUtilizedCapacity();
//			}			
//		auc = sum/this.getRepairedSolutions().size();
//		return auc;
//	}
	
	public String getAverageUtilizedCapacityFromRepairedSolutionsAsString() {	
		String s = String.format("%.3f", getAverageUtilizedCapacityFromRepairedSolutions());
		return s;
	}
	
	/**
	 * @return the average utilized capacity per tour for repaired solutions
	 * @throws Exception 
	 */
	public double getAverageUtilizedCapacityPerTourFromRepairedSolutions() {
		double auc= 0;
		double sum =0;
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
				sum += repairedSolution.getNewSolution().averageUtilizedCapacityPerTour();
			}			
		auc = sum/this.getRepairedSolutions().size();
		return auc;
	}
	
	public String getAverageUtilizedCapacityPerTourFromRepairedSolutionsAsString() {	
		String s = String.format("%.3f", getAverageUtilizedCapacityPerTourFromRepairedSolutions());
		return s;
	}
	

	/**
	 * @return the average of affected tours within a demand scenario
	 */
	public double getAverageOfAffectedTours() {
		double sum = 0;
		int count = 0;
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.getNumberOfAffectedTours() != 0)
			{
				sum += repairedSolution.getNumberOfAffectedTours();
				count += 1;
			}		
		}
		
		double averageOfAffectedTours = 0;
		if (count != 0)
		{
			averageOfAffectedTours = sum / count;
		}
		return averageOfAffectedTours ;
	}
	
	
	/**
	 * @return the numbersOfNewCreatedTours
	 */
	public int getNumbersOfNewCreatedTours() {
		int numbersOfNewCreatedTours=0;
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.isWasNewTourCreated()){
				numbersOfNewCreatedTours +=1; 
			}
		}
		return numbersOfNewCreatedTours;
	}


	
	/**
	 * @return the numberOfExistingProblems
	 */
	public int getNumberOfExistingProblems() {
		int numberOfExistingProblems=0;
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			if (repairedSolution.isExistsProblem()){
				numberOfExistingProblems +=1; 
			}
		}
		return numberOfExistingProblems;
	}
	
	
	/**
	 * @return the totalNumberOfUsedVehicles in this Demand Scenario.
	 * This is the number of planned tours + the numberOfNewCreatedTours
	 */
	//TODO: das funktioniert nur, wenn diese Klasse nur f�r ein Demand Scenario verwendet wird
	public int getNumberOfUsedVehiclesOneDemandScenarion() {
		return getRepairedSolutions().get((getRepairedSolutions().size()-1)).getNewSolution().getTours().size();
	}
	
	/**
	 * @return DeviationOfDemand - the standard deviation of the event demands from the original demands.
	 */
	public double getDeviationOfDemand(){
		double dod= 0;
		long sum= 0;
		for (RepairedSolution repairedSolution: this.getRepairedSolutions()){
			
			long newDemand = (long) repairedSolution.getEvent().getDemand();
			long oldDemand = (long) repairedSolution.getOldSolution().getCustomerWithId(repairedSolution.getEvent().getCustomerNo()).getCustomer().getDemand();
			
			sum += (long) Math.pow((oldDemand - newDemand)/(oldDemand), 2);
		}

		dod = Math.sqrt(sum/this.getRepairedSolutions().size());// Standartabweichung von der Prognose in % 
		return dod;
		
	}
	//end getters 
	
}
