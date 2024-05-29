package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.FileData;
import java.util.HashMap;
import java.util.Map;

public class PropertyProcessor {

    private FileData file;
    private Map<String, Double> averageMarketValueCache;
    private Map<String, Double> averageTotalLivableAreaCache;

    public PropertyProcessor(FileData fileData) {
        this.file = fileData;
        this.averageMarketValueCache = new HashMap<>();
        this.averageTotalLivableAreaCache = new HashMap<>();
    }

    public double calculateAverageMarketValue(String zipCode) throws Exception {
        // Check if the result for the current ZIP code exists in the cache
        if (averageMarketValueCache.containsKey(zipCode)) {
            return averageMarketValueCache.get(zipCode);
        }

        // If not present in the cache, calculate the average market value
        double averageMarketValue = calculateAverage(zipCode, new AverageMarketValueCalculator(file));
        // Store the result in the cache
        averageMarketValueCache.put(zipCode, averageMarketValue);
        return averageMarketValue;
    }

    public double calculateAverageTotalLivableArea(String zipCode) throws Exception {
        // Check if the result for the current ZIP code exists in the cache
        if (averageTotalLivableAreaCache.containsKey(zipCode)) {
            return averageTotalLivableAreaCache.get(zipCode);
        }

        // If not present in the cache, calculate the average total livable area
        double averageTotalLivableArea = calculateAverage(zipCode, new AverageTotalLivableAreaCalculator(file));
        // Store the result in the cache
        averageTotalLivableAreaCache.put(zipCode, averageTotalLivableArea);
        return averageTotalLivableArea;
    }

    private double calculateAverage(String zipCode, AverageCalculator calculator) throws Exception {
        return calculator.calculateAverage(zipCode);
    }

}

