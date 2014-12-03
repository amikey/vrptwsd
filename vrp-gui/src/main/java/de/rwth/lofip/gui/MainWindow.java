package de.rwth.lofip.gui;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import de.rwth.lofip.gui.listeners.DeterministicSolverButtonListener;
import de.rwth.lofip.gui.listeners.LeiEtAlSolverButtonListener;
import de.rwth.lofip.gui.listeners.LoadFileActionListener;
import de.rwth.lofip.gui.listeners.LoadScenariofileListener;
import de.rwth.lofip.gui.listeners.RepairSolutionButtonListener;
import de.rwth.lofip.gui.listeners.SolverButtonListener;
import de.rwth.lofip.gui.listeners.TabuSearchSolverButtonListener;
import de.rwth.lofip.gui.listeners.LoadDemoButtonListener;
import de.rwth.lofip.library.interfaces.SolverInterface;
/**
 * The executable main class for the GUI part of the VRPSolver program. It
 * consists of two big parts. The {@code SolutionPanel} contains all information
 * about the loaded VRP and its solution. The buttons for interacting with the
 * program are on the other half of the program window.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * @author obock
 * 
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = -5105576936008367959L;

    /**
     * This method calls the method createAndShowUI()
     * 
     * @args parameters from invocation - but they are unused since this is a
     *       GUI-only application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }

    /**
     * The actual display-method for showing the GUI. This is according to best
     * practices for Swing/GUI programming, that the actual invocation is done
     * in a separate method.
     */
    private static void createAndShowUI() {

        MainWindow mainWindow = new MainWindow();
        LogPanel logPanel = new LogPanel();

        SolutionPanel solutionPanel = new SolutionPanel();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton loadFileButton = new JButton("open file");
        loadFileButton.addActionListener(new LoadFileActionListener(
                solutionPanel));

        buttonPanel.add(loadFileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // get all solvers by introspection
        ServiceLoader<SolverInterface> solvers = ServiceLoader
                .load(SolverInterface.class);

        for (SolverInterface solver : solvers) {
            JButton solverButton = new JButton(solver.getDescriptiveName());
            try {
                solverButton.addActionListener(new SolverButtonListener(
                        ConstructorUtils.invokeConstructor(solver.getClass()),
                        solutionPanel, logPanel));
                buttonPanel.add(solverButton);
            } catch (NoSuchMethodException | IllegalAccessException
                    | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton leiEtAlMetaHeuristicSolverButton = new JButton(
                "Heuristic from Lei et al.");
        leiEtAlMetaHeuristicSolverButton
                .addActionListener(new LeiEtAlSolverButtonListener(
                        solutionPanel, logPanel));
        buttonPanel.add(leiEtAlMetaHeuristicSolverButton);

        JButton tabuSearchHeuristicSolverButton = new JButton(
                "TabuSearch-Heuristic");
        tabuSearchHeuristicSolverButton
                .addActionListener(new TabuSearchSolverButtonListener(
                        solutionPanel, logPanel));
        buttonPanel.add(tabuSearchHeuristicSolverButton);
        
        JButton DeterministicSolverButton = new JButton(
                "Deterministic-Solver");
        DeterministicSolverButton
                .addActionListener(new DeterministicSolverButtonListener(
                        solutionPanel, logPanel));
        buttonPanel.add(DeterministicSolverButton);
        
        // load a customer-demand-changing-scenario  file 
        
        JButton loadScenarioButton = new JButton("load scenario");
        loadScenarioButton.addActionListener(new LoadScenariofileListener(
                solutionPanel));

        buttonPanel.add(loadScenarioButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        

        
        JButton RepairSolverButton = new JButton(
                "Reparatur-Heuristik");
        RepairSolverButton
                .addActionListener(new RepairSolutionButtonListener( 
                        solutionPanel, logPanel));
        buttonPanel.add(RepairSolverButton);

        mainWindow.setSize(800, 800);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // SplitPane for adjusting the width of the solutionPanel and the
        // buttons
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buttonPanel, solutionPanel);

        mainWindow.getContentPane().add(splitPane);
        // x.getContentPane().add(logPanel);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }
}
