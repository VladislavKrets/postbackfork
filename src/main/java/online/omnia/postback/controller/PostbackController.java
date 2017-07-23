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
        java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
        System.out.println("Getting url parameters");
        Map<String, String> parameters = postbackHandler.getPostbackParameters(postbackUrl);
        System.out.println("Parameters have been got");
        if (parameters.isEmpty()){
            System.out.println("Parameters are empty");
            return "HTTP/1.1 201 error\r\n";
        }

        if (!parameters.containsKey("clickid") || parameters.get("clickid").isEmpty()) {
            System.out.println("clickid is not valid");
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackUrl);
            System.out.println("postback has been written to error_postback");
            return "HTTP/1.1 201 error\r\n";
        }

        FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                new Time(currentDate.getTime()), postbackUrl);
        System.out.println("postback has been written to postback file");
        System.out.println("Creating mySQLDao entity");
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        System.out.println("Creating postback entity");
        PostBackEntity postBackEntity = postbackHandler.fillPostback(parameters);
        postBackEntity.setDate(new Date(currentDate.getTime()));
        postBackEntity.setTime(new Time(currentDate.getTime()));
        postBackEntity.setFullURL(postbackUrl);

        System.out.println("adding postback to dao");
        mySQLDao.addPostback(postBackEntity);
        System.out.println("choosing handler");
        chooseHandler(postBackEntity);

        return "HTTP/1.1 200 OK\r\n";
    }

    private void chooseHandler(PostBackEntity postBackEntity) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        System.out.println("Getting tracker from dao");
        TrackerEntity affiseTracker = mySQLDao.getTrackerByPrefix(101);
        TrackerEntity tracker = mySQLDao.getTrackerByPrefix(postBackEntity.getPrefix());
        if (tracker != null) {
            switch (tracker.getPrefix()) {
                case 101:
                    System.out.println("sending to affise tracker");
                    affiseHandler(postBackEntity, affiseTracker.getDomain());
                    break;
                case 102:
                    System.out.println("sending to binom tracker");
                    binomHandler(postBackEntity, tracker.getDomain(), affiseTracker.getDomain());
                    break;
                default: {
                    System.out.println("sending to default: affise tracker");
                    postBackEntity.setPrefix(101);
                    postBackEntity.setAfid(2);
                    affiseHandler(postBackEntity, affiseTracker.getDomain());
                }
            }
        }
        else {
            System.out.println("No tracker. Sending to affise tracker");
            postBackEntity.setPrefix(101);
            postBackEntity.setAfid(2);
            affiseHandler(postBackEntity, affiseTracker.getDomain());
        }

    }

    private void binomHandler(PostBackEntity postBackEntity, String binomUrl, String affiseUrl) {
        BinomTracker tracker = new BinomTracker(binomUrl + "/");
        AffiseTracker affiseTracker = new AffiseTracker(affiseUrl + "/");
        try {
            System.out.println("sending to binom...");
            tracker.sendPostback(postBackEntity);
            System.out.println("has been sent");
            if (postBackEntity.getAfid() != 0) {
                switch (postBackEntity.getAfid()) {
                    case 1001: postBackEntity.setClickId("596f55f8042391106dcf7230"); break;
                    case 1002: postBackEntity.setClickId("596f5618042391106dcf731c"); break;
                    case 1003: postBackEntity.setClickId("596f562a042391106dcf73a9"); break;
                }
            }
            System.out.println("sending to affise...");
            affiseTracker.sendPostback(postBackEntity);
            System.out.println("has been sent");
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
    private void affiseHandler(PostBackEntity postBackEntity, String affiseUrl) {
        AffiseTracker tracker = new AffiseTracker(affiseUrl + "/");

        try {
            System.out.println("sending to affise...");
            tracker.sendPostback(postBackEntity);
            System.out.println("has been sent");
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
