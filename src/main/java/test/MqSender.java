package test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MqSender {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) throws InterruptedException, IOException {
//		for(int i =0; i <= 10000; i++){
////            Thread.sleep(1000);
//            new MqSender().sendMessage("QUEUE_CONCURRENT_TEST", "CONTENT:"+i, "failover:(nio://10.9.248.9:61618,nio://10.9.248.10:61618,nio://10.9.248.11:61618,nio://10.9.248.13:61618,nio://10.9.249.26:61618)?randomize=false&timeout=3000&initialReconnectDelay=15000@admin@admin");
//		}
        String s = new MqSender().readFile("");
        new MqSender().sendMessage("FLT_PUB_TEST2", s, "failover:(nio://10.9.248.9:61618,nio://10.9.248.10:61618,nio://10.9.248.11:61618,nio://10.9.248.13:61618,nio://10.9.249.26:61618)?randomize=false&timeout=3000&initialReconnectDelay=15000@admin@admin");
    }

    public String readFile(String fileName) throws IOException {
	    File file = new File("src/main/resources/batch_msg_test.txt");
        logger.info(file.getAbsolutePath());
        String fileContent = FileUtils.readFileToString(file, "UTF-8");
        return fileContent;
    }

    /**
     * 发送queue消息
     * @param queueName 队列名
     * @param messageText 消息内容文本
     * @param mqServerInfo 服务器信息（IP端口@用户@密码）
     */
     public boolean sendMessage(String queueName, String messageText, String mqServerInfo){
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;
//        appConfig = ConfigFactory.load(System.getProperty("etl.config"));
        String mqUser = "";
        String mqPassword = "";
        String mqUrl = "";
        String[] mqServerInfoSplit = mqServerInfo.split("@");
        if(mqServerInfoSplit.length>=3){
            mqUrl = "" + mqServerInfoSplit[0];
            mqUser = mqServerInfoSplit[1];
            mqPassword = mqServerInfoSplit[2];
        } else {
            logger.error("the param mq server is not avaliable. ");
        }
        connectionFactory = new ActiveMQConnectionFactory(
                mqUser, mqPassword, mqUrl);
        try{
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            producer.send(session.createTextMessage(messageText));
            session.commit();
            logger.info("Message sent. queue:{}, messageText:{}. ", queueName, messageText);
            return true;
        }catch (Exception e){
        	e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            }catch (Throwable ignore){}
        }
    }
}
