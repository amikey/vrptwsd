package de.rwth.lofip.library.util;

import java.util.Random;

public class RandomUtils {

	private static Random rand = new Random(1); 
	
	public static int generateRandomNumber(int max, int min) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;	    	
	}

}
