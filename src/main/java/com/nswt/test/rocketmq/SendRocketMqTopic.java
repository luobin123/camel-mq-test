package com.nswt.test.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nswt.test.CreateFlightInfo;

public class SendRocketMqTopic {
	private Logger logger = LoggerFactory.getLogger(SendRocketMqTopic.class);
	private static String TOPIC_NAME = "TOPIC-PROCESS_MESSAGE_BASIC";
	private static String TAG = "QUEUE-MM";
	private String NAME_SRV_ADDR = "10.9.248.4:9876;10.9.248.7:9876";
	private static String CONSUMER_GROUP = "CONSUMER-ODS_SERVICE";
	
	public static void main(String[] args) {
		try {
			new SendRocketMqTopic().sendMQ();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMQ() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer(CONSUMER_GROUP);  
        producer.setNamesrvAddr(NAME_SRV_ADDR);  
        producer.start();  
        
        for (int i = 1; i <= 100; i++) {  
        	String body = "message_test_" + i;
            try {  
                Message msg = new Message(TOPIC_NAME, TAG, (body).getBytes("utf-8"));  
                SendResult sendResult = producer.send(msg);  
                logger.info("body:{}, result status:{}", body, sendResult.getSendStatus() );
            } catch (Exception e) {  
            	logger.error("{} error:", body, e);
            }  
        }  
        producer.shutdown();  
    }  
}
