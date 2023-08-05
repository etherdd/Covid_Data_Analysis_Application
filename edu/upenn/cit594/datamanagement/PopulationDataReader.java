package edu.upenn.cit594.studenttests.datamanagement;

import edu.upenn.cit594.studenttests.util.PopulationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopulationDataReader {
    @SuppressWarnings("unused")
    private final CharacterReader reader;

    public PopulationDataReader(CharacterReader reader) {
        this.reader = reader;
    }

    public List<PopulationData> readPopulation1() throws IOException, CSVFormatException {

        StringBuilder buildField = new StringBuilder(); // StringBuilder for each field
        List<String> fields = new ArrayList<>(); // List to store fields of each row
        boolean inQuotes = false;
        boolean starField = false;
        int lineNo = 1; // Keep track of line number
        int columnNo = 0; // Keep track of column number
        int rowNo = 0; // Current row
        int fieldNo = 0; // Current field
        int currentChar;
        int prevChar = 0;
        boolean isHeader = true;
        boolean unvalidZip = false;

        String currentZipCode = null;

        List<PopulationData> populationDataList = new ArrayList<>();
        while ((currentChar = reader.read()) != -1) { // if not EOL
            // skip header
            if (isHeader) {
                if (currentChar == '\n') {
                    isHeader = false;
                }
                continue;
            }

            char cur = (char) currentChar;
            if (currentChar == '\"' && inQuotes) {
                currentChar = reader.read();
                if (currentChar == ',' || currentChar == '\n') {
                    inQuotes = false;
                }
            }

            if (currentChar == '\n') { // end of line, add new population
                rowNo++;
                String populationStr = buildField.toString();
                int population = Integer.parseInt(populationStr.trim());
                // if unvalidZip -> continue
                if (unvalidZip) {
                    continue;
                }
                PopulationData curPopulationData = new PopulationData(currentZipCode, population);
                populationDataList.add(curPopulationData);
                fields.add(populationStr);
                buildField.setLength(0);
            } else if (currentChar == ',' && !inQuotes) {
                String populationStr = buildField.toString();
                if (!isValidZipCode(populationStr)) {
                    unvalidZip = true;
//                    throw new CSVFormatException("Invalid Zip Code", lineNo, columnNo, rowNo, fieldNo);
                } else {
                    currentZipCode = populationStr;
                    fields.add(populationStr);
                    buildField.setLength(0);
                }
                fieldNo++;
            } else if (currentChar == '\"') {
                inQuotes = !inQuotes;
                if (inQuotes && !starField) {
                    starField = true;
                } else if (inQuotes && starField) {
                    buildField.append((char) currentChar);
                    starField = false;
                }
            } else {
                buildField.append((char) currentChar);
                starField = false;
            }
            prevChar = currentChar;
        }

        // Last row does not end with a new line char
        if (buildField.length() > 0) {
            fields.add(buildField.toString());
        }

        // Check formatting
        if (inQuotes) {
            throw new CSVFormatException("Incomplete Quote ", lineNo, columnNo, rowNo, fieldNo);
        }

        if (fields.isEmpty() && currentChar == -1) {
            return null;
        }

        return populationDataList;
    }

    public List<PopulationData> readPopulation() throws IOException, CSVFormatException {

        StringBuilder buildField = new StringBuilder(); // StringBuilder for each field
        List<String> fields = new ArrayList<>(); // List to store fields of each row
        boolean inQuotes = false;
        boolean starField = false;
        int lineNo = 1; // Keep track of line number
        int columnNo = 0; // Keep track of column number
        int rowNo = 0; // Current row
        int fieldNo = 0; // Current field
        int currentChar;
        int prevChar = 0;
        boolean isHeader = true;
        boolean unvalidZip = false;

        String currentZipCode = null;

        List<PopulationData> populationDataList = new ArrayList<>();
        while ((currentChar = reader.read()) != -1) { // if not EOL
            // skip header
            if (isHeader) {
                if (currentChar == '\n') {
                    isHeader = false;
                }
                continue;
            }

            char cur = (char) currentChar;
            if (currentChar == '\"' && inQuotes) {
                currentChar = reader.read();
                if (currentChar == ',' || currentChar == '\n') {
                    inQuotes = false;
                }
            }

            if (currentChar == '\n') { // end of line, add new population
                rowNo++;
                String populationStr = buildField.toString();
                try {
                    int population = Integer.parseInt(populationStr.trim());
                    // if unvalidZip -> continue
                    if (unvalidZip) {
                        continue;
                    }
                    PopulationData curPopulationData = new PopulationData(currentZipCode, population);
                    populationDataList.add(curPopulationData);
                    fields.add(populationStr);
                } catch (NumberFormatException e) {
                    // skip line if population figure is not int
                    buildField.setLength(0);
                    continue;
                }
                buildField.setLength(0);
            } else if (currentChar == ',' && !inQuotes) {
                String populationStr = buildField.toString();
                if (!isValidZipCode(populationStr)) {
                    unvalidZip = true;
//                    throw new CSVFormatException("Invalid Zip Code", lineNo, columnNo, rowNo, fieldNo);
                } else {
                    currentZipCode = populationStr;
                    fields.add(populationStr);
                    buildField.setLength(0);
                }
                fieldNo++;
            } else if (currentChar == '\"') {
                inQuotes = !inQuotes;
                if (inQuotes && !starField) {
                    starField = true;
                } else if (inQuotes && starField) {
                    buildField.append((char) currentChar);
                    starField = false;
                }
            } else {
                buildField.append((char) currentChar);
                starField = false;
            }
            prevChar = currentChar;
        }

        // Last row does not end with a new line char
        if (buildField.length() > 0) {
            fields.add(buildField.toString());
        }

        // Check formatting
        if (inQuotes) {
            throw new CSVFormatException("Incomplete Quote ", lineNo, columnNo, rowNo, fieldNo);
        }

        if (fields.isEmpty() && currentChar == -1) {
            return null;
        }

        return populationDataList;
    }


    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("^\\d{5}$");
    }


}
