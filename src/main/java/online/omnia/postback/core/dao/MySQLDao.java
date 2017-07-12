package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.PostBackEntity;
import online.omnia.postback.core.trackers.entities.RoutingPostbackEntity;
import online.omnia.postback.core.trackers.entities.TrackerEntity;

import java.util.List;

/**
 * Created by lollipop on 12.07.2017.
 */
public interface MySQLDao {
    void addPostback(PostBackEntity postBackEntity);
    List<RoutingPostbackEntity> getAllRoutingPostbacks();
    List<TrackerEntity> getBinomTrackers();
}
