package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.AdvertsEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
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
        sessionFactory = configuration.buildSessionFactory();
    }

    private MySQLDaoImpl() {
    }



    @Override
    public void addPostback(PostBackEntity postBackEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
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
    public TrackerEntity getTrackerByPrefix(int prefix) {
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
