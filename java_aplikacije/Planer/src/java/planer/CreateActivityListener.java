/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import clients.MapsClient;
import com.mysql.cj.util.StringUtils;
import entities.Activity;
import entities.Alarm;
import entities.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import static planer.Planer.getEndLocalDateTime;
import static planer.Planer.getStartLocalDateTime;
import static planer.Planer.toLocalDateTime;
import responses.Position;
import responses.Summary;

/**
 *
 * @author user2
 */
public class CreateActivityListener implements  MessageListener {
    EntityManager em;
    JMSContext context;
    Topic servisTopic;
    CreateActivityListener(EntityManager em, JMSContext context, Topic servisTopic) {
        this.em = em;
        this.context = context;
        this.servisTopic = servisTopic;
    }

    @Override
    public void onMessage(Message message) {
        JMSProducer producer = context.createProducer();
        ObjectMessage om = (ObjectMessage)message;
        System.out.println("planer.CreateActivityListener()");
        ObjectMessage response = context.createObjectMessage();
        try {
            String correlationId = message.getJMSCorrelationID();
            response.setJMSCorrelationID(correlationId);
        } catch (Exception e){e.printStackTrace();}
        
        int idUser = 0;
        boolean setAlarm = false;
        Activity a = null;
        int status = 500;
        try {
            idUser = message.getIntProperty("ID_USER");
            setAlarm = message.getBooleanProperty("ADD_ALARM");
            a = (Activity)om.getObject();
            
            
        } catch (Exception ex) {
            Logger.getLogger(CreateActivityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            boolean ok = okToInsertActivity(a, idUser);
            if (ok) {
                insertActivityTransaction(a, idUser, setAlarm);
                response.setObject(a);
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
        } catch (Exception e) {}
        System.out.println("saljem poruku");
        producer.send(servisTopic, response);
    }
    
    private boolean okToInsertActivity(Activity a, Integer idUser) throws Exception {
        LocalDateTime start = getStartLocalDateTime(a);
        LocalDateTime end = getEndLocalDateTime(a);
        em.clear();
        em.getEntityManagerFactory().getCache().evictAll();
        
        User user = em.find(User.class, idUser);
        List<Activity> activityList = user.getActivityList();
        
        
        return okToInsertUpdate(a, activityList);
    }
    static boolean okToInsertUpdate(Activity newActivity, List<Activity> activityList) throws Exception {
        if (doesOverlap(newActivity, activityList)) {
            System.out.println("Intersect");
            return false;
        }
        
        //uslov za ispitivanje da li je najbliza prethodna obaveza
        //origin activity = lastBefore
        //destination activity
        Activity lastBefore  = getLastBefore(newActivity, activityList);
        if (lastBefore != null) {
            
            if (doesOverlap(lastBefore, newActivity)) {
                System.out.println("Overlap with activity before");
                return false;
            }
        }       
        
        //uslov za ispitivanje da li je najbliza sledeca obaveza ok
        //origin activity
        //destination firstAfter
        //if orAct.end + duration > firstAfter.start return false        
        Activity firstAfter = getFirstAfter(newActivity, activityList);
        if (firstAfter != null) {
            
            if (doesOverlap(newActivity, firstAfter)) {
                System.out.println("Overlap with activity after");
                return false;
            }
        }       
        
        return true;
    }
    static boolean doesOverlap(Activity a, List<Activity> activityList) {
        //(StartA <= EndB)  and  (EndA >= StartB)
        LocalDateTime start = getStartLocalDateTime(a);
        LocalDateTime end = getEndLocalDateTime(a);
        for (Activity activity : activityList) {
            LocalDateTime s = getStartLocalDateTime(activity);
            LocalDateTime e = getEndLocalDateTime(activity);
            if (!start.isAfter(e) && !end.isBefore(s)) {
                return true;
            }
        }
        return false;
    }
    static Activity getFirstAfter(Activity a, List<Activity> activityList) {
        LocalDateTime start = getStartLocalDateTime(a);
        LocalDateTime end = getEndLocalDateTime(a);
        
        Activity firstAfter = null;
        LocalDateTime firstAfterStart = null;
        
        for (Activity activity : activityList) {
            LocalDateTime s = getStartLocalDateTime(activity);
            LocalDateTime e = getEndLocalDateTime(activity);
            if (!s.isAfter(end)) {
                continue;
            }
            if (firstAfterStart == null || firstAfterStart.isAfter(s)) {
                firstAfterStart = s;
                firstAfter = activity;
            }
        }
        return firstAfter;
    }
    static Activity getLastBefore(Activity a, List<Activity> activityList) {
        LocalDateTime start = getStartLocalDateTime(a);
        LocalDateTime end = getEndLocalDateTime(a);
        
        Activity lastBefore = null;
        LocalDateTime lastBeforeEnd = null;
        
        for (Activity activity : activityList) {
            LocalDateTime s = getStartLocalDateTime(activity);
            LocalDateTime e = getEndLocalDateTime(activity);
            if (!e.isBefore(start)) {
                continue;
            }
            if (lastBeforeEnd == null || lastBeforeEnd.isBefore(e)) {
                lastBefore = activity;
                lastBeforeEnd = e;
            }
        }
        return lastBefore;
    }
    static boolean doesOverlap(Activity origin, Activity destination) throws Exception {
        
        Summary s = MapsClient.getRouteList(origin.getLocation(), destination.getLocation()).getFirstSummary();
        LocalDateTime originEndTime = getEndLocalDateTime(origin);
        LocalDateTime destinationStartTime = toLocalDateTime(destination.getStartDateTime());
        if (originEndTime.plusSeconds(s.getDuration()).isAfter(destinationStartTime)) {
            return true;
        }
        
        return false;
    }

    private void insertActivityTransaction(Activity a, int idUser, boolean setAlarm) throws Exception {
        try {
            em.getTransaction().begin();
            
            insertActivity(a, idUser, setAlarm, em);
            
            em.getTransaction().commit();
        }   catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
        
    }
    static void insertActivity(Activity a, int idUser, boolean setAlarm, EntityManager em) throws Exception {
        User user = em.find(User.class, idUser);
        a.setIdUser(user);
        em.persist(a);
        if (setAlarm) {
            System.out.println("ADDING ALARM");
            //dodaj alarm, da zvoni tacno da bi se stiglo na vreme
            Alarm alarm = addAlarm(a, user, user.getActivityList());
            em.persist(alarm);
        }
    }
    static Alarm addAlarm(Activity activity, User user, List<Activity> list) throws Exception {
        Activity before = getLastBefore(activity, list);
        String origin = user.getHomeLocation();
        
        if (before != null) {
            origin = before.getLocation();
        }
        
        Alarm alarm = new Alarm();        
        Summary s = MapsClient.getRouteList(origin, activity.getLocation()).getFirstSummary();
        alarm.setIdUser(user);
        int duration = s.getDuration();
        LocalDateTime ldt = getStartLocalDateTime(activity);
        Date alarmDate = Planer.fromLocalDateTime(ldt.minusSeconds(duration));
        alarm.setDatetimeMoment(alarmDate);
        List<Alarm> alarmList = new ArrayList<>();
        alarmList.add(alarm);        
        activity.setAlarmList(alarmList);
        
        List<Activity> activityList = new ArrayList<>();
        activityList.add(activity);
        alarm.setActivityList(activityList);
        return alarm;
    }
}
