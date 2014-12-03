/**
 * 
 */
package de.rwth.lofip.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import de.rwth.lofip.gui.LogPanel;
import de.rwth.lofip.gui.SolutionPanel;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.solver.repair.RepairSolution;

/**
 * @author Olga Bock
 *
 */
public class RepairSolutionButtonListener implements ActionListener {

	private SolutionPanel solutionPanel;
        private LogPanel logPanel;
    
        public RepairSolutionButtonListener(SolutionPanel solutionPanel, LogPanel logPanel) {
            this.solutionPanel = solutionPanel;
            this.logPanel = logPanel;

        }
    
        @Override
        public void actionPerformed(final ActionEvent e) {
            SwingWorker<Solution, Void> worker = new SwingWorker<Solution, Void>() {
    
                long startTimeInNano;
    
                // create the solution, set it in the DrawPanel and redraw
                // print out some data, just for verification
    
                @Override
                protected Solution doInBackground() throws Exception {
                    if (e.getSource().getClass().equals(JButton.class)) {
                        JButton sourceButton = (JButton) e.getSource();
                        sourceButton.setEnabled(false);
                    } 
                    Solution solution = solutionPanel.getSolution();
                    if (solutionPanel.getDemandScenario()!= null){
                    	SortedSet<Event> events = solutionPanel.getDemandScenario().getEvents();
                    	Event event = events.first();
                    	System.out.println("Bearbeite Ereignis für Kunde " + event.getCustomerNo() + " zum Zeitpunkt " + event.getPointInTime());
                    	try {
                    		solution = new RepairSolution().repair(solutionPanel.getSolution(), event).get(0).getNewSolution();
                    		startTimeInNano = System.nanoTime();
                    		// delete the first event
                    		events.remove(event);
                    		// create a new event-set
                    		SortedSet<Event> newEvents = new TreeSet<Event> (events.tailSet(events.first()));                   
                    		DemandScenario dScenario = new DemandScenario();
                    		dScenario.setDescription(solutionPanel.getDemandScenario().getDescription());
                    		dScenario.setEvents(newEvents);
                    		solutionPanel.setDemandScenario(dScenario);
                    	} catch (NoSuchElementException ne ) {
                    		logPanel.log("No Events");
                    	}
                    }
                    return solution;
                }
    
                @Override
                protected void done() {
                    if (e.getSource().getClass().equals(JButton.class)) {
                        JButton sourceButton = (JButton) e.getSource();
                        sourceButton.setEnabled(true);
                    }
                    try {
                        Solution s = get();
                        long timeTaken = System.nanoTime() - startTimeInNano;
//                        System.out.printf("Took " + timeTaken / 10000
//                                + " miliseconds");
                        solutionPanel.setSolution(s);
//                        System.out.printf("Total demand: %5d\n", solutionPanel
//                                .getVrpProblem().getTotalDemand());

//                        System.out
//                                .printf("vehicle count: %3d, total distance travelled: %7.2f\n",
//                                        s.getVehicleCount(), s.getTotalDistance());
                        long totalDemandHandled = 0;
                        for (Tour tour : s.getTours()) {
                            for (Customer c : tour.getCustomers()) {
                                totalDemandHandled += c.getDemand();
                            }
                            Vehicle vehicle = tour.getVehicle();
//                            System.out
//                                    .printf("vehicle: %3d, capacity used: %3.0f (%6.2f %%), distance: %3.2f\n",
//                                            vehicle.getVehicleId(), vehicle
//                                                    .getUsedCapacity(), (vehicle
//                                                    .getUsedCapacity() / vehicle
//                                                    .getCapacity()) * 100, tour
//                                                    .getDistance());
                        }
//                        System.out.println("total demand handled: "
//                                + totalDemandHandled);
                        logPanel.log(solutionPanel.getVrpProblem().getDescription());
//                        s.printSolution();
                        solutionPanel.repaint();
                    } catch (ExecutionException ee) {
                        System.out.println(ee);
                    } catch (InterruptedException ie) {
                        System.out.println(ie);
                    } 
                    logPanel.repaint();
                }
    
            };
            worker.execute();
        }
  }     
    
    
    
    
    
    
    
    
    
    
    
    
    


