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
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * Method gets event from db
     * @param status postback status
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
     * @param errorPostBackEntity entity of invalid postback
     */
    public synchronized void addErrorPostback(ErrorPostBackEntity errorPostBackEntity) {
        Session session = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                session.beginTransaction();
                session.save(errorPostBackEntity);
                session.getTransaction().commit();
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
    }

    /**
     * Method adds postback to db
     * @param postBackEntity entity which we get after parsing the url
     */
    @Override
    public synchronized void addPostback(PostBackEntity postBackEntity) {
        Session session = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                session.beginTransaction();
                session.save(postBackEntity);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                try {
                    FileWorkingUtils.writeErrorPostback(new java.sql.Date(System.currentTimeMillis()),
                            new Time(System.currentTimeMillis()), postBackEntity.getFullURL());
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
    }

    /**
     * Method gets CurrencyEntity by currency from db
     * @param currency gets from postback for exchanging
     * @return entity of curency from db
     */
    public CurrencyEntity getCurrency(String currency) {
        Session session = null;
        CurrencyEntity currencyEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                currencyEntity = session.createQuery("from CurrencyEntity where code=:currency", CurrencyEntity.class)
                        .setParameter("currency", currency)
                        .getSingleResult();
                break;
            }
            catch (NoResultException e) {
                currencyEntity = null;
            }
            catch (PersistenceException e) {
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
     *
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
     * @param clickId postback parameter
     * @param transactionId postback parameter
     * @param status postback parameter
     * @return postback
     */
    public PostBackEntity getPostbackByClickIdTransactionIdStatus(String clickId, String transactionId, String status) {
        Session session = null;
        if (clickId.isEmpty() || transactionId.isEmpty() || status.isEmpty()) return null;
        List<PostBackEntity> postBackEntities = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();

                    postBackEntities = session.createQuery("from PostbackEntity where clickid=:clickId and transactionid=:transactionId and status=:status",
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

    /**
     * Method gets tracker by prefix from db
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
