package edu.upenn.cit594.util;

import java.util.Map;

public class FileData {
	
	private String covidFile;
    private String propertyFile;
    private String populationFile;
    private String logFile;

    public FileData(Map<String, String> arguments) {
        this.covidFile = arguments.get("covid");
        this.propertyFile = arguments.get("properties");
        this.populationFile = arguments.get("population");
        this.logFile = arguments.get("log");
    }

	public String getCovidFile() {
		return covidFile;
	}


	public String getPropertyFile() {
		return propertyFile;
	}


	public String getPopulationFile() {
		return populationFile;
	}

	public String getLogFile() {
		return logFile;
	}


    
}
