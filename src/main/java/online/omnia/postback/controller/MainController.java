package online.omnia.postback.controller;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.affise.AffiseTracker;
import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.trackers.entities.*;
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
    private PostbackHandler postbackHandler;

    public MainController() {
        postbackHandler = new PostbackHandler();
    }

    public String sendPostback(String postbackURL) {
        currentDate = new java.util.Date(System.currentTimeMillis());
        Map<String, String> parameters = postbackHandler.getPostbackParameters(postbackURL);
        System.out.println("Parameters have been got");
        if (parameters.isEmpty()) {
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
        System.out.println("Creating postback entity");
        PostBackEntity postBackEntity = postbackHandler.fillPostback(parameters);
        System.out.println("Setting date and time");
        postBackEntity.setDate(new Date(currentDate.getTime()));
        postBackEntity.setTime(new Time(currentDate.getTime()));
        System.out.println("Setting url");
        postBackEntity.setFullURL(postbackURL);

        if (isPostbackUrlInDB(postBackEntity.getFullURL())) {
            System.out.println("Postback is FULL");
            postBackEntity.setDuplicate("FULL");
        }
        if (postbackHandler.isEventFilled(postBackEntity)) {
            postBackEntity.setDuplicate("PARTIAL");
        }
        if (MySQLDaoImpl.getInstance().getAffiliateByAffid(postBackEntity.getAfid()) == null) {
            postBackEntity.setAfid(2);
        }

        if (postBackEntity.getPrefix() == null) {
            System.out.println("No prefix or prefix is wrong");
            System.out.println("Writing to error_log");
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            System.out.println("Setting prefix, afid and clickid");
            postBackEntity.setPrefix("333");
            postBackEntity.setAfid(2);
            System.out.println("Adding to db");

            System.out.println("Sending to affise");
            AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                    .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
            try {
                System.out.println("sending postback");
                String answer = tracker.sendPostback(postBackEntity);
                FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                        new Time(currentDate.getTime()), answer);
                setExchange(postBackEntity);
                MySQLDaoImpl.getInstance().addErrorPostback(postbackHandler.createError(postBackEntity));
            } catch (NoClickIdException e) {
                System.out.println("Exception. ClickId is null");
                return "HTTP/1.1 201 error\r\n";
            }
            System.out.println("Done");
            return "HTTP/1.1 200 OK\r\n";
        } else if (isAllEventsEmpty(postBackEntity) && !postBackEntity.getClickId().isEmpty()
                && !postBackEntity.getTransactionId().isEmpty()) {
            FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            return "HTTP/1.1 200 OK\r\n";
        } else {
            System.out.println("Writing to postback_log");
            FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            if (postBackEntity.getPrefix().equals("333")) {
                System.out.println("Using affiseHandler");
                affiseHandler(postBackEntity);
                System.out.println("Done");
            } else {
                System.out.println("Using binomHandler");
                binomHandler(postBackEntity, postbackHandler);
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

    public void binomHandler(PostBackEntity postBackEntity, PostbackHandler postbackHandler) {
        System.out.println("Creating affise tracker entity");
        System.out.println("Creating binom entity");
        BinomTracker binomTracker = new BinomTracker(MySQLDaoImpl.getInstance()
                .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
        try {
            System.out.println("Sending to binom");
            String answer = binomTracker.sendPostback(postBackEntity);
            System.out.println(answer.split(" ")[1]);
            if (answer.split(" ")[1].equals("200")) postBackEntity.setPostbackSend(1); //if answer is ok
            FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), answer);
        } catch (NoClickIdException e) {
            e.printStackTrace();
        }
        System.out.println("Adding to db");
        addingEventToPostback(postBackEntity);
        setExchange(postBackEntity);
        MySQLDaoImpl.getInstance().addPostback(postBackEntity);

    }

    private void affiseHandler(PostBackEntity postBackEntity) {
        System.out.println("Creating affise tracker entity");
        AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
        System.out.println(postBackEntity.getAfid());
        System.out.println("Adding to db");

        try {
            System.out.println("Sending postback");
            String answer = tracker.sendPostback(postBackEntity);
            FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), answer);
            System.out.println(postBackEntity);
            setExchange(postBackEntity);
            MySQLDaoImpl.getInstance().addPostback(postBackEntity);
        } catch (NoClickIdException e) {
            e.printStackTrace();
        }
    }
    private void setExchange(PostBackEntity postBackEntity) {
        CurrencyEntity currencyEntity = MySQLDaoImpl.getInstance().getCurrency(postBackEntity.getCurrency());
        if (currencyEntity == null) currencyEntity = MySQLDaoImpl.getInstance().getCurrency("USD");
        ExchangeEntity exchangeEntity = MySQLDaoImpl.getInstance().getExchange(currencyEntity.getId());
        double sum = (postBackEntity.getSum() / exchangeEntity.getRate()) * 100;
        int tempSum = (int) sum;
        sum = tempSum / 100.0;
        postBackEntity.setSum(sum);
        postBackEntity.setExchange(exchangeEntity.getId());
    }
    private void addingEventToPostback(PostBackEntity postBackEntity) {
        if (!postBackEntity.getAddEvent1().isEmpty()) postBackEntity.setEvent1("+" + postBackEntity.getAddEvent1());
        if (!postBackEntity.getAddEvent2().isEmpty()) postBackEntity.setEvent2("+" + postBackEntity.getAddEvent2());
        if (!postBackEntity.getAddEvent3().isEmpty()) postBackEntity.setEvent3("+" + postBackEntity.getAddEvent3());
        if (!postBackEntity.getAddEvent4().isEmpty()) postBackEntity.setEvent4("+" + postBackEntity.getAddEvent4());
        if (!postBackEntity.getAddEvent5().isEmpty()) postBackEntity.setEvent5("+" + postBackEntity.getAddEvent5());
        if (!postBackEntity.getAddEvent6().isEmpty()) postBackEntity.setEvent6("+" + postBackEntity.getAddEvent6());
        if (!postBackEntity.getAddEvent7().isEmpty()) postBackEntity.setEvent7("+" + postBackEntity.getAddEvent7());
        if (!postBackEntity.getAddEvent8().isEmpty()) postBackEntity.setEvent8("+" + postBackEntity.getAddEvent8());
        if (!postBackEntity.getAddEvent9().isEmpty()) postBackEntity.setEvent9("+" + postBackEntity.getAddEvent9());
        if (!postBackEntity.getAddEvent10().isEmpty()) postBackEntity.setEvent10("+" + postBackEntity.getAddEvent10());
    }

    private boolean isAllEventsEmpty(PostBackEntity postBackEntity) {
        return postBackEntity.getEvent1().isEmpty() && postBackEntity.getEvent2().isEmpty()
                && postBackEntity.getEvent3().isEmpty() && postBackEntity.getEvent4().isEmpty()
                && postBackEntity.getEvent5().isEmpty() && postBackEntity.getEvent6().isEmpty()
                && postBackEntity.getEvent7().isEmpty() && postBackEntity.getEvent8().isEmpty()
                && postBackEntity.getEvent9().isEmpty() && postBackEntity.getEvent10().isEmpty();
    }

    private boolean isAffidInAffiliate(int affid) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AffiliatesEntity affiliate = mySQLDao.getAffiliateByAffid(affid);
        return affiliate != null;
    }
}
