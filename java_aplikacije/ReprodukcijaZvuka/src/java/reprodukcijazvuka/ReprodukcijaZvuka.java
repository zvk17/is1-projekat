/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reprodukcijazvuka;

//import entities.Songs;

import entities.Song;
import entities.User;
import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;



/**
 *
 * @author user2
 */

public class ReprodukcijaZvuka {
    @Resource(lookup="fabrika")
    private static ConnectionFactory fabrika;
    
    @Resource(lookup="zvukTopic")
    public static Topic zvukTopic;
    
    @Resource(lookup="servisTopic")
    public static Topic servisTopic;
    
    
    public static void main(String[] args) {
        System.out.println("reprodukcijazvuka.ReprodukcijaZvuka.main()");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ReprodukcijaZvukaPU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = fabrika.createContext();
        //context.setClientID("ReprodukcijaZvuka");
        
        JMSConsumer playSongConsumer = context.createConsumer(zvukTopic, "OPERATION='PLAY_SONG'");
        JMSConsumer getPlayedListConsumer =  context.createConsumer(zvukTopic, "OPERATION='GET_PLAYED_LIST'");
        JMSProducer producer = context.createProducer();
        
        playSongConsumer.setMessageListener(new PlaySongMessageListener(em));
        getPlayedListConsumer.setMessageListener(new GetPlayedListMessageListener(em,context,producer, servisTopic));
        
        while (true) {            
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReprodukcijaZvuka.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    
}
