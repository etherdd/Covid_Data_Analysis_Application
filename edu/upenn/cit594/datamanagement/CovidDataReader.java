package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.FileData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CovidDataReader {
    private String fileName;
    private FileData file;

    public CovidDataReader(FileData fileInput) {
        this.fileName = fileInput.getCovidFile();
        this.file = fileInput;
    }

    public List<CovidData> readCovidData() throws IOException, ParseException, CSVFormatException, IllegalAccessException {
    	
        List<CovidData> covidDataList = new ArrayList<>();
        
        String logFileName = file.getLogFile();
        Logger logger = Logger.getInstance();
        if (logFileName != null) {
            logger.setOutputDestination(logFileName);
        }
        
        logger.logEvent(fileName);

        if (fileName.endsWith(".json")) {
            // Read JSON data
            covidDataList = readCovidDataFromJSON();
        } else if (fileName.endsWith(".csv")) {
            // Read CSV data
            covidDataList = readCovidDataFromCSV();
        } else {
            throw new IllegalAccessException("Unsupported file format.");
        }

        return covidDataList;
    }

    private List<CovidData> readCovidDataFromJSON() throws IOException, ParseException {
    	List<CovidData> covidDataList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(reader);

        for (Object o : a) {
            JSONObject covidJSON = (JSONObject) o;
            String zipCodeStr = covidJSON.get("zip_code").toString();
            if (!isValidZipCode(zipCodeStr)) {
                // Ignore records with invalid Zipcode
                continue;
            }

            long partiallyVaccinatedLong = covidJSON.get("partially_vaccinated") == null ? 0L : (Long) covidJSON.get("partially_vaccinated");
            int partiallyVaccinated = (int) (long) partiallyVaccinatedLong;
            long fullyVaccinatedLong = covidJSON.get("fully_vaccinated") == null ? 0L : (Long) covidJSON.get("fully_vaccinated");
            int fullyVaccinated = (int) (long) fullyVaccinatedLong;
            String timestampStr = (String) covidJSON.get("etl_timestamp");
            if (!isValidTimestamp(timestampStr)) {
                // Ignore records with invalid timestamp
                continue;
            }
//            covidDataList.add(new CovidData(zipCode, partialVaccinated, fullyVaccinated, timestamp));
            covidDataList.add(new CovidData(zipCodeStr, timestampStr, partiallyVaccinated, fullyVaccinated));
        }
        return covidDataList;
    }

    
    

    private List<CovidData> readCovidDataFromCSV() throws IOException, CSVFormatException {
        List<CovidData> covidDataList = new ArrayList<>();
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
            Integer timestampIdx = headerMap.get("etl_timestamp");
            Integer partiallyVaccinatedIdx = headerMap.get("partially_vaccinated");
            Integer fullyVaccinatedIdx = headerMap.get("fully_vaccinated");

            if (zipCodeIdx == null || timestampIdx == null ||
                    partiallyVaccinatedIdx == null || fullyVaccinatedIdx == null) {
                throw new IllegalArgumentException("CSV file is missing required columns");
            }

            String[] line;
            while ((line = csvReader.readRow()) != null) {
                if (line.length >= 4) {
                    String zipCodeStr = line[zipCodeIdx];
                    String timestampStr = line[timestampIdx];
                    String partiallyVaccinatedStr = line[partiallyVaccinatedIdx];
                    String fullyVaccinatedStr = line[fullyVaccinatedIdx];

                    if (isValidZipCode(zipCodeStr) && isValidTimestamp(timestampStr)) {
                        int partiallyVaccinated = parseInteger(partiallyVaccinatedStr);
                        int fullyVaccinated = parseInteger(fullyVaccinatedStr);

                        covidDataList.add(new CovidData(zipCodeStr, timestampStr, partiallyVaccinated, fullyVaccinated));
                    }
                }
            }
        }

        return covidDataList;
    }


    
    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("^\\d{5}$");
    }

    private boolean isValidTimestamp(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(timestamp);
            String formattedDate = dateFormat.format(date);
            if (timestamp.equals(formattedDate)) {
                return true;
            }
        } catch (java.text.ParseException e) {
            return false;
        }
        return false;
    }

    private int parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException e) {
            return 0; // Return 0 for non-integer or missing values
        }
    }

}