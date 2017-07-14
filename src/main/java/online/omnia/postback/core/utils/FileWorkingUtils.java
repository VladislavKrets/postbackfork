package online.omnia.postback.core.utils;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by lollipop on 12.07.2017.
 */
public class FileWorkingUtils {
    private static FileWriter postbackURLWriter;
    private static FileWriter errorPostbackURLWriter;
    final static Logger logger = Logger.getLogger(FileWorkingUtils.class);

    static {
        try {
            postbackURLWriter = new FileWriter("/postback/postback.url", true);
            errorPostbackURLWriter = new FileWriter("/postback/postback.url", true);
        } catch (IOException e) {
            logger.debug("Exception during initializing fileWriters");
            logger.debug(e.getMessage());
        }
    }

    public static void writePostback(Date date, Time time, String fullUrl){
        String line = buildLine(date, time, fullUrl);
        try {
            postbackURLWriter.write(line);
            postbackURLWriter.flush();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

    public static void writeErrorPostback(Date date, Time time, String fullUrl) {
        String line = buildLine(date, time, fullUrl);
        try {
            errorPostbackURLWriter.write(line);
            errorPostbackURLWriter.flush();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
    private static String buildLine(Date date, Time time, String fullUrl) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(date.toString())
                .append(" ")
                .append(time.toString())
                .append(" ")
                .append(fullUrl)
                .append("\n");
        return lineBuilder.toString();
    }
}
