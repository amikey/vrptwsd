package de.rwth.lofip.cli.util;

import java.io.PrintStream;

public class SystemOutDeactivate extends PrintStream {
	   
	   private static final SystemOutDeactivate INSTANCE = new SystemOutDeactivate();
	 
	   public static void activate() {
	      System.setOut(INSTANCE);
	   }
	 
	   public SystemOutDeactivate() {
	      super(System.out);
	   }
	 
	   @Override
	   public void println(Object x) {
	   }
	 
	   @Override
	   public void println(String x) {
	   }
	 
}	   
