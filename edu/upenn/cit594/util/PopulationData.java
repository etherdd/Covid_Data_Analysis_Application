package edu.upenn.cit594.studenttests.util;

public class PopulationData {

    private String zipCode;
    private int population;

    public PopulationData(int population, String zipCode) {
        this.population = population;
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


}
