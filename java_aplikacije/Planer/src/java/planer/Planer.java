/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;


import entities.Activity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user2
 */
public class Planer {
    @Resource(lookup = "fabrika")
    private static ConnectionFactory fabrika;
    
    @Resource(lookup = "alarmTopic")
    private static Topic alarmTopic;
    
    @Resource(lookup = "servisTopic")
    private static Topic servisTopic;
    
    @Resource(lookup = "planerTopic")
    private static Topic planerTopic;
    
    private static final String persistanceUnitName = "PlanerPU";

    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        
        /*GeocodeList str = MapsClient.geocodeLocation("Beograd");
        Position p1 = str.getFirstPosition();
        GeocodeList str2 = MapsClient.geocodeLocation("Niš");
        Position p2 = str2.getFirstPosition();
        
        RouteList rl = MapsClient.getRouteList(p1, p2);
        
        System.out.println(rl.getFirstSummary());
        */
        JMSContext context = fabrika.createContext();
        JMSConsumer calculateDistanceConsumer = context.createConsumer(planerTopic, "OPERATION='CALCULATE_DISTANCE'");
        JMSConsumer removeActivityConsumer = context.createConsumer(planerTopic, "OPERATION='REMOVE_ACTIVITY'");
        JMSConsumer calculateCurrentActivityDistance = 
                context.createConsumer(planerTopic, "OPERATION='CALCULATE_CURRENT_ACTIVITY_DISTANCE'");
        
        JMSConsumer getActivityListConsumer = context.createConsumer(planerTopic, "OPERATION='GET_ACTIVITY_LIST'");
        JMSConsumer createActivityConsumer = context.createConsumer(planerTopic, "OPERATION='CREATE_ACTIVITY'");
        JMSConsumer updateActivityConsumer = context.createConsumer(planerTopic, "OPERATION='UPDATE_ACTIVITY'");
        
        calculateDistanceConsumer.setMessageListener(new CalculateDistanceListener(context, servisTopic));
        removeActivityConsumer.setMessageListener(new RemoveActivityListener(context, servisTopic, em));
        calculateCurrentActivityDistance.setMessageListener(new CalculateCurrentActivityDistanceListener(context, servisTopic, em));
        getActivityListConsumer.setMessageListener(new GetActivityList(em, context, servisTopic));
        createActivityConsumer.setMessageListener(new CreateActivityListener(em, context, servisTopic));
        updateActivityConsumer.setMessageListener(new UpdateActivityListener(em, context, servisTopic));
        
        while (true) {            
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Planer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    static Date fromLocalDateTime(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    static LocalDateTime getStartLocalDateTime(Activity activity) {
        return toLocalDateTime(activity.getStartDateTime());
    }
    static LocalDateTime getEndLocalDateTime(Activity activity) {
        int duration = activity.getDurationSeconds();
        LocalDateTime ldt = toLocalDateTime(activity.getStartDateTime());
        return ldt.plusSeconds(duration);
    }
    static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
}
