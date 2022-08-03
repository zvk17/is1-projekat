package entities;

import entities.Alarms;
import entities.ListenedSongs;
import entities.Songs;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T00:12:53")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, Integer> idUser;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile ListAttribute<Users, Alarms> alarmsList;
    public static volatile SingularAttribute<Users, Songs> idSong;
    public static volatile SingularAttribute<Users, String> homeLocation;
    public static volatile SingularAttribute<Users, String> username;
    public static volatile SingularAttribute<Users, ListenedSongs> listenedSongs;

}