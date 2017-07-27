package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 15.07.2017.
 */
@Entity
@Table(name = "affiliates")
public class AffiliatesEntity {
    @Id
    @Column(name = "afid")
    private int afid;
    @Column(name = "afname")
    private String afName;
    @Column(name = "afaffiseid")
    private String afaffiseId;
    @Column(name = "description")
    private String description;
    @Column(name = "affise_clickid")
    private String affiseClickid;

    public int getAfid() {
        return afid;
    }

    public String getAfName() {
        return afName;
    }

    public String getAfaffiseId() {
        return afaffiseId;
    }

    public String getDescription() {
        return description;
    }

    public String getAffiseClickid() {
        return affiseClickid;
    }
}
