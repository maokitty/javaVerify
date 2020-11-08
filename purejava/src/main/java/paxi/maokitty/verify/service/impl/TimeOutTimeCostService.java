package paxi.maokitty.verify.service.impl;

import paxi.maokitty.verify.service.TimeCostService;

import java.util.concurrent.TimeoutException;

/**
 * Created by maokitty on 20/11/8.
 */
public class TimeOutTimeCostService implements TimeCostService{

    @Override
    public String delay(String thing, long mills) {
        System.out.println("thing:"+thing+" will time out");
        throw new RuntimeException(thing + " time out");
    }
}
