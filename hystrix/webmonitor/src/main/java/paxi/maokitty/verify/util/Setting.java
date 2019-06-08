package paxi.maokitty.verify.util;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Created by maokitty on 19/5/22.
 * 默认值可以详见 {@link com.netflix.hystrix.HystrixCommandProperties} {@link com.netflix.hystrix.HystrixThreadPoolProperties}
 * 断路器：开启(开闸)就执行 getFallBack (可以想象一个电路，电路有个开关，开关打开，就是电流循环被断开，这个时候点灯就不亮了)，闭合就是执行用户的逻辑
 */
public interface Setting {
        HystrixCommandProperties.Setter DEFAULT_PROPERTIES_SETTER = HystrixCommandProperties.Setter()
            //true表示要使用断路器去判断是否断路，如果配置为false，真实使用的是  NoOpCircuitBreaker ,即所有请求都执行用户自己的方法
            .withCircuitBreakerEnabled(true)
            //必须要有20个请求通过才去走断路器的逻辑，在20个请求之内的访问都会直接执行用户自己的方法，当然执行的结果都会记录袭来
            .withCircuitBreakerRequestVolumeThreshold(20)
            //一旦断路器设定为要执行短路逻辑（开闸），记下最开始的短路时间，从这开始，到5000毫秒之后，再放过1个请求去尝试用户的方法能不能成功
            .withCircuitBreakerSleepWindowInMilliseconds(5000)
            //错误请求占总共请求结果的比例，计算策略为 请求错误数*100/请求总数,超过这个比例执行断路逻辑
            .withCircuitBreakerErrorThresholdPercentage(50)
            //true表示当执行短路逻辑的时候去调用 getFallback 方法
            .withFallbackEnabled(true)
            //执行fallback的并发请求量 ,超过抛出异常
            .withFallbackIsolationSemaphoreMaxConcurrentRequests(10)
            //执行用户方法的隔离策略，分为线程和信号量
            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
            //true表示如果执行超时，那么执行 getFallback
            .withExecutionTimeoutEnabled(true)
            //执行用户方法超时时间
            .withExecutionTimeoutInMilliseconds(1000)
            //超时的时候设置中断标识
            .withExecutionIsolationThreadInterruptOnTimeout(true)
            ;
        HystrixThreadPoolProperties.Setter DEFAULT_THREAD_SETTER = HystrixThreadPoolProperties.Setter()
             //10，核心线程池的数量
             .withCoreSize(10)
            //false 不允许最大线程数与核心线程数不一样
            .withAllowMaximumSizeToDivergeFromCoreSize(false)
            //10，最大线程池数
            .withMaximumSize(10)
            //默认-1，默认用的是 SynchronousQueue ,相当于没有队列
            .withMaxQueueSize(-1)
             //如果 maxQueueSize==-1，这个设置不生效,超过这个设定，就算没有到最大的队列数，也会拒绝，拒绝后走 getFallback 的逻辑
            .withQueueSizeRejectionThreshold(5)
            //如果超过1分钟线程还没有用，当 coreSize < maximumSize 时，超过core的线程就会被回收
            .withKeepAliveTimeMinutes(1)
            //统计线程正常执行的数量和拒绝的数量,统计总共计算的时间窗口数
            .withMetricsRollingStatisticalWindowInMilliseconds(10000)
            //统计每个桶的大小   ，MetricsRollingStatisticalWindowInMilliseconds 必须被它整除，不然算出桶的个数就有问题
             .withMetricsRollingStatisticalWindowBuckets(10)
            ;

        HystrixCommandProperties.Setter ERROR_VOLUMNE_THRESHOLD =  HystrixCommandProperties.Setter()
                .withCircuitBreakerRequestVolumeThreshold(2)
                .withCircuitBreakerErrorThresholdPercentage(50)
                ;
        HystrixCommandProperties.Setter FALLBACK_ISOLATION_SEMAPHORE=HystrixCommandProperties.Setter()
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(1)
                .withCircuitBreakerRequestVolumeThreshold(0)
                ;
        HystrixThreadPoolProperties.Setter SMALL_QUEUE_CORE_SIZE = HystrixThreadPoolProperties.Setter()
                .withCoreSize(1)
                .withMaxQueueSize(2)
                .withQueueSizeRejectionThreshold(2)
                ;
        HystrixThreadPoolProperties.Setter SMALL_CORE_SIZE = HystrixThreadPoolProperties.Setter()
                .withCoreSize(1)
                .withMaxQueueSize(10)
                ;
}
