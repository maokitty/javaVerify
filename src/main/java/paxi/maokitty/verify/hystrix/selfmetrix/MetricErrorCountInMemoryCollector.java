package paxi.maokitty.verify.hystrix.selfmetrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisherCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/7.
 */
public class MetricErrorCountInMemoryCollector extends HystrixMetricsPublisher implements HystrixMetricsPublisherCommand{
    private static final Logger LOG = LoggerFactory.getLogger(MetricErrorCountInMemoryCollector.class);

    private static final ExecutorService service = Executors.newFixedThreadPool(1);

    private HystrixCommandMetrics metrics;
    private HystrixCommandKey commandKey;

    MetricErrorCountInMemoryCollector(HystrixCommandKey commandKey, HystrixCommandMetrics metrics) {
        this.metrics=metrics;
        this.commandKey = commandKey;
    }

    private Map<String,String> map = null;

    public void printAll(){
        for (Map.Entry<String,String> entry:map.entrySet()){
            LOG.info("key:{} value:{}",entry.getKey(),entry.getValue());
        }
    }

    private void collectToMap(HystrixCommandMetrics metrics){
        HystrixCommandKey commandKey = metrics.getCommandKey();
        HystrixCommandMetrics.HealthCounts healthCounts = metrics.getHealthCounts();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(commandKey.name());
        keyBuilder.append(":errorCount:");
        keyBuilder.append(System.currentTimeMillis());
        String key = keyBuilder.toString();
        map.put(key, String.valueOf(healthCounts.getErrorCount()));
    }

    @Override
    public void initialize() {
        LOG.info("initialize metric collector for key:{}",commandKey.name());
        map = new HashMap<>();
        service.submit(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    //每500毫秒统计一次，统计的结果就是最终算出来的结果
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    collectToMap(metrics);
                }
            }
        });
    }

    public void stopCollect(){
        service.shutdownNow();
    }
}
