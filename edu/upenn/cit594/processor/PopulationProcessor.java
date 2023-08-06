package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.FileData;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.PopulationDataReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PopulationProcessor {

    private static PopulationProcessor instance;
    protected FileData file;
    protected Map<Integer, Integer> totalPopulationCache; // Cache for storing calculated total populations

    protected PopulationProcessor(FileData inputFile) {
        this.file = inputFile;
        this.totalPopulationCache = new HashMap<>();
    }

    public static PopulationProcessor getInstance(FileData inputFile) {
        if (instance == null) {
            instance = new PopulationProcessor(inputFile);
        }
        return instance;
    }

    public Map<String, Integer> getPopulationDataMap() throws IOException, CSVFormatException {
        PopulationDataReader populationDataReader = new PopulationDataReader(file);
        return populationDataReader.readPopulationData();
    }

    public int calculateTotalPopulation(Map<String, Integer> populationDataMap) {
        int totalPopulation = 0;

        for (int population : populationDataMap.values()) {
            totalPopulation += population;
        }

        return totalPopulation;
    }

    public int getTotalPopulation(int actionNumber) throws IOException, CSVFormatException {
        // Check if the result is already cached
        if (totalPopulationCache.containsKey(actionNumber)) {
            return totalPopulationCache.get(actionNumber);
        } else {
            Map<String, Integer> populationDataMap = getPopulationDataMap();
            int totalPopulation = 0;

            if (populationDataMap != null) {
                totalPopulation = calculateTotalPopulation(populationDataMap);
            }

            // Cache the result for future use
            totalPopulationCache.put(actionNumber, totalPopulation);

            return totalPopulation;
        }
    }
}

