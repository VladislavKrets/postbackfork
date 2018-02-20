package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 20.02.2018.
 */
@Entity
@Table(name = "adv_reject")
public class AdvRejectEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private String status;
    @Column(name = "new_status")
    private String newStatus;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
