/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reprodukcijazvuka;

import entities.Song;
import entities.User;
import java.awt.Desktop;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;

/**
 *
 * @author user2
 */
public class PlaySongMessageListener implements MessageListener{

    private EntityManager em;

    public PlaySongMessageListener(EntityManager em) {
        this.em = em;
    }
    

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("PLAY_SONG " + System.nanoTime());
            int idSong = message.getIntProperty("ID_SONG");
            int idUser = message.getIntProperty("ID_USER");
            Song song = em.find(Song.class, idSong);
            if (song == null) {
                System.out.println("Song with id " + idSong + " does not exist.");
                return;
            }
            String url = song.getUrl();
            User user = em.find(User.class, idUser);
            if (user != null) {
                //ListenedSongPK pk = new ListenedSongPK(idSong, idUser);
                //ListenedSong ls = em.find(ListenedSong.class, pk);
                /*if (ls == null) {
                ls = new ListenedSong(pk);
                em.getTransaction().begin();
                em.persist(ls);
                em.getTransaction().commit();
                }*/
                List<Song> songList = user.getSongList();
                if (!songList.contains(song)) {
                    System.out.println("doesnt contain");
                    em.getTransaction().begin();
                    user.getSongList().add(song);
                    song.getUserList().add(user);
                    em.getTransaction().commit();
                }

            }

            if (openWebpage(url)) {
                System.out.println("Opened song " + song);
            }
        } catch (JMSException ex) {
            Logger.getLogger(ReprodukcijaZvuka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static boolean openWebpage(String urlString) {
        try {
            //Desktop
            Desktop.getDesktop().browse(new URL(urlString).toURI());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
