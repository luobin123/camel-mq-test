package com.caair.soc.message.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQParseMessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String UTF_8 = "UTF-8";

//    @Override
    public void onMessage(Message message) throws InterruptedException  {
		logger.info(""+ message.getBody().toString().length());
//    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//    	System.out.print("start:" + sdf.format(new Date()));
//    	long sleep = (long)(Math.random()*10000);
//    	System.out.print(", sleep:" + sleep);
//    	Thread.sleep(sleep);
//
//    	System.out.print(", message :" + message.getBody().toString());
//    	System.out.print(", end :" + sdf.format(new Date()));
//    	System.out.println(" finish.");
    	
//    	String messageContent = message.getBody().toString();
//    	Integer sleepTime = Integer.parseInt(messageContent);
//    	for(int i = 0; i<= sleepTime; i++){
//    		System.out.print(i+",");
//    		Thread.sleep(sleep);
//    	}
//    	logger.info("finish.");
    }


}
