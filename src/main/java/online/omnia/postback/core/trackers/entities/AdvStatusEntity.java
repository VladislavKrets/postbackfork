package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 10.05.2018.
 */
@Entity
@Table(name = "adv_status")
public class AdvStatusEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "adv_id")
    private int advId;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "real_status")
    private String realStatus;

    public int getId() {
        return id;
    }

    public int getAdvId() {
        return advId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRealStatus() {
        return realStatus;
    }
}
