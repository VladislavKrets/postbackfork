package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 12.07.2017.
 */
@Table(name = "trackers")
@Entity
public class TrackerEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 20)
    private String name;
    @Column(name = "domain", length = 100)
    private String domain;
    @Column(name = "ip", length = 20)
    private String ip;
    @Column(name = "module_name", length = 50)
    private String moduleName;

    public TrackerEntity() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getIp() {
        return ip;
    }

    public String getModuleName() {
        return moduleName;
    }
}
