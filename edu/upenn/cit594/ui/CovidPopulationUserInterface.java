package edu.upenn.cit594.ui;

import edu.upenn.cit594.util.FileData;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.CovidPopulationProcessor;

import java.util.Scanner;
import java.util.List;

public class CovidPopulationUserInterface {
    private static CovidPopulationUserInterface instance;
    private Scanner scanner;
    private CovidPopulationProcessor processor;
    private FileData file;

    private CovidPopulationUserInterface(FileData inputFiles) {
        this.scanner = new Scanner(System.in);
        this.processor = CovidPopulationProcessor.getInstance(inputFiles);
        this.file = inputFiles;
    }

    public static CovidPopulationUserInterface getInstance(FileData inputFiles) {
        if (instance == null) {
            instance = new CovidPopulationUserInterface(inputFiles);
        }
        return instance;
    }

    public void run() {
        System.out.println("Type 'partial' or 'full': ");
        String vaccineType = scanner.nextLine();
        
        //log
        String logFileName = file.getLogFile();
        Logger logger = Logger.getInstance();
        if (logFileName != null) {
            logger.setOutputDestination(logFileName);
        }            	
        logger.logEvent(vaccineType);
        
        
        vaccineType = vaccineType.toLowerCase();

        if (!vaccineType.equals("partial") && !vaccineType.equals("full")) {
            System.out.println("Invalid input. Please type 'partial' or 'full'.");
            return;
        }

        System.out.println("Type a date in the format: YYYY-MM-DD");
        String dateInput = scanner.nextLine();
        
        //log
        logger.logEvent(dateInput);

        // Process the date input and get the vaccinations per capita data
        List<String> vaccinationsPerCapita = processor.calculateVaccinationsPerCapita(dateInput, vaccineType);

        if (vaccinationsPerCapita.isEmpty()) {
            return;
        }

        // Display the output in ascending order of ZIP Codes
        System.out.println("BEGIN OUTPUT");
        vaccinationsPerCapita.forEach(System.out::println);
        System.out.println("END OUTPUT");

    }
}
