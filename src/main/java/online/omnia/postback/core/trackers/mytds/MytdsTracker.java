package online.omnia.postback.core.trackers.mytds;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.trackers.entities.AbstractPostBackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 27.04.2018.
 */
public class MytdsTracker {
    private String baseUrl;
    private Map<String, String> headers;
    final static Logger logger = Logger.getLogger(BinomTracker.class);

    public MytdsTracker(String baseUrl) {
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
        System.out.println(url);
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

        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();

        TrackerEntity trackerEntity = MySQLDaoImpl.getInstance().getTrackerByPrefix(postBackEntity.getPrefix());
        StringBuilder urlBuilder = new StringBuilder(baseUrl + trackerEntity.getClick() + "?");
        urlBuilder.append(trackerEntity.getClickid()).append("=").append(URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        if (postBackEntity.getSum() != 0) urlBuilder.append("&").append(trackerEntity.getSum()).append("=").append(postBackEntity.getSum());
        /*if (!postBackEntity.getT3().isEmpty()) urlBuilder.append("s3").append("=").append(postBackEntity.getT3());
        if (!postBackEntity.getT4().isEmpty()) urlBuilder.append("s4").append("=").append(postBackEntity.getT4());
        if (!postBackEntity.getT5().isEmpty()) urlBuilder.append("s5").append("=").append(postBackEntity.getT5());
        if (!postBackEntity.getT6().isEmpty()) urlBuilder.append("s6").append("=").append(postBackEntity.getT6());
        if (!postBackEntity.getT6().isEmpty()) urlBuilder.append("s6").append("=").append(postBackEntity.getT6());
*/
        //ToDo
        return urlBuilder.toString();
    }
}
