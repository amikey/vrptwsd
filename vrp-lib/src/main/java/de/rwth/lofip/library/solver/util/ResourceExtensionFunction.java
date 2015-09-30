package de.rwth.lofip.library.solver.util;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.util.CustomerInTour;

public class ResourceExtensionFunction {
	
	//Eine RessourceExtensionFunction speichert die LAT, EDT und Duration
	//für das Segment, das die Kunden in dieser REF beinhaltet.
	//der Anfang der REF ist durch den ersten Kunden und das 
	//Ende der REF durch den letzten Kunden gegeben.
		
	private static int seedId = 1;
	private double duration = 0;
	private double latestArrivalTime = Double.MAX_VALUE;
	private double earliestDepartureTime = 0;
	private int demand = 0;
	//CODE_SMELL_TODO: have depot in list elementsInThisRef
	private List<Customer> customersInThisRef = new LinkedList<Customer>();	
	private int id;
	private boolean containsDepot = false;
	private Depot depot;
	
	public ResourceExtensionFunction() {
		id = seedId;
		seedId++;
		customersInThisRef.clear();
	}
	
	public ResourceExtensionFunction(double d, double e, double f, int i) {
		duration = d;
		latestArrivalTime = e;
		earliestDepartureTime = f;
		demand = i;
		customersInThisRef.clear();
		id = seedId;
		seedId++;
	}
	
	public ResourceExtensionFunction(Customer customer) {
		super();
		duration = customer.getServiceTime();
		latestArrivalTime = customer.getTimeWindowClose();
		earliestDepartureTime = customer.getTimeWindowOpen() + duration;
		demand += customer.getDemand();
		customersInThisRef.add(customer);
	}
	
	//IMPORTANT_TODO: Korrigiere EDT (keine Duration)
	public ResourceExtensionFunction(CustomerInTour customerInTour) {
		super();
		duration = customerInTour.getCustomer().getServiceTime();
		latestArrivalTime = customerInTour.getCustomer().getTimeWindowClose();
		earliestDepartureTime = customerInTour.getCustomer().getTimeWindowOpen() + duration;
		demand += customerInTour.getCustomer().getDemand();
		customersInThisRef.add(customerInTour.getCustomer());
	}

	//Copy Constructor
	public ResourceExtensionFunction(ResourceExtensionFunction ref) {
		super();
		duration = ref.getDuration();
		latestArrivalTime = ref.getLatestArrivalTime();
		earliestDepartureTime = ref.getEarliestDepartureTime();
		demand = ref.getDemand();
		List<Customer> newCustomers = new LinkedList<Customer>();
		for (Customer c : ref.getCustomers())
			newCustomers.add(c.clone());		
		setCustomers(newCustomers);		
		containsDepot = ref.isContainsDepot();
		depot = ref.getDepot();		
	}

	public ResourceExtensionFunction(Depot depot) {
		super();
		duration = depot.getServiceTime();
		latestArrivalTime = depot.getTimeWindowClose();
		earliestDepartureTime = depot.getTimeWindowOpen();
		demand = (int) depot.getDemand();
		containsDepot = true;
		this.depot = depot;
	}

