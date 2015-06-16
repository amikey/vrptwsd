package de.rwth.lofip.library.interfaces;

import java.util.List;

import de.rwth.lofip.library.Tour;

public interface ElementWithTours {

	public Tour getTour(int i);
	
	public int getNumberOfTours();
	
	public double getTotalDistanceWithCostFactor();
	
	public void removeEmptyToursAndGots();
	
	public List<Tour> getTours();

	public String getAsTupel();

	public ElementWithTours clone();

	public String getAsTupelWithDemand();

	public String getUseOfCapacityInTours();
}
