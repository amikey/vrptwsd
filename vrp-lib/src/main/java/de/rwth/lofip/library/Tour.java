package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Iterables;

import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.RessourceExtensionFunction;
import de.rwth.lofip.library.util.CustomerInTour;

public class Tour implements Cloneable, SolutionElement {

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
	private List<Edge> edges = new LinkedList<Edge>();
	private double demand = 0;
	private double tourDistance = 0;
	
	//TODO: Gehen REFs von Einfügeposition zu Einfügeposition oder von Customerposition zu Customerposition?
	private List<RessourceExtensionFunction> refsFromStartUpToPosition = new LinkedList<RessourceExtensionFunction>(); 
	private List<RessourceExtensionFunction> refsFromPositionToEnd = new LinkedList<RessourceExtensionFunction>();
	
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
    
    //copy constructor
    public Tour(Tour tour1) {
    	this();
		this.depot = tour1.getDepot();
		this.vehicle = tour1.getVehicle().clone();
		customers = new LinkedList<CustomerInTour>(tour1.getCustomersInTour());
		refsFromStartUpToPosition = new LinkedList<RessourceExtensionFunction>(tour1.getRefsFromBeginning());
		refsFromPositionToEnd = new LinkedList<RessourceExtensionFunction>(tour1.getRefsToEnd());
	}
    
    /****************************************************************************
     * End Constructors
     ***************************************************************************/

	/****************************************************************************
     * Getter and Setter
     ***************************************************************************/
       
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
    	this.id=id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public Depot getDepot() {
        return depot;
    }

