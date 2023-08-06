package edu.upenn.cit594.util;

public class PopulationData {
	
    private int population;
    private String zipCode;

    public PopulationData(int population, String zipCode) {
        this.population = population;
        this.zipCode = zipCode;
    }
    
    
    public int getPopulation() {
        return population;
    }


    public String getZipCode() {
        return zipCode;
    }
    
}