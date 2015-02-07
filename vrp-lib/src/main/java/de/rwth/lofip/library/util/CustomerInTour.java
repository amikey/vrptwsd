package de.rwth.lofip.library.util;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;

/**
 * Wrapper class which keeps the associated information about a customer in the
 * tour. Make sure to update all information when inserting, removing or
 * changing a customer.<br>
 * <br>
 * Extended Version: With a variable "isFixedCustomer", to mark the customer in
 * the tour as fixed (= true), if he should not be changed in the solution.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
*/

public class CustomerInTour implements Cloneable, SolutionElement {

	private Customer customer;
	private Tour tour;
	private AbstractPointInSpace previousVertex;
	private AbstractPointInSpace nextVertex;
	private Edge incomingEdge = null;
	private Edge outgoingEdge = null;
	private double arrivalTime;
	private int position; 		//first position in tour is 0
	private int insertedInIteration;
	private String insertionHeuristic;
		
	public CustomerInTour(Tour tour) {
		this.tour = tour;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public Edge getIncomingEdge() {
		if (incomingEdge == null && previousVertex != null) {
			incomingEdge = new Edge(previousVertex, customer);
		}
		if (incomingEdge == null) {
			throw new RuntimeException(
					"Invalid access: trying to access an incoming edge although there is no previous vertex. The model is inconsistent.");
		}
		return incomingEdge;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Edge getOutgoingEdge() {
		if (outgoingEdge == null && nextVertex != null) {
			outgoingEdge = new Edge(customer, nextVertex);
		}
		if (outgoingEdge == null) {
			throw new RuntimeException(
					"Invalid access: trying to access an outgoing edge although there is no next vertex. The model is inconsistent.");
		}
		return outgoingEdge;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public AbstractPointInSpace getPreviousVertex() {
		return previousVertex;
	}

	public void setPreviousVertex(AbstractPointInSpace previousVertex) {
		this.previousVertex = previousVertex;
		// clear the incoming edge and have it lazily recalculated
		incomingEdge = null;
	}

	public AbstractPointInSpace getNextVertex() {
		return nextVertex;
	}

	public void setNextVertex(AbstractPointInSpace nextVertex) {
		this.nextVertex = nextVertex;
		// clear the outgoing edge and have it lazily recalculated
		outgoingEdge = null;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getEarliestLeavingTime() {
		return getEarliestLeavingTimeIfArrivalIsAt(arrivalTime);
	}

	public double getEarliestLeavingTimeIfArrivalIsAt(double arrivalTime) {
		return Math.max(arrivalTime, customer.getTimeWindowOpen())
				+ customer.getServiceTime();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getInsertedInIteration() {
		return insertedInIteration;
	}

	public void setInsertedInIteration(int insertedInIteration) {
		this.insertedInIteration = insertedInIteration;
	}

	public String getInsertionHeuristic() {
		return insertionHeuristic;
	}

	public void setInsertionHeuristic(String insertionHeuristic) {
		this.insertionHeuristic = insertionHeuristic;
	}

	protected CustomerInTour clone() {
		CustomerInTour cloned = new CustomerInTour(tour);
		cloned.setCustomer(customer);
		cloned.setArrivalTime(arrivalTime);
		cloned.setPreviousVertex(previousVertex);
		cloned.setNextVertex(nextVertex);
		cloned.setPosition(position);
		cloned.setInsertedInIteration(insertedInIteration);
		cloned.setInsertionHeuristic(insertionHeuristic);
		return cloned;
	}

	public void print() {
		System.out.println(customer.getCustomerNo());
	}

}
