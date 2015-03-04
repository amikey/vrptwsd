package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.util.CustomerInTour;


/**
 * A {@code Solution} consists of several tours. It provides convenience methods
 * to get information about the solution quality, e.g. total vehicle count and
 * total distance traveled.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class SolutionGot implements Cloneable, SolutionElement {

    private VrpProblem vrpProblem;
    private double penaltyCost = 0;
    private int iterationInWhichSolutionWasCreated;  
    private List<GroupOfTours> gots = new ArrayList<GroupOfTours>();       
    
    public SolutionGot(VrpProblem vrpProblem) {
        this.vrpProblem = vrpProblem;
    }

    public VrpProblem getVrpProblem() {
        return vrpProblem;
    }

    public double getTotalDistance() {
        double distance = 0;
        for (Tour t : this.getTours()) {
            distance += t.getTotalDistance();
        }
        return distance;
    }

    /**
     * The total number of vehicles used for the solution may be less than the
     * total number of tours as a vehicle may be used on several tours.
     */
    public int getVehicleCount() {
        // only count the number of distinct vehicle IDs
    	// das ist eigentlich auch überflüssig, da Vehicle nicht mehrfach starten können
        Set<Integer> vehicles = new HashSet<Integer>();
        for (Tour t : getTours()) 
            vehicles.add(t.getVehicle().getVehicleId());        
        return vehicles.size();
    }

    public Set<Edge> getEdges() {
        Set<Edge> edges = new HashSet<Edge>();
        for (Tour t : getTours()) 
            edges.addAll(t.getEdges());        
        return edges;
    }

    public List<Tour> getTours() {
    	List<Tour> tours = new ArrayList<Tour>();
    	for (GroupOfTours got : gots)
    		tours.addAll(got.getTours());
        return tours;
    }

    public void addTour(Tour tour) {
    	throw new RuntimeException("addTour wird verwendet. Muss in SolutionGot erst noch definiert werden");
        //tours.add(tour);
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

	private GroupOfTours createNewGotInSolution() {
		GroupOfTours got = new GroupOfTours();
		this.addGot(got);
		return got;
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
    			if (got.getSimilarityValueToNewCustomer(customer) > simVal)
    				returnGot = got;
    	if (returnGot != null)
    		return returnGot;
    	else 
    		throw new RuntimeException("Es konnte kein Got gefunden werden in getGotWhereCreatingNewTourIsPossibleAndSimilarity...");
    }


    public void removeEmptyTours() {
    	Set<GroupOfTours> emptyGots = new HashSet<GroupOfTours>();
    	for (GroupOfTours got : gots)     		
    	    got.removeEmptyTours();    	  		    
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

    public double getExpectedRecourseCost() {
        double recourseCost = 0;
        for (GroupOfTours got : gots) {
            recourseCost += got.getExpectedRecourseCost();
        }
        return recourseCost;
    }
    
	public double getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() {
		return getTotalDistance() + getExpectedRecourseCost() + penaltyCost;
	}

    public void setPenaltyCost(double penaltyCost) {
        this.penaltyCost = penaltyCost;
    }

    public double getPenaltyCost() {
        return penaltyCost;
    }

    public int getIteration() {
        return iterationInWhichSolutionWasCreated;
    }

    public void setIteration(int iteration) {
        this.iterationInWhichSolutionWasCreated = iteration;
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
        s.setIteration(iterationInWhichSolutionWasCreated);
        s.setPenaltyCost(penaltyCost);
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
        System.out.println(getSolutionAsTupel());
    }

    public String getSolutionAsString() {
        String s = String.format("%.3f %.3f %.3f %d\n", getTotalDistance(),
                getExpectedRecourseCost(), getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost(), getVehicleCount());
        for (Tour t : getTours()) {
            s += ("0 ");
            for (Customer c : t.getCustomers()) {
                s += (c.getCustomerNo() + " ");
            }
            s += "\n";
        }
        return s;
    }
	
    public String getSolutionAsTupel() {    	    	  	
        String s = "";
        for (GroupOfTours got : gots)
        {
        	s += "(";
	        for (Tour t : got.getTours()) {
	            s += t.getTourAsTupel();
	        }
	        s += ") ";
        }
        return s;
    }
    
	public void printSolutionAsTupel() {
		System.out.println(getSolutionAsTupel());
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


}


