package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RecourseCost;

public class SolutionGot implements ElementWithTours, SolutionElement, Cloneable {

    private VrpProblem vrpProblem;
    private List<GroupOfTours> gots = new ArrayList<GroupOfTours>();
  //RUNTIME_TODO: Kosten als Variable speichern
    private double distance = 0;
    
    public SolutionGot() {
    	// dieser Konstruktor wird nur f�r tempor�re Solutions verwendet, in denen das VRP nicht ben�tigt wird. 
	}
    
    public SolutionGot(VrpProblem vrpProblem) {
        this.vrpProblem = vrpProblem;
    }    

	public VrpProblem getVrpProblem() {
		//IMPORTANT_TODO: Konstruktor-Hack beheben: S.o., beim leeren Construktor wird kein vrpProblem gesetzt
		if (vrpProblem == null)
			throw new RuntimeException("VrpProblem existiert nicht in Got. Falscher Konstruktor verwendet; muss dort �bergeben werden");
        return vrpProblem;
    }

    public double getTotalDistanceWithCostFactor() {
    	//RUNTIME_TODO: diesen Wert k�nnte man auch zwischenspeichern
        distance = 0;
        for (Tour t : this.getTours()) {
            distance += t.getTotalDistanceWithCostFactor();
        }
        return distance;
    }
    
    public double getTotalDistanceWithCostFactorAndRecourse() {
    	//RUNTIME_TODO: diesen Wert k�nnte man auch zwischenspeichern
    	double cost = 0;
    	for (GroupOfTours got : getGots()) {
    		cost += got.getTotalDistanceWithCostFactor() + got.getExpectedRecourse().getRecourseCost();
    	}
		return cost;
    }
    
    public double getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse() {
    	double totalDistanceWithCostFactorAndRecourse = getTotalDistanceWithCostFactorAndRecourse();     	
    	//RUNTIME_TODO: diesen Wert k�nnte man auch zwischenspeichern
    	RecourseCost rc = new RecourseCost(getGots());    	
    	double totalDistanceWithCostFactorAndConvexcombinationOfRecourse = totalDistanceWithCostFactorAndRecourse + 
    																		(totalDistanceWithCostFactorAndRecourse * (Parameters.getWeightForConvexcombination() * (rc.getNumberOfDifferentToursForBasicVehicles() / this.getNumberOfTours())));
    	return totalDistanceWithCostFactorAndConvexcombinationOfRecourse;
    }
    
    public double getTotalDistanceWithRecourseWithRecActNumAndAddTours() {
    	double totalDistanceWithCostFactorAndRecourse = getTotalDistanceWithCostFactorAndRecourse();
    	RecourseCost rc = new RecourseCost(getGots());
    	double totalDistanceWithRecourseWithRecActNumAndAddTours() = totalDistanceWithCostFactorAndRecourse + 
				(totalDistanceWithCostFactorAndRecourse * (Parameters.getWeightForConvexcombination() * ((rc.getNumberOfDifferentToursForBasicVehicles() / this.getNumberOfTours())) + (rc.getNumberOfAdditionalTours() / Parameters.getNumberOfDemandScenarioRuns())));
    }
    
    public RecourseCost getRecourseCost() {
    	return new RecourseCost(gots);
    }

    public int getVehicleCount() {
        return getTours().size();
    }

    public List<Tour> getTours() {
    	List<Tour> tours = new ArrayList<Tour>();
    	for (GroupOfTours got : gots)
    		tours.addAll(got.getTours());
        return tours;
    }

    public void addTourToLastOrNewGot(Tour tour) {    	
    	GroupOfTours got = null;
    	for (int i = Math.max(0, gots.size()-1); i < gots.size(); i++) {//
    		GroupOfTours tempGot = gots.get(i);
    		if (tempGot.isNewTourCanBeCreated()) {
    			got = tempGot;
    			break;
    		}
    	}
    	if (got == null) {
    		got = new GroupOfTours(this);
    		addGot(got);
    	}
    	got.addTour(tour);     	
    }
  
	public void createNewTourWithCustomer(Customer c) {
		if (this.isExistsGotWhereNewTourCanBeCreated())
			createNewTourInPossibleGotWhereSimilarityToNewCustomerIsGreatest(c);
		else {
			GroupOfTours got = createNewGotInSolution();
			got.createNewTour(this.getVrpProblem());
			got.insertCustomerIntoLastTour(c);
		}
	}

		private void createNewTourInPossibleGotWhereSimilarityToNewCustomerIsGreatest(Customer customer) {
			GroupOfTours got = getGotWhereCreatingNewTourIsPossibleAndSimilarityToNewCustomerIsGreatest(customer);
			got.createNewTour(this.getVrpProblem());
			got.insertCustomerIntoLastTour(customer);		
		}
	
