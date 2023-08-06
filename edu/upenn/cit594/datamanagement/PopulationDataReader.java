package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.FileData;

public class PopulationDataReader {
    private String fileName;
    private FileData file;

    public PopulationDataReader(FileData fileInput) {
        this.fileName = fileInput.getPopulationFile();
        this.file = fileInput;
    } 
    
    public Map<String, Integer> readPopulationData() throws IOException, CSVFormatException {
        
    	Map<String, Integer> populationDataMap = new HashMap<>();
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
            Integer populationIdx = headerMap.get("population");

            if (zipCodeIdx == null || populationIdx == null) {
                throw new IllegalArgumentException("CSV file is missing required columns");
            }

            String[] line;
            while ((line = csvReader.readRow()) != null) {
                if (line.length >= 2) {
                    String populationStr = line[populationIdx];
                    String zipCodeStr = line[zipCodeIdx];

                    if (isValidZipCode(zipCodeStr) && isValidPopulation(populationStr)) {
                        int population = Integer.parseInt(populationStr);
                        populationDataMap.put(zipCodeStr, population);
                    }
                }
            }
        }

        return populationDataMap;
    }

    private boolean isValidZipCode(String zipCodeStr) {
        if (zipCodeStr == null || zipCodeStr.length() < 5)
            return false;

        String zipCode = zipCodeStr.substring(0, 5);
        return zipCode.matches("\\d{5}");
    }

    private boolean isValidPopulation(String populationStr) {
        if (populationStr == null || populationStr.isEmpty())
            return false;

        return populationStr.matches("\\d+");
    }
}
