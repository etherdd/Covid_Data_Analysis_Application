package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDataReader {
    private String fileName;
    private FileData file;

    public PropertyDataReader(FileData fileInput) {
        this.fileName = fileInput.getPropertyFile();
        this.file = fileInput;
    }
    
    public void printPropertyData() throws IOException, CSVFormatException{
    	try (var reader = new CharacterReader(fileName)) {
    		var csvReader = new CSVReader(reader);
    		String[] row;
    		while ((row = csvReader.readRow()) != null) {
    			System.out.println(Arrays.toString(row));
    			System.out.println(row[0]);
    			System.out.println(row[1]);
    			System.out.println(row[2]);
    		}
    	} catch (IOException | CSVFormatException e) {
    		System.err.println(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public List<PropertyData> readPropertyData() throws IOException, CSVFormatException {
        List<PropertyData> propertyDataList = new ArrayList<>();
        Map<String, Integer> headerMap = new HashMap<>();
        
        
        String logFileName = file.getLogFile();
        Logger logger = Logger.getInstance();
        if (logFileName != null) {
            logger.setOutputDestination(logFileName);
        }
        
        logger.logEvent(fileName);

        try (var reader = new CharacterReader(fileName)) {
            var csvReader = new CSVReader(reader);
            String[] header = csvReader.readRow();
            if (header != null) {
                for (int i = 0; i < header.length; i++) {
                    headerMap.put(header[i].toLowerCase(), i);
                }
            }
            
            Integer zipCodeIdx = headerMap.get("zip_code");
            Integer marketValueIdx = headerMap.get("market_value");
            Integer totalLivableAreaIdx = headerMap.get("total_livable_area");

            if (zipCodeIdx == null || marketValueIdx == null || totalLivableAreaIdx == null) {
                throw new IllegalArgumentException("CSV file is missing required columns");
            }

            
            String[] line;
            while ((line = csvReader.readRow()) != null) {
                //String[] parts = line.split(",");
                if (line.length >= 3) {
                    String marketValueStr = line[marketValueIdx];
                    String totalLivableAreaStr = line[totalLivableAreaIdx];
                    String zipCodeStr = line[zipCodeIdx];

                    if (isValidZipCode(zipCodeStr)) {
                        double marketValue = parseDouble(marketValueStr);
                        double totalLivableArea = parseDouble(totalLivableAreaStr);
                        propertyDataList.add(new PropertyData(marketValue, totalLivableArea, zipCodeStr));
                    }
                }
            }
        }

        return propertyDataList;
    }

    private boolean isValidZipCode(String zipCodeStr) {
        if (zipCodeStr == null || zipCodeStr.length() < 5)
            return false;

        String zipCode = zipCodeStr.substring(0, 5);
        return zipCode.matches("\\d{5}");
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0; // Return 0.0 for non-numeric or missing values
        }
    }

}
