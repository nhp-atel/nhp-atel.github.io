package ece448.iot_hub;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PlugsResourceTest {

    private static String broker = "tcp://127.0.0.1";
    private static String clientId = "001";
    private static String topicPrefix = "dddd";

    @Test
    public void testGetPlugActionIsNull() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object plug = r.getPlug("a", null);
        assertTrue(plug != null);
    }

    @Test
    public void testGetPlugActionIsOn() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object plug = r.getPlug("a", "on");
        assertTrue(plug != "on");
    }

    @Test
    public void testGetPlugActionIsOff() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object plug = r.getPlug("a", "off");
        assertTrue(plug != "off");
    }

    @Test
    public void testGetPlugWithUnsupportedAction() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object plug = r.getPlug("a", "Unsupported Action");

    }

    @Test
    public void testTogglePlugAction() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        try {
            r.getPlug("a", "toggle");
            
        } catch (Exception e) {
            assertTrue("Toggle action should not throw an exception", false);
        }
    }

    @Test
    public void testGetNonExistentPlug() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object plug = r.getPlug("nonexistent", null);
        assertTrue("Method should handle non-existent plugs gracefully", plug != null);
    }

    @Test
    public void testGetPlugsReturnsResult() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);

        
        Object result = r.getPlugs();

        
        assertNotNull("Method should return a result", result);
    }

    @Test
    public void testGetPlugsReturnsList() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);

        
        Object result = r.getPlugs();

        
        assertTrue("Method should return an instance of List", result instanceof List);
    }

    @Test
    public void testGetPlugsWithNoPlugsConfigured() throws Exception {
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object result = r.getPlugs();

        assertTrue("Method should return an instance of List", result instanceof List);
        assertTrue("List should be empty when no plugs are configured", ((List) result).isEmpty());
    }

    @Test
    public void testGetPlugsWithNotNull() throws Exception{
        MqttController c = new MqttController(broker, clientId, topicPrefix);
        PlugsResource r = new PlugsResource(c);
        Object result = r.getPlugs();
        assertNotNull("Result should not be null", result);
    }
    


}
