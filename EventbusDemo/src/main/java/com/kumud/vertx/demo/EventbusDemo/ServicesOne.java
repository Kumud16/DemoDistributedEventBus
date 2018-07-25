package com.kumud.vertx.demo.EventbusDemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Hello world!
 *
 */
public class ServicesOne extends AbstractVerticle {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesOne.class);
			
	
    public static void main( String[] args ) {
    	
    	VertxOptions vertxOptions = new VertxOptions();
    	
	    vertxOptions.setClustered(true);
	    
	    Vertx.clusteredVertx(vertxOptions, results -> {
	    	
		      if (results.succeeded()) {
		    	  
		    	  Vertx vertx = results.result();
		    	  
		    	  vertx.deployVerticle(new ServicesOne());
		      }
		      
	    });
	    

//    	Vertx vertx = Vertx.vertx();
    	
//    	vertx.deployVerticle(new VertxMongoVerticle());
    }
    
    @Override
    public void start() {
     /*   vertx.createHttpServer()
                .requestHandler(httpRequest -> handleHttpRequest(httpRequest) )
                .listen(8080);*/
    	
    	vertx.eventBus().consumer("eventbusservice", message -> {
        	System.out.println("Recevied message: " + message.body());
            dispatchMessage(message);
        });
        
        vertx.setTimer(5000, handler ->{
        	sendTestEvent();
	    });
        
        
    }
    
    private void dispatchMessage(final Message<Object> message) {

    	message.reply(new JsonObject().put("responseCode", "OK").put("message", "This is your response to your event"));
    }

    private void sendTestEvent() {
		// TODO Auto-generated method stub
    JsonObject testInfo = new JsonObject();
	    
	    testInfo.put("info", "Hi");

	    System.out.println("Sending message=" + testInfo.toString());
	    		
		vertx.eventBus().send("eventbusservice", testInfo.toString(), reply -> {
			
			if (reply.succeeded()) {
                /* Send the result from HelloWorldService to the http connection. */
                
                JsonObject replyResults = (JsonObject) reply.result().body();
				
				System.out.println("Got Reply message=" + replyResults.toString());
                
            } else {
            	System.out.println("Can't send message to hello service");
            }
			
		});

    	
		
	}

	private void handleHttpRequest(final HttpServerRequest httpRequest) {

    	JsonObject testInfo = new JsonObject();
	    
	    testInfo.put("info", "Hi");

	    System.out.println("Sending message=" + testInfo.toString());
	    		
		vertx.eventBus().send("eventbusservice", testInfo.toString(), response -> {
			
			if (response.succeeded()) {
                /* Send the result from HelloWorldService to the http connection. */
                httpRequest.response().end(response.result().body().toString());
                
                JsonObject replyResults = (JsonObject) response.result().body();
				
				System.out.println("Got Reply message=" + replyResults.toString());
                
            } else {
            	LOGGER.error("Can't send message to hello service", response.cause());
                httpRequest.response().setStatusCode(500).end(response.cause().getMessage());
            }
			
		});

    	
    	
    	
    }	
}

