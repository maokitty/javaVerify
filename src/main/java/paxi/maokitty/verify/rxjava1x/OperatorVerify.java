package paxi.maokitty.verify.rxjava1x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by maokitty on 19/6/1.
 */
public class OperatorVerify {
    private static final Logger LOG = LoggerFactory.getLogger(OperatorVerify.class);
    public static void main(String[] args) {
        long[] nums=new long[]{1,2,3,4};
        reduceSumVerify(nums);
        flatMapSumVerify(nums);
    }

    private static final Func2<Long, long[], Long> accumulator = new Func2<Long, long[], Long>() {
        public Long call(Long initValue, long[] bucketEventCounts) {
            long sum=initValue;
            for (int i=0;i<bucketEventCounts.length;i++){
                sum+=bucketEventCounts[i];
            }
            return sum;
        }
    };

    private static void flatMapSumVerify(long[] nums) {
        Func1<Observable<long[]>, Observable<Long>> sumFunc = new Func1<Observable<long[]>, Observable<Long>>() {
            public Observable<Long> call(Observable<long[]> eventBucket) {
                return eventBucket.reduce(0l, accumulator);
            }
        };
        Observable.just(Observable.just(nums)).flatMap(sumFunc).subscribe(new Action1<Long>() {
            @Override
            public void call(Long result) {
                LOG.info("flatMapSumVerify result{}", result);
            }
        });
    }

    private static void reduceSumVerify(long[] nums) {
        Observable.just(nums).reduce(0l, accumulator).subscribe(new Action1<Long>() {
            @Override
            public void call(Long result) {
                LOG.info("reduceSumVerify result:{}", result);
            }
        });
    }
}
