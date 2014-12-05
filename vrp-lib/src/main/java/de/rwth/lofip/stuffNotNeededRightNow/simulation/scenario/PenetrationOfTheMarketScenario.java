package de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario;

import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

public class PenetrationOfTheMarketScenario {
	
	static int seed = 1;
	
	public void setSeed(int seed){
		PenetrationOfTheMarketScenario.seed = seed;
	}
	
	/**
     * Static helper method which generates a penetration of the marked scenario.
     * 
     * @param vrpProblem - the VrpProblem 
     * @param solution - the solution for the VrpProblem
     * @param percentageOfCustomersWithControlStation - describes how many percentage of the 
     * customer have a control station. Range [0.0;1.0]
     * @param deviationWith - describes how much the demand of customer with a control station can deviate from expected demand.
     * Say demand should range between +/-10% deviation of expected demand, then param deviationWith should be set to 0.1 . Range [0.0;1.0]
     * @param deviationWithout - describes how much the demand of customer without a control station can deviate from expected demand. 
     * Say demand should range between +/-20% deviation of expected demand, then deviationWithout should be set to 0.2. Range [0.0;1.0]
     *  
     * @author Olga Bock
     */
	// kann in dierser Konstruktion nur mit einer bereit vorhandenen Solution aufgerufen werden
	public static DemandScenario createScenarioForPenetrationOfTheMarked(
												VrpProblem vrpProblem, Solution solution,
												double percentageOfCustomersWithControlStation, 
												double deviationWith, double deviationWithout)
    {	
		seed++;
		Random randomPercentageGeneration = new Random(seed);
		seed++;
		Random randomDemandGeneration = new Random(seed);
		seed++;		
		Random randomTimeGeneration = new Random(seed);
    	
		int amountOfCustomersWithControlStation = (int) Math.round(vrpProblem.getCustomers().size()*percentageOfCustomersWithControlStation);
		int startInterval=0;
		if(percentageOfCustomersWithControlStation!=1.0){		
    		startInterval = randomPercentageGeneration.nextInt((int) (vrpProblem.getCustomers().size() - amountOfCustomersWithControlStation));
    	}
		
    	int endInterval = startInterval + amountOfCustomersWithControlStation;
		
		DemandScenario demandScenario = new DemandScenario();
		//System.out.println("Alle KundenNummern :");
    	for (Customer c: vrpProblem.getCustomers())
    	{  
    		
    		//System.out.println(c.getCustomerNo());
    		//only generate event if it would not happen at time point 0.
    		if (c.getTimeWindowOpen()>0)
    		{
    			// wenn die Nummern sich im Interval befinden, sind es Kunden mit Leitstandsanbindung
    			if(c.getCustomerNo()>startInterval && c.getCustomerNo()<=endInterval)
    			{
    				Event event = new Event(c.getCustomerNo());
    				// kleinere Schwankungsbreite bei den Kunden ohne
    				double sd = deviationWith * 1/3 * c.getDemand();

            		int demand;        	
        		    double val = randomDemandGeneration.nextGaussian() * sd + c.getDemand();
        		    demand = (int) Math.round(val);
        		    if (demand > vrpProblem.getVehicles().iterator().next().getCapacity())
        			    demand = (int) vrpProblem.getVehicles().iterator().next().getCapacity();
        		    if (demand < 1)
        		        demand = 1;   
        		    
    				event.setDemand(demand);
    				// Zeitpunkt ist zufällig gewählt
    				int time = randomTimeGeneration.nextInt((int) c.getTimeWindowOpen());
    				event.setPointInTime(time);
    				demandScenario.addEvent(event);
    				//System.out.println("    Kunden mit Leitstand:"+c.getCustomerNo());
    				
    			}
    			// ansonsten, sind es Kunden ohne Leitstandsanbindung
    			else
    			{
    				Event event = new Event(c.getCustomerNo());
    				double sd = deviationWithout * 1/3 * c.getDemand();
    				
            		int demand;        	
        		    double val = randomDemandGeneration.nextGaussian() * sd + c.getDemand();
        		    demand = (int) Math.round(val);
        		    if (demand > vrpProblem.getVehicles().iterator().next().getCapacity())
        			    demand = (int) vrpProblem.getVehicles().iterator().next().getCapacity();
        		    if (demand < 1)
        		        demand = 1;   
    				
    				event.setDemand(demand);
    				//Bekanntwerden der Änderung erst wenn das Fahrzeug vor Ort ist
    				int time = (int) c.getTimeWindowOpen();
    				if (solution.getCustomerWithId(c.getCustomerNo()).getArrivalTime() > c.getTimeWindowOpen()){
    					time = (int) solution.getCustomerWithId(c.getCustomerNo()).getArrivalTime();
    				}
    				event.setPointInTime(time);
    				demandScenario.addEvent(event);
    			}
    		
    		}
    	}    		
		return demandScenario;    	
    }
	

}
