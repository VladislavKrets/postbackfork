package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.*;
import online.omnia.postback.core.utils.FileWorkingUtils;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Class realizes working with db
 * getting and adding postbacks and invalid postbacks
 * getting trackers
 * getting currency and exchange
 * getting trackers
 */
public class MySQLDaoImpl implements MySQLDao {
    private static Configuration masterDbConfiguration;
    private static SessionFactory masterDbSessionFactory;
    private static Configuration secondDbConfiguration;
    private static SessionFactory secondDbSessionFactory;
    private static MySQLDaoImpl instance;

    static {
        masterDbConfiguration = new Configuration()
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(AdvertsEntity.class)
                .addAnnotatedClass(TrackerEntity.class)
                .addAnnotatedClass(AffiliatesEntity.class)
                .addAnnotatedClass(ErrorPostBackEntity.class)
                .addAnnotatedClass(ExchangeEntity.class)
                .addAnnotatedClass(CurrencyEntity.class)
                .addAnnotatedClass(StatusEventsEntity.class)
                .addAnnotatedClass(PostBackEntity1.class)
                .addAnnotatedClass(TrashStatusEntity.class)
                .addAnnotatedClass(AdvStatusEntity.class)
                .addAnnotatedClass(AdvRejectEntity.class)
                .addAnnotatedClass(StatusRejectEntity.class)
                .configure("/hibernate.cfg.xml");
        /*secondDbConfiguration  = new Configuration()
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(AdvertsEntity.class)
                .addAnnotatedClass(TrackerEntity.class)
                .addAnnotatedClass(AffiliatesEntity.class)
                .addAnnotatedClass(ErrorPostBackEntity.class)
                .configure("/hibernate.cfg.xml");
        */
        Map<String, String> properties = FileWorkingUtils.iniFileReader();

        masterDbConfiguration.setProperty("hibernate.connection.password", properties.get("master_db_password"));
        masterDbConfiguration.setProperty("hibernate.connection.username", properties.get("master_db_username"));
        masterDbConfiguration.setProperty("hibernate.connection.url", properties.get("master_db_url"));
        /*secondDbConfiguration.setProperty("hibernate.connection.password", properties.get("second_db_password"));
        secondDbConfiguration.setProperty("hibernate.connection.username", properties.get("second_db_username"));
        secondDbConfiguration.setProperty("hibernate.connection.url", properties.get("second_db_url"));
*/
        while (true) {
            try {
                masterDbSessionFactory = masterDbConfiguration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {
                try {
                    e.printStackTrace();
                    System.out.println("Can't connect to master db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        /*while (true) {
            try {
                secondDbSessionFactory = secondDbConfiguration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to second db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }*/
    }

    private MySQLDaoImpl() {
    }

    public static SessionFactory getSecondDbSessionFactory() {
        return secondDbSessionFactory;
    }

    public String get780Status(String status, String adv) {
        if (status == null || adv == null) return null;
        Session session = null;
        String st;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                st = session.createQuery("select realStatus from AdvStatusEntity where name=:name and advId=(select id from AdvertsEntity where advShortName=:advname)", String.class)
                        .setParameter("name", status)
                        .setParameter("advname", adv)
                        .getSingleResult();
                break;
            }
            catch (NoResultException e) {
                st = null;
                break;
            }
            catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return st;
    }

    public StatusRejectEntity getStatusReject(String adv, String status) {
        if (adv == null || status == null) return null;
        Session session = null;
        StatusRejectEntity statusRejectEntity;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                statusRejectEntity = session.createQuery("from StatusRejectEntity where adv=:adv and status=:status", StatusRejectEntity.class)
                        .setParameter("adv", adv)
                        .setParameter("status", status)
                        .getSingleResult();
                break;
            }
            catch (NoResultException e) {
                statusRejectEntity = null;
                break;
            }
            catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return statusRejectEntity;
    }

    public AdvRejectEntity getAdvReject(String name, String status) {
        if (name == null || status == null) return null;
        Session session = null;
        AdvRejectEntity advRejectEntity;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                advRejectEntity = session.createQuery("from AdvRejectEntity where name=:name and status=:status", AdvRejectEntity.class)
                        .setParameter("name", name)
                        .setParameter("status", status)
                        .getSingleResult();
                break;
            }
            catch (NoResultException e) {
                advRejectEntity = null;
                break;
            }
            catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return advRejectEntity;
    }

    public TrashStatusEntity getTrashByStatus(String status) {
        Session session = null;
        TrashStatusEntity trashStatusEntity;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                trashStatusEntity = session.createQuery("from TrashStatusEntity where status=:status", TrashStatusEntity.class)
                        .setParameter("status", status).getSingleResult();
                break;
            }
            catch (NoResultException e) {
                trashStatusEntity = null;
                break;
            }
            catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trashStatusEntity;
    }

    public boolean isPostbackWithPrefixAndClickId(String prefix, String clickid) {
        Session session = null;
        List<PostBackEntity> statusEventsEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                statusEventsEntity = session.createQuery("from PostBackEntity where prefix=:prefix and clickid=:clickid", PostBackEntity.class)
                        .setParameter("prefix", prefix)
                        .setParameter("clickid", clickid)
                        .getResultList();
                return !statusEventsEntity.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
    public boolean isPostback1InDb(PostBackEntity1 postBackEntity) {
        Session session = null;
        List<PostBackEntity1> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntities = session.createQuery("from PostBackEntity1 p where clickid=:clickid and date=:date and p.sum=:summ and prefix=:prefix and time=:time", PostBackEntity1.class)
                        .setParameter("clickid", postBackEntity.getClickId())
                        .setParameter("date", postBackEntity.getDate())
                        .setParameter("summ", postBackEntity.getSum())
                        .setParameter("prefix", postBackEntity.getPrefix())
                        .setParameter("time", postBackEntity.getTime())
                        .getResultList();
                return !postBackEntities.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
    public boolean isPostbackErrorInDb(ErrorPostBackEntity postBackEntity) {
        System.out.println(postBackEntity);
        Session session = null;
        List<ErrorPostBackEntity> postBackEntities = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Date date = null;
        try {
             date = dateFormat.parse(dateFormat.format(new Date(postBackEntity.getDate().getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntities = session.createQuery("from ErrorPostBackEntity p where clickid=:clickid and date=:date and p.sum=:summ and prefix=:prefix", ErrorPostBackEntity.class)
                        .setParameter("clickid", postBackEntity.getClickId())
                        .setParameter("date", date)
                        .setParameter("summ", postBackEntity.getSum())
                        .setParameter("prefix", postBackEntity.getPrefix())
                        //.setParameter("time", new Time(date.getTime()))
                        .getResultList();
                System.out.println(postBackEntities);
                return !postBackEntities.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
    public boolean isPostbackInDb(PostBackEntity postBackEntity) {
        Session session = null;
        List<PostBackEntity> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntities = session.createQuery("from PostBackEntity p where clickid=:clickid and date=:date and p.sum=:summ and prefix=:prefix and time=:time", PostBackEntity.class)
                        .setParameter("clickid", postBackEntity.getClickId())
                        .setParameter("date", postBackEntity.getDate())
                        .setParameter("summ", postBackEntity.getSum())
                        .setParameter("prefix", postBackEntity.getPrefix())
                        .setParameter("time", postBackEntity.getTime())
                        .getResultList();
                return !postBackEntities.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
    /**
     * Method gets event from db
     *
     * @param status  postback status
     * @param advName postback advname
     * @return the last event whis is in db
     */
    public StatusEventsEntity getEvent(String status, String advName) {
        Session session = null;
        List<StatusEventsEntity> statusEventsEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                statusEventsEntity = session.createQuery("from StatusEventsEntity where status=:status and advname=:advName", StatusEventsEntity.class)
                        .setParameter("status", status)
                        .setParameter("advName", advName)
                        .getResultList();
                if (statusEventsEntity.isEmpty()) return null;
                return statusEventsEntity.get(statusEventsEntity.size() - 1);
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    /**
     * Method gets postback by transactionid from db
     *
     * @param transactionId postback transactionid
     * @return entity of postback
     */
    public PostBackEntity getPostbackByTransactionId(String transactionId) {
        if (transactionId == null) return null;
        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    postBackEntity = session.createQuery("from PostBackEntity where transactionid=:transactionid", PostBackEntity.class)
                            .setParameter("transactionid", transactionId).getSingleResult();
                } catch (NoResultException e) {
                    postBackEntity = null;
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return postBackEntity;
    }

    /**
     * Method adds invalid postback to db
     *
     * @param errorPostBackEntity entity of invalid postback
     */
    public synchronized void addErrorPostback(ErrorPostBackEntity errorPostBackEntity) {
        Session session = null;
        boolean isWritten = false;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                session.beginTransaction();
                session.save(errorPostBackEntity);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                if (!isWritten) {
                    FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                            new Time(System.currentTimeMillis()), errorPostBackEntity.getFullURL());
                    isWritten = true;
                }
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                        new Time(System.currentTimeMillis()), errorPostBackEntity.getFullURL());
                isWritten = true;
            }
        }
        session.close();
    }

    /**
     * Method adds postback to db
     *
     * @param postBackEntity entity which we get after parsing the url
     */
    @Override
    public synchronized void addPostback(PostBackEntity postBackEntity) {
        Session session = null;
        boolean isWritten = false;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                session.beginTransaction();
                session.save(postBackEntity);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                try {
                    if (!isWritten) {
                        FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                                new Time(System.currentTimeMillis()), postBackEntity.getFullURL());
                        isWritten = true;
                    }
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                        new Time(System.currentTimeMillis()), postBackEntity.getFullURL());
                isWritten = true;
            }
        }
        session.close();
    }

    /**
     * Method gets CurrencyEntity by currency from db
     *
     * @param currency gets from postback for exchanging
     * @return entity of curency from db
     */
    public CurrencyEntity getCurrency(String currency) {
        Session session = null;
        CurrencyEntity currencyEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                currencyEntity = session.createQuery("from CurrencyEntity where code like :currency", CurrencyEntity.class)
                        .setParameter("currency", currency)
                        .getSingleResult();
                break;
            } catch (NoResultException e) {
                currencyEntity = null;
                break;
            } catch (PersistenceException e) {
                e.printStackTrace();
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return currencyEntity;
    }

    /**
     * @param id of currency entity
     * @return exchange entity
     */
    public ExchangeEntity getExchange(int id) {
        Session session = null;
        ExchangeEntity exchangeEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                exchangeEntity = session.createQuery("from ExchangeEntity where currency_id=:currencyId and time=(select max(time) from ExchangeEntity where currency_id=:currencyId)", ExchangeEntity.class)
                        .setParameter("currencyId", id)
                        .getSingleResult();

                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return exchangeEntity;
    }

    /**
     * Method gets adverts from db
     *
     * @return list of adverts
     */
    @Override
    public List<AdvertsEntity> getAllAdverts() {

        Session session = null;
        List<AdvertsEntity> advertsEntityList
                = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                advertsEntityList = session.createQuery("from AdvertsEntity", AdvertsEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return advertsEntityList;
    }

    /**
     * Method gets trackers of db
     *
     * @return list of trackers
     */
    @Override
    public List<TrackerEntity> getTrackers() {
        Session session = null;
        List<TrackerEntity> trackerEntityList = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                trackerEntityList = session.createQuery("from TrackerEntity", TrackerEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trackerEntityList;
    }

    /**
     * Method gets affiliates from db
     *
     * @return list affiliates
     */
    @Override
    public List<AffiliatesEntity> getAffiliates() {
        Session session = null;
        List<AffiliatesEntity> affiliates = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                affiliates = session.createQuery("from AffiliatesEntity", AffiliatesEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return affiliates;
    }

    /**
     * Method gets affiliate by afid from db
     *
     * @param afid parameter from postback
     * @return affilliate
     */
    @Override
    public AffiliatesEntity getAffiliateByAffid(int afid) {
        AffiliatesEntity affiliate = null;
        Session session = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    affiliate = session.createQuery("from AffiliatesEntity where afid=:afid",
                            AffiliatesEntity.class).setParameter("afid", afid).getSingleResult();
                } catch (NoResultException e) {
                    affiliate = null;
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return affiliate;
    }

    /**
     * Method gets postback by clickid
     *
     * @param clickId parameter from postback
     * @return postback entity
     */
    @Override
    public PostBackEntity getPostbackByClickId(String clickId) {

        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId",
                            PostBackEntity.class)
                            .setParameter("clickId", clickId)
                            .getSingleResult();
                } catch (NoResultException e) {
                    postBackEntity = null;
                } catch (NonUniqueResultException e) {
                    postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId",
                            PostBackEntity.class)
                            .setParameter("clickId", clickId)
                            .getResultList().get(0);
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return postBackEntity;
    }

    /**
     * Method gets postback from db by url
     *
     * @param fullUrl url of postback
     * @return postback
     */
    @Override
    public PostBackEntity getPostbackByFullUrl(String fullUrl) {
        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    postBackEntity = session.createQuery("from PostBackEntity where fullurl=:fullurl",
                            PostBackEntity.class)
                            .setParameter("fullurl", fullUrl)
                            .getSingleResult();
                } catch (NoResultException e) {
                    postBackEntity = null;
                } catch (NonUniqueResultException e) {
                    postBackEntity = session.createQuery("from PostBackEntity where fullurl=:fullurl",
                            PostBackEntity.class)
                            .setParameter("fullurl", fullUrl)
                            .getResultList().get(0);
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
        session.close();
        return postBackEntity;
    }

    /**
     * Method gets postback by clickid, transactionid and status from db
     *
     * @param clickId       postback parameter
     * @param transactionId postback parameter
     * @param status        postback parameter
     * @return postback
     */
    public PostBackEntity getPostbackByClickIdTransactionIdStatus(String clickId, String transactionId, String status) {
        Session session = null;
        if (clickId.isEmpty() || transactionId.isEmpty() || status.isEmpty()) return null;
        List<PostBackEntity> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();

                postBackEntities = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId and status=:status",
                        PostBackEntity.class)
                        .setParameter("clickId", clickId)
                        .setParameter("transactionId", transactionId)
                        .setParameter("status", status)
                        .getResultList();
                if (postBackEntities.isEmpty()) return null;
                session.close();
                return postBackEntities.get(0);
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public boolean isPostbackByClickidAndStatus(String clickid, String status) {
        Session session = null;
        List<PostBackEntity> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntities = session.createQuery("from PostBackEntity where clickid=:clickId and status=:status", PostBackEntity.class)
                        .setParameter("clickId", clickid)
                        .setParameter("status", status)
                        .getResultList();
                System.out.println(postBackEntities);
                return !postBackEntities.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public boolean isPostbackByClickidAndTransactonId(String clickid, String transactionId) {
        Session session = null;
        List<PostBackEntity> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntities = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId", PostBackEntity.class)
                        .setParameter("clickId", clickid)
                        .setParameter("transactionId", transactionId)
                        .getResultList();
                return !postBackEntities.isEmpty();
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public List<TrackerEntity> getTrackerWithSecondPrefix(String prefix, String idcPrefix, String idoPrefix) {
        Session session = null;
        List<TrackerEntity> trackerEntities;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                Query query = session.createQuery("from TrackerEntity where prefix=:prefix" +
                        (idcPrefix.isEmpty() ? "" : " and idc_prefix=:idcPrefix")
                        + (idoPrefix.isEmpty() ? "" : " and ido_prefix=:idoPrefix"), TrackerEntity.class)
                        .setParameter("prefix", prefix);
                if (!idcPrefix.isEmpty()) {
                    query = query.setParameter("idcPrefix", idcPrefix);
                }
                if (!idoPrefix.isEmpty()) {
                    query = query.setParameter("idoPrefix", idoPrefix);
                }
                trackerEntities = query.getResultList();
                session.close();
                return trackerEntities;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void addPostback1(PostBackEntity1 postBackEntity1) {
        Session session = null;
        boolean isWritten = false;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                session.beginTransaction();
                session.save(postBackEntity1);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                try {
                    if (!isWritten) {
                        FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                                new Time(System.currentTimeMillis()), postBackEntity1.getFullURL());
                        isWritten = true;
                    }
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                FileWorkingUtils.writeNotSentPostback(new java.sql.Date(System.currentTimeMillis()),
                        new Time(System.currentTimeMillis()), postBackEntity1.getFullURL());
                isWritten = true;
            }
        }
        session.close();
    }

    /**
     * Method gets tracker by prefix from db
     *
     * @param prefix postback parameter
     * @return tracker
     */
    @Override
    public TrackerEntity getTrackerByPrefix(String prefix) {
        Session session = null;
        TrackerEntity trackerEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    trackerEntity = session.createQuery("from TrackerEntity where prefix=:prefix",
                            TrackerEntity.class)
                            .setParameter("prefix", prefix).getSingleResult();
                } catch (NoResultException e) {
                    trackerEntity = null;
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trackerEntity;
    }

    public static SessionFactory getMasterDbSessionFactory() {
        return masterDbSessionFactory;
    }

    public static synchronized MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
