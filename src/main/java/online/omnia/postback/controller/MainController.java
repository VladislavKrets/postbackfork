package online.omnia.postback.controller;

import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.trackers.affise.AffiseTracker;
import online.omnia.postback.core.trackers.binom.BinomTracker;
import online.omnia.postback.core.trackers.entities.*;
import online.omnia.postback.core.trackers.mytds.MytdsTracker;
import online.omnia.postback.core.utils.FileWorkingUtils;
import online.omnia.postback.core.utils.PostbackHandler;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

/**
 * This class contains the main logic of application
 */
public class MainController {
    final static Logger logger = Logger.getLogger(MainController.class);
    private java.util.Date currentDate;
    private PostbackHandler postbackHandler;

    public MainController() {
        postbackHandler = new PostbackHandler();
    }

    /**
     * Main method which gets postbackURL and sending postback to trackers and db
     * parses url, creates PostBackEntity
     * validates postbacks
     * sends postbacks to db and to trackers
     *
     * @param postbackURL url of postback which we get from socket
     * @return serverAnswer
     */
    public String sendPostback(String postbackURL) {
        String[] urlParams = postbackURL.split(" ");
        postbackURL = urlParams[0];
        //String ip = urlParams[1];
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
        PostBackEntity postBackEntity = null;
        try {
            postBackEntity = postbackHandler.fillPostback(parameters);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(postBackEntity);
        System.out.println("Setting date and time");
        postBackEntity.setDate(new Date(currentDate.getTime()));
        postBackEntity.setTime(new Time(currentDate.getTime()));
        System.out.println("Setting url");
        postBackEntity.setFullURL(postbackURL);
        //postBackEntity.setIpAddress(ip);
        System.out.println(postBackEntity);
        System.out.println(postBackEntity.getPrefix());
        if (postbackHandler.isEventFilled(postBackEntity)) {
            postBackEntity.setDuplicate("PARTIAL");
        }
        System.out.println(postBackEntity);
        if (postBackEntity.getPrefix() == null) {
            System.out.println("No prefix or prefix is wrong");
            System.out.println("Writing to error_log");
            FileWorkingUtils.writeErrorPostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            System.out.println("Setting prefix, afid and clickid");
            postBackEntity.setPrefix("");
            System.out.println("Adding to db");
            setExchange(postBackEntity);
            addPostBack(postbackHandler.createError(postBackEntity));

            System.out.println("Done");
            return "HTTP/1.1 200 OK\r\n";
        } else if (!checkDuplicates(postBackEntity)) {
            System.out.println("CheckDuplicates");
            System.out.println(postBackEntity);
            FileWorkingUtils.writePostback(new Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postbackURL);
            addingEventToPostback(postBackEntity);
            CurrencyEntity currencyEntity = MySQLDaoImpl.getInstance().getCurrency(postBackEntity.getCurrency());
            if (currencyEntity == null) currencyEntity = MySQLDaoImpl.getInstance().getCurrency("USD");
            ExchangeEntity exchangeEntity = MySQLDaoImpl.getInstance().getExchange(currencyEntity.getId());
            postBackEntity.setExchange(exchangeEntity.getId());
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
                binomHandler(postBackEntity);
                System.out.println("Done");
            }
        }
        return "HTTP/1.1 200 OK\r\n";
    }

