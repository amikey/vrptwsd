package javaStuff;

import java.util.Random;

import org.junit.Test;

public class TestRandom {

	@Test 
	public void testRND() {
		Random random1 = new Random(1);
		Random random2 = new Random(1);
		
		System.out.println(random1.nextInt(100));
		System.out.println(random2.nextInt(100));
		
		System.out.println(random1.nextInt(100));
		System.out.println(random2.nextInt(100));
		
		System.out.println(random1.nextInt(100));
		System.out.println(random2.nextInt(100));
		
		System.out.println(random1.nextInt(100));
		System.out.println(random2.nextInt(100));
	}
}
