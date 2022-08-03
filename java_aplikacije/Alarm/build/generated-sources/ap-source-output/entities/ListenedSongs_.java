package entities;

import entities.ListenedSongsPK;
import entities.Songs;
import entities.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-06T00:12:53")
@StaticMetamodel(ListenedSongs.class)
public class ListenedSongs_ { 

    public static volatile SingularAttribute<ListenedSongs, Songs> songs;
    public static volatile SingularAttribute<ListenedSongs, ListenedSongsPK> listenedSongsPK;
    public static volatile SingularAttribute<ListenedSongs, Users> users;

}