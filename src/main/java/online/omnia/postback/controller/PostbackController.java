package online.omnia.postback.controller;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import online.omnia.postback.core.utils.PostbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

/**
 * Created by lollipop on 13.07.2017.
 */
@Controller
public class PostbackController {

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


        return "SendPostback";
    }

    private PostBackEntity fillPostback(Map<String, String> parameters) {
        PostBackEntity postBackEntity = new PostBackEntity();

        String clickid = parameters.get("clickid");
        String afid = parameters.get("clickid").length() > 3 ? clickid.substring(0, 4) : null;
        postBackEntity.setClickId(clickid);
        postBackEntity.setAfid(afid);
        if (parameters.containsKey("sum")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency"));
        if (parameters.containsKey("goal")) postBackEntity.setGoal(Integer.parseInt(parameters.get("goal")));
        if (parameters.containsKey("status")) postBackEntity.setStatus(Integer.parseInt(parameters.get("status")));
        if (parameters.containsKey("advname")) postBackEntity.setAdvName(parameters.get("advname"));
        if (parameters.containsKey("offername")) postBackEntity.setOfferName(parameters.get("offername"));
        if (parameters.containsKey("transaction_id")) postBackEntity.setTransactionId(parameters.get("transactionid"));
        if (parameters.containsKey("ios_idfa")) postBackEntity.setIDFA(parameters.get("ios_idfa"));
        if (parameters.containsKey("android_id")) postBackEntity.setGaId("android_id");
        if (parameters.containsKey("action_id")) postBackEntity.setTransactionId(parameters.get("action_id"));
        if (parameters.containsKey("ip")) postBackEntity.setIpAddress(parameters.get("ip"));
        if (parameters.containsKey("secure")) postBackEntity.setSecretKey(parameters.get("secure"));
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

        return postBackEntity;
    }
}
