package paxi.maokitty.verify.hystrix;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import paxi.maokitty.verify.hystrix.command.SelfDefineCommandPropertiesCommand;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

/**
 * Created by maokitty on 19/5/26.
 */
@Controller
public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);
    @RequestMapping("/test")
    @ResponseBody
    public Object  test(){
        RemoteLogicService logicService = new RemoteLogicService();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i=0;i<20;i++)
        {
            try {
                String word="another day";
                if (i>5){
                    word="1";
                }else{
                    word+=""+1;
                }
//                TimeUnit.MILLISECONDS.sleep(190);
                //还和休眠的时间有关系,没有休眠，全部通过,有休眠才执行
                SelfDefineCommandPropertiesCommand command = new SelfDefineCommandPropertiesCommand(logicService,word, Setting.LOW_CIRCUIT_BREAKER_THRESHOLD);
                command.execute();
//                LOG.info("short circuite:{} open:{} fromFallback:{} count:{}",command.isResponseShortCircuited(), command.isCircuitBreakerOpen(),command.isResponseFromFallback(),(i));
            }catch (Exception e){
                LOG.error("a",e);
            }
        }
        stopWatch.split();
        LOG.info("time cost:{}",stopWatch.getSplitTime());
        return "test";
    }
}
