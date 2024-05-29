package edu.upenn.cit594.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger instance;
    private PrintStream output;
    private boolean isFileOutput;
    private Logger() {
        this.output = System.err; // Default output to System.err
        this.isFileOutput = false;
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logEvent(String message) {
        String logMessage = getCurrentTimestamp() + " " + message;
        output.println(logMessage);
    }

    public void setOutputDestination(String fileName) {
        if (isFileOutput) {
            closeLogFile();
        }
        try {
            this.output = new PrintStream(new FileOutputStream(fileName, true));
            this.isFileOutput = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLogFile() {
        if (output != null && isFileOutput) {
            output.close();
            isFileOutput = false;
        }
    }

    private String getCurrentTimestamp() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        return sdf.format(date);
    }
}
