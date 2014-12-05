package de.rwth.lofip.stuffNotNeededRightNow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.CustomerInTour;

/**
 * A {@code Solution} consists of several tours. It provides convenience methods
 * to get information about the solution quality, e.g. total vehicle count and
 * total distance traveled.
 * 
 * The cost for a single Tour is computed in {@code Tour} and those costs are
 * accumulated in solution.
 * 
 * <p>Java class for solution complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="solution">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="iteration" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="iterationRepair" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="penaltyCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="timeNeeded" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="tours" type="{http://library.lofip.rwth.de}tour" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vrpProblem" type="{http://library.lofip.rwth.de}vrpProblem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 */

public class Solution implements Cloneable {

	private VrpProblem vrpProblem;
    private double penaltyCost = 0; 
	private long timeNeededForCalculation;
    private int iterationInWhichSolutionWasCreated;
    private int iterationRepair;
	private List<Tour> tours = new ArrayList<Tour>();

    // Constructor 
    public Solution(VrpProblem vrpProblem) {
        this.vrpProblem = vrpProblem;
        this.tours = new ArrayList<Tour>();
    }
    
    public Solution() {
        this.tours = new ArrayList<Tour>();
    }

    /**
     * Getter und Setter
     */
    public VrpProblem getVrpProblem() {
        return vrpProblem;
    }
    
    public void setVrpProblem(VrpProblem vrpProblem) {
    	this.vrpProblem = vrpProblem;
//    	//ensure that customers in vrpProblem and tours are linked (the same object)
//    	for (Tour t : tours)
//    		for (Customer c : t.getCustomers())
//    			for (Customer cVrp : this.vrpProblem.getCustomers())
//    				if (c.getCustomerNo() == cVrp.getCustomerNo())
//    					c = cVrp;    	
//    	
//    	
//    	reset customer demands
//    	ensure that all customers are linked to the same object	
    }

    public void setTours(List<Tour> tours) {
    	this.tours = tours;
    }
    
    public double getTotalDistanceOfAllTours() {
        double distance = 0;
        for (Tour t : tours) {
            distance += t.getTotalDistance();
        }
        return distance;
    }
    
    public String getTotalDistanceAsString() {
    	String s = String.format("%.3f", getTotalDistanceOfAllTours());
    	return s;
    }        

    /**
     * The total number of vehicles used for the solution may be less than the
     * total number of tours as a vehicle may be used on several tours.
     */
    public int getVehicleCount() {
        // only count the number of distinct vehicle IDs
        Set<Integer> vehicles = new HashSet<Integer>();
        for (Tour t : tours) {
       		for (Tour t1 : t.getTours()) 
        		vehicles.add(t1.getVehicle().getVehicleId());
        }
        return vehicles.size();
    }

    public Set<Edge> getEdgesThatBelongToSolution() {
        Set<Edge> edges = new HashSet<Edge>();
        for (Tour t : tours) {
            edges.addAll(t.getEdges());
        }
        return edges;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }
 
    public void removeEmptyTours() {
        Set<Tour> emptyTours = new HashSet<Tour>();
        for (Tour t : tours) {
            if (t.getCustomers().size() == 0) {
                emptyTours.add(t);
            }
        }
        tours.removeAll(emptyTours);
    }

    public List<CustomerInTour> getCustomersInTours() {
        List<CustomerInTour> returnList = new ArrayList<CustomerInTour>();
        for (Tour t : tours) {
            returnList.addAll(t.getCustomersInTour());
        }
        return returnList;
    }
       
    public CustomerInTour getCustomerWithId(long customerNo){
    	List<CustomerInTour> customerList = getCustomersInTours();
    	
    	for (CustomerInTour cit : customerList){
    		if (cit.getCustomer().getCustomerNo() == customerNo)
    			return cit;
    		}	
    	//case: customer not in tour; sollte hier eine Exception geworfen werden?
    	return null;
    }

    public double getExpectedRecourseCost() {
        double recourseCost = 0;
        for (Tour t : tours) {
            recourseCost += t.getExpectedRecourseCost();
        }
        return recourseCost;
    }

    public double getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() {
        return getTotalDistanceOfAllTours() + getExpectedRecourseCost() + penaltyCost;
    }

    public int getIteration() {
        return iterationInWhichSolutionWasCreated;
    }

    public void setIteration(int iteration) {
        this.iterationInWhichSolutionWasCreated = iteration;
    }
    
    public boolean isSolutionFeasibleWrtDemand()
    {
    	boolean feasible = true;
    	for (Tour t : this.getTours())
    		if (!TourUtils.isTourFeasibleWrtDemand(t))
    		{	
    			feasible = false;
    	        System.out.println("M�p, Tour " + t.getTourAsTupel() + " nicht feasible"); 
    		}
    	return feasible;
    }
    
