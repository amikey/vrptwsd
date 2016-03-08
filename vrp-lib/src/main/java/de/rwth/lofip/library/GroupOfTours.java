package de.rwth.lofip.library;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.solver.util.SimpleTourUtils;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.math.MathUtils;

/**
 * @author Andreas Braun
 */
public class GroupOfTours implements ElementWithTours, SolutionElement, Serializable {

	/****************************************************************************
     * Fields
     ***************************************************************************/
	private static final long serialVersionUID = 417229827834858665L;	
	
	protected List<Tour> tours = new ArrayList<Tour>();	
	RecourseCost expectedRecourseCost = null;
	SolutionGot parentSolution = null;
	
	public GroupOfTours(SolutionGot solutionGot) {
		parentSolution = solutionGot;
	}

	protected void resetExpectedRecourseCost() {
		expectedRecourseCost = null;
	}
	
	private void setExpectedRecourseCost(RecourseCost expectedRecourseCost2) {
		expectedRecourseCost = expectedRecourseCost2;
	}
	
	public List<Tour> getTours() {	
		return tours;
	}
	
	@Override
	public Tour getTour(int i) {
		return getTours().get(i);
	}

	@Override
	public int getNumberOfTours() {
		return getTours().size();
	}
	
	public SolutionGot getParentSolution() {
		if (parentSolution == null)
			throw new RuntimeException("ParentSolution in Got nicht gesetzt. "
					+ "Vermutlich ist dies ein Problem beim Setup des Unit-Tests");
		return parentSolution;
	}


	@Override
	public double getTotalDistanceWithCostFactor() {
		//RUNTIME_TODO: distance zwischenspeichern und nur ändern, wenn sich Touren ändern
		double distance = 0;
        for (Tour t : this.getTours()) {
            distance += t.getTotalDistanceWithCostFactor();
        }
        return distance;
    }
	

	public double getTotalDistanceOfAdditionalToursWithCostFactor() {
		double distance = 0;
		for (Tour t : getTours()) {
			if (t.isNewTourForRecourseAction())
				distance += t.getTotalDistanceWithCostFactor();
		}
		return distance;
	}
    
	@Override
    public void removeEmptyToursAndGots() {
      Set<Tour> emptyTours = new HashSet<Tour>();
      for (Tour t : tours) {
          if (t.getCustomers().size() == 0) {
              emptyTours.add(t);
          }
      }
      tours.removeAll(emptyTours);
    }
    
	public Tour identifyTour(Tour tour) {
		for (Tour t : tours)
			if (t.getId() == tour.getId())
				return t;
		throw new RuntimeException("Tour nicht gefunden");
	}
	
	public boolean isNewTourCanBeCreated() {
		return tours.size() < Parameters.getMaximalNumberOfToursInGots();
	}
	
	public void addTour(Tour t) {
    	tours.add(t);
    	t.setParentGot(this);
    	resetExpectedRecourseCost();
    }
	
	public void createNewTour(VrpProblem vrpProblem) {
		Tour t = new Tour(vrpProblem.getDepot(),
				new Vehicle(vrpProblem.getVehicles().iterator().next().getCapacity()));
		addTour(t); //recourse Cost wird schon resetted, wenn neue Tour geaddet wird
		t.setParentGot(this);
	}
	
	public void setTour(int j, Tour tour) {
		tours.set(j, tour);
		tour.setParentGot(this);
		resetExpectedRecourseCost();
	}
	
	public void insertCustomerIntoLastTour(Customer customer) {
		Tour t = getLastTour();
		t.addCustomer(customer);
		resetExpectedRecourseCost();
	}
	
    public Tour getLastTour() {
    	return tours.get(tours.size() - 1);
	}
    
    public Tour getFirstTour() {
    	return tours.get(0);
	}
	
	public boolean isHasTours() {
		if (tours.size() > 0)
			return true;
		else 
			return false;
	}
	
