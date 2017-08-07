package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.AdvertsEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;

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
        Session session = sessionFactory.openSession();
        PostBackEntity postBackEntity = null;
        try {
            postBackEntity = session.createQuery("from PostBackEntity where transactionid=:transactionid", PostBackEntity.class)
                    .setParameter("transactionid", transactionId).getSingleResult();
        } catch (NoResultException e) {
            postBackEntity = null;
        }
        session.close();
        return postBackEntity;
    }
    @Override
    public void addPostback(PostBackEntity postBackEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        if (postBackEntity.getTransactionId() == null) postBackEntity.setTransactionId(postBackEntity.getActionId());
        session.save(postBackEntity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<AdvertsEntity> getAllAdverts() {
        Session session = sessionFactory.openSession();
        List<AdvertsEntity> advertsEntityList
                = session.createQuery("from Adverts", AdvertsEntity.class).getResultList();
        session.close();
        return advertsEntityList;
    }

    @Override
    public List<TrackerEntity> getTrackers() {
        Session session = sessionFactory.openSession();
        List<TrackerEntity> trackerEntityList =
                session.createQuery("from TrackerEntity", TrackerEntity.class).getResultList();
        session.close();
        return trackerEntityList;
    }

    @Override
    public List<AffiliatesEntity> getAffiliates() {
        Session session = sessionFactory.openSession();
        List<AffiliatesEntity> affiliates =
                session.createQuery("from AffiliatesEntity", AffiliatesEntity.class).getResultList();
        session.close();
        return affiliates;
    }

    @Override
    public AffiliatesEntity getAffiliateByAffid(int afid) {
        AffiliatesEntity affiliate;
        Session session = sessionFactory.openSession();
        try {
            affiliate = session.createQuery("from AffiliatesEntity where afid=:afid",
                    AffiliatesEntity.class).setParameter("afid", afid).getSingleResult();
        }
        catch (NoResultException e) {
            affiliate = null;
        }
        session.close();
        return affiliate;
    }

    @Override
    public PostBackEntity getPostbackByClickAndTransactionId(String clickId, String transactionId) {

        Session session = sessionFactory.openSession();
        PostBackEntity postBackEntity;
        try {
            postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId",
                    PostBackEntity.class)
                    .setParameter("clickId", clickId)
                    .setParameter("transactionId", transactionId)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            postBackEntity = null;
        }
        catch (NonUniqueResultException e) {
            postBackEntity = session.createQuery("from PostBackEntity where clickid=:clickId and transactionid=:transactionId",
                    PostBackEntity.class)
                    .setParameter("clickId", clickId)
                    .setParameter("transactionId", transactionId)
                    .getResultList().get(0);
        }
        session.close();
        return postBackEntity;
    }

    @Override
    public PostBackEntity getPostbackByFullUrl(String fullUrl) {
        Session session = sessionFactory.openSession();
        PostBackEntity postBackEntity;
        try {
            postBackEntity = session.createQuery("from PostBackEntity where fullurl=:fullurl",
                    PostBackEntity.class)
                    .setParameter("fullurl", fullUrl)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            postBackEntity = null;
        }
        catch (NonUniqueResultException e) {
            postBackEntity = session.createQuery("from PostBackEntity where fullurl=:fullurl",
                    PostBackEntity.class)
                    .setParameter("fullurl", fullUrl)
                    .getResultList().get(0);
        }
        session.close();
        return postBackEntity;
    }

    @Override
    public TrackerEntity getTrackerByPrefix(String prefix) {
        Session session = sessionFactory.openSession();
        TrackerEntity trackerEntity;
        try {
            trackerEntity = session.createQuery("from TrackerEntity where prefix=:prefix",
                    TrackerEntity.class)
                    .setParameter("prefix", prefix).getSingleResult();
        } catch (NoResultException e) {
            trackerEntity = null;
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
