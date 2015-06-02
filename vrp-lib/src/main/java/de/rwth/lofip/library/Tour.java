package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.util.CustomerInTour;

public class Tour implements SolutionElement, Serializable {

	private static final long serialVersionUID = 1897416552132511414L;
	
	/****************************************************************************
     * Fields
     ***************************************************************************/
	
	protected static int seedForId = 1;
	protected long id;
    protected String idTour;
    protected Depot depot;
    protected Vehicle vehicle;
    protected List<CustomerInTour> customers = new LinkedList<CustomerInTour>();
	private List<Edge> edges = new LinkedList<Edge>();
	private double demand = 0;
	private double tourDistance = 0;
	private double costFactor = 1;
	
	private List<ResourceExtensionFunction> refsFromStartUpToPosition = new LinkedList<ResourceExtensionFunction>(); 
	private List<ResourceExtensionFunction> refsFromPositionToEnd = new LinkedList<ResourceExtensionFunction>();
	//this is an upper triangle matrix; first index -> row: ending position; second index -> column: starting position
	//ziemlich verwirrend, aber so ist die matrix leichter aufzubauen
	private List<ArrayList<ResourceExtensionFunction>> refForSegment = new ArrayList<ArrayList<ResourceExtensionFunction>>();
	
	//the label that the tour is labeled with for the adaptive memory
	private double solutionValue;
	private GroupOfTours gotThatTourBelongsTo;
	
    /****************************************************************************
     * Constructors
     ***************************************************************************/
    
    /**
     * The default constructor of a {@code Tour} assigns a random ID to the
     * tour. Although this is obviously not guaranteed to be unique forever, it
     * should be good enough to identify a tour for a while.
     */
    public Tour() {    	    
        id = seedForId;
        seedForId++;
        this.idTour=(String)("T"+this.getId());
    }

    public Tour(Depot depot, Vehicle vehicle) {
        this();
        this.depot = depot;
        this.vehicle = vehicle;
    }
        
    //copy constructor
    public Tour(Tour tour1) {
    	this();
		this.depot = tour1.getDepot();
		this.vehicle = tour1.getVehicle().clone();
		for (CustomerInTour cit : tour1.getCustomersInTour())
			customers.add(new CustomerInTour(cit));
		edges = tour1.getEdges();
		demand = tour1.getDemandOnTour();
		tourDistance = tour1.getTotalDistanceWithCostFactor();
		for (ResourceExtensionFunction ref : tour1.getRefsFromBeginning())
			refsFromStartUpToPosition.add(new ResourceExtensionFunction(ref));
		for (ResourceExtensionFunction ref : tour1.getRefsToEnd())
			refsFromPositionToEnd.add(new ResourceExtensionFunction(ref));		
		
		for (int i = 0; i < tour1.getRefMatrix().size(); i++)
			for (int j = 0; j <= i; j++) 
				setEntryInRefMatrix(i,j, tour1.getRefMatrix().get(i).get(j).clone());
		//IMPORTANT_TODO: stimmt das so und ist das was ich machen möchte?
		//Aufruf des Konstruktors angucken und ansehen, wie er verwendet wird.
		this.gotThatTourBelongsTo = tour1.getParentGot();
	}
    
    

	public Tour(double costFactor, long id, String idTour, Depot depot,
			Vehicle vehicle, List<CustomerInTour> customers, List<Edge> edges,
			double demand, double tourDistance,
			List<ResourceExtensionFunction> refsFromStartUpToPosition,
			List<ResourceExtensionFunction> refsFromPositionToEnd,
			List<ArrayList<ResourceExtensionFunction>> refForSegment,
			double solutionValue) {
		super();
		this.id = id;
		this.idTour = idTour;
		this.depot = depot;
		this.vehicle = vehicle;
		this.customers = customers;
		this.edges = edges;
		this.demand = demand;
		this.tourDistance = tourDistance;
		this.refsFromStartUpToPosition = refsFromStartUpToPosition;
		this.refsFromPositionToEnd = refsFromPositionToEnd;
		this.refForSegment = refForSegment;
		this.solutionValue = solutionValue;
	}
	
	
	public Tour cloneWithCopyOfCustomersAndVehicleAndSetParentGot(GroupOfTours got) {
		//RUNTIME_TODO: Das hier ist bestimmt sehr langsam. Kann man das schneller machen?
		//Das neusetzen der Refs ist hier wahrscheinlich gar nicht nötig, weil das eh noch mal gemacht werden muss, wenn die Demands bei den Customern verändert werden.
		Tour clonedTour = new Tour(this.getDepot(),this.getVehicle().clone());
		clonedTour.setParentGot(got);
		for (CustomerInTour cit : this.getCustomersInTour()) {
			clonedTour.addCustomer(cit.getCustomer().clone());			
		}
		return clonedTour;
	}

