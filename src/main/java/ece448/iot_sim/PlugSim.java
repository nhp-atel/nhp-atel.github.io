package ece448.iot_sim;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlugSim {

    private final String name;
    private boolean on = false;
    private double power = 0.0; // in watts
    private List<Observer> observers = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(PlugSim.class);

    public PlugSim(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public interface Observer {
        void update(String name, String key, String value);
    }

    synchronized public void addObserver(Observer observer) {
        observers.add(observer);
        observer.update(name, "state",on?"on":"off");
        observer.update(name,"power",String.format("%.3f",power));
    }

    private void notifyObservers(String key, String value) {
        for (Observer observer : observers) {
            observer.update(this.name, key, value);
        }
    }

    synchronized public void switchOn() {
        if (!this.on) {
            updateState(true);
            // Measure and notify power only if there's a state change
            measurePower();
        }
    }

    synchronized public void switchOff() {
        if (this.on) {
            updateState(false);
            // Measure and notify power only if there's a state change
            measurePower();
        }
    }

    synchronized public void toggle() {
        updateState(!this.on);
        // Always measure and notify power after toggling
        measurePower();
    }

    protected void updateState(boolean o) {
        on = o;
        logger.info("Plug {}: state {}", name,on?"on":"off");
        for(Observer observer: observers){
            observer.update(name,"state",on?"on":"off");
        }
    }

    synchronized public void measurePower() {
        if (!on) {
            updatePower(0.0);
        } else {
            double measuredPower = 0.0;
    
            // Simulate power measurement based on different scenarios
            if ("low".equals(name)) {
                measuredPower = 50 + Math.random() * 100; // Random value between 50 and 150
            } else if ("medium".equals(name)) {
                measuredPower = 130 + Math.random() * 70; // Random value between 130 and 200
            } else if ("high".equals(name)) {
                measuredPower = 150 + Math.random() * 150; // Random value between 150 and 300
            } else {
                measuredPower = Math.random() * 100; // Default random value for other plugs
            }
    
            updatePower(measuredPower);
        }
    }

    protected void updatePower(double p) {
        power = p;
        logger.debug("Plug {}: power {}", name, power);
        notifyObservers("power", String.format("%.3f", power));
    }

    synchronized public boolean isOn() {
        return on;
    }

    synchronized public double getPower() {
        return power;
    }
}
