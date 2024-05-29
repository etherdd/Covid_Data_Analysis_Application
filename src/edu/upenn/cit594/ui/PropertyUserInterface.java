package edu.upenn.cit594.ui;

import java.io.IOException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.PropertyPopulationProcessor;
import edu.upenn.cit594.processor.PropertyProcessor;
import edu.upenn.cit594.util.FileData;

public class PropertyUserInterface {
    private static PropertyUserInterface instance;
    private PropertyProcessor propertyProcessor;
    private PropertyPopulationProcessor propertyPopulationProcessor;
    private FileData file;
    

    private PropertyUserInterface(FileData inputFiles) {
    	this.propertyProcessor = new PropertyProcessor(inputFiles);
    	this.propertyPopulationProcessor = new PropertyPopulationProcessor(inputFiles);
    	this.file = inputFiles;
    }

    public static PropertyUserInterface getInstance(FileData inputFiles) {
        if (instance == null) {
            instance = new PropertyUserInterface(inputFiles);
        }
        return instance;
    }

    public void run(int actionNumber, String zipCode) throws Exception {
    	

        try {
            while (true) {

                //log
                String logFileName = file.getLogFile();
                Logger logger = Logger.getInstance();
                if (logFileName != null) {
                    logger.setOutputDestination(logFileName);
                }            	
                logger.logEvent(zipCode);
                

                if (zipCode.matches("\\d{5}")) {
                    break;
                } else {
                    System.out.println("Invalid ZIP Code format. Please enter a 5-digit ZIP Code.");
                }
            }

            if (actionNumber == 4) {
	            double avgMarketValue = propertyProcessor.calculateAverageMarketValue(zipCode);
	            System.out.println("BEGIN OUTPUT");
	            System.out.println((int) avgMarketValue);
	            System.out.println("END OUTPUT");

            } else if (actionNumber == 5) {
	            double avgTotalLivableArea = propertyProcessor.calculateAverageTotalLivableArea(zipCode);
	            System.out.println("BEGIN OUTPUT");
	            System.out.println((int) avgTotalLivableArea);
	            System.out.println("END OUTPUT");

            }else if (actionNumber == 6) {
                int totalMarketValuePerCapita = propertyPopulationProcessor.calculateTotalMarketValuePerCapita(zipCode);
                System.out.println("BEGIN OUTPUT");
                System.out.println(totalMarketValuePerCapita);
                System.out.println("END OUTPUT");          

	        }else if (actionNumber == 7) {
	            double totalTotalLivableAreaPerCapita = propertyPopulationProcessor.calculateTotalLivableAreaPerCapita(zipCode);
	            System.out.println("BEGIN OUTPUT");
	            System.out.println(totalTotalLivableAreaPerCapita);
	            System.out.println("END OUTPUT");
	        }
     
        } catch (IOException e) {
            System.out.println("Error reading input. Exiting...");
            e.printStackTrace();
        }
    }
}

