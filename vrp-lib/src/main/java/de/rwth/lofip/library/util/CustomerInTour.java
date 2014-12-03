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

/**
 * Wrapper class which keeps the associated information about a customer in the
 * tour. Make sure to update all information when inserting, removing or
 * changing a customer.<br>
 * <br>
 * Extended Version: With a variable "isFixedCustomer", to mark the customer in
 * the tour as fixed (= true), if he should not be changed in the solution.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * @author Olga Bock
 * <p>Java class for customerInTour complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerInTour">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arrivalTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="customer" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *         &lt;element name="nextCustomerInTour" type="{http://library.lofip.rwth.de}customerInTour" minOccurs="0"/>
 *         &lt;element name="nextVertex" type="{http://library.lofip.rwth.de}abstractPointInSpace" minOccurs="0"/>
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="previousCustomerInTour" type="{http://library.lofip.rwth.de}customerInTour" minOccurs="0"/>
 *         &lt;element name="previousVertex" type="{http://library.lofip.rwth.de}abstractPointInSpace" minOccurs="0"/>
 *         &lt;element name="tour" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerInTour", propOrder = {
    "arrivalTime",
    "customer",
    "nextCustomer",
    "nextVertex",
    "position",
    "previousCustomer",
    "previousVertex",
    "tour",
    "isFixedCustomer",
//    "incomingEdge",
//    "outgoingEdge"
    
})
public class CustomerInTour implements Cloneable {
	
	@XmlElement(required = false, type = Object.class)
    @XmlIDREF
	private Customer customer;
	
	@XmlElement(required = false, type = Object.class)
    @XmlIDREF
//    @XmlSchemaType(name = "IDREF")
	private Tour tour;

	private AbstractPointInSpace previousVertex;
	
	@XmlElement(required = false, type = Object.class)
    @XmlIDREF
//    @XmlSchemaType(name = "IDREF")
	private CustomerInTour previousCustomer;

	private AbstractPointInSpace nextVertex;
	@XmlElement(required = false, type = Object.class)
    @XmlIDREF
//    @XmlSchemaType(name = "IDREF")
	private CustomerInTour nextCustomer;
	@XmlTransient
	private Edge incomingEdge = null;
	@XmlTransient
	private Edge outgoingEdge = null;

	private double arrivalTime;
	private int position;
	private boolean isFixedCustomer = false; // OB- neue Variable
	
	@XmlTransient
	private int insertedInIteration;
	@XmlTransient
	private String insertionHeuristic;
	
	@XmlAttribute(name = "idCIT")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
  protected String idCIT;
	


	/**
	 * @return the idCIT
	 */
	public String getIdCIT() {
		return idCIT;
	}

	/**
	 * @param idCIT the idCIT to set
	 */
	public void setIdCIT(String idCIT) {
		this.idCIT = idCIT;
	}
	/**
	 * @param idCIT the idCIT to set
	 */
	public void setIdCIT(){
	
		this.idCIT= (String)("C"+customer.getCustomerNo()+"inT"+(this.tour.getId()-1));
	}

	/**
	 * @return true if customer in tour is fixed, else false
	 */
	public boolean isCustomerInTourFixed() {// OB
		return isFixedCustomer;
	}

	/**
	 * @param isFixedCustomer
	 *            the boolean to set
	 */
	public void setFixCustomerInTour(boolean isFixedCustomer) {// OB
		this.isFixedCustomer = isFixedCustomer;
	}

	/**
	 * Create a new customer with the given {@link Tour}.
	 * 
	 * @param tour
	 */
	public CustomerInTour(Tour tour) {
		this.tour = tour;
	}

	public Tour getTour() {
		return tour;

	}
	/**
	 * @param tour the tour to set
	 */
	public void setTour(Tour tour) {
		this.tour = tour;
		if(customer!=null) this.setIdCIT();
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
		if(tour!=null) this.setIdCIT();
	}

	public AbstractPointInSpace getPreviousVertex() {
		return previousVertex;
	}

	public void setPreviousVertex(AbstractPointInSpace previousVertex) {
		this.previousVertex = previousVertex;
		// clear the incoming edge and have it lazily recalculated
		incomingEdge = null;
	}

	public CustomerInTour getPreviousCustomerInTour() {
		return previousCustomer;
	}

	public void setPreviousCustomerInTour(CustomerInTour previousCustomerInTour) {
		this.previousCustomer = previousCustomerInTour;
	}

	public CustomerInTour getNextCustomerInTour() {
		return nextCustomer;
	}

	public void setNextCustomerInTour(CustomerInTour nextCustomerInTour) {
		this.nextCustomer = nextCustomerInTour;
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

	/**
	 * This is automatically calculated from the arrival of the vehicle plus the
	 * service time.
	 * 
	 * @return
	 */
	public double getEarliestLeavingTime() {
		return getEarliestLeavingTimeIfArrivalIsAt(arrivalTime);
	}

	public double getEarliestLeavingTimeIfArrivalIsAt(double arrivalTime) {
		return Math.max(arrivalTime, customer.getTimeWindowOpen())
				+ customer.getServiceTime();
	}

	/**
	 * Get the position of the customer within the tour. The first position is
	 * 0.
	 * 
	 * @return
	 */
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

	/**
	 * Get the probability that this customer will have exactly this demand
	 * 
	 * @param demand
	 * @return
	 */
	public BigDecimal getDemandProbability(int demand) {
		return customer.getDemandProbability(demand);
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
		cloned.setFixCustomerInTour(isFixedCustomer);
		return cloned;
	}

}
