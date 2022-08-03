/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import clients.MapsClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import responses.RouteList;
import responses.Summary;

/**
 *
 * @author user2
 */
public class CalculateDistanceListener implements MessageListener {
    JMSContext context;
    Topic servisTopic;
    public CalculateDistanceListener(JMSContext context, Topic servisTopic) {
        this.context = context;
        this.servisTopic = servisTopic;
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("planer.CalculateDistanceMessageListener");
        Message response = context.createMessage();
        try {
            String correlationId = message.getJMSCorrelationID();
            response.setJMSCorrelationID(correlationId);
        } catch (Exception e){e.printStackTrace();}
        
        
            
        try {
            String origin = message.getStringProperty("ORIGIN");
            String destination = message.getStringProperty("DESTINATION");
            
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

    
    
}