	public ResourceExtensionFunction(List<Customer> customers) {
		super();
		for (Customer c : customers)
			updateWithSubsequentCustomer(c);
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
	
	public double getEarliestDepartureTime() {
		return earliestDepartureTime;
	}
	
	public void setEarliestLeavingTime(double earliestLeavingTime) {
		this.earliestDepartureTime = earliestLeavingTime;
	}
	
	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	private Depot getDepot() {
		return depot;
	}

	private boolean isContainsDepot() {
		return containsDepot;
	}
	
	public void updateWithSubsequentCustomer(CustomerInTour customerInTour) {
		updateWithSubsequentCustomer(customerInTour.getCustomer());
	}
	
	public void updateWithSubsequentCustomer(Customer newCustomer) {	
		ResourceExtensionFunction newRef = new ResourceExtensionFunction(newCustomer);
		updateWithSubsequentRef(newRef);
	}
	
	//IMPORTANT_TODO: Korrigiere EDT: D_\simga_1 statt D_\sigma_2
	public void updateWithSubsequentRef(ResourceExtensionFunction newRef) {
		//calculate d_{roh_1,roh_2} (= timeBetweenOldSegmentAndNewCustomerInSegment)
		double timeBetweenOldSegmentAndNewSegment = new Edge(getLastCustomer(), newRef.getFirstCustomer()).getLength();
		
		//update duration
		double durationOldSegment = duration;
		double newDuration = durationOldSegment + timeBetweenOldSegmentAndNewSegment + newRef.getDuration();  
		duration = newDuration;
		
		//update latest arrival time
			double latOldSegment = latestArrivalTime;			
	
			//calculate LAT_{\roh_2} - d_{\roh_1\roh_2} - D_\roh_1
			double latNewCustomer = newRef.getLatestArrivalTime() - timeBetweenOldSegmentAndNewSegment - durationOldSegment;
	
			double newLat = Math.min(latOldSegment, latNewCustomer);
			latestArrivalTime = newLat;	
			
		//update earliest departure time				
			
			//calculate EDT_{roh_1} + d_{\roh_1,\roh2} + D_\roh_2 
			double edtFromOldSegement = earliestDepartureTime + timeBetweenOldSegmentAndNewSegment + newRef.getDuration();
					
			double newEdt = Math.max(edtFromOldSegement, newRef.getEarliestDepartureTime());
			earliestDepartureTime = newEdt;
		
		//update demand
			demand += newRef.getDemand();
			
		customersInThisRef.addAll(newRef.getCustomers());			
	}
	
	public void updateWithPreceedingCustomer(CustomerInTour customerInTour) {
		updateWithPreceedingCustomer(customerInTour.getCustomer());		
	}
	
	private void updateWithPreceedingCustomer(Customer newCustomer) {
		ResourceExtensionFunction newRef = new ResourceExtensionFunction(newCustomer);
		updateWithPreceedingRef(newRef);
	}
	
	public void updateWithPreceedingRef(ResourceExtensionFunction newRef) {
		//calculate d_{roh_1,roh_2} (= timeBetweenOldSegmentAndNewCustomerInSegment)
		double timeBetweenOldSegmentAndNewSegment = new Edge(newRef.getLastCustomer(), getFirstCustomer()).getLength();
		
		//update duration
		double durationOldSegment = duration;
		double newDuration = durationOldSegment + timeBetweenOldSegmentAndNewSegment + newRef.getDuration();  
		duration = newDuration;
		
		//update latest arrival time
		//calculate LAT_{\roh_2} - d_{\roh_1\roh_2} - D_\roh_1
			double latOldSegment = latestArrivalTime - timeBetweenOldSegmentAndNewSegment - newRef.getDuration();			
		
			double newLat = Math.min(newRef.getLatestArrivalTime(), latOldSegment);
			latestArrivalTime = newLat;	
			
		//update earliest departure time				
			
			//calculate EDT_{roh_1} + d_{\roh_1,\roh2} + D_\roh_2 
			double edtFromNewSegment = newRef.getEarliestDepartureTime() + timeBetweenOldSegmentAndNewSegment + durationOldSegment;
					
			double newEdt = Math.max(edtFromNewSegment, earliestDepartureTime);
			earliestDepartureTime = newEdt;
		
		//update demand
			demand += newRef.getDemand();
		
		LinkedList<Customer> tempElements = new LinkedList<Customer>(newRef.getCustomers());
		tempElements.addAll(customersInThisRef);
		customersInThisRef = tempElements;			
	}

	public void print() {
		System.out.print("Ref: (" + duration + "," + latestArrivalTime + "," + earliestDepartureTime + ")");
		System.out.print("; Customers: ");
		for (Customer c : customersInThisRef)
			c.print();
		System.out.println("; ID: " + id);
	}

	@Override
	public ResourceExtensionFunction clone() {
		ResourceExtensionFunction ref = new ResourceExtensionFunction();
		ref.setDuration(duration);
		ref.setEarliestLeavingTime(earliestDepartureTime);
		ref.setLatestArrivalTime(latestArrivalTime);
		ref.setDemand(demand);
		//RUNTIME_TODO: wie sieht das runtime technisch aus? Ist der Befehl unten ein Problem, weil er über jeden einzelnen Eintrag in der List iteriert?
		//falls ja, sollte ich eine andere Methode wählen, als die Customer in der Ref zu speichern, nämlich
		//letzten Customer speichern und evtl. ersten Customer speichern
		List<Customer> newCustomers = new LinkedList<Customer>();
		for (Customer c : customersInThisRef)
			newCustomers.add(c);		
		ref.setCustomers(newCustomers);
		ref.setContainsDepot(containsDepot);
		ref.setDepot(depot);
		return ref;
	}
	
	private void setDepot(Depot depot2) {
		this.depot = depot2;		
	}
	
	private void setContainsDepot(boolean containsDepot2) {
		this.containsDepot = containsDepot2;
	}

	private void setCustomers(List<Customer> customers2) {
		customersInThisRef = customers2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(earliestDepartureTime);
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
		ResourceExtensionFunction other = (ResourceExtensionFunction) obj;
		if (Double.doubleToLongBits(duration) != Double
				.doubleToLongBits(other.duration))
			return false;
		if (Double.doubleToLongBits(earliestDepartureTime) != Double
				.doubleToLongBits(other.earliestDepartureTime))
			return false;
		if (Double.doubleToLongBits(latestArrivalTime) != Double
				.doubleToLongBits(other.latestArrivalTime))
			return false;
		if (demand != other.getDemand())
			return false;
		return true;
	}

	public List<Customer> getCustomers() {
		return customersInThisRef;
	}

	public void reset() {
		duration = 0;
		latestArrivalTime = Double.MAX_VALUE;
		earliestDepartureTime = 0;
		demand = 0;
		customersInThisRef.clear();
	}

	//CODE_SMELL_TODO: getCustomers umbenennen, denn kann auch Depot zurückgeben
	public AbstractPointInSpace getFirstCustomer() {
		if (customersInThisRef.isEmpty())
			if (containsDepot)
				return depot;
			else
				//CODE_SMELL_TODO: bad practice
				return null;
		else return customersInThisRef.get(0);
	}

	//CODE_SMELL_TODO: getCustomers umbenennen, denn kann auch Depot zurückgeben
	public AbstractPointInSpace getLastCustomer() {
		if (customersInThisRef.isEmpty())
			if (containsDepot)
				return depot;
			else
				//CODE_SMELL_TODO: bad practice
				return null;
		else return customersInThisRef.get(customersInThisRef.size()-1);
	}
	


	
}
