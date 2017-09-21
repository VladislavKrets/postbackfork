package online.omnia.postback.core.trackers.binom;

import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.entities.AbstractPostBackEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which sends postback to binom
 */
public class BinomTracker {
    private String baseUrl;
    private Map<String, String> headers;
    final static Logger logger = Logger.getLogger(BinomTracker.class);

    public BinomTracker(String baseUrl) {
        this.baseUrl = baseUrl;
        headers = new HashMap<>();
    }

    /**
     * Method forms url and sends postback to binom
     * @param postBackEntity entity which we get after parsing the url
     * @return server answer
     * @throws NoClickIdException when postback doesn't contain clickid
     */
    public String sendPostback(AbstractPostBackEntity postBackEntity) throws NoClickIdException {
        String url = null;
        try {
            url = buildUrl(postBackEntity);
            System.out.println("Url had been built");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        List<String> answer = httpMethodsUtils.getMethod(url, headers);
        return url + " answer=" + (answer.size() == 0 ? "201" : answer.get(0));

    }

    /**
     * Method which formes url for sending to binom
     * @param postBackEntity entity which we get after parsing the url
     * @return formed url
     * @throws NoClickIdException when postback doesn't contain clickid
     * @throws UnsupportedEncodingException when url encoding unsuccessful
     */
    public String buildUrl(AbstractPostBackEntity postBackEntity) throws NoClickIdException, UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "click.php?");
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();
        urlBuilder.append("cnid=").append(URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        if (postBackEntity.getSum() != 0) urlBuilder.append("&sm=").append(postBackEntity.getSum());
        if (!postBackEntity.getStatus().isEmpty()) urlBuilder.append("&cnst1=").append(URLEncoder.encode(postBackEntity.getStatus(), "UTF-8"));
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
        if (!postBackEntity.getAddEvent1().isEmpty()) urlBuilder.append("&add_event1=").append(postBackEntity.getAddEvent1());
        if (!postBackEntity.getAddEvent2().isEmpty()) urlBuilder.append("&add_event2=").append(postBackEntity.getAddEvent2());
        if (!postBackEntity.getAddEvent3().isEmpty()) urlBuilder.append("&add_event3=").append(postBackEntity.getAddEvent3());
        if (!postBackEntity.getAddEvent4().isEmpty()) urlBuilder.append("&add_event4=").append(postBackEntity.getAddEvent4());
        if (!postBackEntity.getAddEvent5().isEmpty()) urlBuilder.append("&add_event5=").append(postBackEntity.getAddEvent5());
        if (!postBackEntity.getAddEvent6().isEmpty()) urlBuilder.append("&add_event6=").append(postBackEntity.getAddEvent6());
        if (!postBackEntity.getAddEvent7().isEmpty()) urlBuilder.append("&add_event7=").append(postBackEntity.getAddEvent7());
        if (!postBackEntity.getAddEvent8().isEmpty()) urlBuilder.append("&add_event8=").append(postBackEntity.getAddEvent8());
        if (!postBackEntity.getAddEvent9().isEmpty()) urlBuilder.append("&add_event9=").append(postBackEntity.getAddEvent9());
        if (!postBackEntity.getAddEvent10().isEmpty()) urlBuilder.append("&add_event10=").append(postBackEntity.getAddEvent10());


        //ToDo
        return urlBuilder.toString();
    }
}
