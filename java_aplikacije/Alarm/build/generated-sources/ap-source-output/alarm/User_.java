package alarm;

import alarm.Alarm_1;
import alarm.ListenedSong;
import alarm.Song;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T09:16:49")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Integer> idUser;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Song> idSong;
    public static volatile SingularAttribute<User, String> homeLocation;
    public static volatile ListAttribute<User, Alarm_1> alarmList;
    public static volatile SingularAttribute<User, ListenedSong> listenedSong;
    public static volatile SingularAttribute<User, String> username;

}