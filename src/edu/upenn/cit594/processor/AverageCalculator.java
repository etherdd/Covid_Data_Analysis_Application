package edu.upenn.cit594.processor;

import java.io.IOException;

public interface AverageCalculator {
    double calculateAverage(String zipCode) throws IOException, Exception;
}
