/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pametnakucaservis.resources;

import entities.Song;
import entities.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author user2
 */
@Path("user")
public class UserAPI {

    @Resource(lookup="fabrika")
    ConnectionFactory fabrika;
    
    @Resource(lookup="alarmTopic")
    Topic alarmTopic;
    
    private String persistanceUnitName = "PametnaKucaServisPU_2"; 
    
    @POST    
    @Path("register")
    public Response register(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("homeLocation") String homeLocation
    ) {
        if (username == null || password == null || homeLocation == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("all parameters required")
                    .build();
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        List<User> queryResult = getUserByUsername(username, em);
        if (queryResult.size() != 0) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("username already taken")
                    .build();
        }
        Song song = em.find(Song.class, 1);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setHomeLocation(homeLocation);
        user.setIdSong(song);
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        emf.close();
        return Response
            .status(Response.Status.OK)
            .entity(user)
            .build();
                
    }
    
    @POST
    @Path("login")
    
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        if (username == null || password == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("all parameters required")
                    .build();
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
        EntityManager em = emf.createEntityManager();
        List<User> queryResult = getUserByUsername(username, em);
        em.close();
        emf.close();
        System.out.println("size: " + queryResult.size());
        if (queryResult.size() == 0) {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity("bad credentials")
                    .build();
        }
        if (queryResult.size() > 1) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("username duplicate")
                    .build();
        }
        User user = queryResult.get(0);
        if (!user.getPassword().equals(password)) {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity("bad credentials")
                    .build();
        }
        JMSContext context = fabrika.createContext();
        JMSProducer producer = context.createProducer();
        Message message = context.createMessage();
        try {
            message.setIntProperty("ID_USER", user.getIdUser());
            message.setStringProperty("OPERATION", "SET_USER");
        } catch (JMSException ex) {
            Logger.getLogger(UserAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        producer.send(alarmTopic, message);
        return Response
                .status(Response.Status.OK)
                .entity(queryResult.get(0))
                .build();
        
    }
    private List<User> getUserByUsername(String username, EntityManager em) {
        TypedQuery<User> query = em.createNamedQuery("User.findByUsername", User.class);
        List<User> queryResult = query.setParameter("username", username).getResultList();
        return queryResult;
    }
    
}
