/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pametnakucaservis.resources;

import entities.Song;
import entities.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.PersistenceContext;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.ws.rs.core.Context;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author user2
 */

@Path("song")
public class SongReproductionAPI {
    @Resource(lookup="fabrika")
    ConnectionFactory fabrika;
    
    @Resource(lookup="zvukTopic")
    Topic zvukTopic;
    
    @Resource(lookup="servisTopic")
    Topic servisTopic;
    
    
    String persistenceUnitName = "PametnaKucaServisPU";
    @PersistenceContext(unitName = "PametnaKucaServisPU")
    EntityManager em;  
    
    @GET
    @Path("test")
    public Response test() {
        return Response
                .status(Response.Status.OK)
                .entity(fabrika.toString())
                .build();  
    }
    @GET
    @Path("play/{idSong}")
    public Response playSong(@Context HttpHeaders httpHeaders, @PathParam("idSong") int idSong) {

        User user = APIHelper.getUser(httpHeaders, em);
        int idUser = user.getIdUser();
        Song song = em.find(Song.class, idSong);

        if (song == null) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .build(); 
        }
        //System.out.println(fabrika);
        JMSContext ctx = fabrika.createContext();
        JMSProducer producer = ctx.createProducer();
        Message msg = ctx.createMessage();
        try {
            msg.setStringProperty("OPERATION", "PLAY_SONG");
            msg.setIntProperty("ID_SONG", idSong);
            msg.setIntProperty("ID_USER", idUser);
            producer.send(zvukTopic, msg);
        } catch (Exception ex) {
            Logger.getLogger(SongReproductionAPI.class.getName()).log(Level.SEVERE, null, ex);
             return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Server error")
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(song)
                .build();
        
    }
    @GET
    @Path("list") 
    public Response list() {

        TypedQuery<Song> query = em.createNamedQuery("Song.findAll", Song.class);
        List<Song> songList = query.getResultList();
        
        return Response
                .status(Response.Status.OK)
                .entity(new GenericEntity<List<Song>>(songList){})
                .build();
    }
    
    @GET
    @Path("played_list")
    
    public Response playedList(@Context HttpHeaders httpHeaders) {

        User user = APIHelper.getUser(httpHeaders, em);
                
        JMSContext ctx = fabrika.createContext();
        try {

            JMSProducer producer = ctx.createProducer();
            //producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //System.out.println("Delivery " + producer.getDeliveryMode());
            //TemporaryTopic tempTopic = ctx.createTemporaryTopic();
            String correlationId = UUID.randomUUID().toString();
            System.out.println("CORREL_ID: " + correlationId);
            JMSConsumer consumer = ctx.createConsumer(servisTopic, "JMSCorrelationID='" + correlationId +  "'");
           
            Message message = ctx.createMessage();
            message.setStringProperty("OPERATION", "GET_PLAYED_LIST");
            message.setIntProperty("ID_USER",user.getIdUser());
            message.setJMSCorrelationID(correlationId);
            
            producer.send(zvukTopic, message);
            
            ObjectMessage receivedMessage =  (ObjectMessage)consumer.receive();
            int size = receivedMessage.getIntProperty("SIZE");
            List<Song> songList;
            if (size != 0) {
                Song[] songArray = (Song[]) receivedMessage.getObject();
                songList = Arrays.asList(songArray);
            } else {
                songList = new ArrayList<Song>();
            }
            
        
            return Response
                .status(Response.Status.OK)
                .entity(new GenericEntity<List<Song>>(songList){})
                .build();
            
        } catch (Exception ex) {
            Logger.getLogger(SongReproductionAPI.class.getName()).log(Level.SEVERE, null, ex);
            return Response
                    .status(Response.Status.OK)
                    .entity(ex.toString())
                    .build();
        }
        
        
    }

    
    
}
