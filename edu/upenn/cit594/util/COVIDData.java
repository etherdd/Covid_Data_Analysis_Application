package edu.upenn.cit594.studenttests.util;

public class COVIDData {

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public long getPartialVaccinated() {
        return partialVaccinated;
    }

    public void setPartialVaccinated(int partialVaccinated) {
        this.partialVaccinated = partialVaccinated;
    }

    public long getFullyVaccinated() {
        return fullyVaccinated;
    }

    public void setFullyVaccinated(int fullyVaccinated) {
        this.fullyVaccinated = fullyVaccinated;
    }

    public String getTimestamp(String timestamp) {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    private String zipCode;
    private long partialVaccinated;
    private long fullyVaccinated;
    private String timestamp;
    public COVIDData(String zipCode, long partialVaccinated, long fullyVaccinated, String timestamp) {
        this.zipCode = zipCode;
        this.partialVaccinated = partialVaccinated;
        this.fullyVaccinated = fullyVaccinated;
        this.timestamp = timestamp;
    }
}
