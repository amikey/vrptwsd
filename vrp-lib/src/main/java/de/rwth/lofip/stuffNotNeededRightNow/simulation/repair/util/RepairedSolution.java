package de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;

/**
 * This class is meant to serve as a "meta class" for repaired solutions, providing more
 * information than just the actual solution. The calling system (GUI, CLI,
 * ...) can then decide what to do with that information.
 * 
 * @author Andreas Braun <braun@or.rwth-aachen.de>
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "repairedSolution", propOrder = {
	    "event",
	    "existsProblem",
	    "newSolution",
	    "oldSolution",
	    "additionalDistance",
	    "additionalCost",
	    "delayAtDepot",
	    "numberOfAffectedTours",
	    "wereTimeWindowsViolated",
	    "sumOfTimeDeviationAtCustomers",
	    "numberOfAffectedCustomers",
	    "wasNewTourCreated",
	    "CostRepairVsCostNewTour",
	    "timeNeededForCalculationOfSolution"
  
	})

public class RepairedSolution {

	// event that has caused repair
	@XmlElement
	private Event event;
	@XmlElement
	private boolean existsProblem = false;
	@XmlElement
	
	private Solution newSolution;
	@XmlElement
	private Solution oldSolution;
	@XmlElement
	
	private double additionalDistance = 0;
	private double additionalCost = 0;
	// TODO: ist delayAtDepot wirklich eine interessante Gr??e?
	// TODO: needs to be calculated
	@XmlElement
	private double delayAtDepot = 0;
	// TODO: numberOfAffectedTours is not calculated at the moment
	@XmlElement
	//TODO: numberOfAffectedTours is not calculated at the moment
	private int numberOfAffectedTours = 0;

	// TODO: this variable is not set at the moment
	@XmlElement
	
	//TODO: this variable is not set at the moment
	private boolean wereTimeWindowsViolated = false;
	@XmlElement
	private double sumOfTimeDeviationAtCustomers = 0;
	@XmlElement
	private int numberOfAffectedCustomers = 0;
	@XmlElement
	
	private boolean wasNewTourCreated = false;
	// this variable compares the costs that were created when using the repair
	// method as opposed to the costs that apply when creating a new tour
	// TODO: calculation needs to be implemented
	@XmlElement
	//this variable compares the costs that were created when using the repair method as opposed to the costs that apply when creating a new tour
	//TODO: calculation needs to be implemented
	private double CostRepairVsCostNewTour = 0;
	@XmlElement
	
	private long timeNeededForCalculationOfSolution = 0;
	
	
	public RepairedSolution() {}
	
	
	//Constructor for solution that was not modified
	public RepairedSolution(Solution solution, Event event, long timeNeededForCalculationOfSolution) {
		this.oldSolution = solution;
		this.newSolution = solution;
		this.setEvent(event); 
		this.timeNeededForCalculationOfSolution = timeNeededForCalculationOfSolution;
	}
	
	//Constructor for solution that was repaired
	public RepairedSolution(Solution oldSolution, Solution newSolution, Event event, long timeNeededForCalculationOfSolution) {
		this.oldSolution = oldSolution;
		this.newSolution = newSolution;
		this.setEvent(event); 
		this.existsProblem = true;
		setWasNewTourCreated();
		System.out.println("wurde neue Tour er�ffnet?: " + wasNewTourCreated);
		System.out.println("Neue L�sung \n" + newSolution.getSolutionAsStringWithCustomer());
		System.out.println("Alte L�sung: \n" + oldSolution.getSolutionAsStringWithCustomer());
		calculateAdditionalDistance();
		calculateAdditionalCost();
		//calculateDelayAtDepot();
		//wereTimeWindowsViolated is always false (at the moment)
		calculateSumOfTimeDeviationAtCustomers();
		calculateNumberOfAffectedTours();
		//calculateCostRepairVsCostNewTour();
		this.timeNeededForCalculationOfSolution = timeNeededForCalculationOfSolution;
	}

	//Getter and Setter
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean isExistsProblem() {
		return existsProblem;
	}

	public void setExistsProblem(boolean existsProblem) {
		this.existsProblem = existsProblem;
	}
	
	public Solution getOldSolution() {
		return this.oldSolution;
	}
	
	public void setOldSolution(Solution oldSolution) {
		this.oldSolution = oldSolution;
	}
	
	public Solution getNewSolution() {
		return this.newSolution;
	}
	
	public void setNewSolution(Solution newSolution) {
		this.newSolution = newSolution;
	}

	public double getAdditionalDistance() {
		return additionalDistance;
	}

	public void setAdditionalDistance(double additionalDistance) {
		this.additionalDistance = additionalDistance;
	}

	public double getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}

	public double getDelayAtDepot() {
		return delayAtDepot;
	}

	public void setDelayAtDepot(double delayAtDepot) {
		this.delayAtDepot = delayAtDepot;
	}

	public int getNumberOfAffectedTours() {
		return this.numberOfAffectedTours;
	}

	public void setNumberOfAffectedTours(int numberOfAffectedTours) {
		this.numberOfAffectedTours = numberOfAffectedTours;
	}

	public boolean isWereTimeWindowsViolated() {
		return wereTimeWindowsViolated;
	}

	public void setWereTimeWindowsViolated(boolean wereTimeWindowsViolated) {
		this.wereTimeWindowsViolated = wereTimeWindowsViolated;
	}

	public double getSumOfTimeDeviationAtCustomers() {
		return sumOfTimeDeviationAtCustomers;
	}

	public void setSumOfTimeDeviationAtCustomers(
			double sumOfTimeDeviationAtCustomers) {
		this.sumOfTimeDeviationAtCustomers = sumOfTimeDeviationAtCustomers;
	}

	public int getNumberOfAffectedCustomers() {
		return numberOfAffectedCustomers;
	}

	public void setNumberOfAffectedCustomers(int numberOfAffectedCustomers) {
		this.numberOfAffectedCustomers = numberOfAffectedCustomers;
	}

	public boolean isWasNewTourCreated() {
		return wasNewTourCreated;
	}

	public void setWasNewTourCreated(boolean wasNewTourCreated) {
		this.wasNewTourCreated = wasNewTourCreated;
	}

	public double getCostRepairVsCostNewTour() {
		return CostRepairVsCostNewTour;
	}

	public void setCostRepairVsCostNewTour(double costRepairVsCostNewTour) {
		CostRepairVsCostNewTour = costRepairVsCostNewTour;
	}

	public long getTimeNeededForCalculationOfSolution() {
		return timeNeededForCalculationOfSolution;
	}

	public void setTimeNeededForCalculationOfSolution(
			long timeNeededForCalculationOfSolution) {
		this.timeNeededForCalculationOfSolution = timeNeededForCalculationOfSolution;
	}
	/**
	 * End getter and setter
	 */
	
	
	//convenience method for printing
	public String getRepairedSolutionAsString()
	{
		String s = String.format("AdditionalDistance: %.3f, TimeDeviation: %.3f, NumberOfAffectedCustomers: %d, NumberOfAffectedTours %d\n", 
				getAdditionalDistance(), getSumOfTimeDeviationAtCustomers(), getNumberOfAffectedCustomers(), getNumberOfAffectedTours());
		s += getOldSolution().getSolutionAsString();
		s += getNewSolution().getSolutionAsString();
		return s;
	}
	
	private void calculateAdditionalDistance()
	{
		additionalDistance = newSolution.getTotalDistanceOfAllTours() - oldSolution.getTotalDistanceOfAllTours();
	}

	private void calculateAdditionalCost()
	{
		System.out.println("Kosten alte L�sung: " + oldSolution.getCostInEuro());
		System.out.println("Kosten neue L�sung: " + newSolution.getCostInEuro() + "\n");
		additionalCost = newSolution.getCostInEuro() - oldSolution.getCostInEuro();
	}
	
	//calculates the number of affected tours
	//note: if a new tour was created in repair method this shows only the number of affected _old_ tours.
	//since the new tour is newly created, is does not really belong to affected tours, does it?
	private void calculateNumberOfAffectedTours()
	{
		for (Tour tOld : oldSolution.getTours()) 
		{
			boolean IsOldTourStillExists = false;
			for (Tour tNew : newSolution.getTours())
				if (tOld.getId() == tNew.getId())
				{
					IsOldTourStillExists = true;
					if (tOld.getTotalDistance() != tNew.getTotalDistance())
						this.numberOfAffectedTours += 1;					
				}
			//check whether tour was deleted -> then it is affected as well
			if (!IsOldTourStillExists)
				this.numberOfAffectedTours += 1;
		}
	}
	
	//Note: this is a naive implementation because when at some customer the vehicle arrives earlier than before it is not checked whether it can simply wait
	//TODO: implement above mechanism (should not be to hard. one should implement a method areTimeWindowsViolatedInTour in class tour to check this)
	//Note: maybe that has also to be checked when vehicle arrives later?
	private void calculateSumOfTimeDeviationAtCustomers()
	{
		for (CustomerInTour citOld : oldSolution.getCustomersInTours())
			for (CustomerInTour citNew : newSolution.getCustomersInTours())
				if (citOld.getCustomer().getCustomerNo() == citNew.getCustomer().getCustomerNo())
				{
					//this is the easy case where vehicle arrives later than before 
					if (citOld.getArrivalTime() > citNew.getArrivalTime())
						sumOfTimeDeviationAtCustomers += (citOld.getArrivalTime() - citNew.getArrivalTime());  
					//this is the case where vehicle arrives earlier than before
					if (citNew.getArrivalTime() < citOld.getArrivalTime())
						sumOfTimeDeviationAtCustomers += (citNew.getArrivalTime() - citNew.getArrivalTime());
				}
	}
	
	private void setWasNewTourCreated()
	{
		if (newSolution.getTours().size() > oldSolution.getTours().size())
			wasNewTourCreated = true;
	}

	public String printParetoCriteria() {
		String s = "(";
		s += String.format("%.3f", additionalCost) +";";
		s += String.format("%.3f", additionalDistance) +";";
		s += numberOfAffectedTours +";";
		s += ")";
		return s;
	}
	
	//TODO: needs to implemented
//	calculateCostRepairVsCostNewTour()
//	{
//		
//	}
	
	public boolean equals(RepairedSolution rs){
		if (rs.getAdditionalCost() == this.additionalCost &&
			rs.getAdditionalDistance() == this.additionalDistance &&
			rs.getNumberOfAffectedTours() == this.numberOfAffectedTours)
			return true;
		else
			return false;
	}
	
}
