package online.omnia.postback.core.trackers.affise;

import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.utils.HttpMethodsUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class AffiseTracker {
    private String baseUrl;
    private Map<String, String> headers;

    public AffiseTracker() {
        baseUrl = "https://offers.omnia.online/";
        headers = new HashMap<>();
    }

    public void sendPostback(PostBackEntity postBackEntity) throws NoClickIdException {
        String url = buildUrl(postBackEntity);
        try {
            String answer = HttpMethodsUtils.getMethod(url, headers);
            //ToDo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildUrl(PostBackEntity postBackEntity) throws NoClickIdException {
        if (postBackEntity.getClickId() == null || postBackEntity.getClickId().isEmpty()) throw new NoClickIdException();
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "postback?clickid=" + postBackEntity.getClickId());
        if (postBackEntity.getGoal() != 0) urlBuilder.append("&goal=").append(postBackEntity.getGoal());
        else urlBuilder.append("&goal=1");
        if (postBackEntity.getSum() != 0) urlBuilder.append("&sum=").append(postBackEntity.getSum());
        if (postBackEntity.getIpAddress() != null) urlBuilder.append("&ip=").append(postBackEntity.getIpAddress());
        if (postBackEntity.getStatus() != 0) urlBuilder.append("&status=").append(postBackEntity.getStatus());
        else urlBuilder.append("&status=2");
        if (postBackEntity.getCurrency() != null) urlBuilder.append("&currency=").append(postBackEntity.getCurrency());
        if (postBackEntity.getT1() != null) urlBuilder.append("&custom_field1=").append(postBackEntity.getT1());
        if (postBackEntity.getT2() != null) urlBuilder.append("&custom_field2=").append(postBackEntity.getT2());
        if (postBackEntity.getT3() != null) urlBuilder.append("&custom_field3=").append(postBackEntity.getT3());
        if (postBackEntity.getT4() != null) urlBuilder.append("&custom_field4=").append(postBackEntity.getT4());
        if (postBackEntity.getT5() != null) urlBuilder.append("&custom_field5=").append(postBackEntity.getT5());
        if (postBackEntity.getT6() != null) urlBuilder.append("&custom_field6=").append(postBackEntity.getT6());
        if (postBackEntity.getT7() != null) urlBuilder.append("&custom_field7=").append(postBackEntity.getT7());
        return urlBuilder.toString();
    }
}
