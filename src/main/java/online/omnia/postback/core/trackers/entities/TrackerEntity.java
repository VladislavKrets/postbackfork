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
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "domain", length = 100)
    private String domain;
    @Column(name = "ip", length = 20)
    private String ip;
    @Column(name = "modulename", length = 50)
    private String moduleName;
    @Column(name = "clickid")
    private String clickid;
    @Column(name = "sum")
    private String sum;
    @Column(name = "status1name")
    private String status1Name;
    @Column(name = "status2name")
    private String status2Name;
    @Column(name = "index_php")
    private String indexPhp;
    @Column(name = "click")
    private String click;
    @Column(name = "second_prefix")
    private String secondPrefix;
    @Column(name = "ido_prefix")
    private String idoPrefix;
    public TrackerEntity() {
    }

    public String getIndexPhp() {
        return indexPhp;
    }

    public String getClick() {
        return click;
    }

    public String getPrefix() {
        return prefix;
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

    public String getClickid() {
        return clickid;
    }

    public String getSum() {
        return sum;
    }

    public String getStatus1Name() {
        return status1Name;
    }

    public String getStatus2Name() {
        return status2Name;
    }

    public String getSecondPrefix() {
        return secondPrefix;
    }

    public String getIdoPrefix() {
        return idoPrefix;
    }
}
