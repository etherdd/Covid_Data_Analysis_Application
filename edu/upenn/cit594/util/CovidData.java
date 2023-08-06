package edu.upenn.cit594.util;

public class CovidData {
	
    private String zipCode;
    private String timestamp;
	private int partiallyVaccinated;
    private int fullyVaccinated;

    public CovidData(String zipCodeStr, String timestampStr, int partiallyVaccinated, int fullyVaccinated) {
        this.zipCode = zipCodeStr;
        this.timestamp = timestampStr;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
    }

	public String getZipCode() {
		return zipCode;
	}


	public String getTimestamp() {
		return timestamp;
	}
    
	public int getPartiallyVaccinated() {
		return partiallyVaccinated;
	}

	public int getFullyVaccinated() {
		return fullyVaccinated;
	}




}