package paxi.maokitty.verify.hystrix.util;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/5/22.
 */
public interface Setting {
    HystrixCommandProperties.Setter DEFAULT_PROPERTIES_SETTER = HystrixCommandProperties.Setter()
            .withCircuitBreakerEnabled(true) //默认true：启用 断路器  (todo 想个通俗易懂的解释)
            .withCircuitBreakerRequestVolumeThreshold(20) //默认20： 开始断路的阈值，20的含义是，就算有19个全部失败了，也不会执行执行降级,默认20 (todo 想个通俗易懂的解释)
            .withCircuitBreakerSleepWindowInMilliseconds(5000) //默认5000：在这个时间之内，所有的请求都会被拒绝，达到这个时间，则执行一次尝试，是否应该把它给关掉
            .withCircuitBreakerErrorThresholdPercentage(50) //默认50：请求错误的百分比，如果异常超过这个数目，就应该执行开闸，来执行fallback的逻辑
            .withFallbackEnabled(true)  //默认：true，启用fallback, 请求失败的时候调用 Fallback方法
            .withFallbackIsolationSemaphoreMaxConcurrentRequests(10) //默认10：调用fallback的时候，最大的并发请求数，如果超过限制，抛出异常，不执行fallback
            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD) //默认HystrixCommandProperties.ExecutionIsolationStrategy.THREAD： 设置run的执行策略，分为多个线程和信号量
            .withExecutionTimeoutEnabled(true) //默认true:使用fallback的逻辑
            .withExecutionTimeoutInMilliseconds(1000) //默认1000：服务调用超过这个时间，会执行fallback的逻辑
            .withExecutionIsolationThreadInterruptOnTimeout(true) //默认true:超时的时候，设置线程中断标识
            ;
    HystrixThreadPoolProperties.Setter DEFAULT_THREAD_SETTER = HystrixThreadPoolProperties.Setter()
            .withCoreSize(10) //默认10，核心线程池的数量
            .withAllowMaximumSizeToDivergeFromCoreSize(false) //默认 false，是否允许最大线程数与核心线程数不一样
            .withMaximumSize(10) //默认10，最大线程池数
            .withMaxQueueSize(-1) //默认-1，默认用的是 SynchronousQueue ,相当于没有队列
            .withQueueSizeRejectionThreshold(5) //默认5，如果 maxQueueSize==-1，这个设置不生效,超过这个设定，就算没有到最大的队列数，也会拒绝
            .withKeepAliveTimeMinutes(1) //默认取值1，如果超过1分钟线程还没有用，当 coreSize < maximumSize 时，线程就会被回收
            .withMetricsRollingStatisticalWindowInMilliseconds(10000) // todo 不知道
            .withMetricsRollingStatisticalWindowBuckets(10) //MetricsRollingStatisticalWindowInMilliseconds 必须被它整除;
            ;

        //todo 我理解的 Circuit breaker 是只有在执行到条件满足的时候才执行断路，但是不符合预期??
        //todo 想要在错误达到3个或者是百分比为 50%就触发
        //todo 禁止Circuit breaker 不执行 fallback
        //todo 如果FallbackEnabled 为false就永远不执行 fallback?
        //1:抵用断路器，希望在错了2个请求之后，或者请求错误的百分比达到之后， 在接下来的1秒钟，都不会再请求服务接口，直接走降级

        HystrixCommandProperties.Setter LOW_CIRCUIT_BREAKER_THRESHOLD =  HystrixCommandProperties.Setter()
                .withCircuitBreakerRequestVolumeThreshold(10) //统计时间窗口内的数量，也就是必须执行这个数量的次数，才会执行 断路
                .withCircuitBreakerErrorThresholdPercentage(50) //错误的量占比时间窗口内的总占比 ,计算方式为 错误量* 100 / (成功数+错误量) 0表示只要有错误，就执行fallback 并开启断路- > todo 时间窗口还是 bucket 待定 ,执行fallback不一定指定断路？
//                .withMetricsRollingStatisticalWindowInMilliseconds(1000)//维持的时间窗口的长度,这段时间内的数据应该都会保存在内存中
//                .withMetricsRollingStatisticalWindowBuckets(100) //每个窗口的大小？，每经过 1000/10 = 100 ms 来更新一次
                .withMetricsHealthSnapshotIntervalInMilliseconds(15); //设置计算成功和错误占比的等待时间，每经过这段时间就计算一次错误的占比
                ;

        //   this.bucketedStream = Observable.defer(new Func0<Observable<Bucket>>() {
        //@Override  withMetricsHealthSnapshotIntervalInMilliseconds 这个参数会设置一个window,然后window再做的统计的操作，那么关键是这个逻辑是怎么生效的呢？
//        public Observable<Bucket> call() {
//                return inputEventStream
//                        .observe()
//                        .window(bucketSizeInMs, TimeUnit.MILLISECONDS) //bucket it by the counter window so we can emit to the next operator in time chunks, not on every OnNext
//                        .flatMap(reduceBucketToSummary)                //for a given bucket, turn it into a long array containing counts of event types
//                        .startWith(emptyEventCountsToStart);
                //如果没有扫描是不会执行的，它是只有在一定的时间之后才会生效
        //超过3个请求之后，
}
