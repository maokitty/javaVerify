package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.constant.LightColor;
import paxi.maokitty.verify.command.inter.TrafficLightObserver;

/**
 * Created by maokitty on 19/6/9.
 */
public class PeopleObserver implements TrafficLightObserver {
    private static final Logger LOG = LoggerFactory.getLogger(PeopleObserver.class);
    private long id;

    public PeopleObserver(long id,TrafficLightsSubject subject) {
        this.id = id;
        subject.attach(this);
    }

    @Override
    public String observerId() {
        return "People:"+id;
    }

    @Override
    public void logicHandle(LightColor colorState) {
        switch (colorState){
            case RED:LOG.info("logicHandle :{},traffic lights is {} stop walk",observerId(),colorState); return;
            case YELLOW:LOG.info("logicHandle :{},traffic lights is {} reday to walk",observerId(),colorState);return;
            case GREEN:LOG.info("logicHandle :{},traffic lights is {}  walk",observerId(),colorState);return;
        }
    }
}
