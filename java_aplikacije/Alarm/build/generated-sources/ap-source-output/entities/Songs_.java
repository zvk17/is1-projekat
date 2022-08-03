package entities;

import entities.ListenedSongs;
import entities.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T00:12:53")
@StaticMetamodel(Songs.class)
public class Songs_ { 

    public static volatile ListAttribute<Songs, Users> usersList;
    public static volatile SingularAttribute<Songs, Integer> idSong;
    public static volatile SingularAttribute<Songs, String> name;
    public static volatile ListAttribute<Songs, ListenedSongs> listenedSongsList;
    public static volatile SingularAttribute<Songs, String> url;

}