	    private GroupOfTours getGotWhereCreatingNewTourIsPossibleAndSimilarityToNewCustomerIsGreatest(Customer customer) {
	    	GroupOfTours returnGot = null;
	    	double simVal = Double.MIN_VALUE;
	    	//set maxDistance in SimilarityUtils to maximal distance between customer and all Customers that are in gots
	    	SimilarityUtils.setMaxDistance(customer, this.getCustomersInTours());
	    	for (GroupOfTours got : gots)
	    		if (got.isNewTourCanBeCreated())
	    			if (got.getSimilarityValueToNewCustomer(customer) >= simVal) {
	    				//TODO: getSimilarityValueToNewCustomer(customer) funktioniert nicht richtig. Wenn man in der Zeile dr�ber das >= durch > 
	    				//ersetzt, kommt es zu einer Fehlermeldung. 
	    				//Wahrscheinlich ist das mit dem similarityValue aber auch eh nicht so wichtig.
	    				simVal = got.getSimilarityValueToNewCustomer(customer);
	    				returnGot = got;
	    			}	    				
	    	if (returnGot != null)
	    		return returnGot;
	    	else {
	    		System.out.println("isExistsGotWhereNewTourCanBeCreated(): " + isExistsGotWhereNewTourCanBeCreated());
	    		System.out.println("Solution: " + getAsTupel());
	    		System.out.println("Customer, der eingef�gt werden soll: " + customer);
	    		System.out.println("Similarity Value zwischen Customer, der eingef�gt werden soll und bester Tour: " + simVal);
	    		throw new RuntimeException("Es konnte kein Got gefunden werden in getGotWhereCreatingNewTourIsPossibleAndSimilarity...");
	    	}
	    }
	
	    private GroupOfTours createNewGotInSolution() {
			GroupOfTours got = new GroupOfTours(this);
			this.addGot(got);
			return got;
		}

    public void removeEmptyToursAndGots() {
    	Set<GroupOfTours> emptyGots = new HashSet<GroupOfTours>();
    	for (GroupOfTours got : gots)     		
    	    got.removeEmptyToursAndGots();    	  		    
    	for (GroupOfTours got : gots) 
    		if(!got.isHasTours())
    	        emptyGots.add(got);    	        	  		   
    	gots.removeAll(emptyGots);
    }
    
    public void removeEmptyGots() {
    	Set<GroupOfTours> emptyGots = new HashSet<GroupOfTours>();
    	for (GroupOfTours got : gots) 
    		if(!got.isHasTours())
    	        emptyGots.add(got);    	        	  		   
    	gots.removeAll(emptyGots);
    }
    
    public List<CustomerInTour> getCustomersInTours() {
        List<CustomerInTour> returnList = new ArrayList<CustomerInTour>();
        for (Tour t : getTours()) {
            returnList.addAll(t.getCustomersInTour());
        }
        return returnList;
    }

    public RecourseCost getExpectedRecourseCost() {
    	//RUNTIME_TODO: Recourse Cost k�nnte auch zwischengespeichert werden
    	return new RecourseCost(getGots());
    }
    	
	public void addGot(GroupOfTours got) {
		gots.add(got);
	}
	
	public List<GroupOfTours> getGots() {
		return gots;
	}

	public boolean isNewTourCanBeCreatedInLastGot() {
		if (isNoGotsExistsSoFar())
			return false;
		GroupOfTours got = getLastGot();
		return got.isNewTourCanBeCreated();
	}
	
	public boolean isExistsGotWhereNewTourCanBeCreated() {
		if (isNoGotsExistsSoFar())
			return false;
		for (GroupOfTours got : gots)
			if (got.isNewTourCanBeCreated())			
				return true;
		return false;				
	}

	private boolean isNoGotsExistsSoFar() {
		if (gots.size() == 0)
			return true;
		else return false;
	}

	public GroupOfTours getLastGot() {
		if ((gots.size() - 1) < 0)
			throw new RuntimeException("getLastGot wurde aufgerufen, aber es gibt keine Got in Solution");
		return gots.get(gots.size() - 1);		
	}

	public Tour getLastTour() {
		GroupOfTours got = getLastGot();
		return got.getLastTour();
	}
	
	public Tour getTour(int i) {
		return getTours().get(i);
	}
	
	public List<Customer> deleteTour(int i) {
		Tour tour = getTour(i);
		List<Customer> customers = tour.removeCustomersBetween(0, tour.getCustomerSize());
		removeEmptyToursAndGots();
		return customers;
	}
	
	public GroupOfTours findGotForTour(Tour tour) {
		GroupOfTours gotForTour = null;
		for (GroupOfTours got : getGots())
			for (Tour t : got.getTours())
				if (t.getId() == tour.getId())
				{ 
					gotForTour = got;
					break;
				}
			if (gotForTour != null)
				return gotForTour;
			else 
				throw new RuntimeException("Tour konnte nicht gefunden werden in getGotForTour");
	}
				
