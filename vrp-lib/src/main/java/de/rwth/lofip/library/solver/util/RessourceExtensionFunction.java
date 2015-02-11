package de.rwth.lofip.library.solver.util;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.util.CustomerInTour;

public class RessourceExtensionFunction {
	
	private static int seedId = 1;
	private double duration = 0;
	private double latestArrivalTime = Double.MAX_VALUE;
	private double earliestLeavingTime = 0;
	private List<Customer> customers = new LinkedList<Customer>();	
	private int id;
	
	public RessourceExtensionFunction() {
		id = seedId;
		seedId++;
		customers.clear();
	}
	
	public RessourceExtensionFunction(double d, double e, double f) {
		duration = d;
		latestArrivalTime = e;
		earliestLeavingTime = f;
		customers.clear();
		id = seedId;
		seedId++;
	}

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
	
	public void updateWithCustomer(Customer newCustomer) {			
		//calculate d_{roh_1,roh_2} (= timeBetweenOldSegmentAndNewCustomerInSegment)
		double timeBetweenOldSegmentAndNewCustomerInSegment;
		if (isFirstCustomerToBeAddedToThisRef()) {
			timeBetweenOldSegmentAndNewCustomerInSegment = 0;			
		} else { 
			timeBetweenOldSegmentAndNewCustomerInSegment = new Edge(customers.get(customers.size()-1), newCustomer).getLength();
		}
		
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
			
			customers.add(newCustomer);
	}
	
	private boolean isFirstCustomerToBeAddedToThisRef() {
		return customers.isEmpty();
	}

	public void print() {
		System.out.print("Ref1: (" + duration + "," + latestArrivalTime + "," + earliestLeavingTime + ")");
		System.out.print("; Customers: ");
		for (Customer c : customers)
			c.print();
		System.out.println("; ID: " + id);
	}

	public void updateWithCustomer(CustomerInTour customerInTour) {
		updateWithCustomer(customerInTour.getCustomer());
	}
	
	@Override
	public RessourceExtensionFunction clone() {
		RessourceExtensionFunction ref = new RessourceExtensionFunction();
		ref.setDuration(duration);
		ref.setEarliestLeavingTime(earliestLeavingTime);
		ref.setLatestArrivalTime(latestArrivalTime);
		//TODO: wie sieht das runtime technisch aus? Ist der Befehl unten ein Problem, weil er über jeden einzelnen Eintrag in der List iteriert?
		//falls ja, sollte ich eine andere Methode wählen, als die Customer in der Ref zu speichern, nämlich
		//letzten Customer speichern und evtl. ersten Customer speichern
		List<Customer> newCustomers = new LinkedList<Customer>(customers);
		ref.setCustomers(newCustomers);
		return ref;
	}
	
	private void setCustomers(List<Customer> customers2) {
		customers = customers2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(earliestLeavingTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latestArrivalTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RessourceExtensionFunction other = (RessourceExtensionFunction) obj;
		if (Double.doubleToLongBits(duration) != Double
				.doubleToLongBits(other.duration))
			return false;
		if (Double.doubleToLongBits(earliestLeavingTime) != Double
				.doubleToLongBits(other.earliestLeavingTime))
			return false;
		if (Double.doubleToLongBits(latestArrivalTime) != Double
				.doubleToLongBits(other.latestArrivalTime))
			return false;
		return true;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void reset() {
		duration = 0;
		latestArrivalTime = Double.MAX_VALUE;
		earliestLeavingTime = 0;
		customers.clear();
	}
	
}
