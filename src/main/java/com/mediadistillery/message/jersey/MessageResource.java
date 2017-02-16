package com.mediadistillery.message.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.jersey.JerseyBroadcaster;
import java.util.logging.Logger;

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
     * @param message a {@link Message}
     * @return a {@link Response}
     */
    @Broadcast(writeEntity = false)
    @POST
    @Produces("application/json")
    public Message broadcast(Message message) {
        //return new Response(message.getId(), message.getMessage());
    	return new Message(message.getId(), message.getMessage());
    }

    public static final class OnDisconnect extends AtmosphereResourceEventListenerAdapter {
        //private final Logger logger = LoggerFactory.getLogger(ChatResource.class);

        /**
         * {@inheritDoc}
         */
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
