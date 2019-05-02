package paxi.maokitty.verify.service.singleton;

/**
 * Created by maokitty on 19/4/28.
 */
public class SingleTonStaticInnerClassService {
    private SingleTonStaticInnerClassService(){};
    private static class SingletonHolder{
        public  static  final SingleTonStaticInnerClassService INSTANCE=new SingleTonStaticInnerClassService();
    }

    public static final SingleTonStaticInnerClassService getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