    /**
     * Method checks is postback in db by url
     *
     * @param postbackUrl url of postback which we get from socket
     * @return result of checking
     */
    private boolean isPostbackUrlInDB(String postbackUrl) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        PostBackEntity postBackEntity = mySQLDao.getPostbackByFullUrl(postbackUrl);
        return postBackEntity != null;
    }

    /**
     * Method which send binom postback to db and to binom
     *
     * @param postBackEntity entity which we get after parsing the url
     */
    public void binomHandler(PostBackEntity postBackEntity) {
        System.out.println("Creating affise tracker entity");
        System.out.println("Creating binom entity");
        try {
            PostBackEntity clone = postBackEntity.clone();
            setExchange(clone);
            postBackEntity.setExchange(clone.getExchange());
            System.out.println("Sending to binom");
            if (postBackEntity.getStatus() == null || MySQLDaoImpl.getInstance().getTrashByStatus(postBackEntity.getStatus()) == null) {
                AdvRejectEntity advRejectEntity = MySQLDaoImpl.getInstance().getAdvReject(postBackEntity.getAdvName(), postBackEntity.getStatus());
                if (advRejectEntity != null) clone.setStatus(advRejectEntity.getNewStatus());
                if (MySQLDaoImpl.getInstance().getStatusReject(postBackEntity.getAdvName(), postBackEntity.getStatus()) == null) {
                    String answer = null;
                    switch (postBackEntity.getPrefix()) {
                        case "780" : {
                            MytdsTracker mytdsTracker = new MytdsTracker(MySQLDaoImpl.getInstance()
                                    .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
                            String stat = MySQLDaoImpl.getInstance().get780Status(clone.getStatus(), clone.getAdvName());
                            clone.setStatus(stat);
                            if (stat != null && stat.equals("approved")) {
                                answer = mytdsTracker.sendPostback(clone);
                            }
                            break;
                        }
                        default: {
                            BinomTracker binomTracker = new BinomTracker(MySQLDaoImpl.getInstance()
                                    .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
                            answer = binomTracker.sendPostback(clone);
                            break;
                        }
                    }
                    if (answer != null) {
                        System.out.println(answer.split(" ")[1]);
                        if (answer.split(" ")[1].equals("200")) postBackEntity.setPostbackSend(1); //if answer is ok
                        FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                                new Time(currentDate.getTime()), answer);
                    }
                }
            }
            else FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), postBackEntity.getFullURL());
        } catch (NoClickIdException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Adding to db");
        addingEventToPostback(postBackEntity);

        if (MySQLDaoImpl.getInstance().getAffiliateByAffid(postBackEntity.getAfid()) != null) {
            if (!postBackEntity.getIdc().isEmpty() || !postBackEntity.getIdo().isEmpty()) {
                List<TrackerEntity> trackerEntity = MySQLDaoImpl.getInstance().getTrackerWithSecondPrefix(postBackEntity.getPrefix(),
                        postBackEntity.getIdcPrefix(), postBackEntity.getIdoPrefix());
                if (!trackerEntity.isEmpty()) {
                    TrackerEntity entity = trackerEntity.get(0);
                    System.out.println(entity);
                    addPostBack(postBackEntity);

                } else addPostBack(postbackHandler.createPostbackEntity1(postBackEntity));
            } else addPostBack(postBackEntity);
        } else addPostBack(postbackHandler.createError(postBackEntity));

    }

    private void addPostBack(AbstractPostBackEntity postBackEntity) {
        if (postBackEntity instanceof PostBackEntity) {
            PostBackEntity entity = (PostBackEntity) postBackEntity;
            MySQLDaoImpl.getInstance().addPostback(entity);

        } else if (postBackEntity instanceof PostBackEntity1) {
            PostBackEntity1 entity = (PostBackEntity1) postBackEntity;
            MySQLDaoImpl.getInstance().addPostback1(entity);

        } else if (postBackEntity instanceof ErrorPostBackEntity) {
            ErrorPostBackEntity entity = (ErrorPostBackEntity) postBackEntity;
            MySQLDaoImpl.getInstance().addErrorPostback(entity);

        }

    }

    /**
     * Method which send affise postback to db and to binom
     *
     * @param postBackEntity entity which we get after parsing the url
     */
    private void affiseHandler(PostBackEntity postBackEntity) {
        System.out.println("Creating affise tracker entity");
        AffiseTracker tracker = new AffiseTracker(MySQLDaoImpl.getInstance()
                .getTrackerByPrefix(postBackEntity.getPrefix()).getDomain() + "/");
        System.out.println(postBackEntity.getAfid());
        System.out.println("Adding to db");

        try {
            System.out.println("Sending postback");
            PostBackEntity clone = postBackEntity.clone();
            setExchange(clone);
            String answer = tracker.sendPostback(clone);
            FileWorkingUtils.writePostback(new java.sql.Date(currentDate.getTime()),
                    new Time(currentDate.getTime()), answer);
            System.out.println(postBackEntity);

            if (MySQLDaoImpl.getInstance().getAffiliateByAffid(postBackEntity.getAfid()) != null) {
                MySQLDaoImpl.getInstance().addPostback(postBackEntity);
            } else MySQLDaoImpl.getInstance().addErrorPostback(postbackHandler.createError(postBackEntity));
        } catch (NoClickIdException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which checks postback status, advname and changes events if it necessary
     *
     * @param postBackEntity entity which we get after parsing the url
     */
    private void checkPostbackStatus(PostBackEntity postBackEntity) {
        if (postBackEntity.getStatus().isEmpty() || postBackEntity.getAdvName().isEmpty() || postBackEntity.getSum() == 0)
            return;
        StatusEventsEntity statusEventsEntity = MySQLDaoImpl.getInstance()
                .getEvent(postBackEntity.getStatus(), postBackEntity.getAdvName());
        if (statusEventsEntity == null) return;
        chooseEvent(postBackEntity, statusEventsEntity);
        postBackEntity.setDuplicate("PARTIAL");
    }

    /**
     * Method which chooses event and changes it
     *
     * @param postBackEntity     entity which we get after parsing the url
     * @param statusEventsEntity entity which we get by status and advname from db
     */
    private void chooseEvent(PostBackEntity postBackEntity, StatusEventsEntity statusEventsEntity) {
        int event;
        switch (statusEventsEntity.getEventName()) {
            case "event1":
                if (!postBackEntity.getEvent1().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent1());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent1(String.valueOf(event));
                } else postBackEntity.setEvent1(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event2":
                if (!postBackEntity.getEvent2().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent2());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent2(String.valueOf(event));
                } else postBackEntity.setEvent2(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event3":
                if (!postBackEntity.getEvent3().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent3());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent3(String.valueOf(event));
                } else postBackEntity.setEvent3(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event4":
                if (!postBackEntity.getEvent4().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent4());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent4(String.valueOf(event));
                } else postBackEntity.setEvent4(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event5":
                if (!postBackEntity.getEvent5().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent5());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent5(String.valueOf(event));
                } else postBackEntity.setEvent5(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event6":
                if (!postBackEntity.getEvent6().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent6());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent6(String.valueOf(event));
                } else postBackEntity.setEvent6(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event7":
                if (!postBackEntity.getEvent7().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent7());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent7(String.valueOf(event));
                } else postBackEntity.setEvent7(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event8":
                if (!postBackEntity.getEvent8().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent8());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent8(String.valueOf(event));
                } else postBackEntity.setEvent8(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event9":
                if (!postBackEntity.getEvent9().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent9());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent9(String.valueOf(event));
                } else postBackEntity.setEvent9(String.valueOf((int) postBackEntity.getSum()));
                break;
            case "event10":
                if (!postBackEntity.getEvent10().isEmpty()) {
                    event = Integer.parseInt(postBackEntity.getEvent10());
                    event += postBackEntity.getSum();
                    postBackEntity.setEvent10(String.valueOf(event));
                } else postBackEntity.setEvent10(String.valueOf((int) postBackEntity.getSum()));
                break;
        }
    }

    /**
     * Method which realized exchanging currency to USD
     *
     * @param postBackEntity entity which we get after parsing the url
     */
    public void setExchange(PostBackEntity postBackEntity) {
        CurrencyEntity currencyEntity = MySQLDaoImpl.getInstance().getCurrency(postBackEntity.getCurrency());
        if (currencyEntity == null) currencyEntity = MySQLDaoImpl.getInstance().getCurrency("USD");
        ExchangeEntity exchangeEntity = MySQLDaoImpl.getInstance().getExchange(currencyEntity.getId());
        double sum = (postBackEntity.getSum() / exchangeEntity.getRate() / currencyEntity.getCount()) * 10000;
        int tempSum = (int) sum;
        sum = tempSum / 10000.0;
        postBackEntity.setSum(sum);
        postBackEntity.setExchange(exchangeEntity.getId());
    }

    /**
     * Method which handles events before sending to db
     *
     * @param postBackEntity entity which we get after parsing the url
     */
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

    /**
     * Method which checks if events are empty
     *
     * @param postBackEntity entity which we get after parsing the url
     * @return result of checking events
     */
    private boolean isAllEventsEmpty(PostBackEntity postBackEntity) {
        return postBackEntity.getEvent1().isEmpty() && postBackEntity.getEvent2().isEmpty()
                && postBackEntity.getEvent3().isEmpty() && postBackEntity.getEvent4().isEmpty()
                && postBackEntity.getEvent5().isEmpty() && postBackEntity.getEvent6().isEmpty()
                && postBackEntity.getEvent7().isEmpty() && postBackEntity.getEvent8().isEmpty()
                && postBackEntity.getEvent9().isEmpty() && postBackEntity.getEvent10().isEmpty();
    }

    /**
     * Method checks is affid in db
     *
     * @param afid the value which contains in postback
     * @return result of checking
     */
    private boolean isAffidInAffiliate(int afid) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AffiliatesEntity affiliate = mySQLDao.getAffiliateByAffid(afid);
        return affiliate != null;
    }

    private boolean checkDuplicates(PostBackEntity postBackEntity) {

        if (!isAllEventsEmpty(postBackEntity)) {
            if (!postBackEntity.getClickId().isEmpty() && !postBackEntity.getTransactionId().isEmpty()) {
                if (MySQLDaoImpl.getInstance().isPostbackByClickidAndTransactonId(postBackEntity.getClickId(),
                        postBackEntity.getTransactionId())) {
                    postBackEntity.setDuplicate("PARTIAL");
                    return true;
                }
            } else if (!postBackEntity.getClickId().isEmpty()) {
                if (MySQLDaoImpl.getInstance().getPostbackByClickId(postBackEntity.getClickId()) != null) {
                    postBackEntity.setDuplicate("PARTIAL");
                    return true;
                }
            }
        } else if (!postBackEntity.getClickId().isEmpty() && !postBackEntity.getTransactionId().isEmpty() &&
                !postBackEntity.getStatus().isEmpty()) {
            if (MySQLDaoImpl.getInstance().getPostbackByClickIdTransactionIdStatus(postBackEntity.getClickId(),
                    postBackEntity.getTransactionId(), postBackEntity.getStatus()) != null) {
                postBackEntity.setDuplicate("FULL");
                return false;
            } else if (MySQLDaoImpl.getInstance().isPostbackByClickidAndTransactonId(postBackEntity.getClickId(),
                    postBackEntity.getTransactionId())) {
                postBackEntity.setDuplicate("PARTIAL");
                return true;
            }
        } else if (!postBackEntity.getClickId().isEmpty() && !postBackEntity.getStatus().isEmpty()) {
            if (!postBackEntity.getStatus().isEmpty() && MySQLDaoImpl.getInstance().isPostbackByClickidAndStatus(postBackEntity.getClickId(), postBackEntity.getStatus())) {
                System.out.println(postBackEntity);
                System.out.println("FULL");
                postBackEntity.setDuplicate("FULL");
                return false;
            } else if (MySQLDaoImpl.getInstance().getPostbackByClickId(postBackEntity.getClickId()) != null) {
                postBackEntity.setDuplicate("PARTIAL");
                System.out.println(postBackEntity);
                System.out.println("PARTIAL");
                return true;
            }

        } else if (!postBackEntity.getClickId().isEmpty() && !postBackEntity.getTransactionId().isEmpty()) {
            if (MySQLDaoImpl.getInstance().isPostbackByClickidAndTransactonId(postBackEntity.getClickId(), postBackEntity.getTransactionId())) {
                postBackEntity.setDuplicate("FULL");
                return false;
            } else if (MySQLDaoImpl.getInstance().getPostbackByClickId(postBackEntity.getClickId()) != null) {
                postBackEntity.setDuplicate("PARTIAL");
                return true;
            }
        }
        /*else if (MySQLDaoImpl.getInstance().isPostbackWithPrefixAndClickId(postBackEntity.getPrefix(), postBackEntity.getClickId())) {
            postBackEntity.setDuplicate("PARTIAL");
        }*/
        else if (!postBackEntity.getClickId().isEmpty()) {
            if (MySQLDaoImpl.getInstance().getPostbackByClickId(postBackEntity.getClickId()) != null) {
                postBackEntity.setDuplicate("FULL");
                return false;
            }
        }

        return true;
    }
}
