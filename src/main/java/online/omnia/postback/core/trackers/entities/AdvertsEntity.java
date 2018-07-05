package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 12.07.2017.
 */
@Table(name = "adverts")
@Entity
public class AdvertsEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "advname", length = 100)
    private String advName;
    @Column(name = "advshortname")
    private String advShortName;
    @Column(name = "secretkey")
    private String secretKey;

    public String getAdvShortName() {
        return advShortName;
    }

    public int getId() {
        return id;
    }

    public String getAdvName() {
        return advName;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
