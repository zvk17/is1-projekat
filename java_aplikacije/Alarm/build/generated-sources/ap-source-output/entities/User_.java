package entities;

import entities.Activity;
import entities.Alarm;
import entities.OldAlarm;
import entities.Song;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-18T16:14:40")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Integer> idUser;
    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, OldAlarm> oldAlarmList;
    public static volatile SingularAttribute<User, Song> idSong;
    public static volatile SingularAttribute<User, String> homeLocation;
    public static volatile ListAttribute<User, Alarm> alarmList;
    public static volatile ListAttribute<User, Song> songList;
    public static volatile ListAttribute<User, Activity> activityList;
    public static volatile SingularAttribute<User, String> username;

}