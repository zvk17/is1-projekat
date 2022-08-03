/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import entities.Song;
import entities.User;
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
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user2
 */
public class Alarm {

    @Resource(lookup="fabrika")
    private static ConnectionFactory fabrika;
    
    @Resource(lookup="alarmTopic")
    public static Topic alarmTopic;
    
    @Resource(lookup="servisTopic")
    public static Topic servisTopic;
    
    @Resource(lookup="zvukTopic")
    public static Topic zvukTopic;
    
    public static void main(String[] args) {
        System.out.println("alarm.Alarm.main()");
        AlarmWorkingThread thread = new AlarmWorkingThread(zvukTopic, 1, fabrika);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AlarmPU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = fabrika.createContext();
        //context.setClientID("AlarmApp");
        JMSConsumer setSongConsumer = context.createConsumer(alarmTopic, "OPERATION='SET_SONG'");
        JMSConsumer addAlarmConsumer = context.createConsumer(alarmTopic, "OPERATION='ADD_ALARM'");
        JMSConsumer setUserConsumer = context.createConsumer(alarmTopic, "OPERATION='SET_USER'");
        
        setSongConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("setSong");
                try {
                    int idSong = message.getIntProperty("ID_SONG");
                    int idUser = message.getIntProperty("ID_USER");
                    System.out.println("SET_SONG: "+idSong + " " + idUser);
                    
                   // synchronized (em) {
                        User user = em.find(User.class, idUser);
                        Song song = em.find(Song.class, idSong);
                        em.getTransaction().begin();
                        user.setIdSong(song);
                        em.persist(user);
                        em.getTransaction().commit();
                    //}
                        
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        addAlarmConsumer.setMessageListener(new MessageListener() {
            
            @Override
            public void onMessage(Message message) {
                System.out.println("addAlarm");
                String correlationId = null;
                try {
                    correlationId = message.getJMSCorrelationID();
                } catch (JMSException ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
                ObjectMessage omSend = context.createObjectMessage();
                try {                
                    omSend.setJMSCorrelationID(correlationId);
                } catch (JMSException ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
                JMSProducer producer = context.createProducer();
                
                System.out.println("ADD_ALARM received");
                try {
                    ObjectMessage om = (ObjectMessage)message;
                    entities.Alarm alarm = (entities.Alarm)om.getObject();
                    
                    
                    User user = em.find(User.class, om.getIntProperty("ID_USER"));

                    em.getTransaction().begin();
                    entities.Alarm a = new entities.Alarm();
                    a.setIntervalSeconds(alarm.getIntervalSeconds());
                    a.setDatetimeMoment(alarm.getDatetimeMoment());
                    a.setIdUser(user);
                    em.detach(alarm);
                    em.persist(a);
                    em.getTransaction().commit();
                    omSend.setObject(a); 
                    producer.send(servisTopic, omSend);
                    
                } catch (Exception ex) {
                    em.getTransaction().rollback();            
                    
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
                producer.send(servisTopic, omSend);
            }
        });
        setUserConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("SET_USER");
                try {
                    int idUser = message.getIntProperty("ID_USER");
                    thread.setIdUser(idUser);
                } catch (Exception ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        thread.start();
        while (true) {
            try {
                Thread.sleep(10 * 1000);
            } catch (Exception ex) {
                Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
