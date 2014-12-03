package de.rwth.lofip.library.solver.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.insertions.InsertionInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.InsertionInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.insertions.groups.DemandAndFailureSortingInsertionGot;
import de.rwth.lofip.library.solver.metaheuristics.insertions.groups.FeasibilityOrientedInsertionGot;
import de.rwth.lofip.library.solver.metaheuristics.insertions.groups.GreedyInsertionGot;
import de.rwth.lofip.library.solver.metaheuristics.insertions.groups.TimeAndFailureSortingInsertionGot;
import de.rwth.lofip.library.solver.metaheuristics.removals.ExpectedWorstRemovalGot;
import de.rwth.lofip.library.solver.metaheuristics.removals.FeasibilityOrientedRemovalGot;
import de.rwth.lofip.library.solver.metaheuristics.removals.RandomRemovalGot;
import de.rwth.lofip.library.solver.metaheuristics.removals.RemovalInterface;
import de.rwth.lofip.library.solver.metaheuristics.removals.RemovalInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.removals.SimilarityRemovalGot;
import de.rwth.lofip.library.util.IntermediateSolution;
import de.rwth.lofip.library.util.IntermediateSolutionGot;
import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * An implementation of the meta heuristic proposed by Lei et al. (2011).
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class LeiEtAlHeuristicGot implements MetaSolverInterfaceGot {

	/****************************************************************************
	 * Fields
	 ***************************************************************************/

	public final transient static String APPROXIMATE_EQUALITY = "approximateEquality";
	public final static String MAXIMUM_ITERATIONS = "maximumIterations";
	public final static String MAXIMUM_IMPROVEMENT_ITERATIONS = "maximumImprovementIterations";
	public final static String DEVIATION_FACTOR = "deviationFactor";

	// the value was determined by looking at all problems and choosing the
	// value so that P(demand) < Q is valid for all problem instances and
	// all customers. Q = 50, demand = 36 in R211 is the relevant instance.

	private boolean externallyCancelled = false;

	private WeightList insertionHeuristicWeights = new WeightList();
	private WeightList removalHeuristicWeights = new WeightList();

	protected int countOfInfeasibleSolutionsWrtNumberOfVehicles = 0;
	
	private VrpConfiguration configuration;
	
	//parameters
	private static int addedValueGlobalBestSolution = 30;
	private static int addedValueImprovedSolution = 10;
	private static int addedValueAcceptedSolution = 6;
	private static double learningFactorAdaptiveWeight = 0.1;
	static private int seedNumberOfRemovedCustomers = 1;
	static private int seedChoosingOfRemovalAndInsertionHeuristic = 1;
	
	private static double minimalPercentageOfRemovedCustomers = 0.1;
	private static double maximalPercentageOfRemovedCustomers = 0.2;
	
	// this coefficient penalizes the amount of m-infeasible solutions. It
	// is recalculated every 10 iterations with
	// p = p * 2^( (b/5) - 1)
	// where b is the "countOfMInfeasibleSolutions"
	private static double penaltyCoefficient = 1;

	/****************************************************************************
	 * Constructor
	 ***************************************************************************/

	public LeiEtAlHeuristicGot() {
		setUpInsertionAndRemovalHeuristics();
	}
	
	protected void setUpInsertionAndRemovalHeuristics() {
		resetHeuristics();

		addRemovalHeuristic(ExpectedWorstRemovalGot.class, "EWR");
		addRemovalHeuristic(FeasibilityOrientedRemovalGot.class, "FOR");
		addRemovalHeuristic(SimilarityRemovalGot.class, "SR");
		addRemovalHeuristic(RandomRemovalGot.class, "RR");

		addInsertionHeuristic(GreedyInsertionGot.class, "GI");
		addInsertionHeuristic(FeasibilityOrientedInsertionGot.class, "FOI");
		addInsertionHeuristic(DemandAndFailureSortingInsertionGot.class, "DFSI");
		addInsertionHeuristic(TimeAndFailureSortingInsertionGot.class, "TFSI");		
	}

	protected final void resetHeuristics() {
		insertionHeuristicWeights = new WeightList();
		removalHeuristicWeights = new WeightList();
	}

	protected final void addRemovalHeuristic(
			Class<? extends RemovalInterfaceGot> removalClass, String name) {
		removalHeuristicWeights.add(new HeuristicWeight(removalClass, name));
	}

	protected final void addInsertionHeuristic(
			Class<? extends InsertionInterfaceGot> insertionClass, String name) {
		insertionHeuristicWeights
				.add(new HeuristicWeight(insertionClass, name));
	}

	/****************************************************************************
	 * Getter
	 ***************************************************************************/
	
	protected final VrpConfiguration getVrpConfiguration() {
		return configuration;
	}
	
	/****************************************************************************
	 * Setter
	 ***************************************************************************/

	public static void setSeed(int seed) {
		LeiEtAlHeuristicGot.seedNumberOfRemovedCustomers = seed;
		LeiEtAlHeuristicGot.seedChoosingOfRemovalAndInsertionHeuristic = seed;
	}
	
	public static void setMinimalPercentageOfRemovedCustomers(double value) {
		minimalPercentageOfRemovedCustomers = value;
	}

	public static void setMaximalPercentageOfRemovedCustomers(double value) {
		maximalPercentageOfRemovedCustomers = value;
	}
	
	public static void setInitialPenaltyCoefficient(double penaltyFactorForDeviationInNumberOfVehicles) {
		penaltyCoefficient = penaltyFactorForDeviationInNumberOfVehicles;
	}
	
	public static void setSeedNumberOfRemovedCustomersInMetaheuristic(int seed) {
		LeiEtAlHeuristicGot.seedNumberOfRemovedCustomers = seed;
	}
	
	public static void setSeedWhichHeuristicIsChosen(int seed) {
		LeiEtAlHeuristicGot.seedChoosingOfRemovalAndInsertionHeuristic = seed;
	}

	public static void setLearningFactorAdaptiveWeight(double factor){
		LeiEtAlHeuristicGot.learningFactorAdaptiveWeight = factor;
	}
	
	public static void setAddedValueGlobalBestSolution(int value) {
		addedValueGlobalBestSolution = value;		
	}
	
	public static void setAddedValueImprovedSolution(int value) {
		addedValueImprovedSolution = value;		
	}
	
	public static void setAddedValueAcceptedSolution(int value) {
		addedValueAcceptedSolution = value;		
	}

	/****************************************************************************
	 * Algorithm
	 * 
	 * @throws Exception
	 ***************************************************************************/

	@Override
	public SolutionGot improve(SolutionGot solution, VrpConfiguration configuration) {

		VrpProblem problem = solution.getVrpProblem();
		this.configuration = configuration;

		double record = Double.MAX_VALUE;
		double deviation = 0;

		// initialize "parameters" of solution that was created in construction
		// heuristic

		// create a copy of the solution, so we can compare the state before and
		// after the improvement
		SolutionGot bestSolution = solution.clone();
		record = solution
				.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
		deviation = configuration.getConfigurationValueDouble(DEVIATION_FACTOR)
				* record;
		if (solution.getVehicleCount() == problem.getVehicleCount()) {
			bestSolution = solution.clone();
			// Frage AB: M�sste die folgende Zeile nicht die Penalty Kosten
			// immer auf 0 setzen?
			bestSolution.setPenaltyCost(Math.abs(bestSolution.getVehicleCount()
					- problem.getVehicleCount()));
		}

		SolutionGot previousSolution = solution.clone();
		previousSolution.setIteration(0);

		int customerCount = problem.getCustomers().size();

		// this will keep track of the amount of iterations
		int iteration = 1;

		// keep track of the amount of m-infeasible solutions


		while (!isCancelCriterionReached(previousSolution, solution,
				bestSolution)) {
			try {
				// first, let's find out how many vertices to re-insert
				// this is a trick to get a number from the interval [0.1n,
				// 0.2n]

				int amountOfVerticesToBeChanged = getNumberOfVerticesToBeChanged(customerCount);

				HeuristicWeight removalWeight = removalHeuristicWeights
						.getHeuristic();
				RemovalInterfaceGot removal;
				removal = (RemovalInterfaceGot) removalWeight.getHeuristicClass()
						.newInstance();

				HeuristicWeight insertionWeight = insertionHeuristicWeights
						.getHeuristic();
				InsertionInterfaceGot insertion;
				insertion = (InsertionInterfaceGot) insertionWeight
						.getHeuristicClass().newInstance();

				// now the actual removal-insertion-steps
				long beforeRemovalTime = System.nanoTime();
				List<RemovedCustomer> removedCustomers = removal
						.removeCustomers(solution, amountOfVerticesToBeChanged);
				solution.removeEmptyTours();
				long afterRemovalTime = System.nanoTime();

				addRemovedCustomersToTabuListHook(removedCustomers, iteration);
				setTabuPositionsInInsertionHeuristicHook(insertion);
				
				solution = insertion.insertCustomers(solution,
						removedCustomers, iteration, configuration);
				long afterInsertionTime = System.nanoTime();

				AddRemovedCustomersToTabuListHook(insertion);
				deleteExpiredEntriesFromTabuListHook(iteration);			
			
				System.out.println("It " + iteration + " (" + removal.getName() + "," + insertion.getName() + "); HeuristicWeights: " + insertionHeuristicWeights.printWeights() + removalHeuristicWeights.printWeights());
				System.out.println(solution.getSolutionAsTupel());			
				if (solution.getCustomersInTours().size() != solution.getVrpProblem().getCustomerCount())
					throw new RuntimeException("Customer Count stimmt nicht.");

				solution.setIteration(iteration);
				setPenaltyCostToNewSolution(solution, bestSolution, penaltyCoefficient);

				// compare the solutions and add scores to the used heuristics
				// (see chapter 3.4, around the end)
				boolean globalBestSolution = false;
				boolean improvedSolution = false;
				boolean solutionAccepted = false;

				// check whether this iteration results in a new feasible global
				// best solution
				if (isCreatedSolutionResultsInNewFeasibleGlobalBestSolution(solution, bestSolution)) {
					bestSolution = solution.clone();
					bestSolution.setIteration(iteration);
					globalBestSolution = true;
					improvedSolution = true;
					solutionAccepted = true;
				} else {
					// check whether solution is accepted according to RRT
					solutionAccepted = isSolutionAccepted(solution, record,
							deviation);
					if (solutionAccepted) {
						// set solution as current solution (reference solution)
						previousSolution = solution.clone();
						// check whether iteration results in a new (feasible or
						// m-infeasible)
						// solution improved on the current solution
						// if so, update record and deviation
						if (solution
								.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() < record) {
							record = solution
									.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
							deviation = configuration
									.getConfigurationValueDouble(DEVIATION_FACTOR)
									* record;
							improvedSolution = true;
						}
					}
				}

				IntermediateSolutionGot is = new IntermediateSolutionGot();
				is.setSolution(solution.clone());
				is.setAccepted(solutionAccepted);
				is.setBestSolution(globalBestSolution);
				is.setImprovedSolution(improvedSolution);
				is.setRemovalHeuristic(removal.getClass().getSimpleName());
				is.setInsertionHeuristic(insertion.getClass().getSimpleName());
				is.setCustomersChanged(amountOfVerticesToBeChanged);
				is.setTimeForRemoval(afterRemovalTime - beforeRemovalTime);
				is.setTimeForInsertion(afterInsertionTime - afterRemovalTime);
				is.setWeights(insertionHeuristicWeights.printWeights(),
						removalHeuristicWeights.printWeights());
				publishSolution(is);

				if (globalBestSolution) {
					insertionWeight.addScore(addedValueGlobalBestSolution);
					removalWeight.addScore(addedValueGlobalBestSolution);
				} else if (improvedSolution) {
					insertionWeight.addScore(addedValueImprovedSolution);
					removalWeight.addScore(addedValueImprovedSolution);
				} else if (solutionAccepted) {
					insertionWeight.addScore(addedValueAcceptedSolution);
					removalWeight.addScore(addedValueAcceptedSolution);
				}

				increaseCountOfInfeasibleSolutions(solution, bestSolution);
				
				if (iteration % 10 == 0) {
					// recalculate the penalty coefficient
					penaltyCoefficient = penaltyCoefficient
							* Math.pow(2, (countOfInfeasibleSolutionsWrtNumberOfVehicles / 5) - 1);
					countOfInfeasibleSolutionsWrtNumberOfVehicles = 0;
				}
				
				resetWeights(iteration);

				// recalculate the weights for all insertions/removals
				if (iteration % 50 == 0) {
					recalculateWeights();
				}

				// increase the iteration counter - this is really needed, don't
				// you ever forget!
				iteration++;

			} catch (InstantiationException ie) {
				System.out.println("Ouch, Instantion Exception: "
						+ ie.getMessage());
			} catch (IllegalAccessException e) {
				System.out.println("Ouch, IllegalAccess Exception: "
						+ e.getMessage());
			}
		}

		if (bestSolution == null)
			// if solution could not be improved, return the same solution.
			return solution;
		else
			// if solution could be improved, return improved solution.
			return bestSolution;
	}

	protected void AddRemovedCustomersToTabuListHook(InsertionInterfaceGot insertion) {
		return;
	}

	protected int getNumberOfVerticesToBeChanged(int customerCount) {
		seedNumberOfRemovedCustomers++;
		Random rnd = new Random(seedNumberOfRemovedCustomers);
		int low = new Double(minimalPercentageOfRemovedCustomers * customerCount).intValue();
		int high = new Double(maximalPercentageOfRemovedCustomers * customerCount).intValue();
		int range = high - low;
		int dist = rnd.nextInt(range + 1);				
		int value = low + dist;
		int amountOfVerticesToBeChanged = Math.max(1, value);	
		return amountOfVerticesToBeChanged;
	}

	protected void increaseCountOfInfeasibleSolutions(SolutionGot solution, SolutionGot bestSolution) {
		if (solution.getVehicleCount() != solution.getVrpProblem().getVehicleCount()) {
			countOfInfeasibleSolutionsWrtNumberOfVehicles++;
		}	
	}

	protected void resetWeights(int iteration) {
		if (iteration % 5000 == 0)
			setUpInsertionAndRemovalHeuristics();
	}

   	protected boolean isCreatedSolutionResultsInNewFeasibleGlobalBestSolution(SolutionGot solution, SolutionGot bestSolution) {
   		double bestCost = bestSolution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
   		if (solution.getVehicleCount() <= bestSolution.getVehicleCount()
   				&& solution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() < bestCost)
   			return true;
   		else 
   			return false;
   	}
    
	protected void setPenaltyCostToNewSolution(SolutionGot solution, SolutionGot bestSolution, double penaltyCoefficient) {
		solution.setPenaltyCost(penaltyCoefficient * Math.abs(solution.getVehicleCount() - solution.getVrpProblem().getVehicleCount()));		
	}

	/****************************************************************************
	 * Acceptance and stopping criteria
	 ***************************************************************************/

	/**
	 * Determine if the new solution can be accepted, that means if it is good
	 * enough to be used as the new "base" solution.
	 * 
	 * @param solution
	 * @param bestSolution
	 * @return
	 * @throws Exception
	 */
	protected boolean isSolutionAccepted(SolutionGot solution, double record,
			double deviation) {
		if (solution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() >= record
				+ deviation) {
			return false;
		}
		return true;
	}


	protected boolean isCancelCriterionReached(SolutionGot previousSolution,
			SolutionGot solution, SolutionGot bestSolution) {
		if (isExternallyCancelled()) {
			return true;
		}
		// maximum number of iterations reached?
		if (solution.getIteration() > configuration
				.getConfigurationValueLong(LeiEtAlHeuristic.MAXIMUM_ITERATIONS)) {
			return true;
		}
		if (bestSolution == null) {
			return false;
		}
		// maximum number of iterations without improvement reached?
		if (solution.getIteration() - bestSolution.getIteration() > configuration
				.getConfigurationValueLong(MAXIMUM_IMPROVEMENT_ITERATIONS)) {
			return true;
		}
		return false;
	}

	/****************************************************************************
	 * Adaptive Search
	 ***************************************************************************/

	/**
	 * Recalculate the weights for all heuristics of a given type (insertion or
	 * removal). The process is described by Lei et al. in chapter 3.4
	 */
	private void recalculateWeights() {
		for (HeuristicWeight w : removalHeuristicWeights) {
			w.recalculate();
		}
		Collections.shuffle(removalHeuristicWeights, new Random(42));
		for (HeuristicWeight w : insertionHeuristicWeights) {
			w.recalculate();
		}
		Collections.shuffle(insertionHeuristicWeights, new Random(42));
	}

	/**
	 * A specialized set containing all weights for the removals/insertions.
	 * 
	 * With a method to get a random heuristic, according to the heuristic's
	 * weights.
	 * 
	 * @author Dominik Sandjaja <dominik@dadadom.de>
	 * 
	 */
	private class WeightList extends ArrayList<HeuristicWeight> {

		private static final long serialVersionUID = 1L;
		private int factor = 10000;

		public HeuristicWeight getHeuristic() {
			// first, add the weights of all heuristics
			int weight = 0;

			for (int i = 0; i < size(); i++) {
				weight += get(i).getWeight() * factor;
			}
			if (weight == 0) {
				weight = 1;
			}

			// now, get a random number and choose from the available heuristics
			// the one for which this number is relevant
			seedChoosingOfRemovalAndInsertionHeuristic++;
			Random random = new Random(seedChoosingOfRemovalAndInsertionHeuristic);
			int randomValue = random.nextInt(weight);
			int c = 0;
			for (int i = 0; i < size(); i++) {
				if (c <= randomValue
						&& randomValue < (c + get(i).getWeight() * factor)) {
					// we found the one to be returned
					get(i).increaseUsageCount();
					return get(i);
				} else {
					c += get(i).getWeight() * factor;
				}
			}

			for (int i = 0; i < size(); i++) {
				System.err.println(get(i).getHeuristicClass()
						+ " has a weight of " + get(i).getWeight() * factor);
			}

			return get(size() - 1);
		}

		public String printWeights() {
			String s = "";
			for (int i = 0; i < size(); i++) {
				s += "(" + get(i).getName() + " "
						+ String.format("%.3f", get(i).getWeight()) + ")" + ";";
			}
			return s;
		}
	}

	/**
	 * This class associates a wheight to a removal or insertion heuristic
	 */
	private class HeuristicWeight {
		private Class<? extends HeuristicInterface> heuristicClass;
		private double weight;
		private int usageCount;
		private int score;
		private String name;

		public HeuristicWeight(
				Class<? extends HeuristicInterface> heuristicClass, String name) {
			this.heuristicClass = heuristicClass;
			weight = 1;
			usageCount = 0;
			score = 0;
			this.name = name;

		}

		private void increaseUsageCount() {
			usageCount++;
		}

		public void recalculate() {
			if (usageCount > 0) {
				weight = (1-learningFactorAdaptiveWeight) * weight + learningFactorAdaptiveWeight * (score / usageCount);
			}
			// the weight is not recalculated if the heuristic was not used at
			// all otherwise, the weight may get so low that the heuristic is
			// never
			// chosen again.

			// reset the counters
			usageCount = 0;
			score = 0;
		}

		public double getWeight() {
			return weight;
		}

		public String getName() {
			return name;
		}

		public Class<? extends HeuristicInterface> getHeuristicClass() {
			return heuristicClass;
		}

		public void addScore(int score) {
			this.score += score;
		}

	}

	/****************************************************************************
	 * Hooks
	 ***************************************************************************/

	protected void deleteExpiredEntriesFromTabuListHook(int iteration) {
		return;
	}

	/**
	 * This method is needed to allow subclasses to somehow deal with the
	 * removed customers.
	 * 
	 * @param removedCustomers
	 */
	protected void addRemovedCustomersToTabuListHook(List<RemovedCustomer> removedCustomers,
			int iteration) {
		return;
	}

	/**
	 * This method is needed to allow subclasses to somehow deal with the
	 * removal/insertion heuristics.
	 * 
	 * @param insertion
	 */
	protected void setTabuPositionsInInsertionHeuristicHook(InsertionInterfaceGot insertion) {
		return;
	}

	/****************************************************************************
	 * IO / GUI
	 ***************************************************************************/

	public boolean isExternallyCancelled() {
		return externallyCancelled;
	}

	@Override
	public void setExternallyCancelled(boolean externallyCancelled) {
		this.externallyCancelled = true;
	}

	public void publishSolution(IntermediateSolutionGot intermediateSolution) {
		// nothing needs to be done here. It can be overriden
		return;
	}











	// /**
	// * Implement the Greedy Insertion as described by Lei et al. in section
	// * 3.3.5 of their paper.
	// *
	// * @author Dominik Sandjaja <dominik@dadadom.de>
	// *
	// */
	// public class StochasticGreedyInsertion extends AbstractGreedyInsertion {
	//
	// protected Tour createTourHook(Depot depot, int vehicleId,
	// double vehicleCapacity) {
	// Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
	// return tour;
	// }
	//
	// public boolean isInsertionPossibleHook(Customer customer, Tour tour,
	// int i, double approximateEquality) {
	// // stochastic variant
	// return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(
	// customer, tour, i, approximateEquality);
	// }
	//
	// }

}
