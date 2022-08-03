/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reprodukcijazvuka;

import entities.Song;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.persistence.EntityManager;

/**
 *
 * @author user2
 */
public class GetPlayedListMessageListener implements MessageListener{

    private EntityManager em;
    private JMSContext context;
    private JMSProducer producer;
    private Topic servisTopic;

    public GetPlayedListMessageListener(EntityManager em, JMSContext context, JMSProducer producer, Topic servisTopic) {
        this.em = em;
        this.context = context;
        this.producer = producer;
        this.servisTopic = servisTopic;
    }
    

    @Override
    public void onMessage(Message message) {
        try {

            System.out.println("GET_PLAYED_LIST CORREL_ID: " + message.getJMSCorrelationID());
            em.getEntityManagerFactory().getCache().evictAll();
            em.clear();
            int idUser = message.getIntProperty("ID_USER");
            User user = em.find(User.class, idUser);

            List<Song> songs = user.getSongList();
            if (songs == null) {
                songs = new ArrayList<Song>();
            }                    
            Song[] songArray =  songs.toArray(new Song[1]);

            System.out.println("Broj pesama: " + songs.size());

            Message sendMessage = context.createObjectMessage(songArray);
            sendMessage.setIntProperty("ID_USER", idUser);
            sendMessage.setStringProperty("OPERATION", "GET_PLAYED_LIST");
            sendMessage.setIntProperty("SIZE", songs.size());
            sendMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            producer.send(servisTopic, sendMessage);

        } catch (JMSException ex) {
            Logger.getLogger(ReprodukcijaZvuka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
