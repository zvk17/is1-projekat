package alarm;

import alarm.ListenedSong;
import alarm.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T09:16:49")
@StaticMetamodel(Song.class)
public class Song_ { 

    public static volatile ListAttribute<Song, User> userList;
    public static volatile SingularAttribute<Song, Integer> idSong;
    public static volatile SingularAttribute<Song, String> name;
    public static volatile ListAttribute<Song, ListenedSong> listenedSongList;
    public static volatile SingularAttribute<Song, String> url;

}