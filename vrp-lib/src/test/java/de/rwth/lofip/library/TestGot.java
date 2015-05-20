package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class TestGot {
	
	@Test
	public void TestIsGotAlreadyExistsInRecourseActions() {
		
		GroupOfTours got = SetUpUtils.getGotWithCustomer1And2();
		List<GroupOfTours> list = new ArrayList<GroupOfTours>();
		list.add(got);
		
		assertEquals(true, got.isGotAlreadyExistsInRecourseActions(got, list));
		
		list = new ArrayList<GroupOfTours>();
		list.add(SetUpUtils.getGotWithCustomer3And4());
		
		assertEquals(false, got.isGotAlreadyExistsInRecourseActions(got, list));
	}
	
	

}
