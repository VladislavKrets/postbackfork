package online.omnia.postback.core.utils;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class FileWorkingUtils {
    private static FileWriter postbackURLWriter;
    private static FileWriter errorPostbackURLWriter;
    private static BufferedReader fileReader;
    final static Logger logger = Logger.getLogger(FileWorkingUtils.class);

    static {
        try {
            File postbackDirectory = new File("postback");
            if (!postbackDirectory.exists()) postbackDirectory.mkdir();
            File file = new File("postback/postback.log");
            if (!file.exists()) file.createNewFile();

            postbackURLWriter = new FileWriter(file, true);
            file = new File("postback/error_postback.log");
            if (!file.exists()) file.createNewFile();
            errorPostbackURLWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("Exception during initializing fileWriters");
            logger.debug(e.getMessage());
        }
    }
    public static synchronized Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<>();
        try {
            fileReader = new BufferedReader(new FileReader("configuration.ini"));
            String property;
            String[] propertyArray;
            while ((property = fileReader.readLine()) != null) {
                propertyArray = property.split("=");
                properties.put(propertyArray[0], propertyArray[1]);
            }
        } catch (IOException e) {
            logger.debug("Exception during reading ini file");
            logger.debug(e.getMessage());
        }
        return properties;
    }
    public static synchronized void writePostback(Date date, Time time, String fullUrl){
        String line = buildLine(date, time, fullUrl);
        try {
            postbackURLWriter.write(line);
            postbackURLWriter.flush();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

    public static synchronized void writeErrorPostback(Date date, Time time, String fullUrl) {
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
