/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Activity;
import entities.Song;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
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
public class GetActivityList implements MessageListener {
    private EntityManager em;
    private JMSContext context;
    private Topic servisTopic;

    public GetActivityList(EntityManager em, JMSContext context, Topic servisTopic) {
        this.em = em;
        this.context = context;
        this.servisTopic = servisTopic;
    }
    
    @Override
    public void onMessage(Message message) {
        try {

            System.out.println("GET_ACTIVITY_LIST CORREL_ID: " + message.getJMSCorrelationID());
            em.getEntityManagerFactory().getCache().evictAll();
            em.clear();
            int idUser = message.getIntProperty("ID_USER");
            User user = em.find(User.class, idUser);

   
            List<Activity> activities = user.getActivityList();
            if (activities == null) {
                activities = new ArrayList<Activity>();
            }                    
            Activity[] activityArray =  activities.toArray(new Activity[1]);

            System.out.println("Broj obaveza: " + activities.size());

            Message sendMessage = context.createObjectMessage(activityArray);
            sendMessage.setIntProperty("ID_USER", idUser);
            sendMessage.setStringProperty("OPERATION", "GET_PLAYED_LIST");
            sendMessage.setIntProperty("SIZE", activities.size());
            sendMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            JMSProducer producer = context.createProducer();
            producer.send(servisTopic, sendMessage);

        } catch (JMSException ex) {
            Logger.getLogger(GetActivityList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
