package de.rwth.lofip.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.rwth.lofip.gui.SolutionPanel;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.DemandScenarioUtils;

public class LoadScenariofileListener implements ActionListener{
 
	    private SolutionPanel solutionPanel;

	    private JFileChooser jfc = new JFileChooser();

	    public LoadScenariofileListener(SolutionPanel solutionPanel) 
	    {
	        this.solutionPanel = solutionPanel;
	    }
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {

	        int retVal = jfc.showOpenDialog(null);
	        if (retVal == JFileChooser.APPROVE_OPTION) {
	            File file = jfc.getSelectedFile();
	            try {
	                 
	            	List<String> lines = IOUtils.readLines(FileUtils
	                        .openInputStream(file));
	            	// create a scenario from the open file
	                DemandScenario dScenario = DemandScenarioUtils
	                         .createScenarioFromStringList(lines);
	                // set the scenario for the Solution Panel so it can be used later
	                solutionPanel.setDemandScenario(dScenario);
	                solutionPanel.repaint();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
	    }

}
