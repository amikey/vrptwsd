package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;

public class RessourceExctensionFunction {
	
	private double duration = 0;
	private double latestArrivalTime = Double.MAX_VALUE;
	private double earliestLeavingTime = 0;
	private boolean isFirstCustomerToBeAdded = true;
	
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public double getLatestArrivalTime() {
		return latestArrivalTime;
	}
	public void setLatestArrivalTime(double latestArrivalTime) {
		this.latestArrivalTime = latestArrivalTime;
	}
	
	public double getEarliestLeavingTime() {
		return earliestLeavingTime;
	}
	public void setEarliestLeavingTime(double earliestLeavingTime) {
		this.earliestLeavingTime = earliestLeavingTime;
	}
	public void print() {
		System.out.println("Ref1: (" + duration + "," + latestArrivalTime + "," + earliestLeavingTime + ")");
	}
	
	public void reset() {
		duration = 0;
		latestArrivalTime = Double.MAX_VALUE;
		earliestLeavingTime = 0;
		isFirstCustomerToBeAdded = true;
	}
	
	public void updateWithCustomer(AbstractPointInSpace previousPoint, Customer newCustomer) {			
		//calculate d_{roh_1,roh_2} (= timeBetweenOldSegmentAndNewCustomerInSegment)
		double timeBetweenOldSegmentAndNewCustomerInSegment;
		if (isFirstCustomerToBeAdded()) {
			timeBetweenOldSegmentAndNewCustomerInSegment = 0;
			isFirstCustomerToBeAdded = false;
		} else 
			timeBetweenOldSegmentAndNewCustomerInSegment = new Edge(previousPoint, newCustomer).getLength();
		
		//update duration
		double durationOldSegment = duration;
		double newDuration = durationOldSegment + timeBetweenOldSegmentAndNewCustomerInSegment + newCustomer.getServiceTime();  
		duration = newDuration;
		
		//update latest arrival time
			double latOldSegment2 = latestArrivalTime;			
		
			//calculate LAT_{\roh_2} - d_{\roh_1\roh_2} - D_\roh_1
			double latNewCustomer = newCustomer.getTimeWindowClose() - timeBetweenOldSegmentAndNewCustomerInSegment - durationOldSegment;
		
			double newLat = Math.min(latOldSegment2, latNewCustomer);
			latestArrivalTime = newLat;					
		
		//update earliest departure time
			double edtOldSegment2 = earliestLeavingTime;
		
			//calculate EDT_{roh_1} + d_{\roh_1,\roh2} + D_\roh_2 
			double edtNewCustomer = edtOldSegment2 + timeBetweenOldSegmentAndNewCustomerInSegment + newCustomer.getServiceTime();
			
			double newEdt = Math.max(edtNewCustomer, newCustomer.getTimeWindowOpen());
			earliestLeavingTime = newEdt;
	}
	private boolean isFirstCustomerToBeAdded() {
		return isFirstCustomerToBeAdded;
	}

	
}
