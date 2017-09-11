package online.omnia.postback.core.trackers.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lollipop on 18.08.2017.
 */
@Table(name = "postback")
@Entity
public class PostBackEntity extends AbstractPostBackEntity implements Cloneable{

    @Override
    public PostBackEntity clone() throws CloneNotSupportedException {
        return (PostBackEntity) super.clone();
    }
}
