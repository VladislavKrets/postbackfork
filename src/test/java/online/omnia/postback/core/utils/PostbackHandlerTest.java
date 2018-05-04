package online.omnia.postback.core.utils;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by lollipop on 18.09.2017.
 */
public class PostbackHandlerTest {
    private List<String> urls;

    private PostbackHandler postbackHandler;
    @Before
    public void initTest() {
        postbackHandler = new PostbackHandler();
        urls = new ArrayList<>();
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=2.0&t1=101&advname=testName");
        urls.add("http://127.0.0.1:8080/postback?clickid=12345&afid=2&sum=2.0&t1=101&offername=testName");
        urls.add("http://127.0.0.1:8080/postback?clickid=333_12345&afid=2&sum=string&t1=101&advname=testName");
        urls.add("http://127.0.0.1:8080/postback?clickid=&afid=string&sum=2.0&t1=101&advname=testName");
        urls.add("http://127.0.0.1:8080/postback?cLiCkid=780_testId7&AFID=1&suM=10&t1=101&advname=test&stATUs=test");

    }
    @After
    public void afterTest() {
        postbackHandler = null;
        urls = null;
    }
    @Test
    public void getPostbackParameters() throws Exception {
        Map<String, String> map = postbackHandler.getPostbackParameters(urls.get(0));
        assertEquals("333_12345", map.get("clickid"));
        assertEquals("2", map.get("afid"));
        assertEquals("2.0", map.get("sum"));
        assertEquals("101", map.get("t1"));
        assertEquals("testName", map.get("advname"));
        map = postbackHandler.getPostbackParameters(urls.get(4));
        assertEquals("780_testId7", map.get("clickid"));
        assertEquals("1", map.get("afid"));
        assertEquals("10", map.get("sum"));
        assertEquals("test", map.get("status"));
        map = null;
    }

    @Test
    public void fillPostback() throws Exception {
        PostBackEntity postBackEntity = postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(0)));
        assertEquals("333", postBackEntity.getPrefix());
        assertEquals("12345", postBackEntity.getClickId());
        assertEquals(2, postBackEntity.getAfid());
        assertEquals(2, postBackEntity.getSum(), 0.01);
        assertEquals("101", postBackEntity.getT1());
        assertEquals("testName", postBackEntity.getAdvName());
        assertEquals("", postBackEntity.getGaId());
        assertEquals("", postBackEntity.getOfferName());
        postBackEntity = postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(1)));
        assertEquals(null, postBackEntity.getPrefix());
        assertEquals("12345", postBackEntity.getClickId());
        assertEquals(2, postBackEntity.getAfid());
        assertEquals(2.0, postBackEntity.getSum(), 0.01);
        assertEquals("101", postBackEntity.getT1());
        assertEquals("", postBackEntity.getAdvName());
        assertEquals("", postBackEntity.getGaId());
        assertEquals("testName", postBackEntity.getOfferName());
        postBackEntity = postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(2)));
        assertEquals("333", postBackEntity.getPrefix());
        assertEquals("12345", postBackEntity.getClickId());
        assertEquals(2, postBackEntity.getAfid());
        assertEquals(0, postBackEntity.getSum(), 0.01);
        assertEquals("101", postBackEntity.getT1());
        assertEquals("testName", postBackEntity.getAdvName());
        assertEquals("", postBackEntity.getGaId());
        assertEquals("USD", postBackEntity.getCurrency());
        assertEquals("", postBackEntity.getOfferName());
        postBackEntity = postbackHandler.fillPostback(postbackHandler.getPostbackParameters(urls.get(3)));
        assertEquals(null, postBackEntity.getPrefix());
        assertEquals(0, postBackEntity.getAfid());
        assertEquals(2.0, postBackEntity.getSum(), 0.01);
        assertEquals("101", postBackEntity.getT1());
        assertEquals("testName", postBackEntity.getAdvName());
        assertEquals("", postBackEntity.getGaId());
        assertEquals("USD", postBackEntity.getCurrency());
        assertEquals("", postBackEntity.getOfferName());
        postBackEntity = null;
    }

}