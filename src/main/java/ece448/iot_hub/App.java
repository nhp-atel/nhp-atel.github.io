package ece448.iot_hub;


import org.springframework.beans.factory.annotation.Autowired;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class App {
	

	@Autowired
	public App(Environment env) throws Exception {
		
	}
	@Bean(destroyMethod = "close")
	public MqttController createContoller(Environment env) throws Exception{
		MqttController c = new MqttController(env.getProperty("mqtt.broker"),env.getProperty("mqtt.clientId"), env.getProperty("mqtt.topicPrefix"));
		c.start();
		return c;
	}


	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
}
