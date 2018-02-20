package online.omnia.postback.queue;


import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import io.nats.streaming.StreamingConnection;
import io.nats.streaming.StreamingConnectionFactory;
import online.omnia.postback.core.utils.FileWorkingUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lollipop on 15.09.2017.
 */
public class QueueWriter {
    private static FileWriter queueWriter;
    private static FileWriter statusWriter;
    private static BufferedReader statusReader;
    private static BufferedReader queueReader;
    private static StreamingConnection connection;


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

    public static void sendMessage(String data) {
        try {
            if (connection == null) {
                StreamingConnectionFactory cf = new StreamingConnectionFactory("test-cluster", "postback");
                Map<String, String> properties = FileWorkingUtils.iniFileReader();
                cf.setNatsUrl("nats://" + properties.get("nats_username") + ":"
                        + properties.get("nats_password") + "@" + properties.get("nats_host")
                        + ":" + properties.get("nats_port"));
                connection = cf.createConnection();
            }
            connection.getNatsConnection().publish("postback", data.getBytes());
        } catch (IOException e) {
            writeUrl(data);
        } catch (InterruptedException e) {
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
        FileWriter writer;
        try {
            writer = new FileWriter("status.txt", false);
            writer.write("2");
            writer.flush();
            writer.close();
            writer = null;
            queueReader = new BufferedReader(new FileReader("queue.txt"));
            String url;
            while ((url = queueReader.readLine()) != null) {
                if (url.isEmpty()) continue;
                urls.add(url);
            }
            queueReader.close();
            writer = new FileWriter("queue.txt", false);
            writer.write("");
            writer.flush();
            writer.close();
            writer = null;writer = new FileWriter("status.txt", false);
            writer.write("1");
            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
