package online.omnia.postback.core.utils;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.trackers.entities.AbstractPostBackEntity;
import online.omnia.postback.core.trackers.entities.ErrorPostBackEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;

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
        int clickIdLength = (clickid != null && clickid.length() != 0) ? clickid.length() : 0;
        System.out.println("Clickid = " + clickid);
        if (clickIdLength > 0 && clickid.contains("_")){
            prefixNumber = clickid.indexOf("_");
            prefix = clickid.substring(0, prefixNumber);
            if (!isPrefixInTrackers(prefix)) prefix = null;
            else clickid = clickid.substring(prefixNumber + 1, clickIdLength);
        }

        postBackEntity.setClickId(clickid);
        postBackEntity.setPrefix(prefix);

        if (parameters.containsKey("sum") && parameters.get("sum").matches("(\\d+.\\d+)|(\\d+)")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
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
        if (parameters.containsKey("event1")) postBackEntity.setEvent1(parameters.get("event1"));
        if (parameters.containsKey("event2")) postBackEntity.setEvent2(parameters.get("event2"));
        if (parameters.containsKey("event3")) postBackEntity.setEvent3(parameters.get("event3"));
        if (parameters.containsKey("event4")) postBackEntity.setEvent4(parameters.get("event4"));
        if (parameters.containsKey("event5")) postBackEntity.setEvent5(parameters.get("event5"));
        if (parameters.containsKey("event6")) postBackEntity.setEvent6(parameters.get("event6"));
        if (parameters.containsKey("event7")) postBackEntity.setEvent7(parameters.get("event7"));
        if (parameters.containsKey("event8")) postBackEntity.setEvent8(parameters.get("event8"));
        if (parameters.containsKey("event9")) postBackEntity.setEvent9(parameters.get("event9"));
        if (parameters.containsKey("event10")) postBackEntity.setEvent10(parameters.get("event10"));
        if (parameters.containsKey("add_event1")) postBackEntity.setEvent1(postBackEntity.getEvent1() + "+" + parameters.get("add_event1"));
        if (parameters.containsKey("add_event2")) postBackEntity.setEvent2(postBackEntity.getEvent2() + "+" + parameters.get("add_event2"));
        if (parameters.containsKey("add_event3")) postBackEntity.setEvent3(postBackEntity.getEvent3() + "+" + parameters.get("add_event3"));
        if (parameters.containsKey("add_event4")) postBackEntity.setEvent4(postBackEntity.getEvent4() + "+" + parameters.get("add_event4"));
        if (parameters.containsKey("add_event5")) postBackEntity.setEvent5(postBackEntity.getEvent5() + "+" + parameters.get("add_event5"));
        if (parameters.containsKey("add_event6")) postBackEntity.setEvent6(postBackEntity.getEvent6() + "+" + parameters.get("add_event6"));
        if (parameters.containsKey("add_event7")) postBackEntity.setEvent7(postBackEntity.getEvent7() + "+" + parameters.get("add_event7"));
        if (parameters.containsKey("add_event8")) postBackEntity.setEvent8(postBackEntity.getEvent8() + "+" + parameters.get("add_event8"));
        if (parameters.containsKey("add_event9")) postBackEntity.setEvent9(postBackEntity.getEvent9() + "+" + parameters.get("add_event9"));
        if (parameters.containsKey("add_event10")) postBackEntity.setEvent10(postBackEntity.getEvent10() + "+" + parameters.get("add_event10"));
        if (parameters.containsKey("offerid")) postBackEntity.setOfferId(parameters.get("offerid"));
        if (parameters.containsKey("afid") && parameters.get("afid").matches("\\d+")) postBackEntity.setAfid(Integer.parseInt(parameters.get("afid")));
        if (parameters.containsKey("postbacksend") && parameters.get("postbacksend").matches("\\d+")) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);
        postBackEntity.setDuplicate("original");

        if (isPostbackPartial(postBackEntity.getClickId())){
            System.out.println("is postback PARTIAl");
            postBackEntity.setDuplicate("PARTIAL");
        }

        return postBackEntity;
    }

    public ErrorPostBackEntity createError(PostBackEntity postBackEntity) {
        ErrorPostBackEntity errorPostBackEntity = new ErrorPostBackEntity();
        errorPostBackEntity.setActionId(postBackEntity.getActionId());
        errorPostBackEntity.setAdvName(postBackEntity.getAdvName());
        errorPostBackEntity.setAfid(postBackEntity.getAfid());
        errorPostBackEntity.setClickId(postBackEntity.getClickId());
        errorPostBackEntity.setCurrency(postBackEntity.getCurrency());
        errorPostBackEntity.setDate(postBackEntity.getDate());
        errorPostBackEntity.setDuplicate(postBackEntity.getDuplicate());
        errorPostBackEntity.setFullURL(postBackEntity.getFullURL());
        errorPostBackEntity.setGaId(postBackEntity.getGaId());
        errorPostBackEntity.setGoal(postBackEntity.getGoal());
        errorPostBackEntity.setId(postBackEntity.getId());
        errorPostBackEntity.setIDFA(postBackEntity.getIDFA());
        errorPostBackEntity.setIpAddress(postBackEntity.getIpAddress());
        errorPostBackEntity.setOfferId(postBackEntity.getOfferId());
        errorPostBackEntity.setOfferName(postBackEntity.getOfferName());
        errorPostBackEntity.setPostbackSend(postBackEntity.getPostbackSend());
        errorPostBackEntity.setPrefix(postBackEntity.getPrefix());
        errorPostBackEntity.setSecretKey(postBackEntity.getSecretKey());
        errorPostBackEntity.setStatus(postBackEntity.getStatus());
        errorPostBackEntity.setSum(postBackEntity.getSum());
        errorPostBackEntity.setT1(postBackEntity.getT1());
        errorPostBackEntity.setT2(postBackEntity.getT2());
        errorPostBackEntity.setT3(postBackEntity.getT3());
        errorPostBackEntity.setT4(postBackEntity.getT4());
        errorPostBackEntity.setT5(postBackEntity.getT5());
        errorPostBackEntity.setT6(postBackEntity.getT6());
        errorPostBackEntity.setT7(postBackEntity.getT7());
        errorPostBackEntity.setT8(postBackEntity.getT8());
        errorPostBackEntity.setT9(postBackEntity.getT9());
        errorPostBackEntity.setT10(postBackEntity.getT10());
        errorPostBackEntity.setTime(postBackEntity.getTime());
        errorPostBackEntity.setDuplicate(postBackEntity.getDuplicate());
        errorPostBackEntity.setTransactionId(postBackEntity.getTransactionId());

        return errorPostBackEntity;
    }

    private boolean isPostbackPartial(String clickId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AbstractPostBackEntity postBackEntity = mySQLDao.getPostbackByClickId(clickId);
        return postBackEntity != null;
    }

    private boolean isTransactionidInDB(String transactionId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AbstractPostBackEntity postBackEntity = mySQLDao.getPostbackByTransactionId(transactionId);
        return postBackEntity != null;
    }

    private boolean isPrefixInTrackers(String prefix) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        TrackerEntity trackerEntity = mySQLDao.getTrackerByPrefix(prefix);
        return trackerEntity != null;
    }
}
