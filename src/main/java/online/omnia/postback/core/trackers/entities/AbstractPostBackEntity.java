package online.omnia.postback.core.trackers.entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by lollipop on 11.07.2017.
 */
@MappedSuperclass
public abstract class AbstractPostBackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    private Date date;
    @Column(name = "time")
    private Time time;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "clickid")
    private String clickId;
    @Column(name = "sum")
    private double sum;
    @Column(name = "currency")
    private String currency;
    @Column(name = "goal")
    private String goal;
    @Column(name = "afid")
    private int afid;
    @Column(name = "status")
    private String status;
    @Column(name = "advname")
    private String advName;
    @Column(name = "offername")
    private String offerName;
    @Column(name = "transactionid")
    private String transactionId;
    @Column(name = "idfa")
    private String IDFA;
    @Column(name = "gaid")
    private String GaId;
    @Column(name = "t1")
    private String t1;
    @Column(name = "t2")
    private String t2;
    @Column(name = "t3")
    private String t3;
    @Column(name = "t4")
    private String t4;
    @Column(name = "t5")
    private String t5;
    @Column(name = "t6")
    private String t6;
    @Column(name = "t7")
    private String t7;
    @Column(name = "t8")
    private String t8;
    @Column(name = "t9")
    private String t9;
    @Column(name = "t10")
    private String t10;
    @Column(name = "secretkey")
    private String secretKey;
    @Column(name = "ipaddress")
    private String ipAddress;
    @Column(name = "fullurl")
    private String fullURL;
    @Column(name = "postbacksend")
    private int postbackSend;
    @Column(name = "offerid")
    private String offerId;
    @Column(name = "duplicate")
    private String duplicate;
    @Column(name = "event1")
    private String event1;
    @Column(name = "event2")
    private String event2;
    @Column(name = "event3")
    private String event3;
    @Column(name = "event4")
    private String event4;
    @Column(name = "event5")
    private String event5;
    @Column(name = "event6")
    private String event6;
    @Column(name = "event7")
    private String event7;
    @Column(name = "event8")
    private String event8;
    @Column(name = "event9")
    private String event9;
    @Column(name = "event10")
    private String event10;
    @Transient
    private String actionId;
    @Transient
    private String addEvent1;
    @Transient
    private String addEvent2;
    @Transient
    private String addEvent3;
    @Transient
    private String addEvent4;
    @Transient
    private String addEvent5;
    @Transient
    private String addEvent6;
    @Transient
    private String addEvent7;
    @Transient
    private String addEvent8;
    @Transient
    private String addEvent9;
    @Transient
    private String addEvent10;
    @Column(name = "exchange")
    private int exchange;
    @Column(name = "idc")
    private String idc;
    @Column(name = "ido")
    private String ido;
    @Column(name = "second_prefix")
    private String secondPrefix;
    @Transient
    private String idoPrefix;
    public AbstractPostBackEntity() {
        clickId = "";
        currency = "";
        goal = "";
        status = "";
        advName = "";
        offerName = "";
        transactionId = "";
        IDFA = "";
        GaId = "";
        t1 = "";
        t2 = "";
        t3 = "";
        t4 = "";
        t5 = "";
        t6 = "";
        t7 = "";
        t8 = "";
        t9 = "";
        t10 = "";
        secretKey = "";
        ipAddress = "";
        fullURL = "";
        offerId = "";
        event1 = "";
        event2 = "";
        event3 = "";
        event4 = "";
        event5 = "";
        event6 = "";
        event7 = "";
        event8 = "";
        event9 = "";
        event10 = "";
        addEvent1 = "";
        addEvent2 = "";
        addEvent3 = "";
        addEvent4 = "";
        addEvent5 = "";
        addEvent6 = "";
        addEvent7 = "";
        addEvent8 = "";
        addEvent9 = "";
        addEvent10 = "";
        idc = "";
        ido = "";
        secondPrefix = "";

    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getIdo() {
        return ido;
    }

    public void setIdo(String ido) {
        this.ido = ido;
    }

    public String getSecondPrefix() {
        return secondPrefix;
    }

    public void setSecondPrefix(String secondPrefix) {
        this.secondPrefix = secondPrefix;
    }

    public int getId() {
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

    public String getGoal() {
        return goal;
    }

    public String getStatus() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setIDFA(String IDFA) {
        this.IDFA = IDFA;
    }

    public void setGaId(String gaId) {
        GaId = gaId;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public void setT9(String t9) {
        this.t9 = t9;
    }

    public void setT10(String t10) {
        this.t10 = t10;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public void setPostbackSend(int postbackSend) {
        this.postbackSend = postbackSend;
    }

    public int getAfid() {
        return afid;
    }

    public void setAfid(int afid) {
        this.afid = afid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getEvent1() {
        return event1;
    }

    public void setEvent1(String event1) {
        this.event1 = event1;
    }

    public String getEvent2() {
        return event2;
    }

    public void setEvent2(String event2) {
        this.event2 = event2;
    }

    public String getEvent3() {
        return event3;
    }

    public void setEvent3(String event3) {
        this.event3 = event3;
    }

    public String getEvent4() {
        return event4;
    }

    public void setEvent4(String event4) {
        this.event4 = event4;
    }

    public String getEvent5() {
        return event5;
    }

    public void setEvent5(String event5) {
        this.event5 = event5;
    }

    public String getEvent6() {
        return event6;
    }

    public void setEvent6(String event6) {
        this.event6 = event6;
    }

    public String getEvent7() {
        return event7;
    }

    public void setEvent7(String event7) {
        this.event7 = event7;
    }

    public String getEvent8() {
        return event8;
    }

    public void setEvent8(String event8) {
        this.event8 = event8;
    }

    public String getEvent9() {
        return event9;
    }

    public void setEvent9(String event9) {
        this.event9 = event9;
    }

    public String getEvent10() {
        return event10;
    }

    public void setEvent10(String event10) {
        this.event10 = event10;
    }

    public String getAddEvent1() {
        return addEvent1;
    }

    public void setAddEvent1(String addEvent1) {
        this.addEvent1 = addEvent1;
    }

    public String getAddEvent2() {
        return addEvent2;
    }

    public void setAddEvent2(String addEvent2) {
        this.addEvent2 = addEvent2;
    }

    public String getAddEvent3() {
        return addEvent3;
    }

    public void setAddEvent3(String addEvent3) {
        this.addEvent3 = addEvent3;
    }

    public String getAddEvent4() {
        return addEvent4;
    }

    public void setAddEvent4(String addEvent4) {
        this.addEvent4 = addEvent4;
    }

    public String getAddEvent5() {
        return addEvent5;
    }

    public void setAddEvent5(String addEvent5) {
        this.addEvent5 = addEvent5;
    }

    public String getAddEvent6() {
        return addEvent6;
    }

    public void setAddEvent6(String addEvent6) {
        this.addEvent6 = addEvent6;
    }

    public String getAddEvent7() {
        return addEvent7;
    }

    public void setAddEvent7(String addEvent7) {
        this.addEvent7 = addEvent7;
    }

    public String getAddEvent8() {
        return addEvent8;
    }

    public void setAddEvent8(String addEvent8) {
        this.addEvent8 = addEvent8;
    }

    public String getAddEvent9() {
        return addEvent9;
    }

    public void setAddEvent9(String addEvent9) {
        this.addEvent9 = addEvent9;
    }

    public String getAddEvent10() {
        return addEvent10;
    }

    public void setAddEvent10(String addEvent10) {
        this.addEvent10 = addEvent10;
    }

    @Override
    public String toString() {
        return "PostBackEntity{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", prefix=" + prefix +
                ", clickId='" + clickId + '\'' +
                ", sum=" + sum +
                ", currency='" + currency + '\'' +
                ", goal=" + goal +
                ", afid=" + afid +
                ", status=" + status +
                ", advName='" + advName + '\'' +
                ", offerName='" + offerName + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", IDFA='" + IDFA + '\'' +
                ", GaId='" + GaId + '\'' +
                ", t1='" + t1 + '\'' +
                ", t2='" + t2 + '\'' +
                ", t3='" + t3 + '\'' +
                ", t4='" + t4 + '\'' +
                ", t5='" + t5 + '\'' +
                ", t6='" + t6 + '\'' +
                ", t7='" + t7 + '\'' +
                ", t8='" + t8 + '\'' +
                ", t9='" + t9 + '\'' +
                ", t10='" + t10 + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", fullURL='" + fullURL + '\'' +
                ", postbackSend=" + postbackSend +
                ", offerId='" + offerId + '\'' +
                '}';
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public String getIdoPrefix() {
        return idoPrefix;
    }

    public void setIdoPrefix(String idoPrefix) {
        this.idoPrefix = idoPrefix;
    }
}
