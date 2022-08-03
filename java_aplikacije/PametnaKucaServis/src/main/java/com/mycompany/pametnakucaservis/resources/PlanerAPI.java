package com.mycompany.pametnakucaservis.resources;

import entities.Activity;
import entities.Song;
import entities.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("planer")
public class PlanerAPI {
    @Resource(lookup="servisTopic")
    Topic servisTopic;
    
    @Resource(lookup="planerTopic")
    Topic planerTopic;
    
    @Resource(lookup="fabrika")
    ConnectionFactory fabrika;
    
    String persistenceUnitName = "PametnaKucaServisPU";
    @PersistenceContext(unitName = "PametnaKucaServisPU")
    EntityManager em;  
    
    
    @GET
    public Response list(@Context HttpHeaders httpHeaders){
        
        User user = APIHelper.getUser(httpHeaders, em);
        
        List<Activity> activityList = new ArrayList<Activity>();
        try {
            String correlationId = UUID.randomUUID().toString();
            JMSContext context = fabrika.createContext();
            System.out.println("CORREL_ID: " + correlationId);
            JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
            JMSProducer producer = context.createProducer();
            Message message = context.createMessage();
            message.setStringProperty("OPERATION", "GET_ACTIVITY_LIST");
            message.setIntProperty("ID_USER",user.getIdUser());
            message.setJMSCorrelationID(correlationId);

            producer.send(planerTopic, message);

            ObjectMessage receivedMessage =  (ObjectMessage)consumer.receive();
            int size = receivedMessage.getIntProperty("SIZE");
            
            if (size != 0) {
                Activity[] activityArray = (Activity[]) receivedMessage.getObject();
                activityList = Arrays.asList(activityArray);
            }
        } catch (Exception ex) {
            Logger.getLogger(PlanerAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(new GenericEntity<List<Activity>>(activityList){})
                .build();
    }
    
    @POST
    public Response createActivity(
            @Context HttpHeaders httpHeaders,
            Activity activity,
            @QueryParam("addAlarm") boolean addAlarm
    ) {
        System.out.println("DESTINATION: " + activity.getDestinationName());
        System.out.println("START TIME: " + activity.getStartDateTime());
        System.out.println("DURATION: " + activity.getDurationSeconds());
        System.out.println("SHOULD ADD ALARM: " + addAlarm);
        if (activity.getStartDateTime() == null || activity.getDurationSeconds() <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("losi parametri")
                    .build();
        }
        User user = APIHelper.getUser(httpHeaders, em);
        activity.setIdUser(user);
        String correlationId = UUID.randomUUID().toString();
        JMSContext context = fabrika.createContext();
        JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
        JMSProducer producer = context.createProducer();
        int status = 500;
        ObjectMessage receivedMessage = null;
        try {
            ObjectMessage om = context.createObjectMessage();
            om.setStringProperty("OPERATION", "CREATE_ACTIVITY");
            om.setIntProperty("ID_USER", user.getIdUser());
            om.setBooleanProperty("ADD_ALARM", addAlarm);
            om.setJMSCorrelationID(correlationId);
            om.setObject(activity);
            producer.send(planerTopic, om);
            receivedMessage = (ObjectMessage)consumer.receive();
            status = receivedMessage.getIntProperty("STATUS");
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }
        //System.out.println("dohvatio objekat");
        //System.out.println("STATUS: " + status);
        if (status == 200) {
            
            Activity result = null;
            
            try {
                result = (Activity)receivedMessage.getObject();
            } catch (JMSException ex) {
                Logger.getLogger(PlanerAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(result)
                    .build();
        } else if (status == 400) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("bad request")
                    .build();
        } 
        return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
    }
    @PUT
    @Path("{idPlan}")
    public Response updatePlan(
            @Context HttpHeaders httpHeaders,
            @PathParam("idPlan") int idPlan,
            Activity activity,
            @QueryParam("addAlarm") boolean addAlarm
    ) {
        System.out.println("com.mycompany.pametnakucaservis.resources.PlanerAPI.updtePlan()");
        System.out.println("DESTINATION: " + activity.getDestinationName());
        System.out.println("START TIME: " + activity.getStartDateTime());
        System.out.println("DURATION: " + activity.getDurationSeconds());
        System.out.println("SHOULD ADD ALARM: " + addAlarm);
        if (activity.getStartDateTime() == null || activity.getDurationSeconds() <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("losi parametri")
                    .build();
        }
        User user = APIHelper.getUser(httpHeaders, em);
        
        String correlationId = UUID.randomUUID().toString();
        JMSContext context = fabrika.createContext();
        JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
        JMSProducer producer = context.createProducer();
        
        int status = 500;
        ObjectMessage receivedMessage = null;
        try {
            ObjectMessage om = context.createObjectMessage();
            om.setStringProperty("OPERATION", "UPDATE_ACTIVITY");
            om.setIntProperty("ID_USER", user.getIdUser());
            om.setIntProperty("ID_ACTIVITY", idPlan);
            om.setBooleanProperty("ADD_ALARM", addAlarm);
            om.setJMSCorrelationID(correlationId);
            om.setObject(activity);
            producer.send(planerTopic, om);
            receivedMessage = (ObjectMessage)consumer.receive();
            status = receivedMessage.getIntProperty("STATUS");
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }
        if (status == 200) {
            
            Activity result = null;
            
            try {
                result = (Activity)receivedMessage.getObject();
            } catch (JMSException ex) {
                Logger.getLogger(PlanerAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(result)
                    .build();
        } else if (status == 400) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("bad request")
                    .build();
        } else if (status == 409) {
           
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ddd")
                    .build();
        }
        return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        
    }
    
    @DELETE
    @Path("{idPlan}")
    public Response removePlan(@Context HttpHeaders httpHeaders, @PathParam("idPlan") int idPlan) {
        User user = APIHelper.getUser(httpHeaders, em);
        System.out.println("PlanerAPI.removePlan()");
        String correlationId = UUID.randomUUID().toString();
        JMSContext context = fabrika.createContext();
        JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
        JMSProducer producer = context.createProducer();
        Message m = context.createMessage();
        
        try {
            m.setStringProperty("OPERATION", "REMOVE_ACTIVITY");
            m.setIntProperty("ID_USER", user.getIdUser());
            m.setIntProperty(("ID_ACTIVITY"), idPlan);
            m.setJMSCorrelationID(correlationId);
        } catch (JMSException ex) {
            Logger.getLogger(PlanerAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(planerTopic, m);
        Message receivedMessage = consumer.receive();
        boolean success = false;
        try {
            success = receivedMessage.getBooleanProperty("SUCCESS");
        } catch (JMSException ex) {
            Logger.getLogger(PlanerAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (success) {
            return Response
                    .status(Response.Status.OK)
                    .entity("ok")
                    .build();
        }
        return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("bad request")
                    .build();
    }
    @GET
    @Path("distance_between")
    public Response distanceBetween(
            @Context HttpHeaders httpHeaders,
            @QueryParam("origin") String origin,
            @QueryParam("destination") String destination
    ) {
        //User user = APIHelper.getUser(httpHeaders, em);
        //int idUser = user.getIdUser();
        if (origin == null || destination == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Origin and destination parameters are required")
                    .build();
        }
        String correlationId = UUID.randomUUID().toString();
        JMSContext context = fabrika.createContext();
        JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
        JMSProducer producer = context.createProducer();
        Message message = context.createMessage();
        
        try {
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("OPERATION", "CALCULATE_DISTANCE");
            message.setStringProperty("ORIGIN", origin);
            message.setStringProperty("DESTINATION", destination);
        } catch (Exception e) {}
        
        producer.send(planerTopic, message);
        Message receivedMessage = consumer.receive();
        boolean status;
        try {
            status = receivedMessage.getBooleanProperty("SUCCESS");
        } catch (Exception ex){
            status = false;
        }
                
        int length = -1;
        int duration = -1;
        String originResponse = null;
        String destinationResponse = null;
        if (status) { 
            try {
                length = receivedMessage.getIntProperty("LENGTH");
                duration = receivedMessage.getIntProperty("DURATION");
                originResponse = receivedMessage.getStringProperty("ORIGIN");
                destinationResponse = receivedMessage.getStringProperty("DESTINATION");
            } catch (Exception ex){}
            
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }
        Distance d = new Distance();
        d.setDuration(duration);
        d.setLength(length);
        d.setOrigin(originResponse);
        d.setDestination(destinationResponse);
        return Response
                .status(Response.Status.OK)
                .entity(d)
                .build();
        
    }
    @GET
    @Path("distance_current")
    public Response distanceCurrent(
            @Context HttpHeaders httpHeaders,
            @QueryParam("destination") String destination
    ) {
        User user = APIHelper.getUser(httpHeaders, em);
        int idUser = user.getIdUser();
        if (destination == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Destination parameter is required")
                    .build();
        }
        String correlationId = UUID.randomUUID().toString();
        JMSContext context = fabrika.createContext();
        JMSConsumer consumer = context.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
        JMSProducer producer = context.createProducer();
        Message message = context.createMessage();
         try {
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("OPERATION", "CALCULATE_CURRENT_ACTIVITY_DISTANCE");
            message.setStringProperty("DESTINATION", destination);
            message.setIntProperty("ID_USER", idUser);
        } catch (Exception e) {}
        producer.send(planerTopic, message);
        Message receivedMessage = consumer.receive();
        
        boolean status;
        try {
            status = receivedMessage.getBooleanProperty("SUCCESS");
        } catch (Exception ex){
            status = false;
        }
                
        int length = -1;
        int duration = -1;
        String originResponse = null;
        String destinationResponse = null;
        if (status) { 
            try {
                length = receivedMessage.getIntProperty("LENGTH");
                duration = receivedMessage.getIntProperty("DURATION");
                originResponse = receivedMessage.getStringProperty("ORIGIN");
                destinationResponse = receivedMessage.getStringProperty("DESTINATION");
            } catch (Exception ex){}
            
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }
        
        Distance d = new Distance();
        d.setDuration(duration);
        d.setLength(length);
        d.setOrigin(originResponse);
        d.setDestination(destinationResponse);
        
        return Response
                .status(Response.Status.OK)
                .entity(d)
                .build();
    }
    
}
