package paxi.maokitty.verify.service;

import paxi.maokitty.verify.constant.LightColor;
import paxi.maokitty.verify.command.inter.TrafficLightObserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maokitty on 19/6/9.
 */
public class TrafficLightsSubject {
    private Map<String,TrafficLightObserver> observers = new ConcurrentHashMap<>();
    private LightColor colorState;

    public void setColorState(LightColor colorState) {
        this.colorState = colorState;
    }

    public LightColor getColorState() {
        return colorState;
    }

    public void attach(TrafficLightObserver observer){
        observers.put(observer.observerId(),observer);
    }

    public void deAttach(String observerId){
        observers.remove(observerId);
    }

    public void notifyAllObserver(){
        for (Map.Entry<String,TrafficLightObserver> entry:observers.entrySet()){
            entry.getValue().logicHandle(colorState);
        }
    }


}
