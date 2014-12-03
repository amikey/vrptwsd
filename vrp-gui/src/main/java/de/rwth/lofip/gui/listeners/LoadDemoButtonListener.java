package de.rwth.lofip.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFileChooser;

import de.rwth.lofip.gui.SolutionPanel;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.solver.repair.util.Demonstrator;

public class LoadDemoButtonListener implements ActionListener{
	
	private SolutionPanel solutionPanel;
	Demonstrator demo = new Demonstrator();
	
	private JFileChooser jfc = new JFileChooser();
	
    public LoadDemoButtonListener(SolutionPanel solutionPanel) 
    {
        this.solutionPanel = solutionPanel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	    
    	PrintStream err = null;
		try {
			err = new PrintStream(new FileOutputStream("ErrorStreamUnterdruecken"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.setErr(err);
    	
    	Solution solution = null;
    	try {
			solution = demo.loadSolution();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	solutionPanel.setSolution(solution);
    	solutionPanel.setVrpProblem(solution.getVrpProblem());
    	
    	DemandScenario ds = null;
    	ds = demo.loadDS();
    	solutionPanel.setDemandScenario(ds);
    	
    	System.out.println("Demo geladen.");
    }

}
