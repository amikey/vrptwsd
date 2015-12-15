package de.rwth.lofip.library;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.math.MathUtils;

public class VrpProblem implements Cloneable {

 	protected int vehicleCount;	
	private String description;
	private Set<Customer> customers; //implemented as a set because customers are not in a specific order
	private Set<Depot> depots; //implemented as a set to be able to model multi depot VRPs
	private Set<Vehicle> vehicles;
	private long maxTime = 0l; //closing time window of depot
	private double originalCapacity;

	public VrpProblem() {
		customers = new HashSet<Customer>();
	}

	/**
	 * Getter und Setter
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public void addCustomer(Customer customer) {
		if (customers == null) {
			customers = new HashSet<Customer>();
		}
		customers.add(customer);
	}

	public Customer getCustomerWithCustomerNo(long customerNo) {
		for (Customer c : this.customers) {
			if (c.getCustomerNo() == customerNo)
				return c;
		}
		throw new RuntimeException("Customer with customerNr " + customerNo + " not found in VrpProblem");
	}

	public int getCustomerCount() {
		return customers.size();
	}
	
	public void setCustomers(List<CustomerInTour> customers) {
		this.customers = new HashSet<Customer>();
		for (CustomerInTour cit : customers) {
			this.customers.add(cit.getCustomer());
		}
	}
	
	public long getTotalDemandOfAllCustomers() {
		if (getCustomers() == null)
			throw new RuntimeException("Keine Kunden in VrpProblem. Sollte nicht passieren");
		long totalDemand = 0L;		
		for (Customer c : getCustomers()) {
			totalDemand += c.getDemand();
		}		
		return totalDemand;
	}
	
	/**
	 * Convenience method to get a random depot from the set of all depots.
	 * Especially useful when dealing with single depot VRPs.
	 */
	public Depot getDepot() {
		if (depots == null || depots.isEmpty()) {
			throw new RuntimeException("Fehler in Methode getDepot: Es gibt keine Depots");
		}
		return new ArrayList<Depot>(depots).get(0);
	}

	public void addDepot(Depot depot) {
		if (depots == null) {
			depots = new HashSet<Depot>();
		}
		depots.add(depot);
	}

	public Set<Depot> getDepots() {
		if (depots == null || depots.isEmpty()) {
			throw new RuntimeException("Fehler in Methode getDepots: Es gibt keine Depots");
		}
		return depots;
	}

	public void setDepots(Set<Depot> depots) {
		this.depots = depots;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public int getMinimalNumberOfVehiclesWrtDemand() {
		return (int) Math.ceil((double) getTotalDemandOfAllCustomers() / vehicles.iterator().next().getCapacity());		
	}

	public void setVehicleCapacity(double capacity) {
		for (Vehicle v : vehicles)
			v.setCapacity(capacity);
	}   

	/**
	 * Getter and Setter ENDE
	 */


	/**
	 * Because a VRP is not immutable, we need to pass an in-depth-copy. This is
	 * why this .clone() method exists.
	 */
	@Override
	public VrpProblem clone() {
		VrpProblem vrp = new VrpProblem();
		vrp.setDescription(description);
		vrp.setDepots(depots);
		for (Customer c : customers) {
			vrp.addCustomer(c.clone());
		}
		Set<Vehicle> vSet = new HashSet<Vehicle>();
		for (Vehicle v : vehicles) {
			vSet.add(v.clone());
		}
		vrp.setVehicles(vSet);
		vrp.setMaxTime(maxTime);
		return vrp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customers == null) ? 0 : customers.hashCode());
		result = prime * result + ((depots == null) ? 0 : depots.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (maxTime ^ (maxTime >>> 32));
		result = prime * result
				+ ((vehicles == null) ? 0 : vehicles.hashCode());
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
		VrpProblem other = (VrpProblem) obj;
		if (customers == null) {
			if (other.customers != null)
				return false;
		} else if (!customers.equals(other.customers))
			return false;
		if (depots == null) {
			if (other.depots != null)
				return false;
		} else if (!depots.equals(other.depots))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (maxTime != other.maxTime)
			return false;
		if (vehicles == null) {
			if (other.vehicles != null)
				return false;
		} else if (!vehicles.equals(other.vehicles))
			return false;
		return true;
	}

	public String print() {
		String s = description + "; Customers: ";
		for (Customer c : this.customers)
			s += c.toString();
		s += "; Depots: ";
		for (Depot d : this.getDepots())
			s += d.toString();
		for (Vehicle v : this.vehicles)
			s += v.getAsTupel();
		s += maxTime;
		return s;
	}

	public double getPercentageWithTW() {
		double number = 0;
		for (Customer c : customers)
			if (MathUtils.lessThan(c.getTimeWindowClose() - c.getTimeWindowOpen(), 400))
				number++;
		return (number / customers.size());
	}

	public double getPercentageWithTightTW() {
		double number = 0;
		for (Customer c : customers)
			if (MathUtils.lessThan(c.getTimeWindowClose() - c.getTimeWindowOpen(), 200))
				number++;
		return (number / customers.size());
	}

	public Vehicle getNewVehicle() {
		return vehicles.iterator().next().clone();
	}

	public double getOriginalCapacity() {
		return originalCapacity;
	}

	public void setOriginalCapacity(double vehicleCapacity) {
		originalCapacity = vehicleCapacity;
	}
    
}
