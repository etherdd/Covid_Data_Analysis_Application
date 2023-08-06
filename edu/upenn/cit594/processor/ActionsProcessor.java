package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.FileData;

import java.util.ArrayList;
import java.util.List;

public class ActionsProcessor {

    private static ActionsProcessor instance;
    private FileData file;

    private ActionsProcessor(FileData inputFile) {
        this.file = inputFile;
    }

    public static ActionsProcessor getInstance(FileData inputFile) {
        if (instance == null) {
            instance = new ActionsProcessor(inputFile);
        }
        return instance;
    }

    public List<Integer> getAvailableActionsList() {
        List<Integer> availableActions = new ArrayList<>();
        
        availableActions.add(0);
        availableActions.add(1);
        
        if (file.getPopulationFile() != null) {
            availableActions.add(2);
        }

        if (file.getCovidFile() != null && file.getPopulationFile() != null) {
            availableActions.add(3);
        }

        if (file.getPropertyFile() != null) {
            availableActions.add(4);
            availableActions.add(5);

        }
        
        if (file.getPropertyFile() != null && file.getPopulationFile() != null) {
            availableActions.add(6);
            availableActions.add(7);
        }



        return availableActions;
    }
}
