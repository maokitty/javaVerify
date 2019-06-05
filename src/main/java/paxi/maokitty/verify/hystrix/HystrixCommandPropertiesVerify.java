package paxi.maokitty.verify.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.command.DefaultSettingCommand;
import paxi.maokitty.verify.hystrix.command.SelfDefineCommandPropertiesCommand;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maokitty on 19/5/23.
 */
public class HystrixCommandPropertiesVerify {
    private static final Logger LOG = LoggerFactory.getLogger(HystrixCommandPropertiesVerify.class);

    public static void main(String[] args) {
        RemoteLogicService logicService = new RemoteLogicService();
        defaultCommandVerify(logicService);
        errorAndVolumnVerify(logicService);
        fallbackIsolationVerify(logicService);
    }

    private static void fallbackIsolationVerify(final RemoteLogicService logicService) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        LOG.info("fallbackIsolationVerify   commandKey fallback execution rejected.");
        for (int i = 0; i < 2; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        SelfDefineCommandPropertiesCommand command = new SelfDefineCommandPropertiesCommand(logicService, "1", Setting.FALLBACK_ISOLATION_SEMAPHORE);
                        command.execute();
                    } catch (Exception e) {
                        LOG.error("fallbackIsolationVerify", e);
                    }
                }
            });
        }
        executor.shutdown();
    }

    private static void defaultCommandVerify(RemoteLogicService logicService) {
        String world="default command";
        DefaultSettingCommand defaultCommand = new DefaultSettingCommand(logicService,world);
        defaultCommand.execute();
    }

    /**
     * 满足请求数的数量条件之后，才会看错误对比，此时的错误量，则是看时间窗口里面的当前已经保存的的占比，这意味着，虽然要到一定的请求量才处理，但是错误的比例会被保持下来(保留的时间窗口内的请求)
     * @param logicService
     */
    private static void errorAndVolumnVerify(RemoteLogicService logicService ) {
        LOG.info("errorAndVolumnVerify get fallback will execute when i equals 2");
        for (int i=0;i<3;i++)
        {
            try {
                String word="another day";
                if (i>1){
                    word="1";
                }else{
                    word=word+""+i;
                }
                SelfDefineCommandPropertiesCommand command = new SelfDefineCommandPropertiesCommand(logicService,word, Setting.ERROR_VOLUMNE_THRESHOLD);
                command.execute();
            }catch (Exception e){
                LOG.error("errorAndVolumnVerify", e);
            }
        }
    }
}
