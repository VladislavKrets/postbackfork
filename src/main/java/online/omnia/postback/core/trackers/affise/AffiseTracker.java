package online.omnia.postback.core.trackers.affise;

import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class AffiseTracker {
    private String baseUrl;
    private Map<String, String> headers;
    final static Logger logger = Logger.getLogger(AffiseTracker.class);

    public AffiseTracker(String url) {
        baseUrl = url;
        headers = new HashMap<>();
    }

    public void sendPostback(PostBackEntity postBackEntity) throws NoClickIdException {
        String url = "";
        try {
            url = buildUrl(postBackEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Url built for affise:\n" + url);

        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        String answer = httpMethodsUtils.getMethod(url, headers);
        System.out.println(answer);
        //ToDo

    }

    private String buildUrl(PostBackEntity postBackEntity) throws NoClickIdException, UnsupportedEncodingException {
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "postback?clickid=" + URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        if (postBackEntity.getGoal() != 0) urlBuilder.append("&goal=").append(postBackEntity.getGoal());
        if (postBackEntity.getSum() != 0) urlBuilder.append("&sum=").append(postBackEntity.getSum());
        if (postBackEntity.getIpAddress() != null) urlBuilder.append("&ip=").append(URLEncoder.encode(postBackEntity.getIpAddress(), "UTF-8"));
        if (postBackEntity.getStatus() != 0) urlBuilder.append("&status=").append(postBackEntity.getStatus());
        if (postBackEntity.getCurrency() != null) urlBuilder.append("&currency=").append(URLEncoder.encode(postBackEntity.getCurrency(), "UTF-8"));
        if (postBackEntity.getT1() != null) urlBuilder.append("&custom_field1=").append(URLEncoder.encode(postBackEntity.getT1(), "UTF-8"));
        if (postBackEntity.getT2() != null) urlBuilder.append("&custom_field2=").append(URLEncoder.encode(postBackEntity.getT2(), "UTF-8"));
        if (postBackEntity.getT3() != null) urlBuilder.append("&custom_field3=").append(URLEncoder.encode(postBackEntity.getT3(), "UTF-8"));
        if (postBackEntity.getT4() != null) urlBuilder.append("&custom_field4=").append(URLEncoder.encode(postBackEntity.getT4(), "UTF-8"));
        if (postBackEntity.getT5() != null) urlBuilder.append("&custom_field5=").append(URLEncoder.encode(postBackEntity.getT5(), "UTF-8"));
        if (postBackEntity.getT6() != null) urlBuilder.append("&custom_field6=").append(URLEncoder.encode(postBackEntity.getT6(), "UTF-8"));
        if (postBackEntity.getT7() != null) urlBuilder.append("&custom_field7=").append(URLEncoder.encode(postBackEntity.getT7(), "UTF-8"));
        return urlBuilder.toString();
    }
}
