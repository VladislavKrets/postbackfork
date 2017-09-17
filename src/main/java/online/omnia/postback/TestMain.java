package online.omnia.postback;

import online.omnia.postback.controller.MainController;
import online.omnia.postback.queue.QueueWriter;

import java.util.List;

/**
 * Created by lollipop on 16.09.2017.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {

        MainController controller = new MainController();
        List<String> urls;
        while (true) {
            urls = QueueWriter.getUrls();
            for (String url : urls) {
                controller.sendPostback(url);
            }
            Thread.sleep(10000);
        }
    }
}
