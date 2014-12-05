package de.rwth.lofip.library.solver.repair;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.RepairSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.DemandScenario;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.DemandScenarioUtils;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;


public class RepairSolution_TEST2 {
	
//	@Test
//	public void testParetoOptimum() {
//		
//		try {
//			Demonstrator s = new Demonstrator();
//			Solution solution = s.loadSolution();
//			DemandScenario ds = s.loadDS();
//			
//			// now process demandScenario
//			for (Event e : ds.getEvents()) {
//
//			VrpProblem oldVrpProblem = solution.getVrpProblem().clone();
//			System.out.println("Bearbeite Ereignis f�r Kunde "
//					+ e.getCustomerNo() + " zum Zeitpunkt "
//					+ e.getPointInTime());
//			List<RepairedSolution> solutionList = new RepairSolution()
//					.repair(solution, e);
//			
//			solution = solutionList.get(0).getNewSolution();
//			solutionList.get(0).getOldSolution()
//					.setVrpProblem(oldVrpProblem);
//			
//			System.out.println("Solution List vor �berpr�fung auf Dominanz");
//			printSolutionList(solutionList);
//			
//			solutionList = new RepairSolution().checkForDominatedSolutions(solutionList);
//			
//			System.out.println("Solution List nach �berpr�fung auf Dominanz");
//			printSolutionList(solutionList);
//			
//			solutionList = new RepairSolution().checkForEqualSolutions(solutionList);
//			
//			System.out.println("Solution List nach �berpr�fung auf �quivalenz");
//			printSolutionList(solutionList);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	@Test
	public void testParetoOptimumWithSolomonInstance() throws Exception {
		Solution solution = SetUpUtils.SetUpR201Solution();
		Event e = new Event(14, 3, 3);
		VrpProblem oldVrpProblem = solution.getVrpProblem().clone();
		
		System.out.println("Bearbeite Ereignis f�r Kunde "
				+ e.getCustomerNo() + " zum Zeitpunkt "
				+ e.getPointInTime());
		List<RepairedSolution> solutionList = new RepairSolution()
				.repair(solution, e);
		
		solution = solutionList.get(0).getNewSolution();
		solutionList.get(0).getOldSolution()
				.setVrpProblem(oldVrpProblem);
		
		System.out.println("Solution List vor �berpr�fung auf Dominanz");
		printSolutionList(solutionList);
		
		solutionList = new RepairSolution().checkForDominatedSolutions(solutionList);
		
		System.out.println("Solution List nach �berpr�fung auf Dominanz");
		printSolutionList(solutionList);
		
		solutionList = new RepairSolution().checkForEqualSolutions(solutionList);
		
		System.out.println("Solution List nach �berpr�fung auf �quivalenz");
		printSolutionList(solutionList);		
	}
	
	@Test
	public void testParetoOptimumWithAllSolomonInstances() throws Exception {
		List<Solution> solutions = SetUpUtils.SetUpAllSolutions();
		for (Solution solution : solutions) {
			DemandScenario ds = DemandScenarioUtils.createScenarioFromVrpProblem(solution.getVrpProblem(), 0.45);
			processDemandScenario(ds, solution);
		}			
	}
	
	private void processDemandScenario(DemandScenario demandScenario, Solution solution) throws IOException {
		//now process demandScenario	            	
    	int eventNumber = 0;
    	for (Event e : demandScenario.getEvents())
		{	
    		eventNumber++;
    		e.setEventNumber(eventNumber);
    		
    		VrpProblem oldVrpProblem = solution.getVrpProblem().clone();		            		
			List<RepairedSolution> solutionList = new RepairSolution().repair(solution, e);	        			
			solution = solutionList.get(0).getNewSolution();
			solutionList.get(0).getOldSolution().setVrpProblem(oldVrpProblem);     			
			DemandScenarioUtils.updateTWinDS(solution, demandScenario);			
				
			System.out.println("Solution List vor �berpr�fung auf Dominanz");
			printSolutionList(solutionList);
			
			solutionList = new RepairSolution().checkForDominatedSolutions(solutionList);
			
			System.out.println("Solution List nach �berpr�fung auf Dominanz");
			printSolutionList(solutionList);
			
			solutionList = new RepairSolution().checkForEqualSolutions(solutionList);
			
			System.out.println("Solution List nach �berpr�fung auf �quivalenz");
			printSolutionList(solutionList);	
		}		
	}

	private void printSolutionList(List<RepairedSolution> solutionList) {
		String s = "(";
		for (RepairedSolution rs : solutionList)
		{
			s += rs.printParetoCriteria();
			s += "; ";
		}
		s += ")";
		System.out.println(s);
	}
		
	

}