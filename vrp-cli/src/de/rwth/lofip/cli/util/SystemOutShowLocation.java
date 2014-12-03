package de.rwth.lofip.cli.util;

import java.io.PrintStream;
import java.text.MessageFormat;


/**
 * @author Jeeeyul 2011. 11. 1. 4:36:51
 * @since M1.10
 */
public class SystemOutShowLocation extends PrintStream {
   private static final SystemOutShowLocation INSTANCE = new SystemOutShowLocation();
 
   public static void activate() {
      System.setOut(INSTANCE);
   }
 
   private SystemOutShowLocation() {
      super(System.out);
   }
 
   @Override
   public void println(Object x) {
      showLocation();
      super.println(x);
   }
 
   @Override
   public void println(String x) {
      showLocation();
      super.println(x);
   }
 
   private void showLocation() {
      StackTraceElement element = Thread.currentThread().getStackTrace()[3];
      super.print(MessageFormat.format("({0}:{1, number,#}) : ", element.getFileName(), element.getLineNumber()));
   }

}

   


