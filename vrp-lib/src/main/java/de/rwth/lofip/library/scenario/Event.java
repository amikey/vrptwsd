package de.rwth.lofip.library.scenario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * An event contains
 * 
 * - a customer number - the new demand of the customer - and the point in time
 * when the demand changes
 * 
 * @author Andreas Braun
 * @author Olga Bock
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "event", propOrder = {
	    "customerNo",
	    "demand",
	    "pointInTime",

})
public class Event {

	private long customerNo;
	private long demand;
	private double pointInTime;

	// Iteration in which event occurs in a demand scenario
	@XmlTransient
	private int eventNumber;

	// Iteration on DemandScenarios (eg. DemandScenario 10 out of 30)
	@XmlTransient
	private int iterationDemandScenarios;

	// Constructor
	public Event() {
		super();
	}

	public Event(long customerNo, long demand, double pointInTime) {
		this.customerNo = customerNo;
		this.demand = demand;
		this.pointInTime = pointInTime;
	}

	public Event(long customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * Getters and Setters
	 */

	public long getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(long customerNo) {
		this.customerNo = customerNo;
	}

	public long getDemand() {
		return demand;
	}

	public void setDemand(long demand) {
		this.demand = demand;
	}

	public double getPointInTime() {
		return pointInTime;
	}

	public void setPointInTime(double pointInTime) {
		this.pointInTime = pointInTime;
	}

	/**
	 * Override the methods {@code hashCode} and {@code equals} for handling
	 * Sets.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (customerNo ^ (customerNo >>> 32));
		result = prime * result + (int) (demand ^ (demand >>> 32));
		long temp;
		temp = Double.doubleToLongBits(pointInTime);
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
		Event other = (Event) obj;
		if (customerNo != other.customerNo)
			return false;
		if (demand != other.demand)
			return false;
		if (Double.doubleToLongBits(pointInTime) != Double
				.doubleToLongBits(other.pointInTime))
			return false;
		return true;
	}

	public String getEventAsTupel() {
		return "(" + customerNo + ", " + demand + ", " + pointInTime + ")";
	}

	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

	public int getEventNumber() {
		return this.eventNumber;
	}

	public int getIterationDemandScenarios() {
		return iterationDemandScenarios;
	}

	public void setIterationDemandScenarios(int iterationDemandScenarios) {
		this.iterationDemandScenarios = iterationDemandScenarios;
	}

}
