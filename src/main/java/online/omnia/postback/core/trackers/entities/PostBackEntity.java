package online.omnia.postback.core.trackers.entities;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by lollipop on 11.07.2017.
 */
@Table(name = "postback")
@Entity
public class PostBackEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "date")
    @Type(type = "DATE")
    private Date date;
    @Column(name = "time")
    @Type(type = "TIME")
    private Time time;
    @Column(name = "clickid", length = 100)
    private String clickId;
    @Column(name = "sum")
    private double sum;
    @Column(name = "currency", length = 3)
    private String currency;
    @Column(name = "goal")
    private int goal;
    @Column(name = "status")
    private int status;
    @Column(name = "advname", length = 100)
    private String advName;
    @Column(name = "offername", length = 100)
    private String offerName;
    @Column(name = "transactionid", length = 50)
    private String transactionId;
    @Column(name = "idfa", length = 50)
    private String IDFA;
    @Column(name = "gaid", length = 50)
    private String GaId;
    @Column(name = "t1", length = 100)
    private String t1;
    @Column(name = "t2", length = 100)
    private String t2;
    @Column(name = "t3", length = 100)
    private String t3;
    @Column(name = "t4", length = 100)
    private String t4;
    @Column(name = "t5", length = 100)
    private String t5;
    @Column(name = "t6", length = 100)
    private String t6;
    @Column(name = "t7", length = 100)
    private String t7;
    @Column(name = "t8", length = 100)
    private String t8;
    @Column(name = "t9", length = 100)
    private String t9;
    @Column(name = "t10", length = 100)
    private String t10;
    @Column(name = "secret_key", length = 50)
    private String secretKey;
    @Column(name = "ipaddress", length = 20)
    private String ipAddress;
    @Column(name = "full_url", length = 500)
    private String fullURL;
    @Column(name = "postback_send")
    private int postbackSend;

    public PostBackEntity() {
    }

    public PostBackEntity(String id, Date date, Time time, String clickId, double sum, String currency,
                          int goal, int status, String advName, String offerName, String transactionId,
                          String IDFA, String gaId, String t1, String t2, String t3, String t4, String t5,
                          String t6, String t7, String t8, String t9, String t10, String secretKey,
                          String ipAddress, String fullURL, int postbackSend) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.clickId = clickId;
        this.sum = sum;
        this.currency = currency;
        this.goal = goal;
        this.status = status;
        this.advName = advName;
        this.offerName = offerName;
        this.transactionId = transactionId;
        this.IDFA = IDFA;
        GaId = gaId;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
        this.t9 = t9;
        this.t10 = t10;
        this.secretKey = secretKey;
        this.ipAddress = ipAddress;
        this.fullURL = fullURL;
        this.postbackSend = postbackSend;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getClickId() {
        return clickId;
    }

    public double getSum() {
        return sum;
    }

    public String getCurrency() {
        return currency;
    }

    public int getGoal() {
        return goal;
    }

    public int getStatus() {
        return status;
    }

    public String getAdvName() {
        return advName;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getIDFA() {
        return IDFA;
    }

    public String getGaId() {
        return GaId;
    }

    public String getT1() {
        return t1;
    }

    public String getT2() {
        return t2;
    }

    public String getT3() {
        return t3;
    }

    public String getT4() {
        return t4;
    }

    public String getT5() {
        return t5;
    }

    public String getT6() {
        return t6;
    }

    public String getT7() {
        return t7;
    }

    public String getT8() {
        return t8;
    }

    public String getT9() {
        return t9;
    }

    public String getT10() {
        return t10;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getFullURL() {
        return fullURL;
    }

    public int getPostbackSend() {
        return postbackSend;
    }
}
