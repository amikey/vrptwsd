package de.rwth.lofip.library;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import de.rwth.lofip.library.util.CustomerInTour;
/**
 * <p>Java class for tour complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tour">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customers" type="{http://library.lofip.rwth.de}customerInTour" maxOccurs="unbounded"/>
 *         &lt;element name="costFactor" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="depot" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *         &lt;element name="vehicle" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "deterministicTour", propOrder = {
//    "customers",
//    "costFactor",
//    "depot",
//    "vehicle"
//})

public class DeterministicTour extends Tour 
{
		
	public DeterministicTour() {	
        id = seed;
        seed++;
    }

    public DeterministicTour(Depot depot, Vehicle vehicle) {
        this();
        this.depot = depot;
        this.vehicle = vehicle;
    }
    public DeterministicTour(Depot depot, Vehicle vehicle, double costFactor) {
        this();
        this.depot = depot;
        this.vehicle = vehicle;
        this.costFactor = costFactor;
        System.out.println("Kostenfaktor der Tour " + this.getId() + "wird auf " + costFactor + "gesetzt");        
    }
    
    /**
     * Calculate the expected cost of recourse for the given {@code Tour}.
     * 
     * Use this method to make the vrp solver deterministic.
     * In addition to returning no Recourse Cost, one also has to change the 
     * "possible insertion" check in StochasticGreedyInsertion and AbstractFailureSortingInsertion
     * (see comments in these classes) 
     */
	@Override
    public double getExpectedRecourseCost() {
    	return 0;
    }
	
	@Override
    public String getTourType()
    {
    	return "deterministic";
    }
	
	@Override
    public DeterministicTour clone() {
        DeterministicTour t = new DeterministicTour(depot, vehicle.clone());
        for (CustomerInTour cit : customers) {
            t.addCustomer(cit.getCustomer(), cit.getInsertedInIteration(),
                    cit.getInsertionHeuristic(), cit.isCustomerInTourFixed());
        }
        t.setId(this.id);
        return t;
    }
	
	@Override
	public DeterministicTour cloneAndSetPointersToCustomersInVrpProblem(VrpProblem vrpProblemClone) {
		DeterministicTour t = new DeterministicTour(depot, vehicle.clone());
        for (CustomerInTour cit : customers) 
        {
        	//find customer with same number in vrpProblem
        	Customer cust = null;
        	for (Customer c : vrpProblemClone.getCustomers())
        		if (c.getCustomerNo() == cit.getCustomer().getCustomerNo())
        			cust = c;        	
            t.addCustomer(cust, cit.getInsertedInIteration(),
                    cit.getInsertionHeuristic(), cit.isCustomerInTourFixed());
        }
        t.setId(this.id);
        return t;
	}
}


