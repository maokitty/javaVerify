package paxi.maokitty.verify.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.RemoteLogicService;

/**
 * Created by maokitty on 19/5/23.
 */
public class SelfDefineCommandPropertiesCommand extends HystrixCommand<String> {
    private static Logger LOG = LoggerFactory.getLogger(SelfDefineCommandPropertiesCommand.class);
    private RemoteLogicService logicService;
    private String word;

    public SelfDefineCommandPropertiesCommand(RemoteLogicService logicService,String word,HystrixCommandProperties.Setter commandProperties){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("selfDefineCommandPropertiesCommand"))
                .andCommandPropertiesDefaults(commandProperties)
                .andCommandKey(HystrixCommandKey.Factory.asKey("commandKey")));
        this.logicService=logicService;
        this.word=word;
    }

    @Override
    protected String run() throws Exception {
        logicService.errorByCommand(word);
        return "run success";
    }

    @Override
    protected String getFallback() {
        LOG.info("getFallback");
        return "fallback success";
    }


}
