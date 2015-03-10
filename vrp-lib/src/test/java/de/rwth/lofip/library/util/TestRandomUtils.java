package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestRandomUtils {
	
	@Test
	public void testGenerateRandomNumber() {
		int rand = RandomUtils.generateRandomNumber(100, 0);
		assertEquals(true, rand <= 100);
		assertEquals(true, rand >= 0);
		
		int rand2 = RandomUtils.generateRandomNumber(100, 0);
		assertEquals(true, rand <= 100);
		assertEquals(true, rand >= 0);
		
		assertEquals(true, rand != rand2);
	}

}
