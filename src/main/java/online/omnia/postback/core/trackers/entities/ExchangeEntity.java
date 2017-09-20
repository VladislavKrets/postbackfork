package online.omnia.postback.core.trackers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lollipop on 20.09.2017.
 */
@Entity
@Table(name = "exchange")
public class ExchangeEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "time")
    private Date time;
    @Column(name = "currency_id")
    private int currencyId;
    @Column(name = "count")
    private int count;
    @Column(name = "rate")
    private double rate;

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public int getCurrency() {
        return currencyId;
    }

    public double getRate() {
        return rate;
    }

    public int getCount() {
        return count;
    }
}
