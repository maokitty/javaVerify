package paxi.maokitty.verify.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolMetrics;
import com.netflix.hystrix.metric.consumer.HystrixDashboardStream;
import com.netflix.hystrix.strategy.HystrixPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.command.AssignkeySilentCommand;
import paxi.maokitty.verify.hystrix.command.DefaultSettingSilentCommand;
import paxi.maokitty.verify.hystrix.selfmetrix.MetricErrorCountInMemoryCollector;
import paxi.maokitty.verify.hystrix.selfmetrix.MetricPublisher;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import rx.functions.Action1;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/7.
 */
public class DashboardMetricVerify {
    private static final Logger LOG = LoggerFactory.getLogger(DashboardMetricVerify.class);
    public static void main(String[] args) throws InterruptedException {
        RemoteLogicService logicService=new RemoteLogicService();
        metricByDashboardVerify(logicService);
    }
    /**
     * {@link com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet}
     * @param logicService
     * @throws InterruptedException
     */
    private static void metricByDashboardVerify(RemoteLogicService logicService) throws InterruptedException {
        HystrixCommandKey key = HystrixCommandKey.Factory.asKey("myAssignCommandKey");
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("myAssignThreadPoolKey");
        //控制dashboard自己的刷新频率,本质上就是自己产生了一个interval的observable来轮询对应的指标
        System.setProperty("hystrix.stream.dashboard.intervalInMilliseconds","10");
        HystrixDashboardStream.getInstance()
                .observe()
                .subscribe(new Action1<HystrixDashboardStream.DashboardData>() {
                    @Override
                    public void call(HystrixDashboardStream.DashboardData dashboardData) {
                        Collection<HystrixCommandMetrics> commandMetrics = dashboardData.getCommandMetrics();
                        for (HystrixCommandMetrics m : commandMetrics) {
                            HystrixCommandKey commandKey = m.getCommandKey();
                            if (commandKey == key) {
                                //only filtered key will be collect。
                                HystrixCommandMetrics.HealthCounts healthCounts = m.getHealthCounts();
                                if (healthCounts.getTotalRequests()>0)
                                {
                                    LOG.info(" metricByDashboardVerify health metrics for {} ,{}", commandKey.name(), healthCounts);
                                }
                            }
                        }
                        Collection<HystrixThreadPoolMetrics> threadPoolMetrics = dashboardData.getThreadPoolMetrics();
                        for (HystrixThreadPoolMetrics metric : threadPoolMetrics) {
                            // all key to be collect
                            Number currentQueueSize = metric.getCurrentQueueSize();
                            if (currentQueueSize.intValue()>0)
                            {
                                LOG.info("metricByDashboardVerify thread metrics big than 0 only 。 {} , current queue size:{}", metric.getThreadPoolKey().name(), currentQueueSize);
                            }
                        }
                    }
                });
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++)
        {
            final int finalI=i;
            service.submit(new Runnable() {
                @Override
                public void run() {
                    String command = "another day";
                    if (finalI > 1) {
                        command = "1";
                    }
                    AssignkeySilentCommand assignkeyCommand = new AssignkeySilentCommand(key, threadPoolKey, logicService, command);
                    assignkeyCommand.execute();
                }
            });
            service.submit(new Runnable() {
                @Override
                public void run() {
                    String command="another day";
                    if (finalI>1){
                        command="1";
                    }
                    DefaultSettingSilentCommand defaultSettingCommand = new DefaultSettingSilentCommand(key,logicService, command);
                    defaultSettingCommand.execute();
                }
            });
        }
        TimeUnit.MILLISECONDS.sleep(600);
        service.shutdown();
    }
}
