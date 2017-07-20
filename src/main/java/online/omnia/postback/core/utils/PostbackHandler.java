package online.omnia.postback.core.utils;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class PostbackHandler {

    public Map<String, String> getPostbackParameters(String url){

        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String[] urlParts = url.split("\\?");

        if (urlParts.length != 2) return parametersMap;

        String parameters = urlParts[1];

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

        return parametersMap;
    }
    public PostBackEntity fillPostback(Map<String, String> parameters) {
        PostBackEntity postBackEntity = new PostBackEntity();

        String clickid = parameters.get("clickid");
        String prefix = parameters.get("clickid").length() > 3 ? clickid.substring(0, 3) : null;
        if (prefix != null && prefix.matches("\\d\\d\\d")) {
            postBackEntity.setClickId(clickid.substring(3, clickid.length()));
            postBackEntity.setPrefix(Integer.parseInt(prefix));
        }
        else {
            postBackEntity.setClickId(clickid);
            postBackEntity.setPrefix(101);
            postBackEntity.setAfid(2);
        }

        if (!isAffidInAffiliate(postBackEntity.getAfid())) postBackEntity.setAfid(2);

        if (parameters.containsKey("sum") && !parameters.get("sum").isEmpty()) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency"));
        if (parameters.containsKey("goal") && !parameters.get("goal").isEmpty()) postBackEntity.setGoal(Integer.parseInt(parameters.get("goal")));
        if (parameters.containsKey("status") && !parameters.get("status").isEmpty()) postBackEntity.setStatus(Integer.parseInt(parameters.get("status")));
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
        if (parameters.containsKey("affid") && !parameters.get("affid").isEmpty()) postBackEntity.setAfid(Integer.parseInt(parameters.get("affid")));
        else postBackEntity.setAfid(2);
        if (parameters.containsKey("postbacksend") && !parameters.get("postbacksend").isEmpty()) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);

        return postBackEntity;
    }
    private boolean isAffidInAffiliate(int affid) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AffiliatesEntity affiliate = mySQLDao.getAffiliateByAffid(affid);
        return affiliate != null;
    }
}
