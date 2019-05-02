package paxi.maokitty.verify.service.singleton;

/**
 * Created by maokitty on 19/4/28.
 */
public enum SingleTonEnum{
    INSTANCE;
    public static final SingleTonEnum getInstance(){
        return INSTANCE;
    }

}
