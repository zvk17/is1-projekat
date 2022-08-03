package entities;

import entities.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T00:12:53")
@StaticMetamodel(Alarms.class)
public class Alarms_ { 

    public static volatile SingularAttribute<Alarms, Integer> interval;
    public static volatile SingularAttribute<Alarms, Users> users;
    public static volatile SingularAttribute<Alarms, Integer> idAlarm;
    public static volatile SingularAttribute<Alarms, Date> moment;

}