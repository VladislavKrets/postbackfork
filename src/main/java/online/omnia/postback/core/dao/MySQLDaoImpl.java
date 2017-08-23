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
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class MySQLDaoImpl implements MySQLDao {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static MySQLDaoImpl instance;

    static {
        configuration = new Configuration()
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(AdvertsEntity.class)
                .addAnnotatedClass(TrackerEntity.class)
                .addAnnotatedClass(AffiliatesEntity.class)
                .addAnnotatedClass(ErrorPostBackEntity.class)
                .configure("/hibernate.cfg.xml");
        Map<String, String> properties = FileWorkingUtils.iniFileReader();
        configuration.setProperty("hibernate.connection.password", properties.get("password"));
        configuration.setProperty("hibernate.connection.username", properties.get("username"));
        configuration.setProperty("hibernate.connection.url", properties.get("url"));
        while (true) {
            try {
                sessionFactory = configuration.buildSessionFactory();
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

    }

    private MySQLDaoImpl() {
    }


    public PostBackEntity getPostbackByTransactionId(String transactionId) {
        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                postBackEntity = null;
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
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return postBackEntity;
    }
    public synchronized void addErrorPostback(ErrorPostBackEntity errorPostBackEntity) {
        Session session = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();
                session.save(errorPostBackEntity);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
    }
    @Override
    public synchronized void addPostback(PostBackEntity postBackEntity) {
        Session session = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();
                if (postBackEntity.getTransactionId() == null)
                    postBackEntity.setTransactionId(postBackEntity.getActionId());
                session.save(postBackEntity);
                session.getTransaction().commit();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
    }

    @Override
    public List<AdvertsEntity> getAllAdverts() {

        Session session = null;
        List<AdvertsEntity> advertsEntityList
                = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                advertsEntityList = session.createQuery("from Adverts", AdvertsEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return advertsEntityList;
    }

    @Override
    public List<TrackerEntity> getTrackers() {
        Session session = null;
        List<TrackerEntity> trackerEntityList = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                trackerEntityList = session.createQuery("from TrackerEntity", TrackerEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trackerEntityList;
    }

    @Override
    public List<AffiliatesEntity> getAffiliates() {
        Session session = null;
        List<AffiliatesEntity> affiliates = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                affiliates = session.createQuery("from AffiliatesEntity", AffiliatesEntity.class).getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return affiliates;
    }

    @Override
    public AffiliatesEntity getAffiliateByAffid(int afid) {
        AffiliatesEntity affiliate = null;
        Session session = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
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
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return affiliate;
    }

    @Override
    public PostBackEntity getPostbackByClickAndTransactionId(String clickId, String transactionId) {

        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
                try {
                    postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId",
                            PostBackEntity.class)
                            .setParameter("clickId", clickId)
                            .setParameter("transactionId", transactionId)
                            .getSingleResult();
                } catch (NoResultException e) {
                    postBackEntity = null;
                } catch (NonUniqueResultException e) {
                    postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId",
                            PostBackEntity.class)
                            .setParameter("clickId", clickId)
                            .setParameter("transactionId", transactionId)
                            .getResultList().get(0);
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return postBackEntity;
    }

    @Override
    public PostBackEntity getPostbackByFullUrl(String fullUrl) {
        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
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
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
        session.close();
        return postBackEntity;
    }

    @Override
    public TrackerEntity getTrackerByPrefix(String prefix) {
        Session session = null;
        TrackerEntity trackerEntity = null;
        while (true) {
            try {
                session = sessionFactory.openSession();
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
                    Thread.sleep(600);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trackerEntity;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static synchronized MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
