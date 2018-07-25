package com.kumud.vertx.demo.EventbusDemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Hello world!
 *
 */
public class ServicesTwo extends AbstractVerticle {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesTwo.class);
			
	
    public static void main( String[] args ) {
    	
    	VertxOptions vertxOptions = new VertxOptions();
    	
	    vertxOptions.setClustered(true);
	    
	    Vertx.clusteredVertx(vertxOptions, results -> {
	    	
		      if (results.succeeded()) {
		    	  
		    	  Vertx vertx = results.result();
		    	  
		    	  vertx.deployVerticle(new ServicesTwo());
		      }
		      
	    });
	    

//    	Vertx vertx = Vertx.vertx();
    	
//    	vertx.deployVerticle(new VertxMongoVerticle());
    }
    
    @Override
    public void start() {
        vertx.eventBus().consumer("eventbusservice", message -> {
        	System.out.println("Recevied message: " + message.body());
            dispatchMessage(message);
        });
    }

    private void dispatchMessage(final Message<Object> message) {

    	message.reply(new JsonObject().put("responseCode", "OK").put("message", "This is your response to your event"));
    }

}

