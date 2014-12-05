package de.rwth.lofip.library.solver.repair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.solver.repair.util.RepairedSolution;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.VrpUtils;

public class Demonstrator {

	// public final static String INPUT_DIRECTORY = "baseDirectory";
	// private static VrpConfiguration configuration = new VrpConfiguration();
	VrpProblem problem;

	public static void main(String... args) {

		try {
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					System.in));

			PrintStream err = new PrintStream(new FileOutputStream(
					"ErrorStreamUnterdruecken"));
			System.setErr(err);

			Demonstrator s = new Demonstrator();
			Solution solution = s.loadSolution();
			DemandScenario ds = s.loadDS();
			

			// now process demandScenario
			for (Event e : ds.getEvents()) {

				VrpProblem oldVrpProblem = solution.getVrpProblem().clone();
				System.out.println("Bearbeite Ereignis f�r Kunde "
						+ e.getCustomerNo() + " zum Zeitpunkt "
						+ e.getPointInTime());
				List<RepairedSolution> solutionList = new RepairSolution()
						.repair(solution, e);
				solution = solutionList.get(0).getNewSolution();
				solutionList.get(0).getOldSolution()
						.setVrpProblem(oldVrpProblem);

				System.out.println("Bitte Enter dr�cken.");
				System.out
						.println("-----------------------------------------------------------");
				bin.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Solution loadSolution() throws IOException {
		// String directory = configuration
		// .getConfigurationValueString(INPUT_DIRECTORY);

		// File f = new File("."); // current directory
		//
		// File[] files = f.listFiles();
		// for (File file : files) {
		// if (file.isDirectory()) {
		// System.out.print("directory:");
		// } else {
		// System.out.print("     file:");
		// }
		// System.out.println(file.getCanonicalPath());
		// }

		// get all problems / files in the directory
		Iterator<File> files = FileUtils.iterateFiles(new File("."),
				new String[] { "txt" }, false);
		while (files.hasNext()) {
			File file = files.next();
			if (!file.getName().contains("scenario")
					&& file.getName().contains("r101")) {
				FileInputStream openInputStream = null;
				try {
					openInputStream = FileUtils.openInputStream(file);
					List<String> lines = IOUtils.readLines(openInputStream);
					problem = VrpUtils.createProblemFromStringList(lines);
				} finally {
					IOUtils.closeQuietly(openInputStream);
				}
			}
		}

		System.out.println("Juhu, Success :)");

		Set<Vehicle> vs = problem.getVehicles();
		DeterministicTour t1 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t1.addCustomer(problem.getCustomerWithCustomerNo(3));
		if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(
				problem.getCustomerWithCustomerNo(24), t1, 1))
			t1.addCustomer(problem.getCustomerWithCustomerNo(24));
		else
			System.out.println("M�p, Einf�gen in Tour " + t1.getTourAsTupel()
					+ " nicht m�glich");
		System.out.println(t1.getDemandOnTour());
		if (TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(
				problem.getCustomerWithCustomerNo(12), t1, 2))
			t1.addCustomer(problem.getCustomerWithCustomerNo(12));
		else
			System.out.println("M�p, Einf�gen in Tour " + t1.getTourAsTupel()
					+ " nicht m�glich");

		DeterministicTour t2 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t2.addCustomer(problem.getCustomerWithCustomerNo(6));
		t2.addCustomer(problem.getCustomerWithCustomerNo(4));

		DeterministicTour t3 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t3.addCustomer(problem.getCustomerWithCustomerNo(21));
		t3.addCustomer(problem.getCustomerWithCustomerNo(23));

		DeterministicTour t4 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t4.addCustomer(problem.getCustomerWithCustomerNo(2));
		t4.addCustomer(problem.getCustomerWithCustomerNo(22));

		DeterministicTour t5 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t5.addCustomer(problem.getCustomerWithCustomerNo(15));
		t5.addCustomer(problem.getCustomerWithCustomerNo(14));
		t5.addCustomer(problem.getCustomerWithCustomerNo(13));

		DeterministicTour t6 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t6.addCustomer(problem.getCustomerWithCustomerNo(16));

		DeterministicTour t7 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t7.addCustomer(problem.getCustomerWithCustomerNo(18));
		t7.addCustomer(problem.getCustomerWithCustomerNo(8));
		t7.addCustomer(problem.getCustomerWithCustomerNo(17));
		t7.addCustomer(problem.getCustomerWithCustomerNo(5));

		DeterministicTour t8 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t8.addCustomer(problem.getCustomerWithCustomerNo(7));
		t8.addCustomer(problem.getCustomerWithCustomerNo(1));

		DeterministicTour t9 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t9.addCustomer(problem.getCustomerWithCustomerNo(19));
		t9.addCustomer(problem.getCustomerWithCustomerNo(10));

		DeterministicTour t10 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t10.addCustomer(problem.getCustomerWithCustomerNo(11));
		t10.addCustomer(problem.getCustomerWithCustomerNo(20));

		DeterministicTour t11 = new DeterministicTour(problem.getDepot(), vs
				.iterator().next());
		t11.addCustomer(problem.getCustomerWithCustomerNo(9));

		Solution solution = new Solution(problem);
		solution.addTour(t1);
		solution.addTour(t2);
		solution.addTour(t3);
		solution.addTour(t4);
		solution.addTour(t5);
		solution.addTour(t6);
		solution.addTour(t7);
		solution.addTour(t8);
		solution.addTour(t9);
		solution.addTour(t10);
		solution.addTour(t11);

		return solution;
	}

	public DemandScenario loadDS() {

		DemandScenario demandScenario = new DemandScenario();

		// event ohne Problem
		Event e1 = new Event();
		e1.setCustomerNo(12);
		e1.setDemand(25);
		e1.setPointInTime(10);

		// event mit Problem, auf dem Umplanen m�glich ist
		Event e2 = new Event();
		e2.setCustomerNo(15);
		e2.setDemand(8);
		e2.setPointInTime(41);

		// event mit Problem, auf dem Umplanen m�glich ist
		Event e3 = new Event();
		e3.setCustomerNo(8);
		e3.setDemand(49);
		e3.setPointInTime(78);

		demandScenario.addEvent(e1);
		demandScenario.addEvent(e2);
		demandScenario.addEvent(e3);

		return demandScenario;

	}
}
