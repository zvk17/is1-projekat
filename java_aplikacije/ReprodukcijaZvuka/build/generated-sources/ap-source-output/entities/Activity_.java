package entities;

import entities.Alarm;
import entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-20T14:17:00")
@StaticMetamodel(Activity.class)
public class Activity_ { 

    public static volatile SingularAttribute<Activity, User> idUser;
    public static volatile SingularAttribute<Activity, Date> startDateTime;
    public static volatile SingularAttribute<Activity, Integer> durationSeconds;
    public static volatile SingularAttribute<Activity, String> destinationName;
    public static volatile ListAttribute<Activity, Alarm> alarmList;
    public static volatile SingularAttribute<Activity, Integer> idActivity;

}