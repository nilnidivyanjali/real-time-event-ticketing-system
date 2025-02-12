package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A utility class for logging system messages to both the console and the log file.
 */
public class Logger {

    private static final String LOG_FILE_PATH = "system.log"; // Logging file path
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message with a timestamp to the console and appends it to a log file.
     *
     * @param message This is the message to be logged.
     */
    public static void log(String message) {
        String timestampedMessage = "[" + LocalDateTime.now().format(DATE_FORMATTER) + "]" + message;

        // Console Logging
        System.out.println(timestampedMessage);

        // File Logging
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(timestampedMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }

}
