/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import clients.MapsClient;
import entities.Activity;
import entities.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
import static planer.Planer.getEndLocalDateTime;
import static planer.Planer.toLocalDateTime;
import responses.RouteList;
import responses.Summary;

/**
 *
 * @author user2
 */
public class CalculateCurrentActivityDistanceListener implements MessageListener {
    JMSContext context;
    Topic servisTopic;
    private EntityManager em;
    public CalculateCurrentActivityDistanceListener(JMSContext context, Topic servisTopic, EntityManager em) {
        this.context = context;
        this.servisTopic = servisTopic;
        this.em = em;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("planer.CalculateCurrentActivityDistanceMessageListener");
        Message response = context.createMessage();
        try {
            String correlationId = message.getJMSCorrelationID();
            response.setJMSCorrelationID(correlationId);
        } catch (Exception e){e.printStackTrace();}
        try {
            Integer idUser = message.getIntProperty("ID_USER");
            String destination = message.getStringProperty("DESTINATION");
            
            String origin = getCurrentLocation(idUser);
            System.out.println("ORIGIN: " + origin);
            
            RouteList result = MapsClient.getRouteList(origin, destination);
            Summary summary = result.getFirstSummary();
            
            response.setIntProperty("DURATION", summary.getDuration());
            response.setIntProperty("LENGTH", summary.getLength());
            response.setStringProperty("ORIGIN", origin);
            response.setStringProperty("DESTINATION", destination);
            response.setBooleanProperty("SUCCESS", true);
            
        } catch (Exception ex) {
            try {
                response.setBooleanProperty("SUCCESS", false);
            } catch (JMSException e){}            
            
            Logger.getLogger(CalculateDistanceListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        JMSProducer producer = context.createProducer();
        producer.send(servisTopic, response);
    }

    private String getCurrentLocation(Integer idUser) {
        em.clear();
        em.getEntityManagerFactory().getCache().evictAll();
        User user = em.find(User.class, idUser);
        List<Activity> activities = user.getActivityList();
        if (activities == null || activities.size() == 0) {
            return user.getHomeLocation();
        }
        LocalDateTime now = LocalDateTime.now();
        Activity last = null;
        LocalDateTime lastStart = null;
        for (Activity activity : activities) {
            LocalDateTime start = toLocalDateTime(activity.getStartDateTime());
            LocalDateTime end = getEndLocalDateTime(activity);
            
            if (now.isBefore(start)) {
                continue;
            }
            if (now.isBefore(end)) {
                return activity.getDestinationName();
            }
            if (lastStart == null || lastStart.isBefore(start)) {
                last = activity;
                lastStart = start;
            }
            
        }
        if (last != null) {
            System.out.println("CURRENT ACTIVITY:" +last);
            return last.getDestinationName();
        }
        return user.getHomeLocation();
    }
    
    

}
