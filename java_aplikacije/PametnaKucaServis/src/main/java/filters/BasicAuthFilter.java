/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import entities.User;
import java.io.IOException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author user2
 */
@Provider
public class BasicAuthFilter implements ContainerRequestFilter {
    @PersistenceContext(unitName = "PametnaKucaServisPU")
    EntityManager em;
    private String invalidCredentials = "Invalid credentials.";
    
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        if (isRegisterPage(requestContext)) {
            return;
        }
        
        
        List<String> authHeaderValues = requestContext.getHeaders().get("Authorization");
        
        if (authHeaderValues == null || authHeaderValues.size() == 0) {
            Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Credentials required.").build();
            requestContext.abortWith(response);
            return;
        }
        String authHeaderValue = authHeaderValues.get(0);
        String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
        StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
        if (stringTokenizer.countTokens() != 2) {
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(invalidCredentials)
                    .build();
            requestContext.abortWith(response);
            return;
        }
        String username = stringTokenizer.nextToken();
        String password = stringTokenizer.nextToken();
        
        
        List<User> users = em.createNamedQuery("User.findByUsername", User.class)
                            .setParameter("username", username)
                            .getResultList();
        
        if(users.size() != 1){
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(invalidCredentials)
                    .build();
            requestContext.abortWith(response);
            return;
        }
        User user = users.get(0);            
        if(!user.getPassword().equals(password)){
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(invalidCredentials)
                    .build();
            requestContext.abortWith(response);
            return;
        }
        return;//ok
    }

    private boolean isRegisterPage(ContainerRequestContext requestContext) {
        
        
        UriInfo uriInfo = requestContext.getUriInfo();
        List<PathSegment> pathSegments = uriInfo.getPathSegments();
        String pathSegment1 = null;
        String endpointName = pathSegments.get(0).getPath();
        if(pathSegments.size() > 1)
                pathSegment1 = pathSegments.get(1).getPath();
        System.out.println(endpointName + " >:> " + pathSegment1);
        if ("user".equals(endpointName)) {
            return true;
        }
        return false;
    }

    
    
}
