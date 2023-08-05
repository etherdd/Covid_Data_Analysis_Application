package edu.upenn.cit594.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsChecker {
	
	 private String[] args;
	 
	 public ArgumentsChecker(String[] inputArgs) {
	      
		 this.args = inputArgs;
	      
	 }
	 	 
	 
	 public Map<String, String> checkArguments(){
	     Map<String, String> arguments = parseArguments();
	
	     if (arguments == null) {
	         System.out.println("Invalid or missing runtime arguments!");
	         printUsage();
	         return null;
	     }
	
	     String covidFileName = arguments.get("covid");
	     String propertiesFileName = arguments.get("properties");
	     String populationFileName = arguments.get("population");
	     String logFileName = arguments.get("log");
	
	     File covidFile = new File(covidFileName);
	     File propertiesFile = new File(propertiesFileName);
	     File populationFile = new File(populationFileName);
	     File logFile = new File(logFileName);
	
	     if (!isValidFile(covidFile) || !isValidFile(propertiesFile) ||
	         !isValidFile(populationFile) || !isValidLogFile(logFile)) {
	         System.out.println("Invalid or missing input files!");
	         printUsage();
	         return null;
	     }
	
	     // Check if the COVID data file format is valid
	     if (!isValidCovidDataFile(covidFileName)) {
	         System.out.println("Invalid COVID data file format! The file must have .csv or .json extension.");
	         printUsage();
	         return null;
	     }
	     
	     return arguments;

	 }


	 
	 public Map<String, String> parseArguments() {
	        Map<String, String> arguments = new HashMap<>();
	        Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");

	        for (String arg : args) {
	            Matcher matcher = pattern.matcher(arg);
	            if (matcher.matches()) {
	                String name = matcher.group("name");
	                String value = matcher.group("value");

	                if (!isValidArgument(name)) {
	                    return null; // Invalid argument name
	                }

	                if (arguments.containsKey(name)) {
	                    return null; // Name used more than once
	                }

	                arguments.put(name, value);
	            } else {
	                return null; // Invalid argument format
	            }
	        }

	        // Check if all required arguments are provided
	        if (!arguments.containsKey("covid") ||
	            !arguments.containsKey("properties") ||
	            !arguments.containsKey("population") ||
	            !arguments.containsKey("log")) {
	            return null; // Missing required arguments
	        }

	        return arguments;
	    }

	    private static boolean isValidArgument(String argName) {
	        return argName.matches("covid|properties|population|log");
	    }

	    private static boolean isValidFile(File file) {
	        return file.exists() && file.canRead();
	    }

	    private static boolean isValidLogFile(File file) {
	        try {
	            if (!file.exists()) {
	                // If the log file does not exist, create a new file.
	                file.createNewFile();
	            }
	            // Open the log file with the append option.
	            FileWriter writer = new FileWriter(file, true);
	            writer.close();
	            return true;
	        } catch (IOException e) {
	            return false;
	        }
	    }

	    private static boolean isValidCovidDataFile(String covidFileName) {
	        return covidFileName.toLowerCase().endsWith(".csv") || covidFileName.toLowerCase().endsWith(".json");
	    }

	    private static void printUsage() {
	        System.out.println("Usage: java Main --covid=<covid_file> --properties=<properties_file> --population=<population_file> --log=<log_file>");
	    }
}