    public double totalUtilizedCapacityInProblem(){
    	double uc = 0;
    	VrpProblem vrp =this.vrpProblem;
		uc = vrp.getTotalDemandOfAllCustomersOnTour()/(vrp.getVehicleCount()*vrp.getVehicles().iterator().next().getCapacity());
    	return uc;
 	}
    
    public String totalUtilizedCapacityAsString() {
    	String s = String.format("%.3f", totalUtilizedCapacityInProblem());	           
        return s;
    }
    
    public double averageUtilizedCapacityPerTour() {
    	double sum = 0;
    	for (Tour t : tours){
    		long tourDemand = 0;
    		for (Customer c : t.getCustomers()) {
                tourDemand += c.getDemand();
            }
    		sum += tourDemand/t.getVehicle().getCapacity();
    	}
		double auc = sum/tours.size();
    	return auc;
 	}
    
    public String averageUtilizedCapacityPerTourAsString() {
    	String s = String.format("%.3f", averageUtilizedCapacityPerTour());	           
        return s;
    }
   
	public int getNumberOfTours() {
		return tours.size();
	}
	
	private void setPenaltyCost(double penaltyCost2) {
		this.penaltyCost = penaltyCost2;
	}

	public long getTimeNeeded() {
		return timeNeededForCalculation  / 1000 / 1000;
	}

	public void setTimeNeeded(long timeNeeded) {
		this.timeNeededForCalculation = timeNeeded;
	}

	public Tour getTourWithId(long id) {
		Tour tour = null;		
		for (Tour tourInSolution : this.getTours())
			if (tourInSolution.getId() == id)
			{
				tour = tourInSolution;
				break;
			};
		return tour;
	}

	public String getMinNumberOfCustomersInTourAsString() {
		int min = 100000000;
		for (Tour t : tours)
			if (t.getCustomerSize() < min)
				min = t.getCustomerSize();
		return "" + min;
	}
	
	public String getMaxNumberOfCustomersInTourAsString() {
		int max = 0;
		for (Tour t : tours)
			if (t.getCustomerSize() > max)
				max = t.getCustomerSize();
		return "" + max;
	}

	
	/** Clone-Utilities */
	
	/**
     * Because a Solution is not immutable, we need to pass an in-depth-copy,
     * e.g. to a metaheuristic. This is why this .clone() method exists.
     */
    @Override
    public Solution clone() {
        Solution s = new Solution(vrpProblem);
        s.setIteration(iterationInWhichSolutionWasCreated);
        for (Tour t : tours) {
            s.addTour(t.clone());
        }
        s.setPenaltyCost(penaltyCost);
        return s;
    }
	
	public Solution cloneCompletelySeperateCopy() {
		VrpProblem vrpProblemClone = vrpProblem.clone();
		Solution s = new Solution(vrpProblemClone);
		s.setIteration(iterationInWhichSolutionWasCreated);
		for (Tour t : tours) {
			Tour tour = t.cloneAndSetPointersToCustomersInVrpProblem(vrpProblemClone);
			s.addTour(tour);
		}
		s.setPenaltyCost(penaltyCost);
		return s;		
	}


	
	/** Printout-Utilities */

    /**
     * Prints out the solution to stdOut in the same format as the one used at
     * <http://www.idsia.ch/~luca/macs-vrptw/solutions/welcome.htm> 
     */
    public void printSolution() {
        System.out.println(getSolutionAsString());
    }

    public String getSolutionAsString() {    	    	  	
        String s = String.format("%.3f; %.3f; %.3f; %d\n", getTotalDistanceOfAllTours(),
                getExpectedRecourseCost(), getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost(), getVehicleCount());
        for (Tour t : getTours()) {
            s += ("0 ");
            for (Customer c : t.getCustomers()) {
                s += (c.getCustomerNo() + " ");
            }
            s += "; Distance: " + t.getTotalDistance();
            s += "; Id: " + t.getId();
            s += "\n";
        }
        return s;
    }
    
    public String getSolutionAsStringWithCustomer(){
    	String s ="";
        for (Tour t : getTours()) {
            s += t.getTourAsTupel() + "\n";
        }
        s += "\n";
        s += "Kapazit�t der Fahrzeuge: " + vrpProblem.getVehicles().iterator().next().getCapacity();
        return s;
    }
    
    public String getSolutionAsTupel() {    	    	  	
        String s = "";
        for (Tour t : getTours()) {
            s += t.getTourAsTupel();
        }
        return s;
    }
    	
}
