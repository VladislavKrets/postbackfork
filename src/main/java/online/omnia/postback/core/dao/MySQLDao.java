package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.*;

import java.util.List;

/**
 * Created by lollipop on 12.07.2017.
 */
public interface MySQLDao {
    void addPostback(PostBackEntity postBackEntity);
    List<AdvertsEntity> getAllAdverts();
    List<TrackerEntity> getTrackers();
    List<AffiliatesEntity> getAffiliates();
    TrackerEntity getTrackerByPrefix(String prefix);
    AffiliatesEntity getAffiliateByAffid(int affid);
    PostBackEntity getPostbackByClickId(String clickId);
    PostBackEntity getPostbackByFullUrl(String fullUrl);
}
