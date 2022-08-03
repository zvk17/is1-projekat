package alarm;

import alarm.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T09:16:49")
@StaticMetamodel(Alarm_1.class)
public class Alarm_1_ { 

    public static volatile SingularAttribute<Alarm_1, Integer> interval;
    public static volatile SingularAttribute<Alarm_1, User> user;
    public static volatile SingularAttribute<Alarm_1, Integer> idAlarm;
    public static volatile SingularAttribute<Alarm_1, Date> moment;

}