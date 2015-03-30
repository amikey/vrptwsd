package de.rwth.lofip.library;

import java.util.Random;


public class Vehicle implements Cloneable {
	private int vehicleId;
	private double capacity;
	private double usedCapacity = 0;

	public Vehicle(double initialCapacity) {
		vehicleId = new Random().nextInt(Integer.MAX_VALUE);
		capacity = initialCapacity;
	}
	
	public Vehicle(int vehicleId, double initialCapacity) {
		this.vehicleId = vehicleId;
		capacity = initialCapacity;
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
