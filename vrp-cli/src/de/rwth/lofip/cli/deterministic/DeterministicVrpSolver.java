package de.rwth.lofip.cli.deterministic;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.generic.AbstractVrpSolver;
import de.rwth.lofip.library.interfaces.SolverInterface;
import de.rwth.lofip.library.solver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.util.IntermediateSolution;

/**
 * CLI application for the VRP Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class DeterministicVrpSolver extends AbstractVrpSolver {	

    public static void main(String... args) throws IOException {    	
//    	setPrintStream();    			
        AbstractVrpSolver s = new DeterministicVrpSolver();
        s.calculateAll();
    }
	
	protected SolverInterface getInitialSolver() {
		return new DeterministicPushForwardInsertionSolver();
	}
	
	protected LeiEtAlHeuristic getMetaheuristicConsoleOutput() {		
		return new DeterministicLeiEtAlHeuristic() {
            @Override
            public void publishSolution(IntermediateSolution is) {
                DecimalFormat df = new DecimalFormat("000.000");
                System.out.println(is.getSolution().getIteration()
                        + ";"
                        + is.getCustomersChanged()
                        + ";"
                        + is.getRemovalHeuristic()
                        + ";"
                        + df.format(new Double(
                                is.getTimeForRemoval() / 1000)
                                .doubleValue() / 1000)
                        + ";"
                        + is.getInsertionHeuristic()
                        + ";"
                        + df.format(new Double(
                                is.getTimeForInsertion() / 1000)
                                .doubleValue() / 1000) + ";"
                        + is.isBestSolution() + ";"
                        + is.isImprovedSolution() + ";" + is.isAccepted());
            }
        };
	}

	protected LeiEtAlHeuristic getMetaheuristicOutputStream() {
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
		};
	}

}
