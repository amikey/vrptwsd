package de.rwth.lofip.library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.util.CustomerInTour;

/**
 * A {@code Tour} is the route, one {@link Vehicle} travels in a
 * {@link Solution}. It is {@code Cloneable} so it can easily be duplicated,
 * e.g. if calculations with this tour need to be made in a multithreaded
 * environment.
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
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
@XmlRootElement(name = "Tour")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tour", propOrder = {
    "customers",
    "costFactor",
    "id",
    "depot",
    "vehicle"
    ,"idTour"
})
public class Tour implements Cloneable {

    /****************************************************************************
     * Fields
     ***************************************************************************/
	
	protected static int seed = 1;
	
	//TODO: das k�nnte man eventuell zentral abspeichern
	//this factor is used to calculate the cost in Euro from the time needed to travel the tours in a solution
	//vehicles are assumed to cost 20 euro per hour => time in minutes *20/60
	protected double costFactor = 1;
	

	protected long id;
	
	@XmlAttribute(name = "idTour")// required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
//    @XmlSchemaType(name = "ID")
	@XmlID
    protected String idTour;

    /**
     * Saved as a property to avoid unnecessary recalculations.
     */
	@XmlTransient
	protected Double expectedRecourseCost = null;
	@XmlTransient
    protected LoadingCache<CachingKey, BigDecimal> cachedProbabilities = CacheBuilder.newBuilder()
    		.maximumSize(4000)
            .build(
            	new CacheLoader<CachingKey, BigDecimal>() {
                @Override
                public BigDecimal load(CachingKey key) throws Exception {
                    return calculateCumulatedDemandProbabilityFromBeginningOfTourToPosition(key.getPosition(), key.getDemand());
                }

            });
	@XmlElement(required = true, type = Object.class)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Depot depot;
    
