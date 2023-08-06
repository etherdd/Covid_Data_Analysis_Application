package edu.upenn.cit594.ui;

import edu.upenn.cit594.util.FileData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsReader {

    private String[] args;

    public ArgumentsReader(String[] inputArgs) {
        this.args = inputArgs;
    }

    public FileData createFileData() {
    	
    	Map<String, String> args = parseArguments();
    	if (args == null) {
            throw new IllegalArgumentException("Invalid arguments");
    	} else {
	    	FileData files = new FileData(parseArguments());
	    	return files;
    	}
    }
       
        

    private Map<String, String> parseArguments() {
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
        
        if (arguments == null || arguments.isEmpty()) {
            System.out.println("Invalid or missing runtime arguments!");
            printUsage();
            return null;
        }

        // Check if the COVID data file format is valid
        if (arguments.containsKey("covid") && !isValidCovidDataFile(arguments.get("covid"))) {
            System.out.println("Invalid COVID data file format! The file must have .csv or .json extension.");
            printUsage();
            return null;
        }

        // Check if the COVID data file exists and is valid
        if (arguments.containsKey("covid")) {
            String covidFileName = arguments.get("covid");
            File covidFile = new File(covidFileName);
            if (!isValidFile(covidFile) || !isValidCovidDataFile(covidFileName)) {
                return null; // Invalid COVID data file
            }
        }

        // Check if the properties file exists and is valid
        if (arguments.containsKey("properties")) {
            String propertiesFileName = arguments.get("properties");
            File propertiesFile = new File(propertiesFileName);
            if (!isValidFile(propertiesFile)) {
                return null; // Invalid properties file
            }
        }

        // Check if the population file exists and is valid
        if (arguments.containsKey("population")) {
            String populationFileName = arguments.get("population");
            File populationFile = new File(populationFileName);
            if (!isValidFile(populationFile)) {
                return null; // Invalid population file
            }
        }

        // Check if the log file exists and is valid
        if (arguments.containsKey("log")) {
            String logFileName = arguments.get("log");
            File logFile = new File(logFileName);
            if (!isValidLogFile(logFile)) {
                return null; // Invalid log file
            }
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
        System.out.println("Usage: java Main [--covid=<covid_file>] [--properties=<properties_file>] [--population=<population_file>] [--log=<log_file>]");
    }
}
