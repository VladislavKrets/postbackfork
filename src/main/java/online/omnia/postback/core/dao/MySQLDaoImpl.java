package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.RoutingPostbackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by lollipop on 12.07.2017.
 */
public class MySQLDaoImpl implements MySQLDao{
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static MySQLDaoImpl instance;

    static {
        configuration = new Configuration()
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(RoutingPostbackEntity.class)
                .addAnnotatedClass(TrackerEntity.class)
                .configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    private MySQLDaoImpl() {}

    @Override
    public void addPostback(PostBackEntity postBackEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(postBackEntity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<RoutingPostbackEntity> getAllRoutingPostbacks() {
        Session session = sessionFactory.openSession();
        List<RoutingPostbackEntity> routingPostbackEntityList
                = session.createQuery("from RoutingPostbackEntity", RoutingPostbackEntity.class).getResultList();
        session.close();
        return routingPostbackEntityList;
    }

    @Override
    public List<TrackerEntity> getBinomTrackers() {
        Session session = sessionFactory.openSession();
        List<TrackerEntity> trackerEntityList =
                session.createQuery("from TrackerEntity", TrackerEntity.class).getResultList();
        session.close();
        return trackerEntityList;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