//    @XmlElement(required = true, type = Object.class)
//    @XmlIDREF
//    @XmlSchemaType(name = "IDREF")
    protected Vehicle vehicle;

    @XmlElement(name="customerInTour")
	@XmlElementWrapper(name="customers")
    protected List<CustomerInTour> customers = new LinkedList<CustomerInTour>();

    /****************************************************************************
     * Constructors
     ***************************************************************************/
    
    /**
     * The default constructor of a {@code Tour} assigns a random ID to the
     * tour. Although this is obviously not guaranteed to be unique forever, it
     * should be good enough to identify a tour for a while.
     */
    public Tour() {    	    
        id = seed;
        seed++;
        this.idTour=(String)("T"+this.getId());
    }

    public Tour(Depot depot, Vehicle vehicle) {
        this();
        this.depot = depot;
        this.vehicle = vehicle;
    }
    
    public Tour(Depot depot, Vehicle vehicle, double costFactor) {
        this();
        this.depot = depot;
        this.vehicle = vehicle;
        this.costFactor = costFactor;
        System.out.println("Kostenfaktor der Tour " + this.getId() + "wird auf " + costFactor + "gesetzt");
    }
    
    /****************************************************************************
     * End Constructors
     ***************************************************************************/

    /****************************************************************************
     * Getter and Setter
     ***************************************************************************/
    
    public String getTourType()
    {
    	return "stochastic";
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id)
    {
    	this.id=id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public Depot getDepot() {
        return depot;
    }

    public List<Edge> getEdges() {
        List<Edge> edgesList = new ArrayList<Edge>();
        if (customers.size() == 0) {
            return edgesList;
        }
        for (CustomerInTour cit : customers) {
            edgesList.add(cit.getIncomingEdge());
        }
        edgesList.add(customers.get(customers.size() - 1).getOutgoingEdge());
        return edgesList;
    }

    /**
     * Get the {@code CustomerInTour} at the given position. May return null if
     * no such position exists.
     * 
     * @param position
     * @return
     */
    public CustomerInTour getCustomerAtPosition(int position) {
        if (position < 0 || position >= customers.size()) {
            return null;
        }
        return customers.get(position);
    }
    
    public CustomerInTour getCustomerWithId(long customerNo) {
    	for (CustomerInTour cit : this.getCustomersInTour())
    		if (cit.getCustomer().getCustomerNo() == customerNo)
    			return cit;
    	//case: customer not in tour
    	return null;
    }
    
    public List<CustomerInTour> getUnfixedCustomersInTour() { //OB - gibt nicht fixierte Kunden aus
        List<CustomerInTour> returnList = new ArrayList<CustomerInTour>();
        for (CustomerInTour c : this.getCustomersInTour()) {
        if (!c.isCustomerInTourFixed())
            returnList.add(c);
        }
        return returnList;
    }
   
    public CustomerInTour getMostSimilarCustomerInTour(Customer customer) throws Exception
    {   //AB - gibt den CustomerInTour zur�ck, der den h�chsten Similarity Value mit customer hat
    	try 
    	{
    		for (CustomerWithCost cwc : customer.getSimilarCustomers())
	    		for (CustomerInTour cit : this.getCustomersInTour())
	    			if (cwc.getCustomer().getCustomerNo() == cit.getCustomer().getCustomerNo())
	    				return cit;
    	} catch (Exception e) {
    		System.err.println("An error occured when calculating the most similar customer in tour.");
            e.printStackTrace();
        }
    	return null;
    }

    public List<CustomerInTour> getCustomersInTour() {
        return customers;
    }
    
	public int getTheIndexOfTheLastRemovablePosition() {
		return customers.size()-1;
	}

    public List<Customer> getCustomers() {
        List<Customer> customerList = new ArrayList<Customer>();
        for (CustomerInTour cit : customers) {
            customerList.add(cit.getCustomer());
        }
        return customerList;
    }
    
//    /**
//     * Get the last customer of the tour, that is, the one before the vehicle
//     * returns to the depot.
//     * 
//     * @return
//     */
//    public Customer getLastCustomer() {
//        if (customers.size() > 0) {
//            return customers.get(customers.size() - 1).getCustomer();
//        } else {
//            return null;
//        }
//    }
    
    /**
     * Get the total distance of the tour, including the trips from and to the
     * depot.
     * 
     * @return
     */
    public double getDistance() {
        double tourDistance = 0;
        for (Edge v : getEdges()) {
            tourDistance += v.getLength();
        }
        return tourDistance;
    }
    
    public int getCustomerSize()
    {
    	return customers.size();
    } 
       
	public double getCostFactor() {
		return costFactor;
	}
	
	public void setCostFactor(double costFactor) {		 
		this.costFactor = costFactor;
	}

	public boolean isTourExecutedTheSameDay() {
		if (costFactor == 1)
			return true;
		else 
			return false;			
	}
	
    public double getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost() {
        return getDistance()*getCostFactor() + getExpectedRecourseCost();
    }
    
    public double getCostInEuro() {
    	return getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost();
    }
    
    public List<Tour> getTours() 
    {
    	List<Tour> tours = new LinkedList<Tour>();
    	tours.add(this);
    	return tours;    	
    }
    
    public boolean isTourEmpty() {
    	if (customers.size() == 0)
    		return true;
    	else
    		return false;
    }
     
    public String getTourAsTupel()
    {
    	String s = ("(0 ");
        for (Customer c : this.getCustomers()) {
            s += (c.getCustomerNo() + " ");
        }
        s += ")";
//        s += "); Demand: " + getDemandOnTour();
        return s;
    }
    
    public double getDemandOnTour(){
    	double demand = 0;
    	for (Customer c : getCustomers())
    		demand += c.getDemand();
    	return demand;
    }
    
    /****************************************************************************
     * End Getter and Setter
     * @throws  
     ***************************************************************************/
    
    @Override
    public Tour clone() {
        Tour t = new Tour(depot, vehicle.clone());        
        for (CustomerInTour cit : customers) {
            t.addCustomer(cit.getCustomer(), cit.getInsertedInIteration(),
                    cit.getInsertionHeuristic(), cit.isCustomerInTourFixed());
        }
        t.setId(this.id);
        return t;
    }
    
	public Tour cloneAndSetPointersToCustomersInVrpProblem(VrpProblem vrpProblemClone) {
		Tour t = new Tour(depot, vehicle.clone());
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
    
    
    /****************************************************************************
     * Utilities for adding and deleting customers
     ***************************************************************************/

    /**
     * Adds a customer at the last position of the tour. When a customer is
     * added, the edge between that customer and the previous customer is added
     * as well. If the added customer is the first one, the edge from the depot
     * to that customer is added.
     */
    public void addCustomer(Customer customer) {
        insertCustomerAtPosition(customer, customers.size());
    }

    public void addCustomer(Customer customer, int iteration,
            String insertionClassName, boolean isFixedCustomer) {
        insertCustomerAtPosition(customer, customers.size(), iteration,
                insertionClassName, isFixedCustomer);
    }
   
    public void addCustomer(Customer customer, int iteration,
            String insertionClassName) {
        insertCustomerAtPosition(customer, customers.size(), iteration,
                insertionClassName, false);
    }
    
    /**
     * A helper method if you don't have an insertion class name.
     */
    public void insertCustomerAtPosition(Customer customer, int position) {
        insertCustomerAtPosition(customer, position, 0, "", false);
    }
    
    public void insertCustomerAtPosition(Customer customer, int position,
            int iteration, String insertionClassName) {
    	insertCustomerAtPosition(customer, position, iteration, insertionClassName, false);
    }
    
    /**
     * Inserts the customer at the specified position within this tour. This
     * method does not check if this insertion violates the time window of any
     * customers, it only does the insertion!
     * 
     * @param customer
     * @param position
     */
    public void insertCustomerAtPosition(Customer customer, int position,
            int iteration, String insertionClassName, boolean isFixedCustomer) {

        // reset the cached cost
        expectedRecourseCost = null;

        CustomerInTour newCustomerInTour = new CustomerInTour(this);
        newCustomerInTour.setPosition(position);
        newCustomerInTour.setCustomer(customer);
        newCustomerInTour.setInsertedInIteration(iteration);
        newCustomerInTour.setInsertionHeuristic(insertionClassName);
        newCustomerInTour.setFixCustomerInTour(isFixedCustomer);
        if (position == 0) {
            newCustomerInTour.setPreviousVertex(getDepot());
        } else {
            newCustomerInTour.setPreviousVertex(customers.get(position - 1)
                    .getCustomer());
            newCustomerInTour.setPreviousCustomerInTour(customers
                    .get(position - 1));
            customers.get(position - 1)
                    .setNextCustomerInTour(newCustomerInTour);
            customers.get(position - 1).setNextVertex(customer);
        }
        if (position >= customers.size()) {
            newCustomerInTour.setNextVertex(getDepot());
        } else {
            newCustomerInTour.setNextVertex(customers.get(position)
                    .getCustomer());
            newCustomerInTour.setNextCustomerInTour(customers.get(position));
            customers.get(position)
                    .setPreviousCustomerInTour(newCustomerInTour);
            customers.get(position).setPreviousVertex(customer);
        }

        customers.add(position, newCustomerInTour);
        // update the position of all CustomerInTour which come after the newly
        // inserted, increase them by one
        for (int i = position + 1; i < customers.size(); i++) {
            customers.get(i).setPosition(i);
        }
        vehicle.addCapacityUsage(customer.getDemand());
        recalculateTimes();
        
        invalidateKeysAndExpectedRecourseCostHook(position);
    }    
    
    /**
     * Remove the customer at {@code position} if the tour has at least that
     * many customers. Returns the removed customer or null, if no such position
     * exists.
     * 
     * @param position
     * @return
     * @throws Exception 
     */
    public Customer removeCustomerAtPosition(int position) { 			    	
        if (position < 0) 
        	throw new RuntimeException("Es wurde versucht auf einer Tour einen Kunden an einer Position zu entfernen, die kleiner 0 ist. Versuchte Position ist " + position + ". \n " +
        			"Das ist die Tour: " + this.getTourAsTupel());
        if (position >= customers.size())	
        	throw new RuntimeException("Es wurde versucht auf einer Tour einen Kunden an einer Position zu entfernen, die gr��er ist als die letzte Position auf der Tour. Versuchte Position ist " + position + ". \n " +
        			"Das ist die Tour: " + this.getTourAsTupel() +"\n" + 
        			"customers.size(): " + customers.size() + "\n" + 
        			"Kunden fangen bei Position 0 an und gehen bis n-1 bei n Kunden. Wenn daher z.B.  versucht wird, an Position 8 zu entfernen, muss es 9 Kunden in der Tour geben.");	 
                
        if (customerToBeRemovedIsNotAtFirstPositionInTour(position)) {
        	maintainOutgoingPointerInDoublyLinkedList(position);          
        }
        if (customerToBeRemovedIsNotAtLastPositionInTour(position)) {
        	maintainIncomingPointerInDoublyLinkedList(position);
        }
        
        Customer removedCustomer = customers.remove(position).getCustomer();
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setPosition(i);
        }
        vehicle.addCapacityUsage(removedCustomer.getDemand() * -1);
        recalculateTimes();
        
        invalidateKeysAndExpectedRecourseCostHook(position);       
        
        return removedCustomer;
    }            
    
	private boolean customerToBeRemovedIsNotAtFirstPositionInTour(int position) {
    	return position >= 1;
	}
	
	private void maintainOutgoingPointerInDoublyLinkedList(int position) {
		customers.get(position - 1).setNextCustomerInTour(
                customers.get(position).getNextCustomerInTour());
        customers.get(position - 1).setNextVertex(
                customers.get(position).getNextVertex());
	}
	
    private boolean customerToBeRemovedIsNotAtLastPositionInTour(int position) {
    	return position < customers.size() - 1;
	}
    
	private void maintainIncomingPointerInDoublyLinkedList(int position) {
        customers.get(position + 1).setPreviousCustomerInTour(
                customers.get(position).getPreviousCustomerInTour());
        customers.get(position + 1).setPreviousVertex(
                customers.get(position).getPreviousVertex());
	}


	/**
     * helper method to recalculate all edges and the arrival/leaving times for all
     * {@code CustomerInTour} objects of this tour.
     */
    public final void recalculateTimes() {
        // the edges won't need recalculation, they are lazily calculated
        double currentTime = 0;
        for (CustomerInTour cit : customers) {
            cit.setArrivalTime(currentTime + cit.getIncomingEdge().getLength());
            currentTime = cit.getEarliestLeavingTime();
        }
    }

    /****************************************************************************
     * End Utilities for adding and deleting customers
     ***************************************************************************/

    
    
    /****************************************************************************
     * Calculation of Probabilities and Recourse Cost
     ***************************************************************************/        
    
    protected void invalidateKeysAndExpectedRecourseCostHook(int position)
    {
        Set<String> keys = new HashSet<String>();
        for (int i = position; i < customers.size() + 1; i++) {
            for (int j = 0; j <= getVehicle().getCapacity(); j++) {
                keys.add(i + ":" + j);
            }
        }
        cachedProbabilities.invalidateAll(keys);
        // reset the cached cost
        expectedRecourseCost = null;
    }
    
    
    /**
     * This method returns the probability that
     * the cumuluated demand (up to node {@code position} (included)) is *exactly* the given {@code demand}.
     * i.e. P(X = x) at {@code position} 
     * 
     * This probability is either already cached in {@value cachedProbabilities}
     * or calculated using the {@method getProbability} 
     * 
     * @param position
     * @param demand
     * @return
     * @throws Exception 
     */
    public BigDecimal getCumulatedDemandProbabilityAtPosition(int position, int demand) {
        BigDecimal d = cachedProbabilities.getUnchecked(new CachingKey(
                position, demand));
        if (d.compareTo(BigDecimal.ZERO) == -1 || d.compareTo(BigDecimal.ONE) == 1) 
        {
            System.err.println("A probability must be between 0 and 1");
        }
        return d;
    }
    
    
    /**
     * This method calculates the "cumulated demand" (i.e. demand cumulated for all nodes up to node {@code position} 
     * probability for the demand being *exactly* {@code demand} up to the vertex {@code position} on the tour.
     * i.e. P(X = x); (see proposition 1 Lei et al.)
     * 
     * This method is used in the cache cachedProbabilities to compute the entries of the cache.
     * 
     * @param position
     * @param demand
     * @return
     * @throws Exception 
     */
    protected BigDecimal calculateCumulatedDemandProbabilityFromBeginningOfTourToPosition(int position, int demand) {

        CustomerInTour customerAtPosition = getCustomerAtPosition(position);

        if (position == 0) {
            // this is the first customer; the cumulated demand is
            // this customers demand 
        	// (boundary condition in proposition 1)
            BigDecimal probHasDemand = customerAtPosition
                    .getDemandProbability(demand);
            return probHasDemand;
        }

        // this calculation is explained in the paper by Lei et al.
        // in proposition 1
        BigDecimal demandProbability = BigDecimal.ZERO;
        for (int l = 0; l <= demand; l++) {
            demandProbability = demandProbability.add(getCumulatedDemandProbabilityAtPosition(position - 1,
                    demand - l).multiply(
                    customerAtPosition.getDemandProbability(l)));
        }
        return demandProbability;
    }
    
    
    /**
     * Get the probability, that the customer will be failing in this tour 
     * Calculation see proposition 2 in Lei et al paper.
     */
    public BigDecimal getProbabilityOfFailureAtCustomer(CustomerInTour currentCustomer) {
        int position = currentCustomer.getPosition();
        double capacity = getVehicle().getCapacity();
        BigDecimal probOfFailureBeforeCurrentCustomer = BigDecimal.ZERO;
        BigDecimal probOfFailureAfterCurrentCustomer = BigDecimal.ZERO;
       
        for (int t = 0; t < capacity; t++) {
            BigDecimal before = getCumulatedDemandProbabilityAtPosition(position - 1, t);
            probOfFailureBeforeCurrentCustomer = probOfFailureBeforeCurrentCustomer.add(before);
        }

        for (int t = 0; t < capacity; t++) {
            BigDecimal after = getCumulatedDemandProbabilityAtPosition(position, t);
            probOfFailureAfterCurrentCustomer = probOfFailureAfterCurrentCustomer.add(after);
        }

        return probOfFailureBeforeCurrentCustomer.subtract(probOfFailureAfterCurrentCustomer);
    }
    
    
    /**
     * Calculate the expected cost of recourse for the given {@code Tour}. The
     * algorithm for doing so is described in proposition 6 in the paper by Lei
     * et al. (2011).
     */
    public double getExpectedRecourseCost() {
    	 System.err.println( "getExpectedRecourseCost in stochastischer Tour wird verwendet." );
    	
        if (expectedRecourseCost == null) {
            double cost = 0;
            final double beta = 2;

            for (int i = 1; i < getCustomers().size(); i++) {

                CustomerInTour failingCustomerInTour = getCustomerAtPosition(i);

                Set<Customer> typeOneFailingCustomers = new HashSet<Customer>();
                Set<Customer> typeTwoFailingCustomers = new HashSet<Customer>();

                // first, calculate the vehicle load AFTER loading in the amount at i
                double vehicleLoadAtI = 0;
                for (int x = 0; x <= i; x++) {
                    vehicleLoadAtI += getCustomerAtPosition(x).getCustomer()
                            .getDemand();
                }

                // ok, there is a type-1 failure -> recalculate the extra costs
                // according to proposition 4 but only, if there are customers
                // after this one
                if (vehicleLoadAtI > getVehicle().getCapacity()
                        && failingCustomerInTour.getNextCustomerInTour() != null) {
                    // type 1 error
                    CustomerInTour previousCustomerInTour = getCustomerAtPosition(i);
                    Customer previousCustomer = previousCustomerInTour
                            .getCustomer();
                    double previousCustomerArrivalTime = previousCustomerInTour
                            .getArrivalTime();
                    // first calculate the arrival time for the vertex coming
                    // after the failing customer
                    int j = i + 1;
                    CustomerInTour customerAtPosJ = getCustomerAtPosition(j);
                    double customerArrivalTime = 0;

                    if (previousCustomerArrivalTime
                            + new Edge(previousCustomer, depot).getLength() * 2 <= previousCustomer
                                .getTimeWindowClose()) {
                        customerArrivalTime = Math.max(
                                previousCustomer.getTimeWindowOpen(),
                                previousCustomerArrivalTime)
                                + new Edge(previousCustomer, depot).getLength()
                                * 2
                                + previousCustomer.getServiceTime()
                                + new Edge(previousCustomer, customerAtPosJ)
                                        .getLength();
                    } else {
                        customerArrivalTime = previousCustomerArrivalTime
                                + new Edge(previousCustomer, depot).getLength()
                                * 2
                                + new Edge(previousCustomer, customerAtPosJ)
                                        .getLength();
                    }
                    // now check, if the time window is violated. If it is, add
                    // the customer to the respective set
                    if (customerArrivalTime > customerAtPosJ.getCustomer()
                            .getTimeWindowClose()) {
                        typeTwoFailingCustomers.add(customerAtPosJ
                                .getCustomer());
                    }

                    previousCustomerArrivalTime = customerArrivalTime;
                    previousCustomerInTour = customerAtPosJ;
                    previousCustomer = customerAtPosJ.getCustomer();

                    // now do the same for all other customers on the tour
                    for (j = i + 2; j < getCustomers().size(); j++) {
                        customerAtPosJ = getCustomerAtPosition(j);
                        if (previousCustomerArrivalTime < previousCustomer
                                .getTimeWindowClose()) {
                            customerArrivalTime = Math.max(
                                    previousCustomer.getTimeWindowOpen(),
                                    previousCustomerArrivalTime)
                                    + previousCustomer.getServiceTime()
                                    + new Edge(previousCustomer, customerAtPosJ)
                                            .getLength();
                        } else {
                            customerArrivalTime = previousCustomerArrivalTime
                                    + new Edge(previousCustomer, customerAtPosJ)
                                            .getLength();
                        }
                        if (customerArrivalTime > customerAtPosJ.getCustomer()
                                .getTimeWindowClose()) {
                            typeTwoFailingCustomers.add(customerAtPosJ
                                    .getCustomer());
                        }
                        previousCustomerArrivalTime = customerArrivalTime;
                        previousCustomerInTour = customerAtPosJ;
                        previousCustomer = customerAtPosJ.getCustomer();
                    }
                }

                // this is a type 2 failure
                if (vehicleLoadAtI == getVehicle().getCapacity()
                        && failingCustomerInTour.getNextCustomerInTour() != null) {
                    // type 2 error
                    CustomerInTour previousCustomerInTour = getCustomerAtPosition(i);
                    Customer previousCustomer = previousCustomerInTour
                            .getCustomer();
                    double previousCustomerArrivalTime = previousCustomerInTour
                            .getArrivalTime();
                    // first calculate the arrival time for the vertex coming
                    // after
                    // the failing customer
                    int j = i + 1;
                    CustomerInTour customerAtPosJ = getCustomerAtPosition(j);
                    double customerArrivalTime = previousCustomerArrivalTime
                            + previousCustomer.getServiceTime()
                            + new Edge(previousCustomer, depot).getLength()
                            + new Edge(depot, customerAtPosJ).getLength();
                    // now check, if the time window is violated. If it is, add
                    // the
                    // customer to the respective set
                    if (customerArrivalTime > customerAtPosJ.getCustomer()
                            .getTimeWindowClose()) {
                        typeTwoFailingCustomers.add(customerAtPosJ
                                .getCustomer());
                    }

                    previousCustomerArrivalTime = customerArrivalTime;
                    previousCustomerInTour = customerAtPosJ;
                    previousCustomer = customerAtPosJ.getCustomer();

                    for (j = i + 2; j < getCustomers().size(); j++) {
                        customerAtPosJ = getCustomerAtPosition(j);
                        if (previousCustomerArrivalTime < previousCustomer
                                .getTimeWindowClose()) {
                            customerArrivalTime = Math.max(
                                    previousCustomer.getTimeWindowOpen(),
                                    previousCustomerArrivalTime)
                                    + previousCustomer.getServiceTime()
                                    + new Edge(previousCustomer, customerAtPosJ)
                                            .getLength();
                        } else {
                            customerArrivalTime = previousCustomerArrivalTime
                                    + new Edge(previousCustomer, customerAtPosJ)
                                            .getLength();
                        }
                        if (customerArrivalTime > customerAtPosJ.getCustomer()
                                .getTimeWindowClose()) {
                            typeTwoFailingCustomers.add(customerAtPosJ
                                    .getCustomer());
                        }
                        previousCustomerArrivalTime = customerArrivalTime;
                        previousCustomerInTour = customerAtPosJ;
                        previousCustomer = customerAtPosJ.getCustomer();
                    }
                }

                double type1ErrorValue = getType1ErrorValue(
                        failingCustomerInTour, typeOneFailingCustomers, beta);

                double type2ErrorValue = getType2ErrorValue(
                        failingCustomerInTour, typeTwoFailingCustomers, beta);

                cost += type1ErrorValue;
                cost += type2ErrorValue;
            }
            expectedRecourseCost = cost;
        }
        return expectedRecourseCost;
    }

    /**
     * Will calculate the first part of the equation in proposition 6 (type1
     * error): l_i^k * (s_i^k + \beta * \sum_{v \in W_1(i,k)}(2*c(v, depot)))
     * 
     * @param currentCustomer
     * @param depot
     * @param failingCustomers
     *            the customers who will fail (first type) due to the failure at
     *            the currentCustomer; W_1(i,k)
     * @return
     * @throws Exception 
     */
    private double getType1ErrorValue(CustomerInTour currentCustomer,
            Set<Customer> failingCustomers, double beta) {

        BigDecimal type1Cost = BigDecimal.ZERO;

        double extraTrip = 2 * new Edge(currentCustomer, depot).getLength();
        double extraTripsForFailingCustomers = 0;
        for (Customer c : failingCustomers) {
            extraTripsForFailingCustomers += new Edge(c, depot).getLength() * 2;
        }

        // probabilityType2Failure is named kappa in the paper.
        BigDecimal probabilityType2Failure = calculateCumulatedDemandProbabilityFromBeginningOfTourToPosition(currentCustomer.getPosition(),(int) getVehicle().getCapacity());        		

        // probabilityType1Failure is named l in the paper.
        BigDecimal probabilityType1Failure = getProbabilityOfFailureAtCustomer(currentCustomer).subtract(probabilityType2Failure);
        type1Cost = probabilityType1Failure.multiply(new BigDecimal(
                (extraTrip + (beta * extraTripsForFailingCustomers))));

        return type1Cost.doubleValue();
    }   

    /**
     * Will calculate the second part of the equation in proposition 6 (type2
     * error): k_i^k * (squer_i^k + \beta * \sum_{v \in W_2(i,k)}(2*c(v,
     * depot)))
     * 
     * @param currentCustomer
     * @param depot
     * @param failingCustomers
     *            the customer who will fail (second type) due to the failure at
     *            the currentCustomer; W_2(i,k)
     * @return
     * @throws Exception 
     */
    private double getType2ErrorValue(CustomerInTour currentCustomer,
            Set<Customer> failingCustomers, double beta) {
        double type2Cost = 0;
        // probabilityType2Failure is named kappa in the paper 
        BigDecimal probabilityType2Failure = calculateCumulatedDemandProbabilityFromBeginningOfTourToPosition(currentCustomer.getPosition(),(int) getVehicle().getCapacity()); 
        
        double extraTrip = new Edge(currentCustomer, depot).getLength()
                + new Edge(depot, currentCustomer.getNextVertex()).getLength()
                - new Edge(currentCustomer, currentCustomer.getNextVertex())
                        .getLength();
        double extraTripsFailingCustomers = 0;
        for (Customer c : failingCustomers) {
            extraTripsFailingCustomers += (new Edge(c, depot).getLength() * 2);
        }

        type2Cost = probabilityType2Failure.doubleValue()
                * (extraTrip + (beta * extraTripsFailingCustomers));

        if (type2Cost < 0) {
            System.err
                    .println("Type 2 error is less than 0 - that can't be! Kappa: "
                            + probabilityType2Failure
                            + ", extra trip: "
                            + extraTrip
                            + ", extra trips: " + extraTripsFailingCustomers);
        }

        return type2Cost;
    }

    
    
    /**
     * Small procedure to calculate Probability of type 1 failure for customer on tour
	 * only necessary so it can be called with customerInTour instead of demand and position
     *//*    
    private BigDecimal getKappa(CustomerInTour c) {
    	double capacity = getVehicle().getCapacity();
    	int capacityInt = new Double(capacity).intValue();
    	int position = c.getPosition();
        BigDecimal kappa = BigDecimal.ZERO;
        
        kappa.add(calculateCumulatedDemandProbabilityFromBeginningOfTourToPosition(position, capacityInt));
        if (kappa.doubleValue() < 0) {
            System.err.println("Kappa is less than 0 - cannot be!");
        }
        return kappa;
    }
    */

    
    /****************************************************************************
     * END Calculation of Probabilities and Recourse Cost
     ***************************************************************************/ 


    /****************************************************************************
     * Caching Utilities
     ***************************************************************************/            

    /**
     * Needed to easily maintain the caching - no need to build and split
     * Strings or stuff.
     * 
     * @author Dominik Sandjaja <dominik@dadadom.de>
     * 
     */
    protected class CachingKey {
        private int position;
        private int demand;

        public CachingKey(int position, int demand) {
            this.position = position;
            this.demand = demand;
        }

        public int getPosition() {
            return position;
        }

        public int getDemand() {
            return demand;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + demand;
            result = prime * result + position;
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
            CachingKey other = (CachingKey) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (demand != other.demand)
                return false;
            if (position != other.position)
                return false;
            return true;
        }

        private Tour getOuterType() {
            return Tour.this;
        }

    }

}
