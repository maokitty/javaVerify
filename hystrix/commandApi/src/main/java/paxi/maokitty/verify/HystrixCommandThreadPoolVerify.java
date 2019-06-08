package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.command.SelfDefineThreadPoolCommand;
import paxi.maokitty.verify.service.RemoteLogicService;
import paxi.maokitty.verify.util.Setting;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maokitty on 19/6/5.
 */
public class HystrixCommandThreadPoolVerify {
    private static final Logger LOG = LoggerFactory.getLogger(HystrixCommandThreadPoolVerify.class);
    public static void main(String[] args) {
        final RemoteLogicService logicService = new RemoteLogicService();
        smallQueueVerify(logicService);
    }

    private static void smallQueueVerify(final RemoteLogicService logicService) {
        LOG.info("smallQueueVerify only three service will success,other will execute fallback");
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++){
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        SelfDefineThreadPoolCommand command = new SelfDefineThreadPoolCommand(logicService, Setting.SMALL_QUEUE_CORE_SIZE,"another day");
                        command.execute();
                    }catch (Exception e){
                        LOG.error("{}",e);
                    }
                }
            });
        }
        service.shutdown();
    }
}