	/**
     * Clone Utilities
     */
		
    public SolutionGot clone() {
        SolutionGot s = new SolutionGot(vrpProblem);       
        for (GroupOfTours got : gots) {
            s.addGot(got.clone());
        }
        return s;
    }
    	
	/**
     * Print Utilities
     * <http://www.idsia.ch/~luca/macs-vrptw/solutions/welcome.htm>
     */
	
	public void printSolution() {
        System.out.println(getAsTupel());
    }

    public String getSolutionAsString() {
        String s = String.format("%.3f %.3f %.3f %d\n", getTotalDistanceWithCostFactor(),
                getExpectedRecourseCost(), getTotalDistanceWithCostFactorAndRecourse(), getVehicleCount());
        for (Tour t : getTours()) {
            s += ("0 ");
            for (Customer c : t.getCustomers()) {
                s += (c.getCustomerNo() + " ");
            }
            s += "\n";
        }
        return s;
    }
    
	public void printSolutionAsTupel() {
		System.out.println(getAsTupel());
	}   

	public void printSolutionCost() {
		System.out.println(getTotalDistanceWithCostFactor());
	}
	
	public int getNumberOfTours() {
		return getTours().size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolutionGot other = (SolutionGot) obj;
		if (gots == null) {
			if (other.gots != null)
				return false;
		} else { 
			//RUNTIME_TODO: kann man das �ber Hashing in besserer Zeit implementieren?
			for (GroupOfTours got : gots)
				if (!other.isExistsEqualGot(got))
					return false;			
			return true;
		}
		return true;
	}

		private boolean isExistsEqualGot(GroupOfTours gotOther) {
			for (GroupOfTours got : gots)
				if (got.equals(gotOther))
					return true;
			return false;
		}

		public Customer getCustomerWithNo(int i) {
			for (CustomerInTour c : getCustomersInTours())
				if (c.getCustomer().getCustomerNo() == i)
					return c.getCustomer();
			throw new RuntimeException ("Customer not found in getCustomerWithNo");
		}

		public void printVehicleCount() {
			System.out.println(getVehicleCount());
		}

		public int maximalNumberOfToursInGots() {
			int maxNumberOfGots = 0;
			for (GroupOfTours got : getGots()) {
				if (got.getTours().size() > maxNumberOfGots)
					maxNumberOfGots = got.getTours().size();
			}
			return maxNumberOfGots;
		}

		public String getUseOfCapacityInTours() {
			String s = "";
	        for (GroupOfTours got : gots)
	        {
	        	s += "(";
		        for (Tour t : got.getTours()) {
		            s += t.getUseOfCapacity();
		        }
		        s += ") ";
	        }
	        s += "; total: ( " + vrpProblem.getTotalDemandOfAllCustomers() + ":" + getNumberOfTours()*vrpProblem.getOriginalCapacity() + 
	        		") -> ;" + String.format("%.3f", vrpProblem.getTotalDemandOfAllCustomers()/(getNumberOfTours()*vrpProblem.getOriginalCapacity()));
	        return s;
		}
		
		 public String getAsTupel() {    
		    	String s = "";
		        for (GroupOfTours got : gots)
		        {
		        	s += "( ";
			        for (Tour t : got.getTours()) {
			            s += t.getTourAsTupel();
			        }
			        s += ") ";
		        }
		        return s;
		    }
		
		@Override
		public String getAsTupelWithDemand() {
			String s = "";
	        for (GroupOfTours got : gots)
	        {
	        	s += "( ";
		        for (Tour t : got.getTours()) {
		            s += t.getTourAsTupelWithDemand();
		        }
		        s += ") ";
	        }
	        return s;
		}
		
		public String isExistsTourWithCostFactor2() {
			String exists = "nein";
			for (Tour t : getTours())
				if (t.getCostFactor() == 2)
					exists = "ja";
			return exists;
		}
		
		public boolean isHasEmptyToursThatAreNotForRecourse() {
			boolean hasEmptyTours = false;
			for (GroupOfTours got : gots)
				if (got.isHasEmptyToursThatAreNotForRecourse())
					hasEmptyTours = true;
	    	for (GroupOfTours got : gots) 
	    		if(!got.isHasTours())
	    	        hasEmptyTours = true;
	    	return hasEmptyTours;
		}

		public int getLengthOfShortestTour() {
			//RUNTIME_TODO: diesen Wert zwischenspeichern und nur neu berechnen, wenn n�tig
			int length = Integer.MAX_VALUE;
			for (Tour t : getTours())
				if (t.getCustomerSize() < length)
					length = t.getCustomerSize();
			return length;
		}

		public void resetRecourseCost() {
			for (GroupOfTours got : getGots())
				got.resetExpectedRecourseCost();
		}
}


