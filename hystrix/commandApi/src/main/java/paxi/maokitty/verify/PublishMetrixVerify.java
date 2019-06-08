package paxi.maokitty.verify;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.strategy.HystrixPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.command.AssignkeySilentCommand;
import paxi.maokitty.verify.command.DefaultSettingSilentCommand;
import paxi.maokitty.verify.selfmetrix.MetricErrorCountInMemoryCollector;
import paxi.maokitty.verify.selfmetrix.MetricPublisher;
import paxi.maokitty.verify.service.RemoteLogicService;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/8.
 */
public class PublishMetrixVerify {
    private static final Logger LOG = LoggerFactory.getLogger(PublishMetrixVerify.class);

    public static void main(String[] args) {
        RemoteLogicService logicService = new RemoteLogicService();
        publisherVerify(logicService);
    }
    private static void publisherVerify(RemoteLogicService logicService)  {
        HystrixCommandKey key = HystrixCommandKey.Factory.asKey("myAssignCollectorCommandKey");
        //多个测试防止出现问题，先清空
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerMetricsPublisher(MetricPublisher.getInstance());
        for (int i=0;i<10;i++){
            String command="another day";
            if (i>5){
                command="1";
            }
            AssignkeySilentCommand assignkeyCommand = new AssignkeySilentCommand(key,logicService, command);
            assignkeyCommand.execute();
            DefaultSettingSilentCommand defaultSettingCommand = new DefaultSettingSilentCommand(logicService,command);
            defaultSettingCommand.execute();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            LOG.error("publisherVerify",e);
        }
        Set<String> keys = MetricPublisher.getKeys();
        for (String k:keys){
            LOG.info("collect key:{}",k);
        }
        MetricErrorCountInMemoryCollector collector = MetricPublisher.get(key.name());
        collector.printAll();
        collector.stopCollect();
    }

}