    public List<Edge> getEdges() {
        return edges;
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
     
    public List<CustomerInTour> getCustomersInTour() {
        return customers;
    }
    
    public List<Customer> getCustomers() {
        List<Customer> customerList = new ArrayList<Customer>();
        for (CustomerInTour cit : customers) {
            customerList.add(cit.getCustomer());
        }
        return customerList;
    }
    
	public int length() {
		return customers.size();
	}
    
    public double getTotalDistance() {  
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
        
    public boolean isTourEmpty() {
    	if (customers.size() == 0)
    		return true;
    	else
    		return false;
    }
         
    public double getDemandOnTour(){
    	return demand;
    }
    
	public List<RessourceExtensionFunction> getRefsFromBeginning() {
		return refsFromStartUpToPosition;		
	}
	
    private void setRefsFromBeginning(List<RessourceExtensionFunction> refsFromBeginning1) {
    	refsFromStartUpToPosition = refsFromBeginning1;
	}

	public List<RessourceExtensionFunction> getRefsToEnd() {
		return refsFromPositionToEnd;		
	}
	
	private void setRefsToEnd(List<RessourceExtensionFunction> refsToEnd1) {
    	refsFromPositionToEnd = refsToEnd1;		
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
        t.setRefsFromBeginning(getRefsFromBeginning());
        t.setRefsToEnd(getRefsToEnd());
        return t;
    }
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tour other = (Tour) obj;		
//		return Iterables.elementsEqual(customers, other.getCustomersInTour());
		Iterator<CustomerInTour> customersIterator = customers.iterator();
		Iterator<CustomerInTour> otherCustomerIterator = other.getCustomersInTour().iterator();
		while (customersIterator.hasNext()) {
			if (!otherCustomerIterator.hasNext())
				return false;			
			if (customersIterator.next().getCustomer().getCustomerNo() != otherCustomerIterator.next().getCustomer().getCustomerNo())
				return false;
		}
		if (otherCustomerIterator.hasNext())
			return false;
		return true;
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
    
	public void insertCustomersAtPosition(List<Customer> customers2, int position) {			
		Iterator<Customer> customerIterator = customers2.iterator(); 
		while (customerIterator.hasNext()) {
			Customer customer = customerIterator.next();			
			insertCustomerAtPosition(customer, position);
			position++;
		}			
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
        recalculateEdges();
        recalculateTotalDistance();
        recalculateDemand(customer.getDemand());
        recalculateRefsWhenCustomerIsInserted(position);
    }    

	private void recalculateRefsWhenCustomerIsInserted(int position) {
		//recalculate refFromStartUpToPosition	
		for (int i = position; i < customers.size(); i++) {
			RessourceExtensionFunction refTemp = createRefAtPosition(i);			
			if (isLastPosition(i)) {
				refsFromStartUpToPosition.add(refTemp);
			} else {
				System.out.print("i: " + i + "Tour: "); this.print();
				printRefsFromStart();
				refsFromStartUpToPosition.set(i, refTemp);
			}
		}			
		
		//recalculate refsFromPositionToEnd
		for (int i = 0; i <= position; i++) {
			//TODO: implement
		}

	}
	
	private void printRefsFromStart() {
		System.out.println("Länge refsFromStart...: " + refsFromStartUpToPosition.size());
		for (int i = 0; i < refsFromStartUpToPosition.size(); i++)
			refsFromStartUpToPosition.get(i).print();
	}
	
	private boolean isLastPosition(int i) {
		return (i == customers.size()-1 || this.isTourEmpty());
	}

	private boolean isFirstPosition(int i) {
		return i == 0;
	}
	
	private RessourceExtensionFunction createRefAtPosition(int i) {
		RessourceExtensionFunction refTemp;
		if (isFirstPosition(i)) {
			refTemp = new RessourceExtensionFunction();
			refTemp.updateWithCustomer(customers.get(0));
		} else {
			refTemp = refsFromStartUpToPosition.get(i-1).clone();
			refTemp.updateWithCustomer(customers.get(i));
		}
		return refTemp;
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
        	throw new RuntimeException("Es wurde versucht auf einer Tour einen Kunden an einer Position zu entfernen, die kleiner 0 ist. \n " +
        			"Versuchte Position ist " + position + ". Letzte mögliche Position ist " + (this.getCustomerSize()-1) + " \n " +
        			"Das ist die Tour: " + this.getTourAsTupel());
        if (position >= customers.size())	
        	throw new RuntimeException("Es wurde versucht auf einer Tour einen Kunden an einer Position zu entfernen, die groesser ist als die letzte Position auf der Tour. \n" + 
        			"Versuchte Position ist " + position + ". Letzte mögliche Position ist " + (this.getCustomerSize()-1) + " \n " +
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
        //TODO: ist das hier nicht auch völlig unnötig und compulationally teuer?
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setPosition(i);
        }
        vehicle.addCapacityUsage(removedCustomer.getDemand() * -1);
        recalculateTimes();  
        recalculateEdges();
        recalculateTotalDistance();
        recalculateDemand(removedCustomer.getDemand() * -1);
        recalculateRefsWhenCustomerIsDeleted(position);
        return removedCustomer;
    }            
    
	private void recalculateRefsWhenCustomerIsDeleted(int position) {
		//recalculate refFromStartUpToPosition		
		if (this.isTourEmpty())
			refsFromStartUpToPosition.clear();
		else {
			for (int i = position; i < customers.size(); i++) {
				RessourceExtensionFunction refTemp = createRefAtPosition(i);
				refsFromStartUpToPosition.set(i, refTemp);	
			}
			//last position has to be deleted, because one customer was deleted from tour
			refsFromStartUpToPosition.remove(customers.size());		
		}
							
		//recalculate refsFromPositionToEnd
		for (int i = 0; i <= position; i++) {
			//TODO: implement
		}
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

	public List<Customer> removeCustomersBetween(
			int positionStartOfSegmentTour1, int positionEndOfSegmentTour1) {		
		List<Customer> customers = new LinkedList<Customer>();	
		for (int i = positionStartOfSegmentTour1; i < positionEndOfSegmentTour1; i++) {			
			Customer customer = removeCustomerAtPosition(positionStartOfSegmentTour1);
			customers.add(customer);
		}
		return customers;
	}

	/**
     * helper method to recalculate the arrival/leaving times for all
     * {@code CustomerInTour} objects of this tour.
     */
    public final void recalculateTimes() {
        double currentTime = 0;
        if (customers.size() > 0) {
        	//first customer
        	CustomerInTour cit = customers.get(0);
        	cit.setArrivalTime(currentTime + new Edge(depot,cit).getLength());
        	currentTime = cit.getEarliestLeavingTime();
        	//following customers
        	for (int i = 0; i < customers.size()-1; i++) {
        		cit = customers.get(i+1);
        		cit.setArrivalTime(currentTime + new Edge(customers.get(i), cit).getLength());
        		currentTime = cit.getEarliestLeavingTime();
        	}        	
        }	      
    }    
    
    public final void recalculateEdges() {
    	edges.clear();
        if (customers.size() != 0) {                
	        //first customer
	        edges.add(new Edge(depot, customers.get(0)));
	        //customers inbetween
	        for (int i = 0; i < customers.size()-1; i++)
	        	edges.add(new Edge(customers.get(i), customers.get(i+1)));
	        //last customer
	        edges.add(new Edge(customers.get(customers.size()-1),depot));
        }       
    }
        
    private void recalculateDemand(long demand2) {
    	demand += demand2;
	}
    
	private void recalculateTotalDistance() {
        tourDistance = 0;
        for (Edge v : getEdges()) {
            tourDistance += v.getLength();
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

	public void print() {
		System.out.println(getTourAsTupel());		
	}
	

}
