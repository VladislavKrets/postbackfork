package online.omnia.postback;

import io.nats.client.*;
import io.nats.streaming.*;

import online.omnia.postback.controller.MainController;
import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.utils.FileWorkingUtils;
import online.omnia.postback.queue.QueueWriter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lollipop on 16.09.2017.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException, IOException, TimeoutException {
        MySQLDaoImpl.getInstance();
        MainController controller = new MainController();
        List<String> urls;
        while (true) {
            urls = QueueWriter.getUrls();
            for (String url : urls) {
                controller.sendPostback(url);
            }
            Thread.sleep(10000);
        }

        /*Map<String, String> properties = FileWorkingUtils.iniFileReader();
        StreamingConnectionFactory cf = new StreamingConnectionFactory("test-cluster", "postbac");
        cf.setNatsUrl("nats://" + properties.get("nats_username") + ":"
                + properties.get("nats_password") + "@" + properties.get("nats_host")
                + ":" + properties.get("nats_port"));

        StreamingConnection sc = cf.createConnection();
        List<String> urls = QueueWriter.getUrls();
        for (String url : urls) {
            controller.sendPostback(url);
        }
        while (true) {
            try {
                SyncSubscription sub = sc.getNatsConnection().subscribe("postback");
                io.nats.client.Message message;
                while (true) {
                    if ((message = sub.nextMessage()) == null) {
                        sub.clearMaxPending();
                        sub.unsubscribe();
                        sub.close();
                        sub = sc.getNatsConnection().subscribe("postback");
                        Thread.sleep(10000);
                    }
                    controller.sendPostback(new String(message.getData()));

                }

            } catch (IOException e) {
                urls = QueueWriter.getUrls();
                for (String url : urls) {
                    controller.sendPostback(url);
                }
                Thread.sleep(10000);
            }
        }

*/
    }
}