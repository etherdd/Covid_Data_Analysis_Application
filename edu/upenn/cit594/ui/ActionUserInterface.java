package edu.upenn.cit594.ui;

import edu.upenn.cit594.util.FileData;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;

import java.util.Scanner;
import java.util.List;

public class ActionUserInterface {

    private Scanner scanner;
    private FileData files;

    public ActionUserInterface(FileData inputFiles) {
        this.scanner = new Scanner(System.in);
        this.files = inputFiles;
    }

    public void start() throws Exception {
        displayMenu();

        while (true) {
            System.out.print("\n> ");
            System.out.flush();
            String userInput = scanner.nextLine().trim();            
            System.out.println();            
            
            String logFileName = files.getLogFile();
            Logger logger = Logger.getInstance();
            if (logFileName != null) {
                logger.setOutputDestination(logFileName);
            }       	
            logger.logEvent(userInput);
            

            if (!isValidUserInput(userInput)) {
                System.out.println("Invalid action number. Please enter a number between 0 and 7.");
                continue;
            }

            int actionNumber = Integer.parseInt(userInput);

            if (actionNumber == 0) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            } else {
                performAction(actionNumber);
                displayMenu();
            }
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("Menu of possible actions:");
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions.");
        System.out.println("2. Show the total population for all ZIP Codes.");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
        System.out.println("7. Show the total livable area of properties, per capita, for a specified ZIP Code.");
    }

    private boolean isValidUserInput(String userInput) {
        if (userInput.matches("[0-7]")) {
            return true;
        }
        return false;
    }

    private void performAction(int actionNumber) throws Exception {
    	
    	ActionsProcessor actionsProcessor = ActionsProcessor.getInstance(files);
    	List<Integer> actionList = actionsProcessor.getAvailableActionsList();
    	if (!actionList.contains(actionNumber)) {
            System.out.println("Impossbile action!");
            return;
    	}

    	        
    	switch (actionNumber) {
    	
            case 1:
            	//1. Show the available actions.
                System.out.println("BEGIN OUTPUT");
            	actionList.forEach(System.out::println);
                System.out.println("END OUTPUT");
                break;
                
            case 2:
            	//2. Show the total population for all ZIP Codes.
                PopulationProcessor populationProcessor = PopulationProcessor.getInstance(files);
                System.out.println("BEGIN OUTPUT");
                System.out.println(populationProcessor.getTotalPopulation(actionNumber));
                System.out.println("END OUTPUT");
                break;
                
            case 3:
                //3. Show the total vaccinations per capita for each ZIP Code for the specified date.           	
            	System.out.println("Type 'partial' or 'full': ");
                System.out.flush();                  
                String vaccineType = scanner.nextLine();                
                System.out.println("Type a date in the format: YYYY-MM-DD");                
                System.out.flush();
                String dateInput = scanner.nextLine();
                System.out.println();                 
                
            	CovidPopulationUserInterface covidUserInterface = CovidPopulationUserInterface.getInstance(files);
            	covidUserInterface.run(vaccineType, dateInput);
                break;
                
            case 4:         	
            	//4. Show the average market value for properties in a specified ZIP Code.
                System.out.println("Enter a 5-digit ZIP Code:");
                System.out.flush();                  
                String zipCode = scanner.nextLine();    
                
            	PropertyUserInterface propertyUserInterface = PropertyUserInterface.getInstance(files);
            	propertyUserInterface.run(actionNumber, zipCode);
                break;
                
            case 5:
            	//5. Show the average total livable area for properties in a specified ZIP Code.
                System.out.println("Enter a 5-digit ZIP Code:");
                System.out.flush();                  
                String zipCode2 = scanner.nextLine(); 
                
            	PropertyUserInterface propertyUserInterface2 = PropertyUserInterface.getInstance(files);
            	propertyUserInterface2.run(actionNumber, zipCode2);
            	
                break;
                
            case 6:
            	//6. Show the total market value of properties, per capita, for a specified ZIP Code.
                System.out.println("Enter a 5-digit ZIP Code:");
                System.out.flush();                  
                String zipCode3 = scanner.nextLine(); 
                
                PropertyUserInterface propertyUserInterface3 = PropertyUserInterface.getInstance(files);
            	propertyUserInterface3.run(actionNumber, zipCode3);
                break;
                
            case 7:
            	//7. Show the total livable area of properties, per capita, for a specified ZIP Code.
                System.out.println("Enter a 5-digit ZIP Code:");
                System.out.flush();                  
                String zipCode4 = scanner.nextLine(); 
                
                PropertyUserInterface propertyUserInterface4 = PropertyUserInterface.getInstance(files);
            	propertyUserInterface4.run(actionNumber, zipCode4);
                break;
                
            default:
                System.out.println("Invalid action number.");
        }

        System.out.println();
    }
    
   
    
    

}
