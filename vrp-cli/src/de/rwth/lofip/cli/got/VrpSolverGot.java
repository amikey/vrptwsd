package de.rwth.lofip.cli.got;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.generic.AbstractVrpSolverZumTestenGot;
import de.rwth.lofip.cli.util.SystemOutDeactivate;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.GroupPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.LeiEtAlHeuristicGot;
import de.rwth.lofip.library.solver.util.DistanceComparatorWithSimilarity;
import de.rwth.lofip.library.util.IntermediateSolutionGot;

public class VrpSolverGot extends AbstractVrpSolverZumTestenGot {
	
    protected boolean isUseLeiEtAlHeuristic = false;
    private static double factorDistanceAsOpposedToTW;
    private static double factorSimValAsOpposedToDistanceAndTW;
    private SolutionGot solution;
    
    public static void main(String... args) throws IOException {    	
//    	setPrintStream();
    	deactivateSystemOut();
    	for (int alphaValue = 0; alphaValue <= 10; alphaValue++) {
    	    factorDistanceAsOpposedToTW = 0.1 * alphaValue;
    	    for (int simValValue = 0; simValValue <= 10; simValValue++) {
    	    	factorSimValAsOpposedToDistanceAndTW = 0.1 * simValValue;
    	    	setParametersInStartHeuristic();
    	    	setParametersInMetaheuristic();
    	    	calculateSolution();
//    	    	updateBestSolution();
    	    }    	    
    	}   	   	        
    }
    
	private static void setParametersInStartHeuristic() {
    	DistanceComparatorWithSimilarity.setWeightForDistanceAsOpposedToTW(factorDistanceAsOpposedToTW);
    	DistanceComparatorWithSimilarity.setWeightSimvalAsOpposedToDistanceAndTW(factorSimValAsOpposedToDistanceAndTW);
	}
	
	private static void setParametersInMetaheuristic() {
		return;
//		setNumberOfRemovedCustomersPerIteration();
//		setStoppingCriteria();		
//		setAcceptanceCriteria();
//		setPenaltyFactorForInfeasibleSolutionWrtNumberOfVehicles();
//		setUpdateParametersInAdaptiveSearch();
//		setSeedsInMetaheuristic();
//		setSeedsInRemovalHeuristics();		
	}
	
    private static void calculateSolution() throws IOException {
        VrpSolverGot s = new VrpSolverGot();
        s.calculateAll();
	}

	private static void deactivateSystemOut() throws FileNotFoundException {
		PrintStream out = new SystemOutDeactivate();
		System.setOut(out);		
	}
    
    @Override
    protected void setFileNameShouldContain(){
    	fileNameShouldContain = "rc101"; 
    }
    
	protected SolverInterfaceGot getInitialSolver() {
		return new GroupPushForwardInsertionSolver();
	}

	@Override
	protected LeiEtAlHeuristicGot getMetaheuristicOutputStream() {	
		return new LeiEtAlHeuristicGot() {
//		    @Override
//		    public void publishSolution(IntermediateSolutionGot is) {
//		        try {
//		            DecimalFormat df = new DecimalFormat("000.000");
//		            IOUtils.write(
//		                    is.getSolution().getIteration()
//		                            + ";"
//		                            + is.getCustomersChanged()
//		                            + ";"
//		                            + is.getRemovalHeuristic()
//		                            + ";"
//		                            + df.format(new Double(is
//		                                    .getTimeForRemoval() / 1000)
//		                                    .doubleValue() / 1000)
//		                            + ";"
//		                            + is.getInsertionHeuristic()
//		                            + ";"
//		                            + df.format(new Double(is
//		                                    .getTimeForInsertion() / 1000)
//		                                    .doubleValue() / 1000) + ";"
//		                            + is.isBestSolution() + ";"
//		                            + is.isImprovedSolution() + ";"
//		                            + is.isAccepted() + ";"
//		                            + String.format("%.3f", (double) is.getSolution().getTotalDistance()) + ";" 
//		                            + String.format("%.3f", (double) is.getSolution().getExpectedRecourseCost()) + ";"  
//		                            + String.format("%.3f", (double) is.getSolution().getPenaltyCost()) + ";"
//		                            + is.getSolution().getVehicleCount() + ";"
//		                            + is.getSolution().getVrpProblem().getVehicleCount() + ";"
//		                            + is.getSolution().getSolutionAsTupel() + ";"
//		                            + is.getWeightHeuristics() + 
//		                            "\n", outputStream);                                                 
//		           
//		        } catch (IOException ioe) {
//		            ioe.printStackTrace();
//		        }		
//		    }
		};
	}

	@Override
	protected LeiEtAlHeuristicGot getMetaheuristicConsoleOutput() {
		// TODO Auto-generated method stub
		throw new RuntimeException("getMetaheuristicConsoleOutput() sollte nicht benutzt werden.");
	}
	
	public double getSumOfAllSolutions() {
		return sumOfAllSolutions;
	}

	public int getNumberOfAllVehicles() {
		return sumOfAllVehicles;
	}
	

}
