package de.rwth.lofip.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.rwth.lofip.library.solver.util.DistanceComparatorWithSimilarity;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.util.CustomerInTour;

/**
 * @author Andreas Braun
 */
public class GroupOfTours {

	/****************************************************************************
     * Fields
     ***************************************************************************/
	

	final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 1;
	final static double FLUCTUATION_OF_DEMAND_IN_PERCENTAGE = 0.2;
	final static int MAXIMAL_NUMBER_OF_TOURS = 2;		
	
	protected List<Tour> tours = new ArrayList<Tour>();	

    /****************************************************************************
     * Constructors
     ***************************************************************************/
  
    private void addTour(Tour t) {
    	tours.add(t);
    }
    
	public List<Tour> getTours() {	
		return tours;
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
		Tour t = new DeterministicTour(vrpProblem.getDepot(),
				new Vehicle(new Random().nextInt(Integer.MAX_VALUE), vrpProblem.getVehicles().iterator().next().getCapacity()));
		addTour(t);
	}
	
	public void insertCustomerIntoLastTour(Customer customer, int iteration,
			String simpleName) {
		Tour t = getLastTour();
		t.addCustomer(customer, iteration, simpleName);
	}
	
    public Tour getLastTour() {
    	return tours.get(tours.size() - 1);
	}

	@Override
    public GroupOfTours clone() {
    	GroupOfTours got = new GroupOfTours();   		    	                       
        for (Tour t : tours)             	      
	        	got.addTour(t.clone());	        	        	        
        return got;
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
		//Benutze Durchschnitt oder Maximum für Simval (wenn es berechnet wird wie in Lei et al)
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
    
   
	/****************************************************************************
     * Calculation of Probabilities and Recourse Cost
     ***************************************************************************/        
   
    public double getExpectedRecourseCost() {
    	return 0;
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
//	    		System.out.println("L�sung vor Demand Scenario: " + solution.getSolutionAsTupel());
//	    	    System.out.println("L�sung nach Demand Scenario: " + temporarySolution.getSolutionAsTupel());
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
