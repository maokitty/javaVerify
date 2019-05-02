package paxi.maokitty.verify.service.singleton;

/**
 * Created by maokitty on 19/4/28.
 */
public class StaticInnerSingleTonService {
    private StaticInnerSingleTonService(){};
    private static class SingletonHolder{
        public  static  final StaticInnerSingleTonService INSTANCE=new StaticInnerSingleTonService();
    }

    public static final StaticInnerSingleTonService getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
