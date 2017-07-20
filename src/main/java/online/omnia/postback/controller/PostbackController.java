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
        if (parameters.isEmpty()) return "Status error: 201 invalid conversion, parameters non present";

        if (!parameters.containsKey("clickid") || parameters.get("clickid").isEmpty()) {
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackUrl);
            return "Status error: 201 invalid conversion, clickid non present";
        }

        FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                new Time(currentDate.getTime()), postbackUrl);
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();

        PostBackEntity postBackEntity = postbackHandler.fillPostback(parameters);

        postBackEntity.setFullURL(postbackUrl);

        mySQLDao.addPostback(postBackEntity);
        chooseHandler(postBackEntity);

        return "Status: 200 OK";
    }

    private void chooseHandler(PostBackEntity postBackEntity) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
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

    }

    private void binomHandler(PostBackEntity postBackEntity, String url) {
        BinomTracker tracker = new BinomTracker(url + "/");
        AffiseTracker affiseTracker = new AffiseTracker();
        try {
            tracker.sendPostback(postBackEntity);
            if (postBackEntity.getAfid() != 0) {
                switch (postBackEntity.getAfid()) {
                    case 1001: postBackEntity.setClickId("596f55f8042391106dcf7230"); break;
                    case 1002: postBackEntity.setClickId("596f5618042391106dcf731c"); break;
                    case 1003: postBackEntity.setClickId("596f562a042391106dcf73a9"); break;
                }
            }
            affiseTracker.sendPostback(postBackEntity);
        } catch (NoClickIdException e) {
            logger.debug("Invalid click id");
            logger.debug(e.getMessage());
            postBackEntity.setClickId("3D596f4d28042391106dcf344b");
            postBackEntity.setAfid(2);
            try {
                affiseTracker.sendPostback(postBackEntity);
            } catch (NoClickIdException ignore) {}
        }
    }
    private void affiseHandler(PostBackEntity postBackEntity) {
        AffiseTracker tracker = new AffiseTracker();

        try {
            tracker.sendPostback(postBackEntity);
        } catch (NoClickIdException e) {
            logger.debug("Invalid click id");
            logger.debug(e.getMessage());
            postBackEntity.setClickId("3D596f4d28042391106dcf344b");
            postBackEntity.setAfid(2);
            try {
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException ignore) {}
        }


    }

}
