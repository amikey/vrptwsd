package log4j2;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jTest {

	  static Logger logger;
	  private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	 
	  public static void main(String[] args) throws IOException {      
		  createLoggingFileForLog4J2(); 
		  logger = LogManager.getLogger(Log4jTest.class);		 
		  writeLoggerMessages();	      	
	  }

	private static void createLoggingFileForLog4J2() {
		String logFilename = "C:/Users/Andreas/Dropbox/Uni/Diss/Code/logging/" + "Log - "        
                + sdf.format(Calendar.getInstance().getTime()) + ".log";
		File outputFile = new File(logFilename);		
		System.setProperty("logFilename", logFilename);	  
	}
	
	private static void writeLoggerMessages() {
		  logger.debug("This is a debug message");
	      logger.info("This is a info message");
	      logger.warn("This is a warn message");
	      logger.fatal("This is a fatal message");
	      logger.error("This is a error message");
	}

}

