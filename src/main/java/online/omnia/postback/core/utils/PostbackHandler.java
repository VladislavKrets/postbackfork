package online.omnia.postback.core.utils;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.trackers.entities.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class which can parse url and create PostbackEntities
 */
public class PostbackHandler {
    /**
     * Method parses url and gets postback parameters
     * @param url of postback
     * @return map of postback parameters
     */
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

    /**
     * Creates PostBackEntity using url parameters
     * @param parameters map which we get after parsing url
     * @return postback entity
     */
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

        postBackEntity.setClickId(clickid != null ? (clickid.length() < 200 ? clickid : clickid.substring(0, 200)) : null);
        postBackEntity.setPrefix(prefix != null ? (prefix.length() < 50 ? prefix : prefix.substring(0, 50)) : null);

        if (parameters.containsKey("sum") && parameters.get("sum").matches("(\\d+.\\d+)|(\\d+)")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency").length() < 15 ? parameters.get("currency") : parameters.get("currency").substring(0, 15));
        else postBackEntity.setCurrency("USD");
        if (parameters.containsKey("goal")) postBackEntity.setGoal(parameters.get("goal").length() < 50 ? parameters.get("goal") : parameters.get("goal").substring(0, 50));
        if (parameters.containsKey("status")) postBackEntity.setStatus(parameters.get("status").length() < 50 ? parameters.get("status") : parameters.get("status").substring(0, 50));
        if (parameters.containsKey("advname")) postBackEntity.setAdvName(parameters.get("advname").length() < 100 ? parameters.get("advname") : parameters.get("advname").substring(0, 100));
        if (parameters.containsKey("offername")) postBackEntity.setOfferName(parameters.get("offername").length() < 200 ? parameters.get("offername") : parameters.get("offername").substring(0, 200));
        if (parameters.containsKey("transactionid")) postBackEntity.setTransactionId(parameters.get("transactionid").length() < 100 ? parameters.get("transactionid") : parameters.get("transactionid").substring(0, 100));
        if (parameters.containsKey("txid")) postBackEntity.setTransactionId(parameters.get("txid").length() < 100 ? parameters.get("txid") : parameters.get("txid").substring(0, 100));
        if (parameters.containsKey("idfa")) postBackEntity.setIDFA(parameters.get("idfa").length() < 50 ? parameters.get("idfa") : parameters.get("idfa").substring(0, 50));
        if (parameters.containsKey("gaid")) postBackEntity.setGaId(parameters.get("gaid").length() < 50 ? parameters.get("gaid") : parameters.get("gaid").substring(0, 50));
        if (parameters.containsKey("ip")) postBackEntity.setIpAddress(parameters.get("ip").length() < 20 ? parameters.get("ip") : parameters.get("ip").substring(0, 20));
        if (parameters.containsKey("secret")) postBackEntity.setSecretKey(parameters.get("secret").length() < 100 ? parameters.get("secret") : parameters.get("secret").substring(0, 100));
        if (parameters.containsKey("t1")) postBackEntity.setT1(parameters.get("t1").length() < 100 ? parameters.get("t1") : parameters.get("t1").substring(0, 100));
        if (parameters.containsKey("t2")) postBackEntity.setT2(parameters.get("t2").length() < 100 ? parameters.get("t2") : parameters.get("t2").substring(0, 100));
        if (parameters.containsKey("t3")) postBackEntity.setT3(parameters.get("t3").length() < 100 ? parameters.get("t3") : parameters.get("t3").substring(0, 100));
        if (parameters.containsKey("t4")) postBackEntity.setT4(parameters.get("t4").length() < 100 ? parameters.get("t4") : parameters.get("t4").substring(0, 100));
        if (parameters.containsKey("t5")) postBackEntity.setT5(parameters.get("t5").length() < 100 ? parameters.get("t5") : parameters.get("t5").substring(0, 100));
        if (parameters.containsKey("t6")) postBackEntity.setT6(parameters.get("t6").length() < 100 ? parameters.get("t6") : parameters.get("t6").substring(0, 100));
        if (parameters.containsKey("t7")) postBackEntity.setT7(parameters.get("t7").length() < 100 ? parameters.get("t7") : parameters.get("t7").substring(0, 100));
        if (parameters.containsKey("t8")) postBackEntity.setT8(parameters.get("t8").length() < 100 ? parameters.get("t8") : parameters.get("t8").substring(0, 100));
        if (parameters.containsKey("t9")) postBackEntity.setT9(parameters.get("t9").length() < 100 ? parameters.get("t9") : parameters.get("t9").substring(0, 100));
        if (parameters.containsKey("t10")) postBackEntity.setT10(parameters.get("t10").length() < 100 ? parameters.get("t10") : parameters.get("t10").substring(0, 100));
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
        if (parameters.containsKey("add_event1")) postBackEntity.setAddEvent1(parameters.get("add_event1"));
        if (parameters.containsKey("add_event2")) postBackEntity.setAddEvent2(parameters.get("add_event2"));
        if (parameters.containsKey("add_event3")) postBackEntity.setAddEvent3(parameters.get("add_event3"));
        if (parameters.containsKey("add_event4")) postBackEntity.setAddEvent4(parameters.get("add_event4"));
        if (parameters.containsKey("add_event5")) postBackEntity.setAddEvent5(parameters.get("add_event5"));
        if (parameters.containsKey("add_event6")) postBackEntity.setAddEvent6(parameters.get("add_event6"));
        if (parameters.containsKey("add_event7")) postBackEntity.setAddEvent7(parameters.get("add_event7"));
        if (parameters.containsKey("add_event8")) postBackEntity.setAddEvent8(parameters.get("add_event8"));
        if (parameters.containsKey("add_event9")) postBackEntity.setAddEvent9(parameters.get("add_event9"));
        if (parameters.containsKey("add_event10")) postBackEntity.setAddEvent10(parameters.get("add_event10"));

        if (parameters.containsKey("offerid")) postBackEntity.setOfferId(parameters.get("offerid").length() < 50 ? parameters.get("offerid") : parameters.get("offerid").substring(0, 50));
        if (parameters.containsKey("afid") && parameters.get("afid").matches("\\d+")) postBackEntity.setAfid(Integer.parseInt(parameters.get("afid")));
        if (parameters.containsKey("postbacksend") && parameters.get("postbacksend").matches("\\d+")) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);
        postBackEntity.setDuplicate("original");
        if (parameters.containsKey("idc") && parameters.get("idc").contains("_")) {
            String[] idc = parameters.get("idc").split("_");
            postBackEntity.setSecondPrefix(idc[0]);
            postBackEntity.setIdc(idc[1]);
            idc = null;
        }
        if (parameters.containsKey("ido") && parameters.get("ido").contains("_")) {
            String[] ido = parameters.get("ido").split("_");
            postBackEntity.setIdoPrefix(ido[0]);
            postBackEntity.setIdo(ido[1]);
            ido = null;
        }


