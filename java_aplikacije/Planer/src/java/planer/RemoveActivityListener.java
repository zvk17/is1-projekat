/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Activity;
import entities.Alarm;

import entities.User;
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
public class RemoveActivityListener implements MessageListener {
    private JMSContext context;
    private Topic servisTopic;
    private EntityManager em;
    
    public RemoveActivityListener(JMSContext context, Topic servisTopic, EntityManager em) {
        this.context = context;
        this.servisTopic = servisTopic;
        this.em = em;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("planer.RemoveActivityListener");
        Message response = context.createMessage();
        try {
            response.setJMSCorrelationID(message.getJMSCorrelationID());
        }catch (Exception ex) {ex.printStackTrace();}
        
        int idUser = 0;
        int idActivity = 0;
        try {
            idUser = message.getIntProperty("ID_USER");
            idActivity = message.getIntProperty("ID_ACTIVITY");
        } catch (Exception ex) {ex.printStackTrace();}
        
        Activity activity = em.find(Activity.class, idActivity);
           
        try {
            if (activity == null || activity.getIdUser().getIdUser() != idUser) {
                System.out.println("not valid activity and user");
                response.setBooleanProperty("SUCCESS", false);
            } else {
                removeActivityTransaction(activity, em);
                System.out.println("SUCCESS");
                response.setBooleanProperty("SUCCESS", true);
            } 
        } catch (Exception ex) {
            try {response.setBooleanProperty("SUCCESS", true);}catch(Exception eex){}
            Logger.getLogger(RemoveActivityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(servisTopic, response);
    }
    
    static void removeActivityTransaction(Activity activity, EntityManager em) {
        em.getTransaction().begin();
        removeActivity(activity, em);
        em.getTransaction().commit();
    }
    static void removeActivity(Activity activity, EntityManager em) {
        removeAlarm(activity, em);
        em.remove(activity); 
    }
    static void removeAlarm(Activity activity, EntityManager em) {
        if (activity.getAlarmList() != null && activity.getAlarmList().size() != 0) {
            Alarm alarm = activity.getAlarmList().get(0);
            alarm.setActivityList(null);
            activity.setAlarmList(null);
            em.remove(alarm);
        }
    }
    
}
