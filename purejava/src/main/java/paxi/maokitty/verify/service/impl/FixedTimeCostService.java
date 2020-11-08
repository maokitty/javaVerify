package paxi.maokitty.verify.service.impl;

import paxi.maokitty.verify.service.TimeCostService;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 20/11/8.
 */
public class FixedTimeCostService implements TimeCostService{


    @Override
    public String delay(String thing,long mills) {
        try {
            System.out.println(Thread.currentThread().getName()+" doing thing:"+thing+" will cost mills:"+mills);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return thing+" done";
    }
}
