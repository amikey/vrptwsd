package de.rwth.lofip.cli.minimization;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.deterministic.DeterministicVrpSolver;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.util.IntermediateSolution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic.DeterministicDemandAndFailureSortingInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic.DeterministicFeasibilityOrientedInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic.DeterministicGreedyInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic.DeterministicTimeAndFailureSortingInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.DestroyToursRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.ExpectedWorstRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.FeasibilityOrientedRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.SimilarityRemoval;

/**
 * CLI application for the VRP Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class MinimizationVrpSolver extends DeterministicVrpSolver {	

    public static void main(String... args) throws IOException {    	
//    	setPrintStream();  
    	for (int i = 1; i <= 30; i++)
    	{
    		seed = i;
    		MinimizationVrpSolver s = new MinimizationVrpSolver();
            s.calculateAll();
    	}        
    }
        
	@Override
	protected  List<File> getFilesInDirectory(String directory) {
		File file = new File("../diss-lib/original-solomon-problems/c101.txt");
		List<File> files = new LinkedList<File>();
		files.add(file);
		return files;
	}
	
	@Override
	protected Solution minimizationStage(Solution solution) throws IOException {
		int targetVehicleNumber = solution.getVrpProblem().getTargetVehicleNumber();
		do {		
			targetVehicleNumber--;
			solution.getVrpProblem().setVehicleCount(targetVehicleNumber);
			IOUtils.write("Minimizing with targetVehicleNumber " + targetVehicleNumber + "! \n", outputStream);
			LeiEtAlHeuristic minimizationStageLeiEtAl = getMinimizationStageHeuristic();
			solution = minimizationStageLeiEtAl.improve(solution, configuration);
		} while (!MinimizationCancelCriterionReached(solution));
		solution = setTargetVehicleNumberInVrpProblemToNumberOfToursInFoundSolution(solution);
		return solution;
	}

	private LeiEtAlHeuristic getMinimizationStageHeuristic() {
		return new DeterministicLeiEtAlHeuristic() {
			
		    @Override
		    public void publishSolution(IntermediateSolution is) {
		        try {
		            DecimalFormat df = new DecimalFormat("000.000");
		            IOUtils.write(
		                    is.getSolution().getIteration()
		                            + ";"
		                            + is.getCustomersChanged()
		                            + ";"
		                            + is.getRemovalHeuristic()
		                            + ";"
		                            + df.format(new Double(is
		                                    .getTimeForRemoval() / 1000)
		                                    .doubleValue() / 1000)
		                            + ";"
		                            + is.getInsertionHeuristic()
		                            + ";"
		                            + df.format(new Double(is
		                                    .getTimeForInsertion() / 1000)
		                                    .doubleValue() / 1000) + ";"
		                            + is.isBestSolution() + ";"
		                            + is.isImprovedSolution() + ";"
		                            + is.isAccepted() + ";"
		                            + String.format("%.3f", (double) is.getSolution().getTotalDistance()) + ";" 
		                            + String.format("%.3f", (double) is.getSolution().getExpectedRecourseCost()) + ";"  
		                            + String.format("%.3f", (double) is.getSolution().getPenaltyCost()) + ";"
		                            + is.getSolution().getVehicleCount() + ";"
		                            + is.getSolution().getVrpProblem().getVehicleCount() + ";"
		                            + is.getSolution().getSolutionAsTupel() + ";"
		                            + is.getWeightHeuristics() + 
		                            "\n", outputStream);                                                 
		           
		        } catch (IOException ioe) {
		            ioe.printStackTrace();
		        }		
		    }
		    		    
		    //override acceptance criterion for new best solution such that only amount of vehicles is considered
			@Override
			protected boolean isCreatedSolutionResultsInNewFeasibleGlobalBestSolution(Solution solution, Solution bestSolution) {
				if (solution.getVehicleCount() <= solution.getVrpProblem().getVehicleCount())
					return true;
				else 
					return false;
			}
		    
		    @Override
			protected boolean isCancelCriterionReached(Solution previousSolution,
					Solution solution, Solution bestSolution) throws IOException {
		    	if (solutionWithTargetVehicleNumberHasBeenFound(solution, bestSolution)) {
					IOUtils.write("Hurra, hurra, Solution mit " + solution.getVrpProblem().getTargetVehicleNumber() + "Fahrzeugen gefunden! \n\n", outputStream);					
		    		return true;		    
		    	}
				if (maximumNumberOfIterationsReached(solution)) {
					IOUtils.write("Schade, keine Solution mit " + solution.getVrpProblem().getTargetVehicleNumber() + "Fahrzeugen gefunden :-( \n\n", outputStream);					
					return true;
				}			
				if (isExternallyCancelled()) {
					return true;
				}
				return false;
			}

			private boolean maximumNumberOfIterationsReached(Solution solution) {
				return solution.getIteration() >= 10000;
			}

			private boolean solutionWithTargetVehicleNumberHasBeenFound(Solution solution, Solution bestSolution) {
				return bestSolution.getNumberOfTours() <= solution.getVrpProblem().getTargetVehicleNumber();
			}
			
			@Override
			protected void setUpInsertionAndRemovalHeuristics() {
		        resetHeuristics();

		        addRemovalHeuristic(ExpectedWorstRemoval.class, "EWR");
		        addRemovalHeuristic(FeasibilityOrientedRemoval.class, "FOR");
		        addRemovalHeuristic(SimilarityRemoval.class, "SR");
		        addRemovalHeuristic(RandomRemoval.class, "RR");
		        addRemovalHeuristic(DestroyToursRemoval.class, "DTR");

		        addInsertionHeuristic(DeterministicGreedyInsertion.class, "GI");
		        addInsertionHeuristic(DeterministicFeasibilityOrientedInsertion.class, "FOI");
		        addInsertionHeuristic(DeterministicDemandAndFailureSortingInsertion.class, "DFSI");
		        addInsertionHeuristic(DeterministicTimeAndFailureSortingInsertion.class, "TFSI");	
			}
			
			protected void setPenaltyCostToNewSolution(Solution solution,
					Solution bestSolution, double penaltyCoefficient) {
				solution.setPenaltyCost(penaltyCoefficient
						* Math.abs(0));		
			}
			
			//override number of vertices to be changed in one move
			protected int getNumberOfVerticesToBeChanged(int customerCount) {
				seed++;
				Random rnd = new Random(seed);
				return 1 + rnd.nextInt(4);
			}
		};
	}

	private boolean MinimizationCancelCriterionReached(Solution solution) {
		if (solution.getNumberOfTours() > solution.getVrpProblem().getTargetVehicleNumber())
			return true;
		else 
			return false;
	}

	private Solution setTargetVehicleNumberInVrpProblemToNumberOfToursInFoundSolution(
			Solution solution) {
		int vehicleNumber = solution.getNumberOfTours();
		solution.getVrpProblem().setVehicleCount(vehicleNumber);
		return solution;
	}
	
}
