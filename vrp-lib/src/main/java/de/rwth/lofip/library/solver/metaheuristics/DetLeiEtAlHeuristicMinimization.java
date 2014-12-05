package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.stuffNotNeededRightNow.Solution;

//Lei et al Heuristic with objective of minimizing vehicles  
public class DetLeiEtAlHeuristicMinimization extends DeterministicLeiEtAlHeuristic implements
	MetaSolverInterface {

	//override acceptance criterion for new best solution such that 
	// 1) amount of vehicle is minimized
	// 2) distance is minimized
	@Override
	protected boolean isCreatedSolutionResultsInNewFeasibleGlobalBestSolution(Solution solution, Solution bestSolution) {
		double bestCost = bestSolution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
		if (solution.getVehicleCount() <= solution.getVrpProblem().getVehicleCount()
				&& solution.getVehicleCount() <= bestSolution.getVehicleCount()
				&& solution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() < bestCost)
			return true;
		else 
			return false;
	}
	
	//only set penalty costs when number of vehicles is larger 
	//than number of vehicles encountered in best solution so far
	@Override
	protected void setPenaltyCostToNewSolution(Solution solution, Solution bestSolution, double penaltyCoefficient) {
		if (solution.getVehicleCount() > bestSolution.getVehicleCount());
		solution.setPenaltyCost(penaltyCoefficient
				* Math.abs(solution.getVehicleCount()
						- bestSolution.getVehicleCount()));
	}
	
	//only increase number of infeasible solutions if new solution has more vehicles than best known solution
	protected void increaseCountOfInfeasibleSolutions(Solution solution, Solution bestSolution) {
		if (solution.getVehicleCount() > bestSolution.getVehicleCount()) {
			countOfInfeasibleSolutionsWrtNumberOfVehicles++;
		}	
	}	
	
}
