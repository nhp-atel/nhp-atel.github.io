package ece448.iot_sim;

import java.util.List;
import java.util.TreeMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTCommands {
    private static final Logger logger = LoggerFactory.getLogger(MQTTCommands.class);
    private final TreeMap<String, PlugSim> plugs = new TreeMap<>();
    private final String topicPrefix;
    private final MqttClient mqttClient;

    public MQTTCommands(MqttClient mqttClient, List<PlugSim> plugs, String topicPrefix) {
        this.mqttClient = mqttClient;
        this.topicPrefix = topicPrefix;
        for (PlugSim plug : plugs) {
            this.plugs.put(plug.getName(), plug);
        }
        // Subscribe to action topics upon initialization
        subscribeToActions();
    }

    private void subscribeToActions() {
        try {
            String actionTopic = getTopic();
            mqttClient.subscribe(actionTopic, (topic, msg) -> handleMessage(topic, msg));
            logger.info("Subscribed to action topic: {}", actionTopic);
        } catch (MqttException e) {
            logger.error("Failed to subscribe to action topics", e);
        }
    }

    public String getTopic() {
        return topicPrefix + "/action/#";
    }

    public void handleMessage(String topic, MqttMessage msg) {
        try {
            String payload = new String(msg.getPayload());
            logger.info("Received command: {}, Message: {}", topic, payload);

            String[] topicParts = topic.split("/");
            if (topicParts.length < 4) {
                logger.error("Invalid topic format: {}", topic);
                return;
            }

            String plugName = topicParts[topicParts.length - 2];
            String action = topicParts[topicParts.length - 1];

            PlugSim plug = plugs.get(plugName);
            if (plug == null) {
                logger.error("Plug not found: {}", plugName);
                // Consider publishing an error message to an MQTT topic here
                return;
            }

            switch (action.toLowerCase()) {
                case "on":
                    plug.switchOn();
                    break;
                case "off":
                    plug.switchOff();
                    break;
                case "toggle":
                    plug.toggle();
                    break;
                default:
                    logger.error("Unknown action: {}", action);
                    //publishing an error message to an MQTT topic here
            }
        } catch (Exception e) {
            logger.error("Error handling MQTT message", e);
        }
    }

   
}
