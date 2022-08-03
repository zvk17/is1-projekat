/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Activity;
import entities.Alarm;
import entities.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import static planer.CreateActivityListener.addAlarm;
import static planer.CreateActivityListener.doesOverlap;
import static planer.CreateActivityListener.getFirstAfter;
import static planer.CreateActivityListener.getLastBefore;
import static planer.CreateActivityListener.insertActivity;
import static planer.CreateActivityListener.okToInsertUpdate;
import static planer.Planer.getEndLocalDateTime;
import static planer.Planer.getStartLocalDateTime;
import static planer.RemoveActivityListener.removeActivity;
import static planer.RemoveActivityListener.removeAlarm;

/**
 *
 * @author user2
 */
public class UpdateActivityListener implements MessageListener {
    private JMSContext context;
    private Topic servisTopic;
    private EntityManager em;
    
    public UpdateActivityListener(EntityManager em, JMSContext context, Topic servisTopic) {
        this.em = em;
        this.context = context;
        this.servisTopic = servisTopic;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("planer.UpdateActivityListener.onMessage()");
        
        ObjectMessage response = context.createObjectMessage();
        ObjectMessage om = (ObjectMessage)message;
        JMSProducer producer = context.createProducer();
        
        try {
            response.setJMSCorrelationID(message.getJMSCorrelationID());
        }catch (Exception ex) {ex.printStackTrace();}
        
        int idUser = 0;
        int idActivity = 0;
        boolean setAlarm = false;
        Activity a = null;
        int status = 500;
        try {
            idUser = message.getIntProperty("ID_USER");
            setAlarm = message.getBooleanProperty("ADD_ALARM");
            a = (Activity)om.getObject();
            idActivity = message.getIntProperty("ID_ACTIVITY");
            
        } catch (Exception ex) {
            Logger.getLogger(CreateActivityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        Activity oldActivity = em.find(Activity.class, idActivity);
        
        if (oldActivity == null || oldActivity.getIdUser().getIdUser() != idUser) {
            try {
                response.setIntProperty("STATUS", 409);
                response.setStringProperty("MESSAGE", "Podaci se ne slazu");
            } catch (JMSException ex) {
                Logger.getLogger(UpdateActivityListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            producer.send(servisTopic, response);
            return;
        }
        int idOldActivity = oldActivity.getIdActivity();
        try {
            boolean ok = okToUpdateActivity(oldActivity, a, idUser);
            oldActivity = em.find(Activity.class, idOldActivity);
            if (ok) {
                updateActivityTransaction(oldActivity, a, idUser, setAlarm);
                response.setObject(oldActivity);
                status = 200;
                
            } else {
                status = 409;
            }
            
        } catch (Exception ex) {
            System.out.println("uhvacena greska");
            Logger.getLogger(CreateActivityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            response.setIntProperty("STATUS", status);
            if (status == 409) {
                response.setStringProperty("MESSAGE", "cant update");
            }
        } catch (Exception e) {}
        System.out.println("saljem poruku");
        producer.send(servisTopic, response);
        
    }
    boolean okToUpdateActivity(Activity oldActivity, Activity newActivity, int idUser) throws Exception {
        System.out.println("planer.UpdateActivityListener.okToUpdateActivity()");
        LocalDateTime start = getStartLocalDateTime(newActivity);
        LocalDateTime end = getEndLocalDateTime(newActivity);
        //int idOldActivity = oldActivity.getIdActivity();
        em.clear();
        em.getEntityManagerFactory().getCache().evictAll();
        User user = em.find(User.class, idUser);
        //oldActivity = em.find(Activity.class, idOldActivity);
        List<Activity> activityList = filterList(oldActivity, user.getActivityList());
        
        return okToInsertUpdate(newActivity, activityList);
    }
    
    List<Activity> filterList(Activity removeActivity, List<Activity> activityList) {
        System.out.println("planer.UpdateActivityListener.filterList()");
        List<Activity> newList = new ArrayList<Activity>();
        int idActivity = removeActivity.getIdActivity();
        for (Activity activity : activityList) {
            if (activity.getIdActivity() != idActivity) {
                newList.add(activity);
            } else {
                System.out.println("filterList():FILTER_FOUND");
            }
        }
        return newList;
    }

    private void updateActivityTransaction(Activity oldActivity, Activity newActivity, int idUser, boolean setAlarm) throws Exception {
        try {
            em.getTransaction().begin();     
            int idActivity = oldActivity.getIdActivity();
            removeAlarm(oldActivity, em);
            oldActivity.setDestinationName(newActivity.getDestinationName());
            oldActivity.setDurationSeconds(newActivity.getDurationSeconds());
            oldActivity.setStartDateTime(newActivity.getStartDateTime());
            
            if (setAlarm) {
                System.out.println("ADDING ALARM");
                User user = em.find(User.class, idUser);
                //dodaj alarm, da zvoni tacno da bi se stiglo na vreme
                Alarm alarm = addAlarm(oldActivity, user, filterList(oldActivity, user.getActivityList()));
                em.persist(alarm);
            }
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
        
    }
}
