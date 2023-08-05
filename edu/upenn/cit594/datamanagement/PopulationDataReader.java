package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationDataReader {
    private String fileName;

    public PopulationDataReader(String populationFileName) {
        this.fileName = populationFileName;
    }
    
    
    public List<PopulationData> readPopulationData() throws IOException, CSVFormatException {
        List<PopulationData> populationDataList = new ArrayList<>();
        Map<String, Integer> headerMap = new HashMap<>();

        try (var reader = new CharacterReader(fileName)) {
            var csvReader = new CSVReader(reader);
            String[] header = csvReader.readRow();
            if (header != null) {
                for (int i = 0; i < header.length; i++) {
                    headerMap.put(header[i].toLowerCase(), i);
                }
            }
            
            Integer zipCodeIdx = headerMap.get("zip_code");
            Integer populationIdx  = headerMap.get("population");

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
                        populationDataList.add(new PopulationData(population, zipCodeStr));
                    }
                }
            }
        }

        return populationDataList;
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