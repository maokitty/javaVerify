package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.constant.LightColor;
import paxi.maokitty.verify.service.inter.TrafficLightObserver;

/**
 * Created by maokitty on 19/6/9.
 */
public class CarObserver implements TrafficLightObserver {
    private static final Logger LOG = LoggerFactory.getLogger(CarObserver.class);
    private long id;

    public CarObserver(long id,TrafficLightsSubject subject) {
        this.id = id;
        subject.attach(this);
    }

    @Override
    public String observerId() {
        return "car"+id;
    }

    @Override
    public void logicHandle(LightColor colorState) {
        switch (colorState){
            case RED:LOG.info("logicHandle :{},traffic lights is {} stop run",observerId(),colorState); return;
            case YELLOW:LOG.info("logicHandle :{},traffic lights is {} reday to run",observerId(),colorState);return;
            case GREEN:LOG.info("logicHandle :{},traffic lights is {}  run",observerId(),colorState);return;
        }
    }
}
