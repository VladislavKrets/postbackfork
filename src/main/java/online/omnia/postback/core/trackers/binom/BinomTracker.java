package online.omnia.postback.core.trackers.binom;

import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.entities.AbstractPostBackEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class BinomTracker {
    private String baseUrl;
    private Map<String, String> headers;
    final static Logger logger = Logger.getLogger(BinomTracker.class);

    public BinomTracker(String baseUrl) {
        this.baseUrl = baseUrl;
        headers = new HashMap<>();
    }

    public String sendPostback(AbstractPostBackEntity postBackEntity) throws NoClickIdException {
        String url = null;
        try {
            url = buildUrl(postBackEntity);
            System.out.println("Url had been built");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        String answer = httpMethodsUtils.getMethod(url, headers);
        System.out.println(answer);
        return url + "\n" + answer;

    }

    private String buildUrl(AbstractPostBackEntity postBackEntity) throws NoClickIdException, UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "click.php?");
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();
        urlBuilder.append("cnid=").append(URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        if (postBackEntity.getSum() != 0) urlBuilder.append("&sm=").append(postBackEntity.getSum());
        if (!postBackEntity.getStatus().isEmpty()) urlBuilder.append("&cnst1=").append(postBackEntity.getStatus());
        if (!postBackEntity.getEvent1().isEmpty()) urlBuilder.append("&event1=").append(postBackEntity.getEvent1());
        if (!postBackEntity.getEvent2().isEmpty()) urlBuilder.append("&event2=").append(postBackEntity.getEvent2());
        if (!postBackEntity.getEvent3().isEmpty()) urlBuilder.append("&event3=").append(postBackEntity.getEvent3());
        if (!postBackEntity.getEvent4().isEmpty()) urlBuilder.append("&event4=").append(postBackEntity.getEvent4());
        if (!postBackEntity.getEvent5().isEmpty()) urlBuilder.append("&event5=").append(postBackEntity.getEvent5());
        if (!postBackEntity.getEvent6().isEmpty()) urlBuilder.append("&event6=").append(postBackEntity.getEvent6());
        if (!postBackEntity.getEvent7().isEmpty()) urlBuilder.append("&event7=").append(postBackEntity.getEvent7());
        if (!postBackEntity.getEvent8().isEmpty()) urlBuilder.append("&event8=").append(postBackEntity.getEvent8());
        if (!postBackEntity.getEvent9().isEmpty()) urlBuilder.append("&event9=").append(postBackEntity.getEvent9());
        if (!postBackEntity.getEvent10().isEmpty()) urlBuilder.append("&event10=").append(postBackEntity.getEvent10());



        //ToDo
        return urlBuilder.toString();
    }
}
