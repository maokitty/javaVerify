package paxi.maokitty.verify;

import paxi.maokitty.verify.constant.LightColor;
import paxi.maokitty.verify.service.CarObserver;
import paxi.maokitty.verify.service.PeopleObserver;
import paxi.maokitty.verify.service.TrafficLightsSubject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by maokitty on 19/6/9.
 */
public class ObserverVerify {
    public static void main(String[] args) {
        AtomicLong ID_FACTORY = new AtomicLong();
        TrafficLightsSubject subject = new TrafficLightsSubject();
        new PeopleObserver(ID_FACTORY.incrementAndGet(),subject);
        new CarObserver(ID_FACTORY.incrementAndGet(),subject);
        new PeopleObserver(ID_FACTORY.incrementAndGet(),subject);
        subject.setColorState(LightColor.RED);
        subject.notifyAllObserver();
        subject.setColorState(LightColor.YELLOW);
        subject.notifyAllObserver();
        subject.setColorState(LightColor.GREEN);
        subject.notifyAllObserver();
    }
}
