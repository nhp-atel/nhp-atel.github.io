package ece448.iot_sim;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class HTTPCommandsTests {

    private HTTPCommands httpCommands;
    private PlugSim plug1, plug2;

    @Before
    public void setUp() {
        plug1 = new PlugSim("plug1");
        plug2 = new PlugSim("plug2");
        
        httpCommands = new HTTPCommands(Arrays.asList(plug1, plug2));
    }

    @Test
    public void testListAllPlugs() {
        String response = httpCommands.handleGet("/", new HashMap<String, String>());
        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain plug1 and plug2", response.contains("plug1") && response.contains("plug2"));
    }

    @Test
    public void testSwitchOnPlug() {
        Map<String, String> params = new HashMap<>();
        params.put("action", "on");
        httpCommands.handleGet("/plug1", params);
        assertTrue("Plug1 should be on", plug1.isOn());
    }

    @Test
    public void testSwitchOffPlug() {
        plug1.switchOn(); // Ensure plug1 is on before trying to switch off
        Map<String, String> params = new HashMap<>();
        params.put("action", "off");
        httpCommands.handleGet("/plug1", params);
        assertFalse("Plug1 should be off", plug1.isOn());
    }

    @Test
    public void testTogglePlug() {
        plug1.switchOff(); // Make sure plug1 is off before toggling
        Map<String, String> params = new HashMap<>();
        params.put("action", "toggle");
        httpCommands.handleGet("/plug1", params);
        assertTrue("Plug1 should be on after toggle", plug1.isOn());
        
        httpCommands.handleGet("/plug1", params); // Toggle again
        assertFalse("Plug1 should be off after toggle", plug1.isOn());
    }

    @Test
    public void testPlugStatusReport() {
        String response = httpCommands.handleGet("/plug1", new HashMap<String, String>());
        assertNotNull("Response should not be null", response);
        assertTrue("Response should indicate plug1 is off", response.contains("Plug plug1 is off"));
    }

    @Test
    public void testInvalidPath() {
        String response = httpCommands.handleGet("/invalidPlug", new HashMap<String, String>());
        assertNull("Response should be null for an invalid path", response);
    }

    @Test
    public void testInvalidAction() {
        Map<String, String> params = new HashMap<>();
        params.put("action", "invalidAction");
        String response = httpCommands.handleGet("/plug1", params);
        assertNull("Response should be null for an invalid action", response);
    }

    @Test
    public void testActionWithoutParameters() {
        String response = httpCommands.handleGet("/plug1", new HashMap<String, String>());
        assertNotNull("Response should not be null even without action parameters", response);
        assertTrue("Response should report on plug1's status", response.contains("Plug plug1 is"));
    }

    @Test
    public void testSwitchOnNonexistentPlug() {
        Map<String, String> params = new HashMap<>();
        params.put("action", "on");
        String response = httpCommands.handleGet("/plug3", params);
        assertNull("Response should be null for a non-existent plug", response);
    }
    
    
}
