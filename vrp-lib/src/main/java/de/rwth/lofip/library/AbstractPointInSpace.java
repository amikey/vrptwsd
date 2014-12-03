package de.rwth.lofip.library;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Abstract basic class which represents all items which can be placed on some
 * area, e.g. customers or depots.
 * 
 * <p>Java class for abstractPointInSpace complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractPointInSpace">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xCoordinate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="yCoordinate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPointInSpace", propOrder = {
    "xCoordinate",
    "yCoordinate"
})
@XmlSeeAlso({
    Customer.class,
    Depot.class
})
public abstract class AbstractPointInSpace {

	// @XmlElement(required = true)
	private double xCoordinate;
	private double yCoordinate;
	
	@XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

	/**
	 * Constructors
	 */
	public AbstractPointInSpace() {
		super();
	}

	public AbstractPointInSpace(double x, double y) {
		xCoordinate = x;
		yCoordinate = y;
		this.setId();
	}

	/**
	 * END Constructors
	 */

	/**
	 * Getters and Setters
	 */
	

	public double getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
		
		if(this.yCoordinate != 0)
			setId();
		
	}

	public double getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(double yCoordinate) {
		
		this.yCoordinate = yCoordinate;
		
		if(this.xCoordinate != 0)
			setId();	
	}
	
	   /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }
    public void setId() {
    	this.id = "PS"+this.toString();
    }
    
	/**
	 * Getters and Setters
	 */

	@Override
	public String toString() {
		return "x: " + xCoordinate + " y: " + yCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoordinate);
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
		AbstractPointInSpace other = (AbstractPointInSpace) obj;
		if (Double.doubleToLongBits(xCoordinate) != Double
				.doubleToLongBits(other.xCoordinate))
			return false;
		if (Double.doubleToLongBits(yCoordinate) != Double
				.doubleToLongBits(other.yCoordinate))
			return false;
		return true;
	}

}
