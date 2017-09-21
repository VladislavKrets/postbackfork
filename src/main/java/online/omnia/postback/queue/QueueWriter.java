package online.omnia.postback.queue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lollipop on 15.09.2017.
 */
public class QueueWriter {
    private static FileWriter queueWriter;
    private static FileWriter statusWriter;
    private static BufferedReader statusReader;
    private static BufferedReader queueReader;
    static {
        try {
            queueWriter = new FileWriter("queue.txt", true);
            statusWriter = new FileWriter("status.txt", false);
            statusWriter.write("1");
            statusWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized void writeUrl(String url) {
        while (true) {
            try {
                statusReader = new BufferedReader(new FileReader("status.txt"));
                String s = statusReader.readLine();
                System.out.println(s);
                if (s.trim().equals("1")) {
                    System.out.println(true);
                    try {
                        queueWriter.write(url + "\n");
                        queueWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                statusReader.close();
                statusReader = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static synchronized List<String> getUrls() {
        List<String> urls = new ArrayList<>();
        try {
            statusWriter.write("2");
            statusWriter.flush();
            queueReader = new BufferedReader(new FileReader("queue.txt"));
            String url;
            while ((url = queueReader.readLine()) != null) {
                if (url.isEmpty()) continue;
                urls.add(url);
            }
            queueReader.close();
            FileWriter writer = new FileWriter("queue.txt", false);
            writer.close();
            writer = null;
            writer = new FileWriter("status.txt", false);
            writer.close();
            writer = null;
            statusWriter.write("1");
            statusWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
