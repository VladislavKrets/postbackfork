package online.omnia.postback.core.utils;

import online.omnia.postback.core.dao.MySQLDao;
import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lollipop on 11.07.2017.
 */
public class PostbackHandler {

    public Map<String, String> getPostbackParameters(String url){

        System.out.println(url);
        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String[] urlParts = url.split("\\?");

        if (urlParts.length != 2){
            System.out.println("No ?");
            System.out.println(Arrays.asList(urlParts));
            return parametersMap;
        }

        String parameters = urlParts[1];
        if (!parameters.contains("&")) {
            System.out.println("Not found &");
            String[] pair = parameters.split("=");
            if (pair.length == 0) return parametersMap;
            if (pair.length == 2) {
                parametersMap.put(pair[0], pair[1]);
            }
            else if (pair.length == 1) {
                parametersMap.put(pair[0], "");
            }
            return parametersMap;
        }
        String[] keyValuePairs = parameters.split("&");
        String[] pairs;

        for (String keyValuePair : keyValuePairs) {
            pairs = keyValuePair.split("=");
            if (pairs.length == 2) {
                parametersMap.put(pairs[0], pairs[1]);
            }
            else if (pairs.length == 1) {
                parametersMap.put(pairs[0], "");
            }
        }
        System.out.println("Parameters have been got");
        return parametersMap;
    }
    public PostBackEntity fillPostback(Map<String, String> parameters) {
        PostBackEntity postBackEntity = new PostBackEntity();

        String clickid = parameters.containsKey("clickid") ? parameters.get("clickid") : null;
        String prefix = null;
        int prefixNumber;
        int clickIdLength = clickid != null ? clickid.length() : 0;
        System.out.println("Clickid = " + clickid);
        if (clickIdLength > 0 && clickid.contains("_")){
            prefixNumber = clickid.indexOf("_");
            prefix = clickid.substring(0, prefixNumber);
            if (!isPrefixInTrackers(prefix)) prefix = null;
            else clickid = clickid.substring(prefixNumber + 1, clickIdLength);
        }

        postBackEntity.setClickId(clickid);
        postBackEntity.setPrefix(prefix);

        if (parameters.containsKey("sum") && parameters.get("sum").matches("\\d+.\\d+") || parameters.get("sum").matches("\\d+")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency"));
        else postBackEntity.setCurrency("USD");
        if (parameters.containsKey("goal")) postBackEntity.setGoal(parameters.get("goal"));
        if (parameters.containsKey("status")) postBackEntity.setStatus(parameters.get("status"));
        if (parameters.containsKey("advname")) postBackEntity.setAdvName(parameters.get("advname"));
        if (parameters.containsKey("offername")) postBackEntity.setOfferName(parameters.get("offername"));
        if (parameters.containsKey("transactionid")) postBackEntity.setTransactionId(parameters.get("transactionid"));
        if (parameters.containsKey("idfa")) postBackEntity.setIDFA(parameters.get("idfa"));
        if (parameters.containsKey("gaid")) postBackEntity.setGaId("gaid");
        if (parameters.containsKey("ip")) postBackEntity.setIpAddress(parameters.get("ip"));
        if (parameters.containsKey("secret")) postBackEntity.setSecretKey(parameters.get("secret"));
        if (parameters.containsKey("t1")) postBackEntity.setT1(parameters.get("t1"));
        if (parameters.containsKey("t2")) postBackEntity.setT2(parameters.get("t2"));
        if (parameters.containsKey("t3")) postBackEntity.setT3(parameters.get("t3"));
        if (parameters.containsKey("t4")) postBackEntity.setT4(parameters.get("t4"));
        if (parameters.containsKey("t5")) postBackEntity.setT5(parameters.get("t5"));
        if (parameters.containsKey("t6")) postBackEntity.setT6(parameters.get("t6"));
        if (parameters.containsKey("t7")) postBackEntity.setT7(parameters.get("t7"));
        if (parameters.containsKey("t8")) postBackEntity.setT8(parameters.get("t8"));
        if (parameters.containsKey("t9")) postBackEntity.setT9(parameters.get("t9"));
        if (parameters.containsKey("t10")) postBackEntity.setT10(parameters.get("t10"));
        if (parameters.containsKey("offerid")) postBackEntity.setOfferId(parameters.get("offerid"));
        if (parameters.containsKey("afid") && parameters.get("afid").matches("\\d+")) postBackEntity.setAfid(Integer.parseInt(parameters.get("afid")));
        if (parameters.containsKey("postbacksend") && parameters.get("postbacksend").matches("\\d+")) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);


        Random random = new Random();
        StringBuilder actionId = new StringBuilder();
        int numberLength = random.nextInt(8) + 12;
        while (true) {
            for (int i = 0; i < numberLength; i++) {
                actionId.append(random.nextInt(10));
            }
            if (!isTransactionidInDB(actionId.toString())) break;
            else actionId = new StringBuilder();
        }
        postBackEntity.setActionId(actionId.toString());

        if (postBackEntity.getTransactionId() != null
                && isPostbackPartial(postBackEntity.getClickId(), postBackEntity.getTransactionId())){
            postBackEntity.setDuplicate("PARTIAL");
        }

        return postBackEntity;
    }

    private boolean isPostbackPartial(String clickId, String transactionId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        PostBackEntity postBackEntity = mySQLDao.getPostbackByClickAndTransactionId(clickId, transactionId);
        return postBackEntity != null;
    }

    private boolean isTransactionidInDB(String transactionId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        PostBackEntity postBackEntity = mySQLDao.getPostbackByTransactionId(transactionId);
        return postBackEntity != null;
    }

    private boolean isPrefixInTrackers(String prefix) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        TrackerEntity trackerEntity = mySQLDao.getTrackerByPrefix(prefix);
        return trackerEntity != null;
    }
}
