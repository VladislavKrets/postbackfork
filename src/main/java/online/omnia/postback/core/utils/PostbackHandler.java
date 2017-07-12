package online.omnia.postback.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class PostbackHandler {

    public Map<String, String> getPostbackParameters(String url){
        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String parameters = url.split("\\?")[1];

        String[] keyValuePairs = parameters.split("&");
        String[] pairs;

        for (String keyValuePair : keyValuePairs) {
            pairs = keyValuePair.split("=");
            parametersMap.put(pairs[0], pairs[1]);
        }

        return parametersMap;
    }
}
