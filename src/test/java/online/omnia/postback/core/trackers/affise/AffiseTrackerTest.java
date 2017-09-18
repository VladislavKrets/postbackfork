package online.omnia.postback.core.trackers.affise;

import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.utils.PostbackHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lollipop on 18.09.2017.
 */
public class AffiseTrackerTest {
    private List<String> urls;
    private List<String> resultUrls;
    @Before
    public void initTest(){
        urls = new ArrayList<>();
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=2.0&t1=101&advname=testName");
        urls.add("http://127.0.0.1:8080/postback?clickid=12345&afid=2&sum=2.0&t2=101&offername=testName&add_event1=90");
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=string&t1=101&advname=testName&currency=EUR");
        urls.add("http://127.0.0.1:8080/postback?clickid=12345=&afid=string&sum=2.0&t1=101&advname=testName&secret=qwerty");
        resultUrls = new ArrayList<>();
        resultUrls.add("http://testaffiseurl.com/postback?clickid=12345&sum=2.0&currency=USD&custom_field1=101");
        resultUrls.add("http://testaffiseurl.com/postback?clickid=12345&sum=2.0&currency=USD&custom_field2=101");
        resultUrls.add("http://testaffiseurl.com/postback?clickid=12345&currency=EUR&custom_field1=101");
        resultUrls.add("http://testaffiseurl.com/postback?clickid=12345&sum=2.0&currency=USD&custom_field1=101&secret=qwerty");
    }
    @Test
    public void buildUrl() throws Exception {
        PostbackHandler postbackHandler = new PostbackHandler();
        AffiseTracker tracker = new AffiseTracker("http://testaffiseurl.com/");
        String url;
        for (int i = 0; i < urls.size(); i++) {
            url = tracker.buildUrl(postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(i))));
            assertEquals(resultUrls.get(i), url);
        }
    }

}