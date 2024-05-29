package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.FileData;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.CovidDataReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class CovidPopulationProcessor extends PopulationProcessor {

    private static CovidPopulationProcessor instance;
    private Map<String, Map<String, List<String>>> vaccinationsCache;

    private CovidPopulationProcessor(FileData inputFile) {
        super(inputFile);
        this.vaccinationsCache = new HashMap<>();
    }

    public static CovidPopulationProcessor getInstance(FileData inputFile) {
        if (instance == null) {
            instance = new CovidPopulationProcessor(inputFile);
        }
        return instance;
    }

    public List<String> calculateVaccinationsPerCapita(String dateInput, String vaccineType) {
        // Check if the result for the current dateInput and vaccineType combination exists in the cache
        if (vaccinationsCache.containsKey(dateInput) && vaccinationsCache.get(dateInput).containsKey(vaccineType)) {
            return vaccinationsCache.get(dateInput).get(vaccineType);
        }

        try {
            // Get the list of CovidData objects
            List<CovidData> covidDataList = getCovidDataList();
            
            // Create maps to store total vaccinations and population data
            Map<String, Integer> totalVaccinationsMap = new HashMap<>();
            Map<String, Integer> populationMap = getPopulationDataMap();

            // Filter data for the specified date and vaccine type
            for (CovidData data : covidDataList) {
                if (parseTimestamp(data.getTimestamp()).equals(dateInput)) {
                    // Determine whether to use partially vaccinated or fully vaccinated counts based on the vaccineType
                    int vaccinations = vaccineType.equals("partial")
                            ? data.getPartiallyVaccinated()
                            : data.getFullyVaccinated();

                    // Update the totalVaccinationsMap with the cumulative vaccinations for each ZIP code
                    totalVaccinationsMap.put(data.getZipCode(), totalVaccinationsMap.getOrDefault(data.getZipCode(), 0) + vaccinations);
                }
            }

            // Create a list to store the output
            List<String> result = new ArrayList<>();

            // Calculate vaccinations per capita and add to the result list
            for (String zipCode : totalVaccinationsMap.keySet()) {
                int totalVaccinations = totalVaccinationsMap.get(zipCode);
                int population = populationMap.getOrDefault(zipCode, 0);

                // Ensure that there are valid values to calculate per capita vaccinations
                if (totalVaccinations > 0 && population > 0) {
                    double vaccinationsPerCapita = (double) totalVaccinations / population;
                    DecimalFormat df = new DecimalFormat("#0.0000");
                    String outputLine = zipCode + " " + df.format(vaccinationsPerCapita);
                    result.add(outputLine);
                }
            }

            // Sort the result list in ascending order of ZIP Codes
            result.sort(Comparator.naturalOrder());

            // Store the result in the cache
            if (!vaccinationsCache.containsKey(dateInput)) {
                vaccinationsCache.put(dateInput, new HashMap<>());
            }
            vaccinationsCache.get(dateInput).put(vaccineType, result);

            // Return the final result list
            return result;

        } catch (Exception e) {
            // In case of any exception, print the stack trace and return an empty list
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static String parseTimestamp(String inputDate) throws java.text.ParseException {
        // Define the input format for parsing the date
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Parse the input date string into a Date object
        Date date = inputFormat.parse(inputDate);
        
        // Define the output format for formatting the date
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Format the Date object into a string with the desired output format
        String outputDate = outputFormat.format(date);
        
        return outputDate;
    }

    public List<CovidData> getCovidDataList() throws IOException, CSVFormatException, IllegalAccessException, ParseException {
        CovidDataReader covidDataReader = new CovidDataReader(file);
        List<CovidData> covidDataList = covidDataReader.readCovidData();
        return covidDataList;
    }
}
