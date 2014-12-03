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
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;

public class LoadFileActionListener implements ActionListener {

    private SolutionPanel solutionPanel;

    private JFileChooser jfc = new JFileChooser();

    public LoadFileActionListener(SolutionPanel solutionPanel) {
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
                VrpProblem vrpProblem = VrpUtils
                        .createProblemFromStringList(lines);
                solutionPanel.setVrpProblem(vrpProblem);
                solutionPanel.reset();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
