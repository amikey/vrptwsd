package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.solver.util.SimpleTourUtils;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.SetUpUtils;

/**
 * @author Andreas Braun
 */
public class GroupOfTours implements ElementWithTours, SolutionElement {

	/****************************************************************************
     * Fields
     ***************************************************************************/

	final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 1;
	final static double FLUCTUATION_OF_DEMAND_IN_PERCENTAGE = 0.2;
	static int MAXIMAL_NUMBER_OF_TOURS = 1;		
	
	protected List<Tour> tours = new ArrayList<Tour>();	
	RecourseCost expectedRecourseCost = null;

	public void addTour(Tour t) {
    	tours.add(t);
    	t.setParentGot(this);
    	resetExpectedRecourseCost();
    }
    
	public static void setNumberOfToursInGot(int numberOfToursInGot) {
		MAXIMAL_NUMBER_OF_TOURS = numberOfToursInGot;
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

	@Override
	public double getTotalDistance() {
		//TODO: distance zwischenspeichern und nur ändern, wenn sich Touren ändern
		double distance = 0;
        for (Tour t : this.getTours()) {
            distance += t.getTotalDistanceWithCostFactor();
        }
        return distance;
    }
    
	@Override
    public void removeEmptyTours() {
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
		return tours.size() < MAXIMAL_NUMBER_OF_TOURS;
	}
	
	public void createNewTour(VrpProblem vrpProblem) {
		Tour t = new Tour(vrpProblem.getDepot(),
				new Vehicle(vrpProblem.getVehicles().iterator().next().getCapacity()));
		addTour(t); //recourse Cost wird schon resetted, wenn neue Tour geaddet wird
		t.setParentGot(this);
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

	public int getMaximalNumberOfTours() {
		return MAXIMAL_NUMBER_OF_TOURS;
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
				if (similarity > maxSimVal)
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
   
    public RecourseCost getExpectedRecourseCost() {
    	if (expectedRecourseCost == null) {   		    		
    		double overallRecourseCost = 0;
    		int [] listOfPossibleSolutionValues = new int[1000000]; //wird automatisch mit 0-en initialisiert
    		int numberOfDifferentRecourseActions = 0;
    		SimulationUtils.resetSeed();
    		
    		for (int i = 1; i <= NUMBER_OF_DEMAND_SCENARIO_RUNS; i++) {
    			GroupOfTours gotClone = this.cloneWithCopyOfCustomers();
    			SimulationUtils.setDemandForCustomersWithDeviation(gotClone, FLUCTUATION_OF_DEMAND_IN_PERCENTAGE);    			
    			if (!ElementWithToursUtils.isElementDemandFeasible(gotClone)) {
    				LocalSearchForElementWithTours ls = new LocalSearchForElementWithTours(); //TODO: Hier auch Tabu Search testen
    				//create Solution from gotClone for processing with local search    				    				
    				ls.improve(gotClone);
    				if (!ElementWithToursUtils.isElementDemandFeasible(gotClone)) //got is still demand infeasible -> no feasible solution could be found for demand
    					gotClone.addEmptyTour();
    					ls.improve(gotClone);
    			}
    			//TODO: Will ich hier auch zusätzliche Tour mit doppelten Kosten bestrafen? Eigentlich schon, oder?
    			double recourseCost = -this.getTotalDistance() + gotClone.getTotalDistance();
    			overallRecourseCost += recourseCost;
    			
    			//calculate number of different recourse actions
    			//TODO: For problems with more than 100 customers its possible that the index exceeds the size of the array -> modulo wie in TabuList
    			if (listOfPossibleSolutionValues[(int) gotClone.getTotalDistance() * 1000] == 0) {
    				numberOfDifferentRecourseActions++;
    				listOfPossibleSolutionValues[(int) gotClone.getTotalDistance() * 1000] = 1;
    			}
    		}
    		overallRecourseCost = overallRecourseCost / NUMBER_OF_DEMAND_SCENARIO_RUNS;
    		
    		expectedRecourseCost = new RecourseCost(overallRecourseCost, numberOfDifferentRecourseActions);
    	}    	
		return expectedRecourseCost;
    }
    
    private void addEmptyTour() {
    	Tour tour = SimpleTourUtils.getEmptyTourWithDoubleCostFactor(getFirstTour());
    	addTour(tour);
    	tour.setParentGot(this);
	}

	@Override
    public GroupOfTours clone() {
    	GroupOfTours got = new GroupOfTours();   		    	                       
        for (Tour t : tours)  {
        	Tour tour = new Tour(t);
        	got.addTour(tour);
        	tour.setParentGot(got);
        }	        
        got.setExpectedRecourseCost(expectedRecourseCost);
        return got;
    }
    
	GroupOfTours cloneWithCopyOfCustomers() {
		GroupOfTours got = new GroupOfTours();
		for (Tour t : tours) {
			Tour tour = t.cloneWithCopyOfCustomers();
			got.addTour(tour);
			tour.setParentGot(got);
		}        	
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
