/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pametnakucaservis.resources;

import entities.Alarm;
import entities.Song;
import entities.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author user2
 */
@Path("alarm")
public class AlarmAPI {
    @Resource(lookup="fabrika")
    ConnectionFactory fabrika;
    
    @Resource(lookup="alarmTopic")
    Topic alarmTopic;
    
    @Resource(lookup="servisTopic")
    Topic servisTopic;
    
    String persistanceUnitName = "PametnaKucaServisPU";
    //@PersistenceContext(unitName = "PametnaKucaServisPU")
    //EntityManager em; 
    
    @GET
    @Path("list")
    public Response listAlarmsForUser(@Context HttpHeaders httpHeaders) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        emf.getCache().evictAll();
        EntityManager em = emf.createEntityManager();
        
        User user = APIHelper.getUser(httpHeaders, em);
        
        List<Alarm> alarmList = new ArrayList<Alarm>();
        
        List<Alarm> pom = user.getAlarmList();
        if (pom != null) {
            for (Alarm alarm : pom) {
              alarmList.add(alarm);
            }
        }
        
        em.close();
        emf.close();
        //(new GenericEntity<List<Alarm>>(alarmList){})
        try {
            return Response
                .status(Response.Status.OK)
                .entity(new GenericEntity<List<Alarm>>(alarmList){})
                .build();
        } catch (Exception ex) {
            Logger.getLogger(SongReproductionAPI.class.getName()).log(Level.SEVERE, null, ex);
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("GRESKA")
                .build();
        }
        
    }
    
    @PUT    
    @Path("song")
    public Response setSong(@Context HttpHeaders httpHeaders, @FormParam("idSong") int idSong) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        User user = APIHelper.getUser(httpHeaders, em);
        int idUser = user.getIdUser();
        
        Song song = em.find(Song.class, idSong);
        em.close();
        emf.close();
        if (song == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Song doesn't exist")
                .build(); 
        }
        
        try {
            JMSContext ctx = fabrika.createContext();
            JMSProducer producer = ctx.createProducer();
            Message message = ctx.createMessage();
            message.setIntProperty("ID_USER", idUser);
            message.setIntProperty("ID_SONG", idSong);
            message.setStringProperty("OPERATION", "SET_SONG");
            producer.send(alarmTopic, message);
        } catch (Exception ex) {
            Logger.getLogger(AlarmAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return Response
                .status(Response.Status.OK)
                .entity("ok")
                .build(); 
        
    }
    @POST
    public Response createAlarm(@Context HttpHeaders httpHeaders, Alarm alarm) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        User user = APIHelper.getUser(httpHeaders, em);
        Integer i = alarm.getIntervalSeconds();
        if (i == 0) {
            alarm.setIntervalSeconds(null);
        }
        em.close();
        emf.close();
        
        try {
            Alarm result = sendCreateAlarmRequestJMS(user, alarm);
            return Response
                    .status(Response.Status.OK)
                    .entity(result)
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(AlarmAPI.class.getName()).log(Level.SEVERE, null, ex);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }
    @GET
    @Produces("application/xml")
    @Path("moment_list")
    public Response getMomentList(@Context HttpHeaders httpHeaders) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        User user = APIHelper.getUser(httpHeaders, em);
        em.close();
        emf.close();
        List<Date> dateList = createMomentList();
        List<XmlMoment> momentList = new ArrayList<XmlMoment>();
        int i = 0;
        for (Date date : dateList) {
            momentList.add(new XmlMoment(date, i++));
        }
         return Response
                .status(Response.Status.OK)
                .entity(new GenericEntity<List<XmlMoment>>(momentList){})
                .build();  
    }
    @POST
    @Path("{idMoment}")
    public Response createAlarmFromMoment(@Context HttpHeaders httpHeaders, @PathParam("idMoment") int idMoment) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        User user = APIHelper.getUser(httpHeaders, em);
        em.close();
        emf.close();
        List<Date> momentList = createMomentList();
        if (idMoment < 0 || idMoment >= momentList.size()) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("idMoment out of range.")
                .build();  
        }
        Date date = momentList.get(idMoment);
        Alarm alarm = new Alarm();
        alarm.setDatetimeMoment(date);
        try {
            Alarm result = sendCreateAlarmRequestJMS(user, alarm);
            return Response
                    .status(Response.Status.OK)
                    .entity(result)
                    .build();
            
        } catch (Exception ex) {
            Logger.getLogger(AlarmAPI.class.getName()).log(Level.SEVERE, null, ex);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    @GET
    @Path("test")
    public Response test() {
        return Response
                .status(Response.Status.OK)
                .entity(fabrika.toString())
                .build();  
    }
    private List<Date> createMomentList() {
        LocalTime currentTime = java.time.LocalTime.now();
        LocalDate currentDate = java.time.LocalDate.now();
        LocalDate tommorowDate = currentDate.plusDays(1);
        
        List<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 2; j++) {
                LocalTime time = java.time.LocalTime.of(i, j*30);
                LocalDateTime dateTime;
                Date date;
                if (currentTime.isBefore(time)) {
                    dateTime = LocalDateTime.of(currentDate, time);
                } else {
                    dateTime = LocalDateTime.of(tommorowDate, time);
                    
                }
                date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                dateList.add(date);
            }
        }
        return dateList;
    }
    private Alarm sendCreateAlarmRequestJMS(User user, Alarm alarm) throws JMSException {
            JMSContext ctx = fabrika.createContext();
            JMSProducer producer = ctx.createProducer();
            String correlationId = UUID.randomUUID().toString();
            JMSConsumer consumer = ctx.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +"'");
            ObjectMessage om = ctx.createObjectMessage(alarm);
            om.setStringProperty("OPERATION", "ADD_ALARM");
            om.setIntProperty("ID_USER", user.getIdUser());
            om.setJMSCorrelationID(correlationId);
            producer.send(alarmTopic, om);
            ObjectMessage omr = (ObjectMessage)consumer.receive();
            Alarm result = (Alarm)omr.getObject();
            System.out.println("IDD:" + result.getIdAlarm());
            return result;
    }
}
