package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.PropertyDataReader;
import edu.upenn.cit594.util.FileData;

import java.io.IOException;
import java.util.List;

public class AverageTotalLivableAreaCalculator implements AverageCalculator {

    private FileData file;

    public AverageTotalLivableAreaCalculator(FileData fileData) {
        this.file = fileData;
    }

    @Override
    public double calculateAverage(String zipCode) throws IOException, CSVFormatException {
    	
    	PropertyDataReader propertyDataReader = new PropertyDataReader(file);
        List<PropertyData> propertyDataList = propertyDataReader.readPropertyData();
        
        
        int count = 0;
        double totalLivableArea = 0.0;

        for (PropertyData property : propertyDataList) {
            if (property.getZipCode().equals(zipCode)) {
                totalLivableArea += property.geTotalLivableArea();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return totalLivableArea / count;
    }
}

