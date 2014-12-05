package de.rwth.lofip.library.solver.repair.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.SchemaCreatingSchemaOutput;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.initialSolver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.library.util.VrpProblemUtils;
import de.rwth.lofip.library.util.VrpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.RepairSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolutionUtils;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;

/**
 * Test class which contains tests for the class RepairedSolution
 * 
 * @author Andreas Braun
 */
public class RepairedSolution_TEST {

	Customer c11 = new Customer();
	Customer c12 = new Customer();
	Customer c13 = new Customer();
	List<Solution> solutionList = new LinkedList<Solution>();
	Solution solution;
	Solution solution2;
	Tour tour3;
	private static VrpConfiguration configuration = new VrpConfiguration();

	@Before
	public void setUp() {
		// set up
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(1);
		c11.setDemand(10);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		c12.setCustomerNo(2);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(40);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

		c13.setCustomerNo(3);
		c13.setxCoordinate(25);
		c13.setyCoordinate(85);
		c13.setDemand(20);
		c13.setTimeWindowOpen(625);
		c13.setTimeWindowClose(721);
		c13.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new DeterministicTour(depot, vehicle);
		tour.addCustomer(c11);
		tour.addCustomer(c12);
		tour.addCustomer(c13);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c11);
		vrpProblem.addCustomer(c12);
		vrpProblem.addCustomer(c13);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		solution = new Solution(vrpProblem);
		solution.addTour(tour);
		// End set up

	}

	@Test
	public void testAdditionalCost() {

		try {
			int newDemand = 55;
			Event event = new Event(1, newDemand, 100);
			List<RepairedSolution> rep = new RepairSolution().repair(solution,
					event);

			System.out.println("Strecke alte L�sung: "
					+ solution.getTotalDistanceOfAllTours());
			System.out.println("Kosten alte L�sung: "
					+ solution.getCostInEuro());
			System.out.println("Diferenz rep und ist : "
					+ rep.get(0).getCostRepairVsCostNewTour());
			System.out.println("Neue L�sung: "
					+ (rep.get(0).getNewSolution()
							.getSolutionAsStringWithCustomer()));

			System.out.println("Strecke neue L�sung: "
					+ (rep.get(0).getNewSolution().getTotalDistanceOfAllTours()));
			System.out.println("Kosten neue L�sung : "
					+ (rep.get(0).getNewSolution().getCostInEuro()));

			System.out.println("zusatz distanz: "
					+ rep.get(0).getAdditionalDistance());

			System.out.println("zusatz Kosten "
					+ rep.get(0).getAdditionalCost());

			int newDemand2 = 55;
			Event event2 = new Event(2, newDemand2, 200);
			List<RepairedSolution> rep2 = new RepairSolution().repair(rep
					.get(0).getNewSolution(), event2);

			System.out.println("Strecke alte L�sung: "
					+ rep2.get(0).getOldSolution().getTotalDistanceOfAllTours());
			System.out.println("Kosten alte L�sung: "
					+ rep2.get(0).getOldSolution().getCostInEuro());
			System.out.println("Diferenz rep und ist : "
					+ rep2.get(0).getCostRepairVsCostNewTour());
			System.out.println("Neue L�sung: "
					+ (rep2.get(0).getNewSolution()
							.getSolutionAsStringWithCustomer()));

			System.out.println("Strecke neue L�sung: "
					+ (rep2.get(0).getNewSolution().getTotalDistanceOfAllTours()));
			System.out.println("Kosten neue L�sung : "
					+ (rep2.get(0).getNewSolution().getCostInEuro()));

			System.out.println("zusatz distanz: "
					+ rep2.get(0).getAdditionalDistance());

			System.out.println("zusatz Kosten "
					+ rep2.get(0).getAdditionalCost());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	// public void testDaviation(){
	//
	// Solution temporarySolution = solution.clone();
	// VrpProblem oldVrpProblem = temporarySolution.getVrpProblem().clone();
	//
	// Event e = new Event();
	// e.setCustomerNo(c11.getCustomerNo());
	// e.setDemand(20);
	// e.setPointInTime(270);
	//
	// KeyPerformanceIndicators keyPerformanceIndicators = new
	// KeyPerformanceIndicators();
	//
	//
	// System.out.println("Processing Event for Customer " + e.getCustomerNo() +
	// " at time " + e.getPointInTime());
	// List<RepairedSolution> solutionList = new
	// RepairSolution().repair(temporarySolution, e);
	// temporarySolution = solutionList.get(0).getNewSolution();
	// solutionList.get(0).getOldSolution().setVrpProblem(oldVrpProblem);
	// keyPerformanceIndicators.addRepairedSolution(solutionList.get(0));
	//
	//
	// System.out.println("demand alte L�sung: " +
	// oldVrpProblem.getTotalDemand());
	// System.out.println("demand alte L�sung in rs: " +
	// solutionList.get(0).getOldSolution().getVrpProblem().getTotalDemand());
	// System.out.println("demand neue L�sung in rs: " +
	// solutionList.get(0).getNewSolution().getVrpProblem().getTotalDemand());
	// System.out.println("vrp ist identisch in beiden L�sungen: " +
	// solutionList.get(0).getOldSolution().getVrpProblem().equals(solutionList.get(0).getNewSolution().getVrpProblem())
	// );
	//
	//

	// }
	
	
	@Test
	public void testXmlRS() throws Exception{

		//setUp();
		//---------VRP------------------------------------------------
		File datei1 = new File("solomon-problems/rc201t.txt");
		BufferedReader br = new BufferedReader(new FileReader(datei1));
		String zeile = "";
		List<String> liste = new ArrayList<String>(0);
		while ((zeile = br.readLine()) != null) {
			liste.add(zeile);
		}
		
		VrpProblem vrpProblem = VrpUtils
                .createProblemFromStringList(liste);
		br.close();
		
		//-------------VRP in Datei schreiben---------------- (\/)
//				JAXBContext ctx2 = JAXBContext
//						.newInstance(new Class[] { VrpProblem.class });
//				
//				Marshaller marsh2 = ctx2.createMarshaller();
//				File file1 = new File("testVRP.xml");
//				marsh2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//				marsh2.marshal(vrpProblem, file1);
//				marsh2.marshal(vrpProblem, System.out);
		

				//--------------Solution--------------------------------------
		double pc =  (6.0/7.0);//0.85715;
		
		VrpProblem vrpTemp = vrpProblem.clone();
        vrpTemp = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrpTemp, pc);
        Solution solutionDat = null;
        //VrpProblem vrpProblemWithModifiedCapacity = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrp, pc);
        try
        {
            //Solve VRP Problem
            long startTime = System.nanoTime();
            Solution solution1 = createSolution(vrpTemp);
            long endTime = System.nanoTime();
            long timeTaken = (endTime - startTime);
            
            vrpTemp = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrpTemp, (1/pc));
            
            solution1.setVrpProblem(vrpTemp);
            solution1.setTimeNeeded(timeTaken);	                
            //set vehicle capacities in solution to capacity of the vrp problem
            for (Tour t : solution1.getTours())
            {
            	t.getVehicle().setCapacity(vrpTemp.getVehicles().iterator().next().getCapacity());
            }  
            solutionDat = solution1;
            
        }
        catch(Exception id)
        {
            System.err.println( "Solution aus Lei Solver ist nicht feasible" );
            id.printStackTrace();
            System.exit(0);
        }
    
		
		
  
        
        //-------------Solution in Datei schreiben----------------
		JAXBContext ctx1 = JAXBContext
				.newInstance(new Class[] { Solution.class });
		
		SchemaCreatingSchemaOutput out = new SchemaCreatingSchemaOutput();
		ctx1.generateSchema(out);
		System.out.println(out.out.toString());
		Schema imlicitSchema = out.createSchema();
//		System.out.println(out.out.toString());
		
		Marshaller marsh1 = ctx1.createMarshaller();
		File file = new File("SolutionSzenario.xml");
		marsh1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		
		
//		marsh1.marshal(solutionDat, System.out);
//		marsh1.marshal(solutionDat, file);
//		
//		File file5 = new File("SolutionSzenario.xsd");
//		
//		FileOutputStream outputStream = FileUtils.openOutputStream(
//				file5, true);
//		IOUtils.write(out.out.toString(), outputStream);
     
        //----------------DemandSzenario------------------------------
//		File datei2 = new File("solomon-problems/r201n.txt");
//		BufferedReader br2 = new BufferedReader(new FileReader(datei2));
//		String zeile2 = "";
//		List<String> liste2 = new ArrayList<String>(0);
//		while ((zeile2 = br2.readLine()) != null) {
//			liste2.add(zeile2);
//		}
//		DemandScenario dScenario = DemandScenarioUtils
//				.createScenarioFromStringList(liste2);
//		br2.close();
		
		
//		DemandScenario demandScenario = DemandScenarioUtils
//				.createScenarioFromVrpProblem(vrpProblem, 0.2);
//		System.out.println(demandScenario.getDemandScenarioAsString());
		
		
		
		
		
		
		
		
		//---------------RepairedSolution---------------------------------
	
//		List<RepairedSolution> rep = new RepairSolution().repair(solutionDat,
//				demandScenario.getEvents().first());
		
		//Event 1
		Event event1 = new Event(14, 3, 35);
		List<RepairedSolution> rep = new RepairSolution().repair(solutionDat, event1);
		
//		JAXBContext ctx2 = JAXBContext
//				.newInstance(new Class[] { RepairedSolution.class });
//		
//		SchemaCreatingSchemaOutput out1 = new SchemaCreatingSchemaOutput();
//		ctx2.generateSchema(out1);
//		Schema imlicitSchema1 = out1.createSchema();
//		Marshaller marsh2 = ctx2.createMarshaller();
//		marsh2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		File file2 = new File("RepairedSzenario1.xml");
//		marsh2.marshal(rep.get(0), file2);
		
		RepairedSolutionUtils.printAllSolutionsInRepairedSolutionList(rep);
//		
//		Event event2 = new Event(4,3,rep.get(0).getNewSolution().getCustomerWithId(3).getArrivalTime()-1);
//		List<RepairedSolution> rep2 = new RepairSolution().repair(rep.get(0).getNewSolution(), event2);
//		
//		JAXBContext ctx3 = JAXBContext
//				.newInstance(new Class[] { RepairedSolution.class });
//		
//		SchemaCreatingSchemaOutput out2 = new SchemaCreatingSchemaOutput();
//		ctx3.generateSchema(out2);
//		Schema imlicitSchema2 = out2.createSchema();
//		Marshaller marsh = ctx3.createMarshaller();
//		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		File file3 = new File("RepairedSzenario2.xml");
//		marsh.marshal(rep2.get(0), file3);
//		
//		RepairedSolutionUtils.printAllSolutionsInRepairedSolutionList(rep);
//		
//		Event event3 = new Event(3,3,rep.get(0).getNewSolution().getCustomerWithId(3).getArrivalTime());
//		List<RepairedSolution> rep3 = new RepairSolution().repair(rep2.get(0).getNewSolution(), event3);
//		
//		JAXBContext ctx4 = JAXBContext
//				.newInstance(new Class[] { RepairedSolution.class });
//		
//		SchemaCreatingSchemaOutput out3 = new SchemaCreatingSchemaOutput();
//		ctx4.generateSchema(out3);
//		Schema imlicitSchema3 = out3.createSchema();
//		Marshaller marsh3 = ctx4.createMarshaller();
//		marsh3.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		File file4 = new File("RepairedSzenario3.xml");
//		marsh3.marshal(rep3.get(0), file4);
//		
//		RepairedSolutionUtils.printAllSolutionsInRepairedSolutionList(rep);
//		
	/*


		File file5 = new File("testRS.xsd");
		
		FileOutputStream outputStream = FileUtils.openOutputStream(
				file5, true);
		IOUtils.write(out3.out.toString(), outputStream);
//		
//		
//		
//		
//
//
//		Marshaller marsh = ctx.createMarshaller();
//		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		File file3 = new File("testRS.xml");
//		marsh.marshal(rep.get(0), file3);
//		marsh.marshal(rep.get(0), System.out);
		
 
//		Unmarshaller unmarsh = ctx.createUnmarshaller();
//		Object o = unmarsh.unmarshal(file);
//		
//		assertEquals(rep.get(0).getClass(), o.getClass());


		//file.delete();

*/		
		
		
	}
	
	

	
	//------------Help Class--------------------------------------------------
	public Solution createSolution(VrpProblem problem) throws Exception
	{
		// first, create an initial solution
		DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
		Solution solution = initialSolver.solve(problem);
    
		System.out.println("Berechnete Lösung mit PFI: " + solution.getSolutionAsTupel());             
    
		//then, improve solution
		Solution improvedSolution = new	DeterministicLeiEtAlHeuristic().improve(solution,
            configuration);
    
		System.out.println("Berechnete Lösung mit LeiEtAl: " + improvedSolution.getSolutionAsTupel());
    
		if (solution.isSolutionFeasibleWrtDemand() == false)
		{
			System.err.println("Lösung der Instanz " + problem.getDescription() + " nicht feasible wrt demand");
			System.err.println("Lösung sieht so aus: \n" + improvedSolution.getSolutionAsStringWithCustomer());
			throw new Exception();
		}						
    
		return improvedSolution;               
	}

}
