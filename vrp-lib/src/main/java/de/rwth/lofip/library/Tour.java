package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.util.CustomerInTour;

public class Tour implements Cloneable {

    /****************************************************************************
     * Fields
     ***************************************************************************/
	
	protected static int seed = 1;
	protected double costFactor = 1;
	protected long id;
    protected String idTour;
    protected Depot depot;
    protected Vehicle vehicle;
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
    
	public int getIndexOfTheLastRemovablePosition() {
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
    
    public double getTotalDistance() {  
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

    public double getSumOfDistanceMultipliedWithCostFactor() {
        return getTotalDistance()*getCostFactor();
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
                    cit.getInsertionHeuristic());
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
                    cit.getInsertionHeuristic());
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
       
    // A helper method if you don't have an insertion class name.     
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
     */
    public void insertCustomerAtPosition(Customer customer, int position,
            int iteration, String insertionClassName, boolean isFixedCustomer) {

        CustomerInTour newCustomerInTour = new CustomerInTour(this);
        newCustomerInTour.setPosition(position);
        newCustomerInTour.setCustomer(customer);
        newCustomerInTour.setInsertedInIteration(iteration);
        newCustomerInTour.setInsertionHeuristic(insertionClassName);
        if (position == 0) {
            newCustomerInTour.setPreviousVertex(getDepot());
        } else {
            newCustomerInTour.setPreviousVertex(customers.get(position - 1)
                    .getCustomer());
            customers.get(position - 1).setNextVertex(customer);
        }
        if (position >= customers.size()) {
            newCustomerInTour.setNextVertex(getDepot());
        } else {
            newCustomerInTour.setNextVertex(customers.get(position)
                    .getCustomer());
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
        
        return removedCustomer;
    }            
    
	private boolean customerToBeRemovedIsNotAtFirstPositionInTour(int position) {
    	return position >= 1;
	}
	
	private void maintainOutgoingPointerInDoublyLinkedList(int position) {
        customers.get(position - 1).setNextVertex(
                customers.get(position).getNextVertex());
	}
	
    private boolean customerToBeRemovedIsNotAtLastPositionInTour(int position) {
    	return position < customers.size() - 1;
	}
    
	private void maintainIncomingPointerInDoublyLinkedList(int position) {
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
     * END Calculation of Probabilities and Recourse Cost
     ***************************************************************************/ 
    
    
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

}
