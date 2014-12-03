package de.rwth.lofip.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JScrollPane {

	private static final long serialVersionUID = 4544648876246724407L;

	private JTextArea textPane = new JTextArea();

	public LogPanel() {
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setEditable(false);
		this.add(textPane);
	}

	public void log(String logmessage) {
		String currentText = textPane.getText();
		currentText += "\n" + logmessage;
		textPane.setText(currentText);
		repaint();
	}

}
