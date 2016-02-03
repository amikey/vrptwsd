package de.rwth.lofip.cli;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.cli.withoutTW.CreateResultsForUmplanungOhneRiskPoolingOhneTW;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;

public class TestDeleteTimeWindows {
	
	protected List<VrpProblem> problems = new LinkedList<VrpProblem>();
	
	@Test
	public void testDeleteTimeWindows() throws IOException {
		givenC101Problem();
		whenDeletingTimeWindows();
		thenTimeWindowsShouldBeDeleted();
	}

	private void givenC101Problem() throws IOException {
		problems = ReadAndWriteUtils.readSolomonProblemC101();
	}

	private void whenDeletingTimeWindows() {
		new CreateResultsForUmplanungOhneRiskPoolingOhneTW().deleteTimeWindwos(problems);
	}

	private void thenTimeWindowsShouldBeDeleted() {
		for (VrpProblem problem : problems)
			for (Customer c : problem.getCustomers()) {
				assertEquals(0.0,c.getTimeWindowOpen(),0.00001);
				assertEquals(problem.getMaxTime(),c.getTimeWindowClose(),0.00001);
			}
	}

}
