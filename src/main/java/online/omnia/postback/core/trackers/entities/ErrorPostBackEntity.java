package online.omnia.postback.core.trackers.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lollipop on 18.08.2017.
 */
@Entity
@Table(name = "postback_incorrect")
public class ErrorPostBackEntity extends PostBackEntity{
}
