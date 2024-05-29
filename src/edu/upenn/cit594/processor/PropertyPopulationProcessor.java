package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.FileData;
import edu.upenn.cit594.util.PropertyData;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.PropertyDataReader;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class PropertyPopulationProcessor extends PopulationProcessor {

    private static PropertyPopulationProcessor instance;
    private Map<String, Integer> marketValueCache;
    private Map<String, Double> livableAreaCache;

    public PropertyPopulationProcessor(FileData inputFile) {
        super(inputFile);
        this.marketValueCache = new HashMap<>();
        this.livableAreaCache = new HashMap<>();
    }

    public static PropertyPopulationProcessor getInstance(FileData inputFile) {
        if (instance == null) {
            instance = new PropertyPopulationProcessor(inputFile);
        }
        return instance;
    }

    public int calculateTotalMarketValuePerCapita(String zipCode) throws IllegalAccessException, IOException, CSVFormatException, ParseException {
       
        if (marketValueCache.containsKey(zipCode)) {
            return marketValueCache.get(zipCode);
        }  
               
    	Map<String, Double> propertyDataMap = getZipcodeMarketValueDataMap();
    	int totalMarketValuePerCapita = (int) calculateTotalPerCapita(zipCode, propertyDataMap);
    	
    	marketValueCache.put(zipCode, totalMarketValuePerCapita);

        return totalMarketValuePerCapita;
    }

    public double calculateTotalLivableAreaPerCapita(String zipCode) throws IllegalAccessException, IOException, CSVFormatException, ParseException {
        
        if (livableAreaCache.containsKey(zipCode)) {
            return livableAreaCache.get(zipCode);
        }  
    	
        Map<String, Double> propertyDataMap = getZipcodeLivableAreaDataMap();
        double result = calculateTotalPerCapita(zipCode, propertyDataMap);

        // Format the result in "0.0000" format
        DecimalFormat df = new DecimalFormat("#0.0000");
        double totalLivableAreaPerCapita = Double.parseDouble(df.format(result));
        
        livableAreaCache.put(zipCode, totalLivableAreaPerCapita);

        return totalLivableAreaPerCapita;
    }
    
    
    

    public double calculateTotalPerCapita(String zipCode, Map<String, Double> propertyDataMapInput) {
        try {
            // Get the property data map
            Map<String, Double> propertyDataMap = propertyDataMapInput;
            // Get the population data map
            Map<String, Integer> populationDataMap = getPopulationDataMap();

            // Check if the ZIP code exists in both property and population data
            if (propertyDataMap.containsKey(zipCode) && populationDataMap.containsKey(zipCode)) {
                Double totalMarketValue = propertyDataMap.get(zipCode);
                Integer population = populationDataMap.get(zipCode);

                // Check if either market value or population is 0
                if (totalMarketValue == null || totalMarketValue == 0 || population == null || population == 0) {
                    return 0.0;
                }

                // Calculate market value per capita as a double
                double marketValuePerCapita = (double) totalMarketValue / population;
                
                // Format the result in "0.0000" format
                DecimalFormat df = new DecimalFormat("#0.0000");
                return Double.parseDouble(df.format(marketValuePerCapita));
            }
        } catch (IOException | CSVFormatException e) {
            e.printStackTrace();
        }
        // Return 0 if ZIP code not found or data is not valid
        return 0.0;
    }



    
    
    public List<PropertyData> getPropertyDataList() throws IOException, CSVFormatException, IllegalAccessException, ParseException {
    	
    	PropertyDataReader propertyDataReader = new PropertyDataReader(file);
        List<PropertyData> propertyDataList = propertyDataReader.readPropertyData();
        return propertyDataList;
    }
    
    

    public Map<String, Double> getZipcodeMarketValueDataMap() throws IOException, CSVFormatException, IllegalAccessException, ParseException {
        List<PropertyData> propertyDataList = getPropertyDataList();
        Map<String, Double> propertyZipcodeMarketvalueMap = new HashMap<>();

        for (PropertyData data : propertyDataList) {
            String zipCode = data.getZipCode();
            double marketValue = data.getMarketValue();

            // Check if the ZIP code is already present in the map
            if (propertyZipcodeMarketvalueMap.containsKey(zipCode)) {
                // If present, add the market value to the existing value
                double currentMarketValue = propertyZipcodeMarketvalueMap.get(zipCode);
                propertyZipcodeMarketvalueMap.put(zipCode, currentMarketValue + marketValue);
            } else {
                // If not present, create a new entry in the map with the market value
                propertyZipcodeMarketvalueMap.put(zipCode, marketValue);
            }
        }

        return propertyZipcodeMarketvalueMap;
    }
    
    
    public Map<String, Double> getZipcodeLivableAreaDataMap() throws IOException, CSVFormatException, IllegalAccessException, ParseException {
        List<PropertyData> propertyDataList = getPropertyDataList();
        Map<String, Double> propertyZipcodeLivableAreaMap = new HashMap<>();

        for (PropertyData data : propertyDataList) {
            String zipCode = data.getZipCode();
            double livableArea = data.geTotalLivableArea();

            if (propertyZipcodeLivableAreaMap.containsKey(zipCode)) {
                double currentMarketValue = propertyZipcodeLivableAreaMap.get(zipCode);
                propertyZipcodeLivableAreaMap.put(zipCode, currentMarketValue + livableArea);
            } else {
            	propertyZipcodeLivableAreaMap.put(zipCode, livableArea);
            }
        }

        return propertyZipcodeLivableAreaMap;
    }
}
