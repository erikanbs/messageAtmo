package com.mediadistillery.message.jersey;

import java.net.URLDecoder;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.jersey.JerseyBroadcaster;

import com.sun.jersey.api.container.MappableContainerException;

@Path("/")
@AtmosphereService (broadcaster = JerseyBroadcaster.class)
public class MessageResource {
	
	private static Logger log = Logger.getLogger(MessageResource.class.getName());
	
    /**
     * Suspend the response without writing anything back to the client.
     *
     * @return a white space
     */
    @Suspend(contentType = "application/json", listeners = {OnDisconnect.class})
    @GET
    public String suspend() {
        return "";
    }

    /**
     * Broadcast the received message object to all suspended response. Do not write back the message to the calling connection.
     *
     */
    @Broadcast(writeEntity = false)
    @POST
    @Produces("application/json")
    public Message broadcast(@HeaderParam("Authentication") String userUUID, Message message) {
    	String UUID = "";
    			
    	try {
    		UUID = URLDecoder.decode(userUUID, "UTF-8");
    	} catch (Exception e) {
    		throw new MappableContainerException(new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build()));
    	}
    	return new Message(UUID, message.getMessage());
    }

    public static final class OnDisconnect extends AtmosphereResourceEventListenerAdapter {

        @Override
        public void onDisconnect(AtmosphereResourceEvent event) {
            if (event.isCancelled()) {
                log.info("Browser {} unexpectedly disconnected");
            } else if (event.isClosedByClient()) {
                log.info("Browser {} closed the connection");
            }
        }
    }

}
