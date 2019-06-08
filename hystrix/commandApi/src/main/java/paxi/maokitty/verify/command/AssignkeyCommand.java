package paxi.maokitty.verify.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.RemoteLogicService;
import paxi.maokitty.verify.util.Setting;

/**
 * Created by maokitty on 19/6/7.
 */
public class AssignkeyCommand extends HystrixCommand<String>{
    private static final Logger LOG = LoggerFactory.getLogger(AssignkeyCommand.class);
    private RemoteLogicService logicService ;
    private String command;

    public AssignkeyCommand(HystrixCommandKey key,HystrixThreadPoolKey threadPoolKey,RemoteLogicService remoteLogicService,String command){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("myAssignGroupKey")).andCommandKey(key).andThreadPoolKey(threadPoolKey).andThreadPoolPropertiesDefaults(Setting.SMALL_CORE_SIZE));
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
