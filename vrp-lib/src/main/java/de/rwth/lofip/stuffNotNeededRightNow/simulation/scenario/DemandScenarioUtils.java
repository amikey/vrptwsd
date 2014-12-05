package de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;


public class DemandScenarioUtils {
	
	static int seed = 1;
	
	public void setSeed(int seed){
		DemandScenarioUtils.seed = seed;
	}
	
	public enum ImportFileState {
        NEW, EVENT
    }	

    /**
     * Given a list of Strings, create a changed Demand-Scenario from that information. The
     * format of that list needs to be like
     * 
     * <pre>
     * R101_n
     *  
     * CUSTOMER-EVENT
     * CUST NO.   DEMAND    Action TIME
     *        
     * 1          10       161          
     * 2           7        50           
     * 3          13       116          
     * ...
     * </pre>
     * <p>
     * The first line is a textual description of the Scenario instance and 
     * should be before "_" the same as the solved VRP.<br>
     * The second and third line are fixed.<br>
     * The following lines until the end of the file are definitions of
     * customers-events. All values must be separated by whitespace.
     * The instance should be the the same size as the solved VRP.
     * The point of time of the event should be never after the ready 
     * time of the time window in the solved VRP.
     * </p>
     * 
     * @param list
     * @return
     */
    public static DemandScenario createScenarioFromStringList(List<String> list) {
    	// Create a new scenario
        ImportFileState state = ImportFileState.NEW;
        DemandScenario demandScenario = new DemandScenario();

        for (String line : list) {
            // ignore empty lines
        	  line = StringUtils.normalizeSpace(StringUtils.trim(line));
            if (line.isEmpty()) {
                continue;
            }
          
            switch (state) {
            // Set a new description of the scenario 
            case NEW:
                if (line.startsWith("CUSTOMER-EVENT")) {
                    state = ImportFileState.EVENT;
                } else {
                	demandScenario.setDescription(line);
                }
                break;
             // Create a new events for the scenario
            case EVENT:
            	if (!line.startsWith("CUST")) {
            		String[] split = StringUtils.split(line," ");
            		// a test to show the selected line,
            		//System.out.println("->"+line+"<-");
                	Event event = new Event();
                	event.setCustomerNo(Long.parseLong(split[0]));
                	event.setDemand(Long.parseLong(split[1]));
                	event.setPointInTime(Long.parseLong(split[2]));
                	demandScenario.addEvent(event);
                	// a test to compare the created event-contend with the line.
                	//System.out.println("<"+event.getCustomerNo()+","+event.getDemand()+","+event.getPointInTime()+">");
            	}
                break;
            }
        }
        return demandScenario;
    }
    
    
    /**
     * Static helper method which generates a demand scenario.
     * 
     * @param deviaton: describes how much the demand can deviate from expected demand
     * Say demand should range between +/-20% deviation of expected demand, then param deviation should be set to 0.2
     *  
     * @author Andreas Braun <braun@dpor.rwth-aachen.de>
     */
    public static DemandScenario createScenarioFromVrpProblem(VrpProblem vrpProblem, double deviation)
    {    	
		seed++;
		Random randomDemandGeneration = new Random(seed);
		seed++;		
		Random randomTimeGeneration = new Random(seed);
    	DemandScenario demandScenario = new DemandScenario();
    	for (Customer c: vrpProblem.getCustomers())
    	{   		
    		//only generate event if it would not happen at time point 0.
    		if (c.getTimeWindowOpen()>0)
    		{
    			Event event = new Event(c.getCustomerNo());
        		double sd = deviation * 1/3 * c.getDemand();
        		
        		int demand;        	
    		    double val = randomDemandGeneration.nextGaussian() * sd + c.getDemand();
    		    demand = (int) Math.round(val);
    		    if (demand > vrpProblem.getVehicles().iterator().next().getCapacity())
    			    demand = (int) vrpProblem.getVehicles().iterator().next().getCapacity();
    		    if (demand < 1)
    		        demand = 1;        		
    		 
        		event.setDemand(demand);
    			int time = randomTimeGeneration.nextInt((int) c.getTimeWindowOpen());
	    		event.setPointInTime(time);
	    		demandScenario.addEvent(event);
    		}
    	}    		
		return demandScenario;    	
    }
       
    
    public static DemandScenario updateTWinDS(Solution solution, DemandScenario ds)
    {
    	for (Event event : ds.getEvents())
    		//Falls neue Ankunftszeit des Kunden vor dem Auftreten des Events liegt,
    		//setze Zeitpunkt des Events auf Ankunftszeit des Kunden
    		if (solution.getCustomerWithId(event.getCustomerNo()).getArrivalTime() < event.getPointInTime())
    			event.setPointInTime(solution.getCustomerWithId(event.getCustomerNo()).getArrivalTime());
    	    //TODO: eigentlich müsste hier noch unterschieden werden, ob es ein Kunde mit Anbindung an Leitstand war oder ohne.
    	return ds;
    }
    
}
