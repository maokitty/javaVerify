package paxi.maokitty.verify.designPattern.singleton.service;

/**
 * Created by maokitty on 19/4/28.
 */
public enum SingleTonEnum{
    INSTANCE;
    public static  SingleTonEnum getInstance(){
        return INSTANCE;
    }

}
