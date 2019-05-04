package paxi.maokitty.verify.service.singleton;

/**
 * Created by maokitty on 19/5/2.
 */
public class DoubleCheckSingleTonService {
    private static volatile DoubleCheckSingleTonService INSTANCE;
    public static  DoubleCheckSingleTonService getInstance(){
        if (INSTANCE==null){
            synchronized (DoubleCheckSingleTonService.class){
              if (INSTANCE == null){
                  INSTANCE = new DoubleCheckSingleTonService();
              }
            }
        }
        return INSTANCE;
    }
    private DoubleCheckSingleTonService() {
    }
}
