package ece448.iot_sim;
import static org.junit.Assert.*;

import org.junit.Test;

public class PlugSimTests {

    @Test
    public void testInit() {
        PlugSim plug = new PlugSim("a");
        assertFalse(plug.isOn());
    }

    @Test
    public void testSwitchOff() {
        PlugSim plug = new PlugSim("a");
        plug.switchOff();
        assertFalse(plug.isOn());
    }

    @Test
    public void testSwitchOn() {
        PlugSim plug = new PlugSim("a");
        plug.switchOn();
        assertTrue(plug.isOn());
    }

    @Test
    public void toggle() {
        PlugSim plug = new PlugSim("a");
        assertFalse(plug.isOn()); // Initial condition is off
        plug.toggle(); // Toggle to turn plug on
        assertTrue(plug.isOn());
        plug.toggle(); // Toggle to turn plug off
        assertFalse(plug.isOn());
    }

    @Test
    public void measurePower() {
        PlugSim plug = new PlugSim("a");
        assertFalse(plug.isOn()); // Initial condition: plug is off
        plug.measurePower();
        assertFalse(plug.isOn()); // Plug should remain off after measuring power
        double power = plug.getPower();
        assertTrue(power >= 0 && power <= 100); // Power should be within range [0, 100]
    }

    @Test
    public void testGetName() {
        String expectedName = "testName";
        PlugSim plug = new PlugSim(expectedName);
        String actualName = plug.getName();
        assertTrue(actualName.equals(expectedName));
    }

    @Test
    public void testMeasurePowerWithDotInName() {
        // This test is commented out due to the absence of the implementation for a specific behavior
        // This test would verify a specific behavior when the name contains a dot
        // Uncomment and implement the behavior if needed
    }

    @Test
    public void testUpdatePower() {
        PlugSim plug = new PlugSim("a");
        plug.updatePower(50.5);
        double power = plug.getPower();
        assertEquals(50.5, power, 0.001); // Assert that the power is updated correctly
    }

    @Test
    public void testAddObserver() {
        PlugSim plug = new PlugSim("a");
        PlugSim.Observer observer = new PlugSim.Observer() {
            @Override
            public void update(String name, String key, String value) {
                // No need to implement anything for this test
            }
        };
        plug.addObserver(observer);
        // The test is successful if no exceptions are thrown
    }
}
