package de.rwth.lofip.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import de.rwth.lofip.gui.LogPanel;
import de.rwth.lofip.gui.SolutionPanel;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.library.solver.metaheuristics.MetaSolverInterface;
import de.rwth.lofip.library.util.IntermediateSolution;

public class LeiEtAlSolverButtonListener implements ActionListener {

    private SolutionPanel solutionPanel;
    private LogPanel logPanel;

    SwingWorker<Solution, IntermediateSolution> worker;
    private boolean workerIsWorking = false;

    public LeiEtAlSolverButtonListener(SolutionPanel solutionPanel,
            LogPanel logPanel) {
        this.solutionPanel = solutionPanel;
        this.logPanel = logPanel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (workerIsWorking) {
            if (e.getSource().getClass().equals(JButton.class)) {
                JButton sourceButton = (JButton) e.getSource();
                sourceButton.setText("Heuristic from Lei et al.");
            }
            worker.cancel(false);
            // cancel the worker
            workerIsWorking = false;
        } else {
            if (e.getSource().getClass().equals(JButton.class)) {
                JButton sourceButton = (JButton) e.getSource();
                sourceButton.setText("Stop the heuristic");
            }
            worker = new SwingWorker<Solution, IntermediateSolution>() {

                @Override
                protected Solution doInBackground() {
                    MetaSolverInterface solver = new LeiEtAlHeuristic() {
                        @Override
                        public void publishSolution(
                                IntermediateSolution intermediateSolution) {
                            publish(intermediateSolution);
                        }

                        @Override
                        public boolean isExternallyCancelled() {
                            return worker.isCancelled();
                        }
                    };
                    return solver.improve(solutionPanel.getSolution(),
                            new VrpConfiguration());
                }

                @Override
                protected void process(List<IntermediateSolution> chunks) {
                    for (IntermediateSolution is : chunks) {
                        if (is.isBestSolution()) {
                            solutionPanel.setSolution(is.getSolution());
                            solutionPanel.repaint();
                        }
                    }
                }

                protected void done() {
                    if (e.getSource().getClass().equals(JButton.class)) {
                        JButton sourceButton = (JButton) e.getSource();
                        sourceButton.setText("Heuristic from Lei et al.");
                    }
                    try {
                        Solution s = get();
                        solutionPanel.setSolution(s);
                        s.printSolution();
                        for (Tour t : s.getTours()) {

                            System.out.print("expected recourse: "
                                    + t.getExpectedRecourseCost()
                                    + " for customers ");
                            for (Customer c : t.getCustomers()) {
                                System.out.print(c.getCustomerNo() + " ");
                            }
                            System.out.println();
                        }
                        solutionPanel.repaint();
                    } catch (InterruptedException e1) {
                        logPanel.log(e1.toString());
                    } catch (ExecutionException e1) {
                        logPanel.log(e1.toString());
                    } catch (CancellationException ce) {
                        logPanel.log("Cancelled by user");
                    }
                }
            };
            worker.execute();
            workerIsWorking = true;
        }
    }
}
