package edu.upenn.cit594;

import edu.upenn.cit594.util.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.ui.*;


public class Main {

    public static void main(String[] args) throws Exception {
        
    	
    	ArgumentsReader  argumentsChecker = new ArgumentsReader(args);
    	FileData files  =  argumentsChecker.createFileData();

        // Extract the log file names from the arguments to initialize the Logger   	
        String logFileName = files.getLogFile();
        Logger logger = Logger.getInstance();
        if (logFileName != null) {
            logger.setOutputDestination(logFileName);
        }
   	
        logger.logEvent(String.join(" ", args));
        
    	ActionUserInterface action = new ActionUserInterface (files);
    	action.start();  
    	
		return;

    }


}
