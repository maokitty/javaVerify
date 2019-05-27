package paxi.maokitty.verify.hystrix.command;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

/**
 * Created by maokitty on 19/5/21.
 */
public class DefaultSettingCommand extends HystrixCommand<String> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSettingCommand.class);

    private RemoteLogicService logicService;
    private String word;

    public DefaultSettingCommand(RemoteLogicService logicService,String word){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("defaultCommand"))
                .andCommandPropertiesDefaults(Setting.DEFAULT_PROPERTIES_SETTER)
                .andThreadPoolPropertiesDefaults(Setting.DEFAULT_THREAD_SETTER));
        this.logicService=logicService;
        this.word=word;
    }
    @Override
    protected String run() throws Exception {
        LOG.info("run :{}",word);
        logicService.normalSay(word);
        return "success";
    }

    @Override
    protected String getFallback() {
        LOG.info("getFallback :{}",word);
        return "fallback";
    }
}