	public double getSimilarityValueToNewCustomer(Customer customer) {
		//use method from simRemoval oder PFI, um die Similarity zu berechnen.
		//Benutze Durchschnitt oder Maximum fÃ¼r Simval (wenn es berechnet wird wie in Lei et al)
		double maxSimVal = Double.MIN_VALUE;
		for (Tour t : tours)
			for (CustomerInTour cit : t.getCustomersInTour())
			{
				double similarity = SimilarityUtils.calculateSimilarity(cit.getCustomer(),customer);
				if (MathUtils.greaterThan(similarity, maxSimVal))
					maxSimVal = similarity;
			}
		if (tours.isEmpty()) throw new RuntimeException("keine Tour in Got. Kann keine Similarity berechnen");
		return maxSimVal;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupOfTours other = (GroupOfTours) obj;
		if (tours == null) {
			if (other.tours != null)
				return false;			
		} else {
			//RUNTIME_TODO: kann man das mit Hashing in besserer Zeit implementieren?
			for (Tour tour : tours)
				if (!other.isExists(tour))
					return false;						
		};
		return true;
	}

	private boolean isExists(Tour other) {
		for (Tour tour : tours)
			if (other.equals(tour))
				return true;
		return false;
	}

	/****************************************************************************
     * Calculation of Probabilities and Recourse Cost
     ***************************************************************************/        
   
    public RecourseCost getExpectedRecourse() {    	
    	if (expectedRecourseCost == null) {  
    		expectedRecourseCost = new RecourseCost(this);
    	}    	
		return expectedRecourseCost;
    }
    
	public void addEmptyTour() {
    	Tour tour = SimpleTourUtils.getEmptyTourWithDoubleCostFactor(getFirstTour());
    	addTour(tour);
    	tour.setParentGot(this);
	}

	@Override
    public GroupOfTours clone() {
    	GroupOfTours got = new GroupOfTours(getParentSolution());   		    	                       
        for (Tour t : tours)  {
        	Tour tour = new Tour(t);
        	got.addTour(tour);
        	tour.setParentGot(got);
        }	        
        got.setExpectedRecourseCost(expectedRecourseCost);
        return got;
    }
    
	
	public GroupOfTours cloneWithCopyOfTourAndCustomers() {
		if (parentSolution == null)
			throw new RuntimeException("ParentSolution in got, das geclont werden soll, ist null");
		GroupOfTours got = new GroupOfTours(parentSolution.clone());
		for (Tour t : tours) {
			Tour tour = t.cloneWithCopyOfCustomersAndVehicleAndSetParentGot(got);
			got.addTour(tour);			
		}        	
		//expectedRecourseCost wird immer null sein
		got.setExpectedRecourseCost(expectedRecourseCost);
		return got;
	}

	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		for (Tour tour : getTours())
			customers.addAll(tour.getCustomers());
		return customers;			
	}

	@Override
	public String getAsTupel() {
		String s = "";
	    s += "(";
		for (Tour t : getTours()) {
			s += t.getTourAsTupel();
		}
		s += ") ";	     
	    return s;
	}
	
	@Override
	public String getAsTupelWithDemand() {
		String s = "";
	    s += "(";
		for (Tour t : getTours()) {
			s += t.getTourAsTupelWithDemand();
		}
		s += ") ";	     
	    return s;
	}

	public Tour getTourThatIsEqualTo(Tour tour1) {
		for (Tour t : tours) {
			if (t.equals(tour1))
				return t; 
		}
		throw new RuntimeException("No tour found. Should not happen");
	}

	public void print() {
		System.out.println(getAsTupel());
	}

	@Override
	public String getUseOfCapacityInTours() {
		String s = "(";                	
	        for (Tour t : getTours()) {
	            s += t.getUseOfCapacity();
	        }
	        s += ") ";              
        return s;
	}

	public void setSolution(SolutionGot newSolution) {
		parentSolution = newSolution;
	}

	public Customer getCustomerWithNo(long customerNo) {
		for (Customer c : getCustomers()) 
			if (c.getCustomerNo() == customerNo)
				return c;
		throw new RuntimeException("Customer not found in getCustomerWithNo");
	}

	public boolean isHasEmptyToursThatAreNotForRecourse() {
		boolean hasEmptyTours = false;
		for (Tour t : getTours())
			if (t.isTourEmptyAndNotForRecourse()) {
				hasEmptyTours = true;
				break;
			}
		return hasEmptyTours;
	}
	
	//DESIGN_TODO: Oberklasse ElementWithTours für Solution und GroupOfTours löschen und jeweils separat behandeln.
	public void removeEmptyGots() {
		//nothing to do here; exists only for Solution
	}

	@Override
	public double getTotalDistanceWithCostFactorAndRecourse() {
		throw new RuntimeException("Sollte nicht benutzt werden. Nicht implementiert.");
	}

	@Override
	public double getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse() {
		throw new RuntimeException("Sollte nicht benutzt werden. Nicht implementiert.");
	}

}
