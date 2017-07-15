package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.AffiliatesEntity;
import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.AdvertsEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import online.omnia.postback.core.utils.FileWorkingUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
                .configure("hibernate.cfg.xml");
        Map<String, String> properties = FileWorkingUtils.iniFileReader();
        configuration.setProperty("hibernate.connection.password", properties.get("password"));
        configuration.setProperty("hibernate.connection.username", properties.get("username"));

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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