	/****************************************************************************
     * End Constructors
     ***************************************************************************/

	/****************************************************************************
     * Getter and Setter
     ***************************************************************************/
    
	public void setParentGot(GroupOfTours groupOfTours) {
		gotThatTourBelongsTo = groupOfTours;
	}
	
	public GroupOfTours getParentGot() {
		return gotThatTourBelongsTo;
	}
	
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
            throw new RuntimeException("getCustomerAtPosition was called with nonvalid index " + position);           
        }
        return customers.get(position);
    }
    
    public AbstractPointInSpace getCustomerAtPositionIncludingDepot(int position) {
    	if (position < -1 || position > customers.size()) 
            throw new RuntimeException("getCustomerAtPositionIncludingDepot was called with nonvalid index " + position);
    	if (position == -1 || position == customers.size()) 
            return depot;                   
        return customers.get(position).getCustomer();
    }
    
    
    public CustomerInTour getCustomerWithId(long customerNo) {
    	for (CustomerInTour cit : this.getCustomersInTour())
    		if (cit.getCustomer().getCustomerNo() == customerNo)
    			return cit;
    	//case: customer not in tour
    	//CODE_SMELL_TODO: bad practice
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
    
    public double getTotalDistanceWithCostFactor() {  
        return costFactor * tourDistance;
    }
    
    public int getCustomerSize() {
    	return customers.size();
    } 
               
    public boolean isTourEmpty() {
    	if (customers.size() == 0)
    		return true;
    	else
    		return false;
    }
         
    public double getDemandOnTour(){
    	if (isTourEmpty())
    		return 0;
    	return refsFromStartUpToPosition.get(refsFromStartUpToPosition.size()-1).getDemand();
    }
    
	public List<ResourceExtensionFunction> getRefsFromBeginning() {
		return refsFromStartUpToPosition;		
	}
	
    public List<ResourceExtensionFunction> getRefsToEnd() {
		return refsFromPositionToEnd;		
	}
	
	public ResourceExtensionFunction getRefFromBeginningAtPosition(int i) {
		if (i == -1)
			return new ResourceExtensionFunction(depot);
		else 
			return refsFromStartUpToPosition.get(i);
	}

	public ResourceExtensionFunction getRefToEndAtPosition(int i) {
		if (i == refsFromPositionToEnd.size())
			return new ResourceExtensionFunction(depot);
		else
			return refsFromPositionToEnd.get(i);
	}
	
	public boolean containsCustomers(Tour currentNewTour) {
		List<Customer> customersInThisTour = getCustomers();
		for (Customer c : currentNewTour.getCustomers())
			if (customersInThisTour.contains(c))
				return true;
		return false;
	}

	public void setCostFactor(int i) {
		costFactor = i;
	}
    
    /****************************************************************************
     * End Getter and Setter
     * @throws  
     ***************************************************************************/
     
 
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
    
	public void insertCustomersAtPosition(List<Customer> customers, int position) {
		//RUNTIME_TODO: Das kann man auch in konstanter Zeit implementieren
		Iterator<Customer> customerIterator = customers.iterator(); 
		while (customerIterator.hasNext()) {
			Customer customer = customerIterator.next();			
			insertCustomerAtPosition(customer, position);
			position++;
		}			
	}
	         
    /**
     * Inserts the customer at the specified position within this tour. This
     * method does not check if this insertion violates the time window of any
     * customers, it only does the insertion!
     */
    public void insertCustomerAtPosition(Customer customer, int position) {

    	assertThatRefsFromPositionToEndContainSameCustomersAsTour();
    	
    	//CODE_SMELL_TODO: Klasse Customer in Tour loswerden und nur noch Customer benutzen
        CustomerInTour newCustomerInTour = new CustomerInTour(this);        
        newCustomerInTour.setCustomer(customer);                   
        customers.add(position, newCustomerInTour);

        vehicle.addCapacityUsage(customer.getDemand());
        recalculateTimes();    
        recalculateEdges();
        recalculateTotalDistance();
        recalculateDemand(customer.getDemand());
        recalculateRefsWhenCustomerIsInserted(position);
        recalculateRefMatrixWhenCustomerIsInserted(position);
        
        resetRecourseCostInParentGot();
        
        assertThatRefsFromPositionToEndContainSameCustomersAsTour();
    }    	

	private void assertThatRefsFromPositionToEndContainSameCustomersAsTour() {		
		for (int i = 0; i < getCustomers().size(); i++) {
			ResourceExtensionFunction ref = refsFromPositionToEnd.get(0);
			List<Customer> customersRef = ref.getCustomers();
			Customer customerRef = customersRef.get(i);
			Customer customerTour = getCustomers().get(i);	
			assertEquals(true, customerRef.equals(customerTour));
		}
	}

	private void recalculateRefsWhenCustomerIsInserted(int position) {
		//recalculate refFromStartUpToPosition	
		for (int i = position; i < customers.size(); i++) {
			ResourceExtensionFunction refTemp = createRefFromStartAtPosition(i);			
			if (isLastPosition(i)) {
				refsFromStartUpToPosition.add(refTemp);
			} else {				
				refsFromStartUpToPosition.set(i, refTemp);
			}
		}			
		
		//recalculate refsFromPositionToEnd		
		shiftRefsUpFromPositionToEndOneTimeToTheRight(position);
		for (int i = position; i >= 0; i--) {
			ResourceExtensionFunction refTemp = createRefUpToEndAtPosition(i);
			setRefUpToEndAtPosition(refTemp,i);
		}
	}
	
	private void deleteLastRef() {
		//der Index muss customers.size sein (und nicht customers.size()-1,
		//wie das normalerweise der Fall wäre), da ja ein Kunde schon entfernt wurde 
		//und die neue customers.size nach entfernen des Kunden eins kürzer ist 
		//als bevor der Kunde entfernt wurde. Die Liste der Refs bezieht sich ja 
		//immer noch auf die alte Customer-Länge.
		refsFromPositionToEnd.remove(customers.size());
	}

	private void shiftRefsUpFromPositionToEndOneTimeToTheRight(int position) {		
		for (int i = customers.size()-1; i > position; i--) {
			if (isLastPosition(i))
				refsFromPositionToEnd.add(refsFromPositionToEnd.get(i-1));
			else
				refsFromPositionToEnd.set(i,refsFromPositionToEnd.get(i-1));
		}
	}
		
	private void setRefUpToEndAtPosition(ResourceExtensionFunction refTemp,int i) {
		if (isLastPosition(i)) {
			refsFromPositionToEnd.add(refTemp);
		} else {			
			refsFromPositionToEnd.set(i, refTemp);
		}
	}
	
	private void recalculateRefMatrixWhenCustomerIsInserted(int position) {
		calculateNecessaryMatrixEntries(position);		
	}

	private void calculateNecessaryMatrixEntries(int position) {
		//customers.size() already contains the new number of customers, after inserting or deleting!
		for (int i = position; i < customers.size(); i++)
			//index i is for rows
			for (int j = 0; j <= i; j++) {
				//index j is for columns
				ResourceExtensionFunction newEntry = calculateNewMatrixEntry(i,j);
				setEntryInRefMatrix(i,j, newEntry);
			}
	}

	private ResourceExtensionFunction calculateNewMatrixEntry(int i, int j) {
		if (j == i)
			return new ResourceExtensionFunction(customers.get(i));
		else {
			ResourceExtensionFunction newRef = refForSegment.get(i-1).get(j).clone();
			newRef.updateWithSubsequentCustomer(customers.get(i));
			return newRef;
		}
	}
	
	private void setEntryInRefMatrix(int i, int j,ResourceExtensionFunction newEntry) {
		if (i == refForSegment.size())
			refForSegment.add(new ArrayList<ResourceExtensionFunction>());
		if (j == refForSegment.get(i).size())
			refForSegment.get(i).add(newEntry);
		else 
			refForSegment.get(i).set(j, newEntry);
	}
	
	private boolean isLastPosition(int i) {
		return (i == customers.size()-1 || this.isTourEmpty());
	}

	private boolean isFirstPosition(int i) {
		return i == 0;
	}
	
	private ResourceExtensionFunction createRefUpToEndAtPosition(int i) {
		ResourceExtensionFunction refTemp;
		if (isLastPosition(i)) {
			refTemp = new ResourceExtensionFunction(depot);
			refTemp.updateWithPreceedingCustomer(customers.get(customers.size()-1));
		} else {
			refTemp = refsFromPositionToEnd.get(i+1).clone();
			refTemp.updateWithPreceedingCustomer(customers.get(i));			
		}
		return refTemp;			
	}
	
	private ResourceExtensionFunction createRefFromStartAtPosition(int i) {
		ResourceExtensionFunction refTemp;
		if (isFirstPosition(i)) {
			refTemp = new ResourceExtensionFunction(depot);
			refTemp.updateWithSubsequentCustomer(customers.get(0));
		} else {
			refTemp = refsFromStartUpToPosition.get(i-1).clone();
			refTemp.updateWithSubsequentCustomer(customers.get(i));
		}
		return refTemp;
	}
	
	private void resetRecourseCostInParentGot() {
		gotThatTourBelongsTo.resetExpectedRecourseCost();
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
                
        assertThatRefsFromPositionToEndContainSameCustomersAsTour();        
               
        Customer removedCustomer = customers.remove(position).getCustomer();
        vehicle.addCapacityUsage(removedCustomer.getDemand() * -1);
        //RUNTIME_TODO: Ist recalculate times noch nötig?
        recalculateTimes();  
        recalculateEdges();
        recalculateTotalDistance();
        //RUNTIME_TODO: ist recalculate Demand noch nötig?
        recalculateDemand(removedCustomer.getDemand() * -1);
        recalculateRefsWhenCustomerIsDeleted(position);
        recalculateRefMatrixWhenCustomerIsDeleted(position);
        
        resetRecourseCostInParentGot();
        
        assertThatRefsFromPositionToEndContainSameCustomersAsTour();
        return removedCustomer;
    }            
    
    private void recalculateRefMatrixWhenCustomerIsDeleted(int position) {
		calculateNecessaryMatrixEntries(position);    	
		deleteLastMatrixRow();    	
	}
    
	private void deleteLastMatrixRow() {
		refForSegment.remove(customers.size());
	}

	private void recalculateRefsWhenCustomerIsDeleted(int position) {
		//recalculate refFromStartUpToPosition		
		if (this.isTourEmpty())
			refsFromStartUpToPosition.clear();
		else {
			for (int i = position; i < customers.size(); i++) {
				ResourceExtensionFunction refTemp = createRefFromStartAtPosition(i);
				refsFromStartUpToPosition.set(i, refTemp);	
			}
			//last position has to be deleted, because one customer was deleted from tour
			refsFromStartUpToPosition.remove(customers.size());		
		}
							
		//recalculate refsFromPositionToEnd	
		shiftRefsToEndOneTimeToTheLeft(position);
		for (int i = position-1; i >= 0; i--) {
			ResourceExtensionFunction refTemp = createRefUpToEndAtPosition(i);
			refsFromPositionToEnd.set(i, refTemp);
		}	
	}

	private void shiftRefsToEndOneTimeToTheLeft(int position) {
		for (int i = position; i < refsFromPositionToEnd.size()-1; i++)
			refsFromPositionToEnd.set(i, refsFromPositionToEnd.get(i+1));
		deleteLastRef();
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
        for (Edge v : edges) {
            tourDistance += v.getLength();
        }
	}    
    

    /****************************************************************************
     * End Utilities for adding and deleting customers
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
    
    public String getTourAsTupelWithDemand() {
    	String s = ("(0 ");
        for (Customer c : this.getCustomers()) {
            s += (c.getCustomerNo() + ":" + c.getDemand() + " ");
        }
        s += ")";
        return s;
	}

	public void print() {
		System.out.println(getTourAsTupel());		
	}

	public List<ArrayList<ResourceExtensionFunction>> getRefMatrix() {
		return refForSegment;
	}

	public void printRefMatrix() {
		for (int i = 0; i < refForSegment.size(); i++) {
		    for (int j = 0; j <= i; j++) {
		    	System.out.print("X" + " ");
//		        System.out.print(refForSegment.get(i).get(j) + " ");
		    }
		    System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	@SuppressWarnings("unused")
	private void printRefsFromStart() {
		System.out.println("Länge refsFromStart...: " + refsFromStartUpToPosition.size());
		for (int i = 0; i < refsFromStartUpToPosition.size(); i++)
			refsFromStartUpToPosition.get(i).print();
	}
	
	@SuppressWarnings("unused")
	private void printRefsToEnd() {
		System.out.println("Länge refsToEnd...: " + refsFromPositionToEnd.size());
		for (int i = 0; i < refsFromPositionToEnd.size(); i++)
			refsFromPositionToEnd.get(i).print();
	}

	public void setSolutionValue(double totalDistanceOfAllTours) {
		solutionValue = totalDistanceOfAllTours;
	}

	public double getSolutionValue() {
		return solutionValue;
	}

	public String getUseOfCapacity() {
		String s = "(" + getDemandOnTour() + ":" + getVehicle().getCapacity() + ")";
		return s;
	}

	


}
