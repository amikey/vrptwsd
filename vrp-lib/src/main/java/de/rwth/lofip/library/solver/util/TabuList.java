package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.SolutionGot;

public class TabuList {
	
	private int lengthOfList = 100000;
	private int[] tabuList = new int[lengthOfList]; // array ist automatisch mit 0 initialisiert, da ints mit 0 initialisiert werden

	public void addSolutionToTabuList(double objectiveValue, int iteration) {
		int position = (int) objectiveValue * 1000;
		position = position % lengthOfList; //modulo
		tabuList[position] = (int) ((int) iteration * 1.5);
	}
	
	public boolean isMoveTaboo(double objectiveValue, int iteration) {
		int position = (int) objectiveValue * 1000;
		position = position % lengthOfList; //modulo
		if (tabuList[position] > iteration)
			return true;
		else 
			return false;			
	}
}
