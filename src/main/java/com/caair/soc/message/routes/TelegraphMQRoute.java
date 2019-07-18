package com.caair.soc.message.routes;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.caair.soc.message.mq.ActiveMQParseMessageListener;

@Component
//@Profile("telegraph")
public class TelegraphMQRoute extends RouteBuilder {
	
	@Autowired
   	private ActiveMQParseMessageListener activeMQParseMessageListener;

    private Logger logger = LoggerFactory.getLogger(TelegraphMQRoute.class);

    @Override
    public void configure() {
    	//其他类
        String fromURI = "activemq:queue:FLT_PUB_TEST2?destination.consumer.exclusive=true"
    	+"&destination.jms.optimizeAcknowledge=true"+"&destination.jms.optimizeAcknowledgeTimeOut=3000"+
        						   "&destination.jms.redeliveryPolicy.maximumRedeliveries=6";
        logger.info("fromURI: {}", fromURI);
        from(fromURI).bean(activeMQParseMessageListener);
    }
}