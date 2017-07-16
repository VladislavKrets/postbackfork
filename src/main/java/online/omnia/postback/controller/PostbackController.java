package online.omnia.postback.controller;

import online.omnia.postback.core.dao.MySQLDao;
import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.affise.AffiseTracker;
import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import online.omnia.postback.core.utils.PostbackHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 13.07.2017.
 */
@Controller
public class PostbackController {
    final static Logger logger = Logger.getLogger(PostbackController.class);

    @RequestMapping(value = "/{postback_url}", method = RequestMethod.GET)
    public String sendPostback(@PathVariable(name = "postback_url") String postbackUrl, Model model){
        PostbackHandler postbackHandler = new PostbackHandler();
        java.util.Date currentDate = new java.util.Date();
        Map<String, String> parameters = postbackHandler.getPostbackParameters(postbackUrl);
        if (!parameters.containsKey("clickid") || parameters.get("clickid").isEmpty()) {
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackUrl);
            return "SendPostback";
        }
        FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                new Time(currentDate.getTime()), postbackUrl);
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();

        PostBackEntity postBackEntity = fillPostback(parameters);
        postBackEntity.setFullURL(postbackUrl);

        mySQLDao.addPostback(postBackEntity);

        TrackerEntity tracker = mySQLDao.getTrackerByPrefix(postBackEntity.getPrefix());
        if (tracker != null) {
            switch (tracker.getPrefix()) {
                case 101:
                    affiseHandler(postBackEntity);
                    break;
                case 102:
                    binomHandler(postBackEntity, tracker.getDomain());
                    break;
                default: {
                    postBackEntity.setPrefix(101);
                    postBackEntity.setAfid(2);
                    affiseHandler(postBackEntity);
                }
            }
        }
        else {
            postBackEntity.setPrefix(101);
            postBackEntity.setAfid(2);
            affiseHandler(postBackEntity);
        }

        return "SendPostback";
    }
    private boolean isAffidInAffiliate(int affid) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AffiliatesEntity affiliate = mySQLDao.getAffiliateByAffid(affid);
        return affiliate != null;
    }
    private void binomHandler(PostBackEntity postBackEntity, String url) {
        BinomTracker tracker = new BinomTracker(url + "/");
        try {
            tracker.sendPostback(postBackEntity);
        } catch (NoClickIdException e) {
            logger.debug("Invalid click id");
            logger.debug(e.getMessage());
        }
    }
    private void affiseHandler(PostBackEntity postBackEntity) {
        AffiseTracker tracker = new AffiseTracker();
        try {
            tracker.sendPostback(postBackEntity);
        } catch (NoClickIdException e) {
            logger.debug("Invalid click id");
            logger.debug(e.getMessage());
        }
    }

    private PostBackEntity fillPostback(Map<String, String> parameters) {
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

        if (parameters.containsKey("sum")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency"));
        if (parameters.containsKey("goal")) postBackEntity.setGoal(Integer.parseInt(parameters.get("goal")));
        if (parameters.containsKey("status")) postBackEntity.setStatus(Integer.parseInt(parameters.get("status")));
        if (parameters.containsKey("advname")) postBackEntity.setAdvName(parameters.get("advname"));
        if (parameters.containsKey("offername")) postBackEntity.setOfferName(parameters.get("offername"));
        if (parameters.containsKey("transaction_id")) postBackEntity.setTransactionId(parameters.get("transactionid"));
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
        if (parameters.containsKey("affid")) postBackEntity.setAfid(Integer.parseInt(parameters.get("affid")));
        else postBackEntity.setAfid(2);
        if (parameters.containsKey("postback_send")) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);

        return postBackEntity;
    }
}
