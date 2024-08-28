package ece448.iot_hub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ece448.iot_sim.HTTPCommands;
@RestController
public class PlugsResource {

    private static final Logger logger = LoggerFactory.getLogger(PlugsResource.class);

    public MqttController controller;
   

    public PlugsResource(MqttController controller) {
        this.controller = controller;
        
    }

    @GetMapping("/api/plugs")
    public Object getPlugs() {
        ArrayList<Map<String, String>> plugs = new ArrayList<>();
        Map<String, String> states = controller.getStates();
        logger.info("states: {}", states);
        for (String name : states.keySet()) {
            Map<String, String> plug = new HashMap<>();
            plug.put("name", name);
            plug.put("state", controller.getState(name));
            plug.put("power", controller.getPower(name));
            plugs.add(plug);
        }
        return plugs;
    }

    @GetMapping("/api/plugs/{plug:.+}")
    public Object getPlug(@PathVariable("plug") String plugName,
            @RequestParam(name = "action", required = false) String action) throws MqttException {
        if (action == null) {
            Map<String, String> plug = new HashMap<>();
            plug.put("name", plugName);
            plug.put("state", controller.getState(plugName));
            plug.put("power", controller.getPower(plugName));
            return plug;
        }
        controller.publishAction(plugName, action);
        return null;
    }
    
}
