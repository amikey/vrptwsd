package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.rwth.lofip.library.solver.util.TabuList;

public class TestTabuList {
	
	@Test
	public void testTabuList() {
		TabuList tabuList = new TabuList();
		
		tabuList.addSolutionToTabuList(825.853907,10);
		
		assertEquals(true, tabuList.isMoveTaboo(825.853907, 12));
		assertEquals(false, tabuList.isMoveTaboo(825.853907, 20));
		
		assertEquals(true, tabuList.isMoveTaboo(725.853907, 12));
		
	}

}
