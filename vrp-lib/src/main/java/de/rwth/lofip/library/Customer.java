package de.rwth.lofip.library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.math3.distribution.PoissonDistribution;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.rwth.lofip.library.solver.util.CustomerWithCost;

/**
 * A {@code Customer} is the central representation of a customer within a VRP.
 * It has the basic properties like demand, time windows and service time.
 * 
 * <p>
 * Java class for customer complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="customer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://library.lofip.rwth.de}abstractPointInSpace">
 *       &lt;sequence>
 *         &lt;element name="customerNo" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="demand" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="serviceTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="similarCustomers" type="{http://library.lofip.rwth.de}customerWithCost" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timeWindowClose" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="timeWindowOpen" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * * TODO: this needs to be split up into a separate "main" customer, containing
 * all properties which are common to ALL customers and subclasses, having the
 * special features (like probabilistic demand).
 * 
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customer", propOrder = { 
		"customerNo", 
		"demand",
		"serviceTime", 
		"similarCustomers", 
		"timeWindowClose", 
		"timeWindowOpen"

})
public class Customer extends AbstractPointInSpace implements Cloneable {

	protected long customerNo;
	protected long demand;
	protected double serviceTime;

	@XmlElement(nillable = true, required = false)
	private List<CustomerWithCost> similarCustomers = new ArrayList<CustomerWithCost>();
	protected double timeWindowClose;
	protected double timeWindowOpen;
	
//	@XmlAttribute(name = "idC")
//	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
//	@XmlID
//	protected String idC;

	@XmlTransient
	private LoadingCache<Integer, BigDecimal> probabilityCache = CacheBuilder
			.newBuilder().build(new CacheLoader<Integer, BigDecimal>() {
				public BigDecimal load(Integer key) throws Exception {

					return getProbability(key);
				}
			});

	private void setProbabilityCache(LoadingCache<Integer, BigDecimal> cache) {
		probabilityCache = cache;
	}

	/**
	 * Returns the probability that customer has {@code demand}
	 */
	private BigDecimal getProbability(int demand) {
		return new BigDecimal(
				new PoissonDistribution(this.demand).probability(demand));
	}

	public Customer() {
		super();
	}

	/**
	 * Get the probability that the demand is exactly the parameter
	 * {@code demand}. This is interesting when it comes to stochastic demands.
	 * 
	 * @param demand
	 * @return
	 */
	public BigDecimal getDemandProbability(int demand) {
		return probabilityCache.getUnchecked(demand);
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
//		this.idC = (String)("C"+customerNo);
		this.setId();
	}

	public long getDemand() {
		return demand;
	}

	public void setDemand(long demand) {
		this.demand = demand;
	}

	public double getTimeWindowOpen() {
		return timeWindowOpen;
	}

	public void setTimeWindowOpen(double timeWindowOpen) {
		this.timeWindowOpen = timeWindowOpen;
	}

	public double getTimeWindowClose() {
		return timeWindowClose;
	}

	public void setTimeWindowClose(double timeWindowClose) {
		this.timeWindowClose = timeWindowClose;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
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

	// what does this method do?
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

	// compares this object with some other object.
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
		c.setSimilarCustomers(similarCustomers);
		c.setProbabilityCache(probabilityCache);
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

}
