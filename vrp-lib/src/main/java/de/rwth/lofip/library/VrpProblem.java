package de.rwth.lofip.library;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.rwth.lofip.library.util.CustomerInTour;

/**
 * An instance of this class collects all the information which is relevant for
 * describing an instance of a Vehicle Routing Problem (VRP).
 * 
 * <p>Java class for vrpProblem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vrpProblem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customers" type="{http://library.lofip.rwth.de}customer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="depots" type="{http://library.lofip.rwth.de}depot" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="vehicles" type="{http://library.lofip.rwth.de}vehicle" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vrpProblem", propOrder = {
    "customers",
    "depots",
    "description",
    "maxTime",
    "vehicles"
})
public class VrpProblem implements Cloneable {


   
	/**
	 * protected int vehicleCount;
	 */
	private String description;

	/**
	 * The customers of this problem. It is a set because they are generally not
	 * in a specific order.
	 */
	@XmlElement(name="customer")
	@XmlElementWrapper(name="customers")
	private Set<Customer> customers;

	/**
	 * To be able to model multi depot VRPs, the depots are a set of
	 * {@link Depot}s
	 */
	@XmlElement(name="depot")
	@XmlElementWrapper(name="depots")
	private Set<Depot> depots;

	/**
	 * All the vehicles in this problem. This may later be re-modelled to have
	 * the vehicles in the depots, not in the general problem.
	 */
	@XmlElement(name="vehicle")
	@XmlElementWrapper(name="vehicles")
	private Set<Vehicle> vehicles;

	/**
	 * This time is the latest time until which the whole problem needs to be
	 * calculated. No time window must be after this time.
	 */
	
	private long maxTime = 0l;

	// Konstruktor
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
		return null;

	}

	/**
	 * Convenience method to get a random depot from the set of all depots.
	 * Especially useful when dealing with single depot VRPs.
	 * 
	 * @return
	 */
	public Depot getDepot() {
		if (depots == null || depots.isEmpty()) {
			return null;
		}
		return new ArrayList<Depot>(depots).get(0);
	}

	/**
	 * Convenience method for adding a new depot to the list of depots.
	 * 
	 * @param depot
	 */
	public void addDepot(Depot depot) {
		if (depots == null) {
			depots = new HashSet<Depot>();
		}
		depots.add(depot);
	}

	public Set<Depot> getDepots() {
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

	public int getVehicleCount() {
		if (vehicles == null) {
			return 0;
		}
		return vehicles.size();
	}

	// AB: Attention: this only works if we deal with a homogeneous fleet
	public void setVehicleCount(int vehicleAmount) {
		double vehicleCapacity = vehicles.iterator().next().getCapacity();
		Set<Vehicle> newVehicles = new HashSet<Vehicle>();
		for (int i = 0; i < vehicleAmount; i++) {
			newVehicles.add(new Vehicle(i, vehicleCapacity));
		}
		this.setVehicles(newVehicles);
	}

	public void setVehicleCapacity(double capacity) {
		for (Vehicle v : vehicles)
			v.setCapacity(capacity);
	}

	/**
	 * Getter and Setter ENDE
	 */

	/**
	 * Get the total demand of all customers. This is only possible in the
	 * non-stochastic case.
	 * 
	 * @return
	 */
	public long getTotalDemand() {
		long totalDemand = 0L;
		if (getCustomers() != null) {
			for (Customer c : getCustomers()) {
				totalDemand += c.getDemand();
			}
		}
		return totalDemand;
	}

	public void setCustomers(List<CustomerInTour> customers) {
		this.customers = new HashSet<Customer>();
		for (CustomerInTour cit : customers) {
			this.customers.add(cit.getCustomer());
		}
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
			s += c.getAsTupel();
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
			if ((c.getTimeWindowClose() - c.getTimeWindowOpen()) < 400)
				number++;
		return (number / customers.size());
	}

	public double getPercentageWithTightTW() {
		double number = 0;
		for (Customer c : customers)
			if ((c.getTimeWindowClose() - c.getTimeWindowOpen()) < 200)
				number++;
		return (number / customers.size());
	}

	public int getCustomerCount() {
		return customers.size();
	}

	public int getTargetVehicleNumber() {
		return getVehicleCount();
	}
    
    
}
