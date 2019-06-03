package paxi.maokitty.verify.hystrix;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.command.SelfDefineCommandPropertiesCommand;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/5/23.
 */
public class HystrixCommandPropertiesVerify {
    private static final Logger LOG = LoggerFactory.getLogger(HystrixCommandPropertiesVerify.class);

    public static void main(String[] args) {
        RemoteLogicService logicService = new RemoteLogicService();


        for (int i=0;i<1;i++)
        {
            try {
                String word="another day";
                if (i>1){
                    word="1";
                }else{
                    word=word+""+i;
                }
                SelfDefineCommandPropertiesCommand command = new SelfDefineCommandPropertiesCommand(logicService,word, Setting.LOW_CIRCUIT_BREAKER_THRESHOLD);
                String execute = command.execute();
                LOG.info("execute:{}",execute);
//                LOG.info("short circuite:{} open:{} fromFallback:{} count:{}",command.isResponseShortCircuited(), command.isCircuitBreakerOpen(),command.isResponseFromFallback(),(i));
            }catch (Exception e){
                LOG.error("a", e);
            }
        }
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        for (int i=0;i<20;i++)
//        {
//            try {
//                String word="another day";
//                if (i>1){
//                    word="1";
//                }else{
//                    word=word+""+i;
//                }
//                SelfDefineCommandPropertiesCommand command = new SelfDefineCommandPropertiesCommand(logicService,word, Setting.LOW_CIRCUIT_BREAKER_THRESHOLD);
//                command.execute();
//                stopWatch.split();
//                LOG.info("time cost:{}",stopWatch.getSplitTime());
////                LOG.info("short circuite:{} open:{} fromFallback:{} count:{}",command.isResponseShortCircuited(), command.isCircuitBreakerOpen(),command.isResponseFromFallback(),(i));
//            }catch (Exception e){
//                LOG.error("a", e);
//            }
//        }
//        stopWatch.split();
//        LOG.info("time cost:{}",stopWatch.getSplitTime());
    }
}
