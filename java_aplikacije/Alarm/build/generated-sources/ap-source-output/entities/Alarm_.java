package entities;

import entities.Activity;
import entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-18T16:14:40")
@StaticMetamodel(Alarm.class)
public class Alarm_ { 

    public static volatile SingularAttribute<Alarm, User> idUser;
    public static volatile ListAttribute<Alarm, Activity> activityList;
    public static volatile SingularAttribute<Alarm, Date> datetimeMoment;
    public static volatile SingularAttribute<Alarm, Integer> idAlarm;
    public static volatile SingularAttribute<Alarm, Integer> intervalSeconds;

}