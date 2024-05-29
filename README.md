## Overview

This Java program performs data analysis on three datasets related to the city of Philadelphia: COVID-19 data, property values, and population data. The program supports several functionalities to analyze and display different aspects of these datasets. It is designed using an N-tier architecture, ensuring separation of concerns and modularity, and employs various design patterns for efficiency and maintainability.

## Installation

1. Ensure you have Java (version 11 or later) installed on your system.
2. Clone the repository to your local machine.
3. Navigate to the project directory.

## Running the Program

To run the program, use the following command:

```sh
java -cp "bin:lib/json-simple-1.1.1.jar" edu.upenn.cit594.Main --population=<population_file.csv> --covid=<covid_file.csv/json> --properties=<properties_file.csv> --log=<log_file.txt>
```

Replace `<population_file.csv>`, `<covid_file.csv/json>`, `<properties_file.csv>`, and `<log_file.txt>` with the paths to your respective data files. 

### Command Line Arguments

- `--population`: Path to the population data CSV file.
- `--covid`: Path to the COVID-19 data file (CSV or JSON format).
- `--properties`: Path to the property values data CSV file.
- `--log`: Path to the log file for recording user interactions and errors.

Here are provided files: `population.csv`, `covid_data.csv`, `covid_data.json`, `property.csv`, `log.txt`

## Menu of Possible Actions

Upon starting, the program will display a menu with the following actions:

0. **Exit the program**
   - Terminates the application.

1. **Show the available actions**
   - Displays the list of possible actions.

2. **Show the total population for all ZIP Codes**
   - Calculates and displays the total population across all provided ZIP Codes.

3. **Show the total vaccinations per capita for each ZIP Code for the specified date**
   - Prompts for "partial" or "full" and a date (YYYY-MM-DD) to display the number of partial or full vaccinations per capita for each ZIP Code.

4. **Show the average market value for properties in a specified ZIP Code**
   - Prompts for a ZIP Code and displays the average market value of properties in that ZIP Code.

5. **Show the average total livable area for properties in a specified ZIP Code**
   - Prompts for a ZIP Code and displays the average total livable area of properties in that ZIP Code.

6. **Show the total market value of properties, per capita, for a specified ZIP Code**
   - Prompts for a ZIP Code and displays the total market value of properties per capita for that ZIP Code.

7. **Show the total livable area of properties, per capita, for a specified ZIP Code**
   - Prompts for a ZIP Code and displays the total livable area of properties per capita for that ZIP Code.

## Logging

The program logs user inputs and activities to the specified log file. The log entries include:

- Command line arguments at program start.
- The name of each input file when it is opened.
- User responses to prompts.

Each log entry is prepended with a timestamp for reference.

## Design

The program follows an N-tier architecture consisting of:

- **Presentation/User Interface tier**: Handles user interactions.
- **Logic/Processor tier**: Contains the core business logic.
- **Data Management tier**: Manages data reading and writing.
- **Logger**: Singleton class responsible for logging.

### Design Patterns

- **Singleton Pattern**: Implemented in the Logger class to ensure a single instance of the logger.
- **Strategy Pattern**: Used for computing the average market value and average total livable area of properties.

## Efficiency

Memoization is used to optimize the performance of features 2 through 7, caching results to avoid redundant calculations.

## Resources

- OpenDataPhilly: https://www.opendataphilly.org/
- US Census Bureau: https://www.census.gov/
- Java documentation for `java.io.File`: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html
