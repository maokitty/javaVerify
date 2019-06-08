package paxi.maokitty.verify.hystrix.command;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import paxi.maokitty.verify.hystrix.util.Setting;

/**
 * Created by maokitty on 19/6/7.
 */
public class AssignkeyCommand extends HystrixCommand<String>{
    private static final Logger LOG = LoggerFactory.getLogger(AssignkeyCommand.class);
    private RemoteLogicService logicService ;
    private String command;

    public AssignkeyCommand(HystrixCommandKey key,HystrixThreadPoolKey threadPoolKey,RemoteLogicService remoteLogicService,String command){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("myAssignGroupKey")).andCommandKey(key).andThreadPoolKey(threadPoolKey).andThreadPoolPropertiesDefaults(Setting.SMALL_CORE_SIZE));
        this.logicService=remoteLogicService;
        this.command=command;
    }

    @Override
    protected String run() throws Exception {
        logicService.errorByCommand(command);
        return "run success";
    }

    @Override
    protected String getFallback() {
        return "fallback";
    }
}
