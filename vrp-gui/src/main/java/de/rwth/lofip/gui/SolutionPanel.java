package de.rwth.lofip.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.scenario.DemandScenario;



public class SolutionPanel extends JPanel {

    private static final long serialVersionUID = 6443250346736669143L;

    private JGraphPanel jGraphPanel = new JGraphPanel();
    private VrpProblem vrpProblem;
    private JLabel vehicleCountLabel = new JLabel("Vehicle count: 0");
    private JLabel totalCostLabel = new JLabel("Total cost: 0");
    private Solution solution;
    private JLabel problemInfoLabel = new JLabel("", SwingConstants.CENTER);
    private DemandScenario dScenario; 
    private JLabel eventCountLabel = new JLabel("Events: 0");
    private JLabel nextEventTimeLabel = new JLabel("Next event time: 0");

    public SolutionPanel() {
        setLayout(new BorderLayout());
        problemInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(problemInfoLabel, BorderLayout.PAGE_START);
        add(jGraphPanel, BorderLayout.CENTER);

        JPanel intermediatePanel = new JPanel(new FlowLayout());
        intermediatePanel.add(vehicleCountLabel);
        intermediatePanel.add(totalCostLabel);
        intermediatePanel.add(eventCountLabel);
        intermediatePanel.add(nextEventTimeLabel);
        add(intermediatePanel, BorderLayout.PAGE_END);
    }

    public Solution getSolution() {
        return solution;
    }

    public VrpProblem getVrpProblem() {
        return vrpProblem;
    }
    
    public DemandScenario  getDemandScenario() {
        return dScenario;
    } 

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (vrpProblem != null) {
            // set the meta information about the problem in the info window
            problemInfoLabel.setText("Problem: " + vrpProblem.getDescription());
            if (dScenario != null) {
                // set the meta information about the Scenario in the info window
                problemInfoLabel.setText("Problem: " + vrpProblem.getDescription()+"Scenario: " + dScenario.getDescription());
            }
        }
        if (solution != null && dScenario != null){
        	vehicleCountLabel.setText("Vehicle count: "
                    + solution.getVehicleCount());
            totalCostLabel
                    .setText("Cost: "
                            + new DecimalFormat("0.###").format(solution
                                    .getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost()) + " (in iteration "
                            + solution.getIteration() + ")");
            if (dScenario.getEvents().size()!= 0){
            eventCountLabel.setText("Events: "+ dScenario.getEvents().size());
            nextEventTimeLabel.setText("Next event time: "+ dScenario.getEvents().first().getPointInTime());
            } else{dScenario = null; }
        }
        if (dScenario == null){
        	eventCountLabel.setText("Events: 0");
            nextEventTimeLabel.setText("Next event time: 0");
        }
        if (solution == null) {
            vehicleCountLabel.setText("Vehicle count: 0");
            totalCostLabel.setText("Total cost: 0 + 0 = 0");
        } else {
            vehicleCountLabel.setText("Vehicle count: "
                    + solution.getVehicleCount());
            totalCostLabel
                    .setText("Cost: "
                            + new DecimalFormat("0.###").format(solution
                                    .getTotalDistance())
                            + " + "
                            + new DecimalFormat("0.###").format(solution
                                    .getExpectedRecourseCost())
                            + " = "
                            + new DecimalFormat("0.###").format(solution
                                    .getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost()) + " (in iteration "
                            + solution.getIteration() + ")");
        }
    }

    public void reset() {
        setSolution(null);
        vehicleCountLabel.setText("Vehicle count: 0");
        totalCostLabel.setText("Total distance: 0");
        repaint();
    }

    public void setSolution(Solution solution) {
        jGraphPanel.setSolution(solution);
        this.solution = solution;
        repaint();
    }

    public void setVrpProblem(VrpProblem vrpProblem) {
        jGraphPanel.setVrpProblem(vrpProblem);
        this.vrpProblem = vrpProblem;
        repaint();
    }
    
    public void setDemandScenario(DemandScenario dScenario) {
        this.dScenario = dScenario;
    }
}
