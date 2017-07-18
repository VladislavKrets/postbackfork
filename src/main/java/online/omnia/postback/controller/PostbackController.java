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
        if (parameters.isEmpty()) return "SendPostback";

        if (!parameters.containsKey("clickid") || parameters.get("clickid").isEmpty()) {
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackUrl);
            return "SendPostback";
        }

        FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                new Time(currentDate.getTime()), postbackUrl);
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();

        PostBackEntity postBackEntity = postbackHandler.fillPostback(parameters);

        postBackEntity.setFullURL(postbackUrl);

        mySQLDao.addPostback(postBackEntity);
        chooseHandler(postBackEntity);

        return "SendPostback";
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

}
