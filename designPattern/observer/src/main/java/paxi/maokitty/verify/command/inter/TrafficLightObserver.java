package paxi.maokitty.verify.command.inter;

import paxi.maokitty.verify.constant.LightColor;

/**
 * Created by maokitty on 19/6/9.
 */
public interface TrafficLightObserver {
    String observerId();
    void logicHandle(LightColor colorState);
}
