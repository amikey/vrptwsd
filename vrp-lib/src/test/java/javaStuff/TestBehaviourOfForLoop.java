package javaStuff;

import org.junit.Test;

public class TestBehaviourOfForLoop {
	
	@Test
	public void testForLoop() {
		for (int i = 0; i < 0; i++)
			System.out.println("entered loop1");
		for (int i = 0; i < 1; i++)
			System.out.println("entered loop2");
	}

}
