package online.omnia.postback.core.trackers.affise;

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

    public String sendPostback(AbstractPostBackEntity postBackEntity) throws NoClickIdException {
        String url = "";
        try {
            url = buildUrl(postBackEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Url built for affise:\n" + url);

        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        List<String> answer = httpMethodsUtils.getMethod(url, headers);
        System.out.println(answer.get(1));
        //ToDo
        return url + "\n" +answer.get(1);
    }

    private String buildUrl(AbstractPostBackEntity postBackEntity) throws NoClickIdException, UnsupportedEncodingException {
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty())
            throw new NoClickIdException();
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "postback?clickid=" + URLEncoder.encode(postBackEntity.getClickId(), "UTF-8"));
        if (!postBackEntity.getGoal().isEmpty()) urlBuilder.append("&goal=").append(postBackEntity.getGoal());
        if (postBackEntity.getSum() != 0) urlBuilder.append("&sum=").append(postBackEntity.getSum());
        if (!postBackEntity.getIpAddress().isEmpty()) urlBuilder.append("&ip=").append(URLEncoder.encode(postBackEntity.getIpAddress(), "UTF-8"));
        if (!postBackEntity.getStatus().isEmpty()) urlBuilder.append("&status=").append(postBackEntity.getStatus());
        if (!postBackEntity.getCurrency().isEmpty()) urlBuilder.append("&currency=").append(URLEncoder.encode(postBackEntity.getCurrency(), "UTF-8"));
        if (!postBackEntity.getT1().isEmpty()) urlBuilder.append("&custom_field1=").append(URLEncoder.encode(postBackEntity.getT1(), "UTF-8"));
        if (!postBackEntity.getT2().isEmpty()) urlBuilder.append("&custom_field2=").append(URLEncoder.encode(postBackEntity.getT2(), "UTF-8"));
        if (!postBackEntity.getT3().isEmpty()) urlBuilder.append("&custom_field3=").append(URLEncoder.encode(postBackEntity.getT3(), "UTF-8"));
        if (!postBackEntity.getT4().isEmpty()) urlBuilder.append("&custom_field4=").append(URLEncoder.encode(postBackEntity.getT4(), "UTF-8"));
        if (!postBackEntity.getT5().isEmpty()) urlBuilder.append("&custom_field5=").append(URLEncoder.encode(postBackEntity.getT5(), "UTF-8"));
        if (!postBackEntity.getT6().isEmpty()) urlBuilder.append("&custom_field6=").append(URLEncoder.encode(postBackEntity.getT6(), "UTF-8"));
        if (!postBackEntity.getT7().isEmpty()) urlBuilder.append("&custom_field7=").append(URLEncoder.encode(postBackEntity.getT7(), "UTF-8"));
        if (!postBackEntity.getSecretKey().isEmpty()) urlBuilder.append("&secret=").append(URLEncoder.encode(postBackEntity.getSecretKey(), "UTF-8"));
        if (!postBackEntity.getTransactionId().isEmpty()) urlBuilder.append("&action_id=").append(URLEncoder.encode(postBackEntity.getTransactionId(), "UTF-8"));
        return urlBuilder.toString();
    }
}
