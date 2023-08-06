package edu.upenn.cit594.util;

public class PropertyData {
	
    private double marketValue;
    private double totalLivableArea;
    private String zipCode;

    public PropertyData(double marketValue, double totalLivableArea, String zipCode) {
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
        this.zipCode = zipCode;
    }
    
    
    public double getMarketValue() {
        return marketValue;
    }

    public double geTotalLivableArea() {
        return totalLivableArea;
    }

    public String getZipCode() {
        return zipCode;
    }
    
}
