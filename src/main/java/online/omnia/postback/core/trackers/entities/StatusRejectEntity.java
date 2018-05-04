package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 24.03.2018.
 */
@Entity
@Table(name = "status_reject")
public class StatusRejectEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "adv")
    private String adv;
    @Column(name = "status")
    private String status;

    public int getId() {
        return id;
    }

    public String getAdv() {
        return adv;
    }

    public String getStatus() {
        return status;
    }
}
