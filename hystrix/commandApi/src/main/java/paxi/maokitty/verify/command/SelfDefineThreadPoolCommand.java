package paxi.maokitty.verify.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.RemoteLogicService;

/**
 * Created by maokitty on 19/6/5.
 */
public class SelfDefineThreadPoolCommand extends HystrixCommand<String> {
    private static final Logger LOG = LoggerFactory.getLogger(SelfDefineThreadPoolCommand.class);
    private RemoteLogicService remoteLogicService;
    private String word;

    public SelfDefineThreadPoolCommand(RemoteLogicService logicService,HystrixThreadPoolProperties.Setter threadPoolProperties,String word){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SelfDefineThreadPoolCommand"))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("MyThreadPoolKey"))
        .andThreadPoolPropertiesDefaults(threadPoolProperties));
        this.remoteLogicService=logicService;
        this.word=word;

    }
    @Override
    protected String run() throws Exception {
        LOG.info("run");
        remoteLogicService.errorByCommand(word);
        return "run success";
    }

    @Override
    protected String getFallback() {
        LOG.info("getFallback");
        return "fallback success";
    }
}
