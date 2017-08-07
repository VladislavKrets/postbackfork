package online.omnia.postback.controller;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.affise.AffiseTracker;
import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import online.omnia.postback.core.utils.PostbackHandler;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

/**
 * Created by lollipop on 27.07.2017.
 */
public class MainController {
    final static Logger logger = Logger.getLogger(MainController.class);
    private java.util.Date currentDate;
    public MainController() {
        currentDate = new java.util.Date(System.currentTimeMillis());
    }

    public String sendPostback(String postbackURL) {
        PostbackHandler postbackHandler = new PostbackHandler();
        Map<String, String> parameters = postbackHandler.getPostbackParameters(postbackURL);
        System.out.println("Parameters have been got");
        if (parameters.isEmpty()){
            System.out.println("Parameters are empty");
            return "HTTP/1.1 201 error\r\n";
        }
        if (!parameters.containsKey("clickid") || parameters.get("clickid").isEmpty()) {
            System.out.println("clickid is not valid");
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            System.out.println("postback has been written to error_postback");
            return "HTTP/1.1 201 error\r\n";
        }

        System.out.println("Creating mySQLDao entity");
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        System.out.println("Creating postback entity");
        PostBackEntity postBackEntity = postbackHandler.fillPostback(parameters);
        System.out.println("Setting date and time");
        postBackEntity.setDate(new Date(currentDate.getTime()));
        postBackEntity.setTime(new Time(currentDate.getTime()));
        System.out.println("Setting url");
        postBackEntity.setFullURL(postbackURL);

        if (isPostbackUrlInDB(postBackEntity.getFullURL())) {
            postBackEntity.setDuplicate("FULL");
        }

        if (postBackEntity.getPrefix() == null) {
            System.out.println("No prefix or prefix is wrong");
            System.out.println("Writing to error_log");
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            System.out.println("Setting prefix, afid and clickid");
            postBackEntity.setPrefix("333");
            postBackEntity.setAfid(2);
            postBackEntity.setClickId(MySQLDaoImpl.getInstance().getAffiliateByAffid(2).getAffiseClickid());
            System.out.println("Adding to db");
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);

            System.out.println("Sending to affise");
            AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                    .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
            try {
                System.out.println("sending postback");
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }
            System.out.println("Done");
            return "HTTP/1.1 200 OK\r\n";
        }
        else {
            System.out.println("Writing to postback_log");
            FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            if (postBackEntity.getPrefix().equals("333")) {
                System.out.println("Using affiseHandler");
                affiseHandler(postBackEntity);
                System.out.println("Done");
            }
            else {
                System.out.println("Using binomHandler");
                binomHandler(postBackEntity);
                System.out.println("Done");
            }
        }
        return "HTTP/1.1 200 OK\r\n";
    }

    private boolean isPostbackUrlInDB(String postbackUrl) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        PostBackEntity postBackEntity = mySQLDao.getPostbackByFullUrl(postbackUrl);
        return postBackEntity != null;
    }

    private void binomHandler(PostBackEntity postBackEntity) {
        System.out.println("Creating affise tracker entity");
        AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                .getTrackerByPrefix("333").getDomain() + "/");
        if (!isAffidInAffiliate(postBackEntity.getAfid())) {
            System.out.println("No afid in affiliates");
            System.out.println("Setting afid to 2");
            postBackEntity.setAfid(2);
            System.out.println("Setting clickid");
            postBackEntity.setClickId(MySQLDaoImpl.getInstance()
                    .getAffiliateByAffid(postBackEntity.getAfid()).getAffiseClickid());
            System.out.println("Adding to db");
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            try {
                System.out.println("Sending postback");
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Creating binom entity");
            BinomTracker binomTracker = new BinomTracker(MySQLDaoImpl.getInstance()
                    .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
            try {
                System.out.println("Sending to binom");
                binomTracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }
            System.out.println("Adding to db");
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            System.out.println("Setting clickid");
            postBackEntity.setClickId(MySQLDaoImpl.getInstance()
                    .getAffiliateByAffid(postBackEntity.getAfid()).getAffiseClickid());
            try {
                System.out.println("Sending to affise");
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }

        }
    }

    private void affiseHandler(PostBackEntity postBackEntity) {
        System.out.println("Creating affise tracker entity");
        AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
        if (!isAffidInAffiliate(postBackEntity.getAfid())) {
            System.out.println("No afid in affiliates");
            System.out.println("Setting afid to 2");
            postBackEntity.setAfid(2);
            System.out.println("Setting clickid");
            postBackEntity.setClickId(MySQLDaoImpl.getInstance()
                    .getAffiliateByAffid(postBackEntity.getAfid()).getAffiseClickid());
            System.out.println("Adding to db");
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            try {
                System.out.println("Sending postback");
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(postBackEntity.getAfid());
            System.out.println("Adding to db");
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            try {
                System.out.println("Sending postback");
                tracker.sendPostback(postBackEntity);
            } catch (NoClickIdException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAffidInAffiliate(int affid) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AffiliatesEntity affiliate = mySQLDao.getAffiliateByAffid(affid);
        return affiliate != null;
    }
}
