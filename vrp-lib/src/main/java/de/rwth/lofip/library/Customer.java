package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.List;

import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.util.CustomerWithCost;

public class Customer extends AbstractPointInSpace implements Cloneable{

	protected long customerNo;
	private List<CustomerWithCost> similarCustomers = new ArrayList<CustomerWithCost>();
	
	public Customer() {
		super();
	}

	public Customer(double x, double y) {
		super(x, y);
	}

	/**
	 * END Constructors
	 */

	/**
	 * Getters and Setters
	 * 
	 */
	@Override
	public void setId() {
    	this.id ="C"+customerNo+" PS"+this.toString();
    }

	public long getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(long customerNo) {
		this.customerNo = customerNo;
		this.setId();
	}
	
	public List<CustomerWithCost> getSimilarCustomers() {
		return similarCustomers;
	}

	public void setSimilarCustomers(List<CustomerWithCost> similarCustomers) {
		this.similarCustomers = similarCustomers;
	}

	/**
	 * END Getters and Setters
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (customerNo ^ (customerNo >>> 32));
		result = prime * result + (int) (demand ^ (demand >>> 32));
		long temp;
		temp = Double.doubleToLongBits(serviceTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(timeWindowClose);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(timeWindowOpen);
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
		Customer other = (Customer) obj;
		if (customerNo != other.customerNo)
			return false;
		if (demand != other.demand)
			return false;
		if (Double.doubleToLongBits(serviceTime) != Double
				.doubleToLongBits(other.serviceTime))
			return false;
		if (Double.doubleToLongBits(timeWindowClose) != Double
				.doubleToLongBits(other.timeWindowClose))
			return false;
		if (Double.doubleToLongBits(timeWindowOpen) != Double
				.doubleToLongBits(other.timeWindowOpen))
			return false;
		return true;
	}

	@Override
	public Customer clone() {
		Customer c = new Customer(this.getxCoordinate(), this.getyCoordinate());
		c.setCustomerNo(customerNo);
		c.setDemand(demand);
		c.setServiceTime(serviceTime);
		c.setTimeWindowOpen(timeWindowOpen);
		c.setTimeWindowClose(timeWindowClose);
		return c;
	}

	
	public String getAsString() {
		return "Customer: " + customerNo + " " + demand + " " + serviceTime;
	}

	public String getAsTupel() {
		return "(" + customerNo + ", " + demand + ", " + serviceTime + ")";
	}
	
	public String getCompleteDescription() {
		return "" + customerNo + " " + getxCoordinate() + " " + getyCoordinate() + " " + demand + " " + timeWindowOpen + " " + timeWindowClose + " " + serviceTime;
	}

	public void print() {
		System.out.print(customerNo);
	}
	
	@Override
	public String toString() {
		return "" + customerNo;
	}

}
