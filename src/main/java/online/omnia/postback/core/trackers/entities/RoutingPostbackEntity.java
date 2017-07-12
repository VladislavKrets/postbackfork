package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 12.07.2017.
 */
@Table(name = "postback_routing")
@Entity
public class RoutingPostbackEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "advname", length = 100)
    private String advName;
    @Column(name = "tracker_id")
    private int trackerId;
    @Column(name = "secret_key")
    private String secretKey;

    public int getId() {
        return id;
    }

    public String getAdvName() {
        return advName;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
