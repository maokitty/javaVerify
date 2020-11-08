package paxi.maokitty.verify.service.impl;

import paxi.maokitty.verify.service.TimeCostService;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 20/11/8.
 */
public class RandomTomeCostService implements TimeCostService{
    @Override
    public String delay(String thing, long mills) {
        try {
            System.out.println(thing+" random cost most:"+mills);
            TimeUnit.MILLISECONDS.sleep((long)Math.ceil(Math.random()*mills));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return thing+" done";
    }
}
