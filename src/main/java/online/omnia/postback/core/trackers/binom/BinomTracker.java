package online.omnia.postback.core.trackers.binom;

import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
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

    public void sendPostback(PostBackEntity postBackEntity) throws NoClickIdException {
        String url = null;
        try {
            url = buildUrl(postBackEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        String answer = httpMethodsUtils.getMethod(url, headers);

    }

    private String buildUrl(PostBackEntity postBackEntity) throws NoClickIdException, UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "click.php?");
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();
        urlBuilder.append("cnv_id=").append(URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        //ToDo
        return urlBuilder.toString();
    }
}