        return postBackEntity;
    }

    /**
     * Method checks if events in postback are empty
     * @param postBackEntity entity which we get after parsing the url
     * @return checking if events empty
     */
    public boolean isEventFilled(PostBackEntity postBackEntity) {
        return !postBackEntity.getEvent1().isEmpty() || !postBackEntity.getEvent2().isEmpty()
                || !postBackEntity.getEvent3().isEmpty() || !postBackEntity.getEvent4().isEmpty()
                || !postBackEntity.getEvent5().isEmpty() || !postBackEntity.getEvent6().isEmpty()
                || !postBackEntity.getEvent7().isEmpty() || !postBackEntity.getEvent8().isEmpty()
                || !postBackEntity.getEvent9().isEmpty() || !postBackEntity.getEvent10().isEmpty();
    }

    public PostBackEntity1 createPostbackEntity1(PostBackEntity postBackEntity) {
        PostBackEntity1 postBackEntity1 = new PostBackEntity1();
        postBackEntity1.setActionId(postBackEntity.getActionId());
        postBackEntity1.setAdvName(postBackEntity.getAdvName());
        postBackEntity1.setAfid(postBackEntity.getAfid());
        postBackEntity1.setClickId(postBackEntity.getClickId());
        postBackEntity1.setCurrency(postBackEntity.getCurrency());
        postBackEntity1.setDate(postBackEntity.getDate());
        postBackEntity1.setDuplicate(postBackEntity.getDuplicate());
        postBackEntity1.setFullURL(postBackEntity.getFullURL());
        postBackEntity1.setGaId(postBackEntity.getGaId());
        postBackEntity1.setGoal(postBackEntity.getGoal());
        postBackEntity1.setId(postBackEntity.getId());
        postBackEntity1.setIDFA(postBackEntity.getIDFA());
        postBackEntity1.setIpAddress(postBackEntity.getIpAddress());
        postBackEntity1.setOfferId(postBackEntity.getOfferId());
        postBackEntity1.setOfferName(postBackEntity.getOfferName());
        postBackEntity1.setPostbackSend(postBackEntity.getPostbackSend());
        postBackEntity1.setPrefix(postBackEntity.getPrefix());
        postBackEntity1.setSecretKey(postBackEntity.getSecretKey());
        postBackEntity1.setStatus(postBackEntity.getStatus());
        postBackEntity1.setSum(postBackEntity.getSum());
        postBackEntity1.setT1(postBackEntity.getT1());
        postBackEntity1.setT2(postBackEntity.getT2());
        postBackEntity1.setT3(postBackEntity.getT3());
        postBackEntity1.setT4(postBackEntity.getT4());
        postBackEntity1.setT5(postBackEntity.getT5());
        postBackEntity1.setT6(postBackEntity.getT6());
        postBackEntity1.setT7(postBackEntity.getT7());
        postBackEntity1.setT8(postBackEntity.getT8());
        postBackEntity1.setT9(postBackEntity.getT9());
        postBackEntity1.setT10(postBackEntity.getT10());
        postBackEntity1.setTime(postBackEntity.getTime());
        postBackEntity1.setDuplicate(postBackEntity.getDuplicate());
        postBackEntity1.setTransactionId(postBackEntity.getTransactionId());
        postBackEntity1.setEvent1(postBackEntity.getEvent1());
        postBackEntity1.setEvent2(postBackEntity.getEvent2());
        postBackEntity1.setEvent3(postBackEntity.getEvent3());
        postBackEntity1.setEvent4(postBackEntity.getEvent4());
        postBackEntity1.setEvent5(postBackEntity.getEvent5());
        postBackEntity1.setEvent6(postBackEntity.getEvent6());
        postBackEntity1.setEvent7(postBackEntity.getEvent7());
        postBackEntity1.setEvent8(postBackEntity.getEvent8());
        postBackEntity1.setEvent9(postBackEntity.getEvent9());
        postBackEntity1.setEvent10(postBackEntity.getEvent10());
        postBackEntity1.setAddEvent1(postBackEntity.getAddEvent1());
        postBackEntity1.setAddEvent2(postBackEntity.getAddEvent2());
        postBackEntity1.setAddEvent3(postBackEntity.getAddEvent3());
        postBackEntity1.setAddEvent4(postBackEntity.getAddEvent4());
        postBackEntity1.setAddEvent5(postBackEntity.getAddEvent5());
        postBackEntity1.setAddEvent6(postBackEntity.getAddEvent6());
        postBackEntity1.setAddEvent7(postBackEntity.getAddEvent7());
        postBackEntity1.setAddEvent8(postBackEntity.getAddEvent8());
        postBackEntity1.setAddEvent9(postBackEntity.getAddEvent9());
        postBackEntity1.setAddEvent10(postBackEntity.getAddEvent10());
        postBackEntity1.setExchange(postBackEntity.getExchange());
        postBackEntity1.setIdc(postBackEntity.getIdc());
        postBackEntity1.setIdo(postBackEntity.getIdo());

        return postBackEntity1;
    }
    /**
     * Method creates ErrorPostBackEntity from PostBackEntity
     * @param postBackEntity entity which we get after parsing the url
     * @return error postback
     */
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
        errorPostBackEntity.setEvent1(postBackEntity.getEvent1());
        errorPostBackEntity.setEvent2(postBackEntity.getEvent2());
        errorPostBackEntity.setEvent3(postBackEntity.getEvent3());
        errorPostBackEntity.setEvent4(postBackEntity.getEvent4());
        errorPostBackEntity.setEvent5(postBackEntity.getEvent5());
        errorPostBackEntity.setEvent6(postBackEntity.getEvent6());
        errorPostBackEntity.setEvent7(postBackEntity.getEvent7());
        errorPostBackEntity.setEvent8(postBackEntity.getEvent8());
        errorPostBackEntity.setEvent9(postBackEntity.getEvent9());
        errorPostBackEntity.setEvent10(postBackEntity.getEvent10());
        errorPostBackEntity.setAddEvent1(postBackEntity.getAddEvent1());
        errorPostBackEntity.setAddEvent2(postBackEntity.getAddEvent2());
        errorPostBackEntity.setAddEvent3(postBackEntity.getAddEvent3());
        errorPostBackEntity.setAddEvent4(postBackEntity.getAddEvent4());
        errorPostBackEntity.setAddEvent5(postBackEntity.getAddEvent5());
        errorPostBackEntity.setAddEvent6(postBackEntity.getAddEvent6());
        errorPostBackEntity.setAddEvent7(postBackEntity.getAddEvent7());
        errorPostBackEntity.setAddEvent8(postBackEntity.getAddEvent8());
        errorPostBackEntity.setAddEvent9(postBackEntity.getAddEvent9());
        errorPostBackEntity.setAddEvent10(postBackEntity.getAddEvent10());
        errorPostBackEntity.setExchange(postBackEntity.getExchange());
        errorPostBackEntity.setIdc(postBackEntity.getIdc());
        errorPostBackEntity.setIdo(postBackEntity.getIdo());

        return errorPostBackEntity;
    }

    /**
     * Method checks is postback partial
     * @param clickId parameter of postback
     * @return checking if postback is partial
     */
    private boolean isPostbackPartial(String clickId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AbstractPostBackEntity postBackEntity = mySQLDao.getPostbackByClickId(clickId);
        return postBackEntity != null;
    }

    /**
     * Method checks is transactionid in db
     * @param transactionId parameter of postback
     * @return checking if transaction is in db
     */
    private boolean isTransactionidInDB(String transactionId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AbstractPostBackEntity postBackEntity = mySQLDao.getPostbackByTransactionId(transactionId);
        return postBackEntity != null;
    }

    /**
     * Method checks is prefix in db
     * @param prefix parameter of postback
     * @return checking if prefix in db
     */
    private boolean isPrefixInTrackers(String prefix) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        TrackerEntity trackerEntity = mySQLDao.getTrackerByPrefix(prefix);
        return trackerEntity != null;
    }
}
