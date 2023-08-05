package edu.upenn.cit594.studenttests.datamanagement;

import edu.upenn.cit594.studenttests.util.COVIDData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CovidDataReader {

    // reading COVID data from either a CSV or a JSON file

    // You should ignore any records where the ZIP Code is not 5 digits or the timestamp is not
    // in the specified format. For any other fields, an empty value should be interpreted as being 0.

    public List<COVIDData> readCovidData(String covidFile) throws IOException, ParseException {
        List<COVIDData> covidDataList = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(covidFile));
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(reader);

        for (Object o : a) {
            JSONObject covidJSON = (JSONObject) o;
            String zipCode = covidJSON.get("zip_code").toString();
            if (!isValidZipCode(zipCode)) {
                // Ignore records with invalid Zipcode
                continue;
            }
            Long neg = covidJSON.get("NEG") == null ? 0L : (Long) covidJSON.get("NEG");
//            int pos = covidJSON.get("POS") == null ? (int) 0L : (Integer) covidJSON.get("POS");
//            int deaths = covidJSON.get("deaths") == null ? (int) 0L : (Integer) covidJSON.get("deaths");
//            int hospitalized = covidJSON.get("hospitalized") == null ? (int) 0L : (Integer) covidJSON.get("hospitalized");
            Long partialVaccinated = covidJSON.get("partially_vaccinated") == null ? 0L : (Long) covidJSON.get("partially_vaccinated");
            Long fullyVaccinated = covidJSON.get("fully_vaccinated") == null ? 0L : (Long) covidJSON.get("fully_vaccinated");
            String timestamp = (String) covidJSON.get("etl_timestamp");
            if (!isValidTimestamp(timestamp)) {
                // Ignore records with invalid timestamp
                continue;
            }
            covidDataList.add(new COVIDData(zipCode, partialVaccinated, fullyVaccinated, timestamp));
        }
        System.out.println("finished");
        return covidDataList;
    }


    //    public List<COVIDData> readCovidData1(String covidFile) {
//        List<COVIDData> covidDataList = new ArrayList();
//
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(covidFile));
//
//            try {
//                JSONParser parser = new JSONParser();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    if (covidFile.endsWith(".json")) {
//                        line = line.trim();
//                        if (line.charAt(0) == '[') {
//                            line = line.substring(1);
//                        }
//                        line = line.substring(0, line.length() - 1);
//                        JSONObject covidJSON = (JSONObject) parser.parse(line);
//                        String zipCode = (String) covidJSON.get("zip_code");
//                        int neg = covidJSON.get("NEG") == null ? (int) 0L : (Integer) covidJSON.get("NEG");
//                        int pos = covidJSON.get("POS") == null ? (int) 0L : (Integer) covidJSON.get("POS");
//                        int deaths = covidJSON.get("deaths") == null ? (int) 0L : (Integer) covidJSON.get("deaths");
//                        int hospitalized = covidJSON.get("hospitalized") == null ? (int) 0L : (Integer) covidJSON.get("hospitalized");
//                        int partialVaccinated = (Integer) covidJSON.get("partially_vaccinated");
//                        int fullyVaccinated = (Integer) covidJSON.get("fully_vaccinated");
//                        String timestamp = (String) covidJSON.get("timestamp");
//
//                        covidDataList.add(new COVIDData(zipCode, partialVaccinated, fullyVaccinated, timestamp));
//                    } else if (covidFile.endsWith(".csv")) {
//                        System.out.println("csv reader.");
//                    }
//                }
//            } catch (Throwable var19) {
//                try {
//                    reader.close();
//                } catch (Throwable var18) {
//                    var19.addSuppressed(var18);
//                }
//
//                throw var19;
//            }
//
//            reader.close();
//        } catch (NumberFormatException | IOException | org.json.simple.parser.ParseException var20) {
//            var20.printStackTrace();
//        }
//
//        return covidDataList;
//    }

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

}