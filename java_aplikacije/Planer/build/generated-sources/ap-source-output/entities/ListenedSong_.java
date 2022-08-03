package entities;

import entities.ListenedSongPK;
import entities.Song;
import entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-15T15:04:12")
@StaticMetamodel(ListenedSong.class)
public class ListenedSong_ { 

    public static volatile SingularAttribute<ListenedSong, ListenedSongPK> listenedSongPK;
    public static volatile SingularAttribute<ListenedSong, Song> song;
    public static volatile SingularAttribute<ListenedSong, User> user;

}