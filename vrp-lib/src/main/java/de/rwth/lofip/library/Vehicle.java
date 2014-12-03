package de.rwth.lofip.library;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This class describes a vehicle with its corresponding properties. May later
 * be re-modelled to attach a vehicle to a certain depot.
 * 
 * <p>Java class for vehicle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehicle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="capacity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="usedCapacity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="vehicleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehicle", propOrder = {
    "capacity",
    "usedCapacity",
    "vehicleId"
})
public class Vehicle implements Cloneable {
	private int vehicleId;
	private double capacity;
	private double usedCapacity = 0;
	
	@XmlAttribute(name = "idV")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	protected String idV;
	

	// Konstruktor
	public Vehicle(int vehicleId, double initialCapacity) {
		this.vehicleId = vehicleId;
		capacity = initialCapacity;
		this.idV= (String)("V"+ this.getVehicleId());
	}

	/**
	 * Getter und Setter
	 */
	/**
	 * @return the idV
	 */
	public String getIdV() {
		if(idV==null)setIdV();
		return idV;
	}
	/**
	 * @param idV the idV to set
	 */
	public void setIdV(String idV) {
		this.idV = idV;
	}

	public void setIdV() {
		this.idV = (String)("V"+this.getVehicleId());
	}
	
	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public double getCapacity() {
		return capacity;
	}

	public double getUsedCapacity() {
		return usedCapacity;
	}

	public void setUsedCapacity(double usedCapacity) {
		this.usedCapacity = usedCapacity;
	}

	public void addCapacityUsage(double capacityUsage) {
		usedCapacity += capacityUsage;
	}

	public boolean isCapacityAvailable(double capacityNeeded) {
		return usedCapacity + capacityNeeded <= capacity;
	}

	public Vehicle clone() {
		Vehicle newVehicle = new Vehicle(getVehicleId(), capacity);
		newVehicle.setUsedCapacity(0L);
		return newVehicle;
	}

	public void setCapacity(double d) {
		this.capacity = d;
	}

	public String getAsTupel() {
		return "(" + vehicleId + capacity + ") ";
	}
}
