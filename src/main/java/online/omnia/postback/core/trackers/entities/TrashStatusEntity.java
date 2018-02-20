package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 22.01.2018.
 */
@Entity
@Table(name = "trash_status")
public class TrashStatusEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    private String status;
    @Column(name = "adv_id")
    private int advId;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getAdvId() {
        return advId;
    }
}
