package online.omnia.postback.core.utils;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

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
            e.printStackTrace();
        }
    }

    public static void writePostback(PostBackEntity postBackEntity){
        String line = buildLine(postBackEntity);
        try {
            postbackURLWriter.write(line);
            postbackURLWriter.flush();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

    public static void writeErrorPostback(PostBackEntity postBackEntity) {
        String line = buildLine(postBackEntity);
        try {
            errorPostbackURLWriter.write(line);
            errorPostbackURLWriter.flush();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
    private static String buildLine(PostBackEntity postBackEntity) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(postBackEntity.getDate().toString())
                .append(" ")
                .append(postBackEntity.getTime().toString())
                .append(" ")
                .append(postBackEntity.getFullURL())
                .append("\n");
        return lineBuilder.toString();
    }
}
