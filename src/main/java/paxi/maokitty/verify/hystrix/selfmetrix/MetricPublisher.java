package paxi.maokitty.verify.hystrix.selfmetrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisherCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maokitty on 19/6/8.
 */
public class MetricPublisher extends HystrixMetricsPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(MetricPublisher.class);
    private static MetricPublisher metricPublisher = new MetricPublisher();
    private static Map<String,MetricErrorCountInMemoryCollector> COLLECTORS=new ConcurrentHashMap<>();
    public static MetricPublisher getInstance(){
        return metricPublisher;
    }
    private MetricPublisher(){}

    /**
     *  每个command初始化的时候会先回调这个方法 {@link com.netflix.hystrix.AbstractCommand} AbstractCommand 构造函数
     *  <code>HystrixMetricsPublisherFactory.createOrRetrievePublisherForCommand(this.commandKey, this.commandGroup, this.metrics, this.circuitBreaker, this.properties);</code>
     * @param commandKey
     * @param commandGroupKey
     * @param metrics
     * @param circuitBreaker
     * @param properties
     * @return
     */
    @Override
    public HystrixMetricsPublisherCommand getMetricsPublisherForCommand(HystrixCommandKey commandKey, HystrixCommandGroupKey commandGroupKey, HystrixCommandMetrics metrics, HystrixCircuitBreaker circuitBreaker, HystrixCommandProperties properties) {
        LOG.info("getMetricsPublisherForCommand key:{} ",commandKey.name());
        MetricErrorCountInMemoryCollector collector = new MetricErrorCountInMemoryCollector(commandKey,metrics);
        COLLECTORS.put(commandKey.name(),collector);
        return collector;
    }

    public static MetricErrorCountInMemoryCollector get(String commandKey){
        return COLLECTORS.get(commandKey);
    }

    public static Set<String> getKeys(){
        return COLLECTORS.keySet();
    }
}
