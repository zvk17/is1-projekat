/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pametnakucaservis.resources;

import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author user2
 */
public class APIHelper {
    static User getUser(HttpHeaders httpHeaders, EntityManager em) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        
        if(authHeaderValues != null && authHeaderValues.size() > 0){
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            String password = stringTokenizer.nextToken();
            
            User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
            
            return user;
        }
        
        return null;
    }
    
}
