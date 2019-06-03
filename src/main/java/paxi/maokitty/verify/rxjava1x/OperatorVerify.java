package paxi.maokitty.verify.rxjava1x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/1.
 */
public class OperatorVerify {
    private static final Logger LOG = LoggerFactory.getLogger(OperatorVerify.class);

    public static void main(String[] args) {
        long[] nums=new long[]{1,2,3,4};
//        reduceSumVerify(nums);
//        flatMapSumVerify(nums);
//        timeWindownVerify();
//        countWindownVerify();
//        scanWindownVerify();
//        scanSkipWindownVerify();
        subscribeOnVerify();

    }

    private static void subscribeOnVerify() {
        Observable.just(1).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                LOG.info("subscribeOnVerify thread:{} is not main thread",Thread.currentThread().getName());
                return integer;
            }
        }).subscribeOn(Schedulers.computation()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LOG.info("subscribeOnVerify thread:{} is not main thread", Thread.currentThread().getName());
            }
        });
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            LOG.error("subscribeOnVerify",e);
        }
    }


    /**
     * scan会对每一项的计算结果都进行返回，传递给后面的数据输出，因而不仅输出最终的计算结果，而且也会输出中间计算成功的过程
     */
    private static void scanWindownVerify() {
        Observable.from(new Long[]{1l, 2l, 3l, 4l}).window(2,2).flatMap(new Func1<Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(Observable<Long> longObservable) {
                return longObservable.scan(0l, new Func2<Long, Long, Long>() {
                    @Override
                    public Long call(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                });
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LOG.info("countWindownVerify r:{}", aLong);
            }
        });
    }

    /**
     * 利用skip的特性，省略掉中间的计算过程向下传输，只输出最新的计算结果
     */
    private static void scanSkipWindownVerify() {
        Observable.from(new Long[]{1l, 2l, 3l, 4l}).window(2,2).flatMap(new Func1<Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(Observable<Long> longObservable) {
                return longObservable.scan(0l, new Func2<Long, Long, Long>() {
                    @Override
                    public Long call(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                }).skip(2);
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LOG.info("countWindownVerify r:{}", aLong);
            }
        });
    }

    /**
     * 每两个两个相加
     */
    private static void countWindownVerify() {
        //收集前两个参数，窗口满了之后，扔掉这两个数据，再重新等两个数据
        Observable.from(new Long[]{0l, 1l, 2l, 3l}).window(2,2).flatMap(new Func1<Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(Observable<Long> longObservable) {
                return longObservable.reduce(0l, new Func2<Long, Long, Long>() {
                    @Override
                    public Long call(Long preSum, Long newData) {
                        return preSum+newData;
                    }
                });
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LOG.info("countWindownVerify r:{}", aLong);
            }
        });
    }

    /**
     * 每100毫秒产生一个数字，聚合每500毫秒的数据，对他们就和，并把每个500毫秒内的数据之和打印出来
     */
    private static void timeWindownVerify() {
        Observable.interval(0, 100, TimeUnit.MILLISECONDS).window(500, TimeUnit.MILLISECONDS).flatMap(new Func1<Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(Observable<Long> longObservable) {

                return longObservable.reduce(0l, new Func2<Long, Long, Long>() {
                    @Override
                    public Long call(Long preSum, Long newData) {
                        LOG.info("windownVerify preSum:{} newData:{}",preSum,newData);
                        return preSum+newData;
                    }
                });
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long val) {
                LOG.info("windownVerify get result for each time windown:{}", val);
            }
        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            LOG.error("windownVerify",e);
        }
    }

    /**
     * flatMap将收到的多个数据，转换成另外一个数据
     * @param nums
     */

    private static void flatMapSumVerify(long[] nums) {
        final Func2<String, long[], String> accumulator = new Func2<String, long[], String>() {
            public String call(String preValue, long[] bucketEventCounts) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append(preValue);
                strBuilder.append(",");
                for (int i=0;i<bucketEventCounts.length;i++){
                    strBuilder.append(bucketEventCounts[i]);
                    if (i!=bucketEventCounts.length-1){
                        strBuilder.append(",");
                    }
                }
                return strBuilder.toString();
            }
        };
        Func1<Observable<long[]>, Observable<String>> sumFunc = new Func1<Observable<long[]>, Observable<String>>() {
            public Observable<String> call(Observable<long[]> eventBucket) {
                return eventBucket.reduce("", accumulator);
            }
        };
        Observable.just(Observable.just(nums)).flatMap(sumFunc).subscribe(new Action1<String>() {
            @Override
            public void call(String result) {
                LOG.info("flatMapSumVerify result{}", result);
            }
        });
    }

    /**
     * 通过函数处理某一项，并把最后的结果输出
     * @param nums
     */
    private static void reduceSumVerify(long[] nums) {
        final Func2<Long, long[], Long> accumulator = new Func2<Long, long[], Long>() {
            public Long call(Long initValue, long[] bucketEventCounts) {
                long sum=initValue;
                for (int i=0;i<bucketEventCounts.length;i++){
                    sum+=bucketEventCounts[i];
                }
                return sum;
            }
        };
        Observable.just(nums).reduce(0l, accumulator).subscribe(new Action1<Long>() {
            @Override
            public void call(Long result) {
                LOG.info("reduceSumVerify result:{}", result);
            }
        });
    }
}
