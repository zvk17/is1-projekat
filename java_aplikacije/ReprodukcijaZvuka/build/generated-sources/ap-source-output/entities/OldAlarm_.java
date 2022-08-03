package entities;

import entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-20T14:17:00")
@StaticMetamodel(OldAlarm.class)
public class OldAlarm_ { 

    public static volatile SingularAttribute<OldAlarm, User> idUser;
    public static volatile SingularAttribute<OldAlarm, Integer> idOldAlarm;
    public static volatile SingularAttribute<OldAlarm, Date> datetimeMoment;

}