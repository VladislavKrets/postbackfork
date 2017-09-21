package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 20.09.2017.
 */
@Entity
@Table(name = "status_events")
public class StatusEventsEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "adv_id")
    private int advId;
    @Column(name = "advname")
    private String advName;
    @Column(name = "status")
    private String status;
    @Column(name = "event_name")
    private String eventName;

    public int getId() {
        return id;
    }

    public int getAdvId() {
        return advId;
    }

    public String getAdvName() {
        return advName;
    }

    public String getStatus() {
        return status;
    }

    public String getEventName() {
        return eventName;
    }
}
