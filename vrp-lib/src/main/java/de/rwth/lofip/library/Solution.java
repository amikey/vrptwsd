package de.rwth.lofip.library;

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
 * @author Olga Bock
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "solution", propOrder = {
    "iteration",
    "iterationRepair",
    "penaltyCost",
    "timeNeeded", 
    "vrpProblem",
    "tours"
    
})
public class Solution implements Cloneable {

    // the problem is used here, just for reference
	private VrpProblem vrpProblem;
    private double penaltyCost = 0;
    
    // time needed for calculation of solution

	private long timeNeeded;

    /**
     * Just an informationary field which tells us, in which iteration this
     * solution was created
     */
    private int iteration;

    private int iterationRepair;

    // List of all tours
    @XmlElement(name="tour")
	@XmlElementWrapper(name="tours")
	private List<Tour> tours = new ArrayList<Tour>();

    // Constructor
    
//    public Solution(){
//    	this.tours = new ArrayList<Tour>();
//    }
    
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
    
    /**
     * Return the travel distance of this solution, that is the distance of all
     * tours in this solution.
     * 
     * @return
     */
    public double getTotalDistance() {
        double distance = 0;
        for (Tour t : tours) {
            distance += t.getDistance();
        }
        return distance;
    }
    
    public String getTotalDistanceAsString() {
    	String s = String.format("%.3f", getTotalDistance());
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

    /**
     * @return the edges (aka. lines) which belong to this solution.
     */
    public Set<Edge> getEdges() {
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

    /**
     * Prints out the solution to stdOut in the same format as the one used at
     * <http://www.idsia.ch/~luca/macs-vrptw/solutions/welcome.htm> 
     */
    public void printSolution() {
        System.out.println(getSolutionAsString());
    }

    public String getSolutionAsString() {    	    	  	
        String s = String.format("%.3f; %.3f; %.3f; %d\n", getTotalDistance(),
                getExpectedRecourseCost(), getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost(), getVehicleCount());
        for (Tour t : getTours()) {
            s += ("0 ");
            for (Customer c : t.getCustomers()) {
                s += (c.getCustomerNo() + " ");
            }
            s += "; Distance: " + t.getDistance();
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
    
    public List<CustomerInTour> getUnfixedCustomersInTours() { //OB - gibt nicht fixierte Kunden aus
        List<CustomerInTour> returnList = new ArrayList<CustomerInTour>();
        for (CustomerInTour c : this.getCustomersInTours()) {
        if (!c.isCustomerInTourFixed())
            returnList.add(c);
        }
        return returnList;
    }
    
    public CustomerInTour getCustomerWithId(long customerNo){
    	List<CustomerInTour> customerList = getCustomersInTours();
    	
    	for (CustomerInTour cit : customerList){
    		if (cit.getCustomer().getCustomerNo() == customerNo)
    			return cit;
    		}	
    	//case: customer not in tour
    	return null;
    }

    public double getExpectedRecourseCost() {
        double recourseCost = 0;
        for (Tour t : tours) {
            recourseCost += t.getExpectedRecourseCost();
        }
        return recourseCost;
    }

    /**
     * Get the total cost, which is calculated from the distance, a possible
     * 
     * @return 
     */
    public double getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() {
        return getTotalDistance() + getExpectedRecourseCost() + penaltyCost;
    }
    
    public double getCostInEuro() {
    	double cost = 0;
    	for (Tour t : tours)
    		cost += t.getCostInEuro();
    	return cost;
    }

    public void setPenaltyCost(double penaltyCost) {
        this.penaltyCost = penaltyCost;
    }

    /**
     * The penalty cost is the cost which this solution has, which is not part
     * of the objective function. It is described in the paper from Lei et al.
     * in section 3.1.
     * 
     * @return
     */
    public double getPenaltyCost() {
        return penaltyCost;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
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
    
    /**
     * Return the total utilized capacity for the problem. 
     */
    public double totalUtilizedCapacityInProblem(){
    	double uc = 0;
    	VrpProblem vrp =this.vrpProblem;
		uc = vrp.getTotalDemand()/(vrp.getVehicleCount()*vrp.getVehicles().iterator().next().getCapacity());
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
 
	public double percentageOfToursExecutedTheSameDay() {
    	return (double) NumberOfToursExecutedTheSameDay() / (double) getNumberOfTours(); 	    	    		
    }
    
    protected int NumberOfToursExecutedTheSameDay() {    
		return ToursExecutedTheSameDay().size();
    }
    
	public int getNumberOfTours() {
		return tours.size();
	}
    
    protected List<Tour> ToursExecutedTheSameDay() {
    	List<Tour> toursExecutedTheSameDay = new LinkedList<Tour>();
    	for (Tour t : tours) 
    		if (t.isTourExecutedTheSameDay())
    			toursExecutedTheSameDay.add(t);
		return toursExecutedTheSameDay;
    }
        
    //this is \alpha-service-degree
	public double percentageOfCustomersServedTheSameDay() {		
		return (double) NumberOfCustomersServedTheSameDay() / (double) NumberOfCustomers();		
	}
    
    protected int NumberOfCustomersServedTheSameDay() {
    	int numberOfCustomersServedTheSameDay = 0;
    	for (Tour t : ToursExecutedTheSameDay())    		
    			numberOfCustomersServedTheSameDay += t.getCustomerSize();
		return numberOfCustomersServedTheSameDay;
	}

	private int NumberOfCustomers() {		
		return this.getVrpProblem().getCustomerCount();
	}
    
	//this is \beta-service-degree
	public double percentageOfParcelsCollectedTheSameDay() {
		return (double) NumberOfParcelsCollectedTheSameDay() / (double) NumberOfParcels();
	}
    
	private int NumberOfParcelsCollectedTheSameDay() {
		int numberOfParcelsCollectedTheSameDay = 0;
		for (Tour t : this.ToursExecutedTheSameDay())
			numberOfParcelsCollectedTheSameDay += t.getDemandOnTour();
		return numberOfParcelsCollectedTheSameDay;
	}
	
	private int NumberOfParcels() {
		int numberOfParcels = 0;
		for (Tour t : tours)
			numberOfParcels += t.getDemandOnTour();
		return numberOfParcels;
	}

	/**
     * Because a Solution is not immutable, we need to pass an in-depth-copy,
     * e.g. to a metaheuristic. This is why this .clone() method exists.
     */
    @Override
    public Solution clone() {
        Solution s = new Solution(vrpProblem);
        s.setIteration(iteration);
        for (Tour t : tours) {
            s.addTour(t.clone());
        }
        s.setPenaltyCost(penaltyCost);
        return s;
    }

	public long getTimeNeeded() {
		return timeNeeded  / 1000 / 1000;
	}

	public void setTimeNeeded(long timeNeeded) {
		this.timeNeeded = timeNeeded;
	}
	
	public String getCostInEuroAsString() {
		String s = String.format("%.3f", getCostInEuro());	           
        return s;
	}
	
	public String getTourTypesAsString() {
		String s = "(";
		for (Tour t : tours)
			s += t.getTourType() + ", ";
		s += ")";
		return s;
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

	public int getIterationRepair() {
		return iterationRepair;
	}

	public void setIterationRepair(int iterationRepair) {
		this.iterationRepair = iterationRepair;
	}


	public Solution cloneCompletelySeperateCopy() {
		VrpProblem vrpProblemClone = vrpProblem.clone();
		Solution s = new Solution(vrpProblemClone);
		s.setIteration(iteration);
		for (Tour t : tours) {
			Tour tour = t.cloneAndSetPointersToCustomersInVrpProblem(vrpProblemClone);
			s.addTour(tour);
		}
		s.setPenaltyCost(penaltyCost);
		return s;		
	}






    	
}
