package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.util.*;
import edu.upenn.cit594.ui.*;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException, CSVFormatException {
        
    	
    	ArgumentsChecker  argumentsChecker = new ArgumentsChecker(args);
    	Map<String, String> arguments = argumentsChecker.checkArguments();    	

        // Extract the file names from the arguments
    	if (arguments == null) {
    		throw new IllegalArgumentException("Invalid arguments");
	    }
    	
        String covidFileName = arguments.get("covid");
        String propertiesFileName = arguments.get("properties");
        String populationFileName = arguments.get("population");
        String logFileName = arguments.get("log");
        
        
        

        PropertyDataReader propertyDataReader = new PropertyDataReader(propertiesFileName);
        List<PropertyData> propertyDataList = propertyDataReader.readPropertyData();
        
        for (PropertyData property : propertyDataList) {
        	System.out.println("Market Value: " + property.getMarketValue() +
                    ", Total Livable Area: " + property.getotalLivableArea() +
                    ", Zip Code: " + property.getZipCode());
        }

        
        
        PopulationDataReader populationDataReader = new PopulationDataReader(populationFileName);
        List<PopulationData> populationDataList = populationDataReader.readPopulationData();
        
        for (PopulationData population : populationDataList) {
        	System.out.println("Population: " + population.getPopulation() +
                    ", Zip Code: " + population.getZipCode());
        }
        
        
        
        
        CovidDataReader covidDataReader = new CovidDataReader(covidFileName);
        List<CovidData> covidDataList = covidDataReader.readCovidData();
        
        for (CovidData covid :covidDataList) {
        	System.out.println("Zip Code: " + covid.getZipCode() +
                    ", Timestamp: " + covid.getTimestamp() +
                    ", Partially Vaccinated: " + covid.getPartiallyVaccinated() +
                    ", Fully Vaccinated: " + covid.getFullyVaccinated());
        }

    }


}
