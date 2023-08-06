package edu.upenn.cit594.datamanagement;
/*
 * I attest that the code in this file is entirely my own except for the starter
 * code provided with the assignment and the following exceptions:
 * <Enter all external resources and collaborations here. Note external code may
 * reduce your score but appropriate citation is required to avoid academic
 * integrity violations. Please see the Course Syllabus as well as the
 * university code of academic integrity:
 *  https://catalog.upenn.edu/pennbook/code-of-academic-integrity/ >
 * Signed,
 * Author: YOUR NAME HERE
 * Penn email: <YOUR-EMAIL-HERE@seas.upenn.edu>
 * Date: YYYY-MM-DD
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * {@code CSVReader} provides a stateful API for streaming individual CSV rows
 * as arrays of strings that have been read from a given CSV file.
 *
 * @author YOUR-NAME-HERE
 */
public class CSVReader {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5130409650040L;
    private final CharacterReader reader;

    public CSVReader(CharacterReader reader) {
        this.reader = reader;
    }

    /**
     * This method uses the class's {@code CharacterReader} to read in just enough
     * characters to process a single valid CSV row, represented as an array of
     * strings where each element of the array is a field of the row. If formatting
     * errors are encountered during reading, this method throws a
     * {@code CSVFormatException} that specifies the exact point at which the error
     * occurred.
     *
     * @return a single row of CSV represented as a string array, where each
     *         element of the array is a field of the row; or {@code null} when
     *         there are no more rows left to be read.
     * @throws IOException when the underlying reader encountered an error
     * @throws CSVFormatException when the CSV file is formatted incorrectly
     */
    public String[] readRow() throws IOException, CSVFormatException {
        List<String> row = new ArrayList<>(); // Stores the fields of the current row
        StringBuilder field = new StringBuilder(); // Accumulates characters for the current field
        int line = 1; // Current line number
        int column = 1; // Current column number
        int rowField = 0; // Field index within the row
        int currentState = 0; // Current state of the state machine
        int character;

        while ((character = reader.read()) != -1) {
            switch (currentState) {
                case 0: // Initial state, outside quotes
                    if (character == ',') {
                        row.add(""); // Empty field
                        rowField++;
                    } else if (character == '"') {
                        currentState = 1; // Transition to inside quotes state
                    } else if (character == '\r' || character == '\n') {
                        // Handle line breaks
                        if (character == '\r' && reader.read() != '\n') {
                            throw new CSVFormatException("Invalid line break", line, column, line, rowField);
                        }
                        line++;
                        column = 1;
                        currentState = 0; // Start of new line
                        
                        if (rowField == 0 || field.length() == 0) {
                        	row.add("");
                        } else {
                            continue; // Skip empty line
                        }
                        return row.toArray(new String[0]);
                    } else {
                        field.append((char) character); // Append character to field
                        currentState = 2; // Transition to inside field state
                    }
                    break;
                case 1: // Inside quotes state
                    if (character == '"') {
                        int nextChar = reader.read();
                        if (nextChar == '"') {
                            field.append('"'); // Double quote within quotes
                        } else if (nextChar == ',' ) {
                            currentState = 0; // Transition to end of quotes state
                            row.add(field.toString());
                            field.setLength(0);
                            rowField++;
                        } else if ( nextChar == '\r'  || nextChar == '\n' || nextChar == -1) {
                        	if (character == '\r' && reader.read() != '\n') {
                                throw new CSVFormatException("Invalid line break", line, column, line, rowField);
                            }
                            currentState = 0; // Transition to end of quotes state
                            row.add(field.toString());
                            field.setLength(0);
                            rowField++;
                            return row.toArray(new String[0]);

                        } else {
                            throw new CSVFormatException("Invalid character", line, column + 1, line, rowField);
                        }
                    } else {
                        field.append((char) character); // Append character to field
                    }
                    break;
                case 2: // Inside field state
                    if (character == ',') {
                        row.add(field.toString()); // Add completed field to row
                        field.setLength(0); // Reset field StringBuilder
                        rowField++;
                        currentState = 0; // Transition to start of new field state
                    } else if (character == '\r' || character == '\n') {
                        // Handle line breaks
                        if (character == '\r' && reader.read() != '\n') {
                            throw new CSVFormatException("Invalid line break", line, column, line, rowField);
                        }
                        line++;
                        column = 1;
                        currentState = 0; // Transition to start of new line state
                        row.add(field.toString()); // Add completed field to row
                        field.setLength(0); // Reset field StringBuilder
                        rowField++;
                        return row.toArray(new String[0]);
                    }else if (character == '"') {
                        throw new CSVFormatException("Comma in field", line, column + 1, line, rowField);

                    } else {
                        field.append((char) character); // Append character to field
                    }
                    break;
            }

            // Update line and column numbers
            if (character == '\r' || character == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }

        // Handle end of file
        if (currentState == 0 && (rowField > 0 || field.length() > 0)) {
            row.add(field.toString());
            field.setLength(0);
            return row.toArray(new String[0]);
        } else if (currentState == 2) {
            row.add(field.toString());
            field.setLength(0);
            return row.toArray(new String[0]);
        }

        return null; // No more rows left to be read
    }




 

}
