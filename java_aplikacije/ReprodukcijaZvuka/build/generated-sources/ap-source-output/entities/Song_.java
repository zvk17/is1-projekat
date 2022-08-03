package entities;

import entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-08-20T14:17:00")
@StaticMetamodel(Song.class)
public class Song_ { 

    public static volatile ListAttribute<Song, User> userList;
    public static volatile ListAttribute<Song, User> userList1;
    public static volatile SingularAttribute<Song, Integer> idSong;
    public static volatile SingularAttribute<Song, String> name;
    public static volatile SingularAttribute<Song, String> url;

}