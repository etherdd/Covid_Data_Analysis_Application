package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;
import edu.upenn.cit594.datamanagement.PropertyDataReader;
import edu.upenn.cit594.util.FileData;

import java.io.IOException;
import java.util.List;

public class AverageMarketValueCalculator implements AverageCalculator {

    private FileData file;

    public AverageMarketValueCalculator(FileData fileData) {
        this.file = fileData;
    }

    @Override
    public double calculateAverage(String zipCode) throws IOException, Exception {
    	
    	PropertyDataReader propertyDataReader = new PropertyDataReader(file);
        List<PropertyData> propertyDataList = propertyDataReader.readPropertyData();
        
        
        int count = 0;
        double totalMarketValue = 0.0;

        for (PropertyData property : propertyDataList) {
            if (property.getZipCode().equals(zipCode)) {
                totalMarketValue += property.getMarketValue();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return totalMarketValue / count;
    }
}

