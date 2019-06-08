package paxi.maokitty.verify.hystrix.command;

import com.netflix.hystrix.*;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

/**
 * Created by maokitty on 19/6/8.
 */
public class DefaultSettingSilentCommand extends HystrixCommand<String> {
    private RemoteLogicService logicService;
    private String word;

    public DefaultSettingSilentCommand(RemoteLogicService logicService,String word){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("defaultSilentCommand"))
                .andCommandPropertiesDefaults(Setting.DEFAULT_PROPERTIES_SETTER)
                .andThreadPoolPropertiesDefaults(Setting.DEFAULT_THREAD_SETTER));
        this.logicService=logicService;
        this.word=word;
    }
    public DefaultSettingSilentCommand(HystrixCommandKey key,RemoteLogicService logicService,String word){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("defaultSilentCommand"))
                .andCommandKey(key)
                .andCommandPropertiesDefaults(Setting.DEFAULT_PROPERTIES_SETTER)
                .andThreadPoolPropertiesDefaults(Setting.DEFAULT_THREAD_SETTER));
        this.logicService=logicService;
        this.word=word;
    }
    @Override
    protected String run() throws Exception {
        logicService.sayNothing(word);
        return "success";
    }

    @Override
    protected String getFallback() {
        return "fallback";
    }
}
