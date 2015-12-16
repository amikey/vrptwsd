package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.solver.util.SimpleTourUtils;
import de.rwth.lofip.library.solver.util.TourUtils;
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




	

	

	
    
  
//    @Override
//    public double getExpectedRecourseCost() {
//    	
//    	//create solution
//    	//to do so, first create vrpProblem
//    	VrpProblem vrpProblem = new VrpProblem();
//    	vrpProblem.setCustomers(customers);
//    	
//    	Set<Depot> depotSet = new HashSet<Depot>();
//    	depotSet.add(depot);
//    	vrpProblem.setDepots(depotSet);
//    	
//    	Set<Vehicle> vehicleSet = new HashSet<Vehicle>();
//    	vehicleSet.addAll(vehicles);
//    	vrpProblem.setVehicles(vehicleSet);
//    	
//    	//now create solution
//    	Solution solution = new Solution(vrpProblem);
//    	solution.setTours(tours);
//    	
//    	// Process DemandScenarios
//    	AverageValuesForMultipleDemandScenariosOnOneSolomonInstance avg = new AverageValuesForMultipleDemandScenariosOnOneSolomonInstance();		
//    	
//    	for (int i = 1; i <= NUMBER_OF_DEMAND_SCENARIO_RUNS; i++)
//    	{
//    		DemandScenario demandScenario = DemandScenarioUtils.createScenarioFromVrpProblem(vrpProblem, FLUCTUATION_OF_DEMAND_IN_PERCENTAGE);
//    		KeyPerformanceIndicators keyPerformanceIndicators = new KeyPerformanceIndicators(demandScenario);
//    		//now process demandScenario
//    		Solution temporarySolution = solution.cloneCompletelySeperateCopy();
//        	
//	    		for (Event e : demandScenario.getEvents())
//	    		{				            
//	    			VrpProblem oldVrpProblem = temporarySolution.getVrpProblem().clone();
//	    			System.out.println("Processing Event for Customer " + e.getCustomerNo() + " at time " + e.getPointInTime());		            		
//	    			List<RepairedSolution> solutionList = new RepairSolution().repair(temporarySolution, e);	        			
//	    			temporarySolution = solutionList.get(0).getNewSolution();
//	    			solutionList.get(0).getOldSolution().setVrpProblem(oldVrpProblem);
//	    			keyPerformanceIndicators.addRepairedSolution(solutionList.get(0));
//	    		}  
//	    		avg.addDemandScenario(keyPerformanceIndicators);
//	    		System.out.println("Lï¿½sung vor Demand Scenario: " + solution.getSolutionAsTupel());
//	    	    System.out.println("Lï¿½sung nach Demand Scenario: " + temporarySolution.getSolutionAsTupel());
//    	}
//    	return avg.getMeanValueOfAdditionalDistanceTotalDistancePerScenario(); 	
//    }
      
//    private List<RepairedSolution> repair(Solution temporarySolution, Event e)
//    {
//    	Solution oldSolution = temporarySolution.clone();
//    	new RepairSolution().updateVrpProblemAttachedToSolution(temporarySolution, e);
//    	if (temporarySolution.isSolutionFeasibleWrtDemand() == false)
//    	{
//    		//find tour
//    		Tour tour;
//    		for (Tour t: temporarySolution.getTours())
//    			for (Customer c : t.getCustomers())
//    				if (c.getCustomerNo() == e.getCustomerNo())
//    					tour = 
//    	}
//    }
//    
}
