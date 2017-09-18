package online.omnia.postback.core.trackers.binom;

import online.omnia.postback.core.utils.PostbackHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lollipop on 18.09.2017.
 */
public class BinomTrackerTest {
    private List<String> urls;
    private List<String> resultUrls;

    @Before
    public void initTest() {
        urls = new ArrayList<>();
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=2.0&t1=101&advname=testName");
        urls.add("http://127.0.0.1:8080/postback?clickid=12345&afid=2&sum=2.0&t1=101&offername=testName&add_event1=90");
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=string&t1=101&advname=testName&event3=10&add_event4=3");
        urls.add("http://127.0.0.1:8080/postback?clickid=12345=&afid=string&sum=2.0&t1=101&advname=testName&event1=70");
        resultUrls = new ArrayList<>();
        resultUrls.add("http://testbinomurl.com/click.php?cnid=12345&sm=2.0");
        resultUrls.add("http://testbinomurl.com/click.php?cnid=12345&sm=2.0&add_event1=90");
        resultUrls.add("http://testbinomurl.com/click.php?cnid=12345&event3=10&add_event4=3");
        resultUrls.add("http://testbinomurl.com/click.php?cnid=12345&sm=2.0&event1=70");
    }
    @Test
    public void buildUrl() throws Exception {
        PostbackHandler postbackHandler = new PostbackHandler();
        BinomTracker binomTracker = new BinomTracker("http://testbinomurl.com/");
        String url;
        for (int i = 0; i < urls.size(); i++) {
            url = binomTracker.buildUrl(postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(i))));
            assertEquals(resultUrls.get(i), url);
        }
    }

}