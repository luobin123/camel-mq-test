package com.caair.soc.message;

import com.caair.soc.message.mq.ActiveMQParseMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:META-INF/spring/root.xml")
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws MQClientException {
        ApplicationContext context = SpringApplication.run(Application.class, args);

//        context.getBean(ActiveMQParseMessageListener.class);
//        DefaultMQPushConsumer consumer = context.getBean(DefaultMQPushConsumer.class);
//        logger.info("start consumer: {}", consumer);
//        consumer.start();
    }
}