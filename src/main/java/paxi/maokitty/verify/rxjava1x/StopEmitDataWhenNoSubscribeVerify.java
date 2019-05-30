package paxi.maokitty.verify.rxjava1x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

/**
 * Created by liwangchun on 19/5/30.
 */
public class StopEmitDataWhenNoSubscribeVerify {
    private static final Logger LOG = LoggerFactory.getLogger(StopEmitDataWhenNoSubscribeVerify.class);
    public static void main(String[] args) throws InterruptedException {
        unStopEmitVerify();
        stopEmitVerify();
    }

    private static void unStopEmitVerify() throws InterruptedException {
        ConnectableObservable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).publish();
        observable.connect();
        Action1<Long> firstAction = getAction("unStopEmitVerify firstAction");
        Action1<Long> secondAction = getAction("unStopEmitVerify secondAction");
        Subscription firstSubscribe = observable.subscribe(firstAction);
        TimeUnit.SECONDS.sleep(2);
        firstSubscribe.unsubscribe();

        //第二个订阅者的数据仍然是沿着第一个订阅者原先的数据
        Subscription secondSubscribe = observable.subscribe(secondAction);
        TimeUnit.SECONDS.sleep(2);
        secondSubscribe.unsubscribe();
    }

    private static void stopEmitVerify() throws InterruptedException {
        //share()方法实际上就是 publish().refCount(); 的简写
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).share();
        Action1<Long> firstAction = getAction("stopEmitVerify firstAction");
        Action1<Long> secondAction = getAction("stopEmitVerify secondAction");
        Subscription firstSubscribe = observable.subscribe(firstAction);
        TimeUnit.SECONDS.sleep(2);
        firstSubscribe.unsubscribe();

        //第二个订阅者的数据从0从新开始
        Subscription secondSubscribe = observable.subscribe(secondAction);
        TimeUnit.SECONDS.sleep(2);
        secondSubscribe.unsubscribe();
    }

    private static Action1<Long> getAction(final String msg){
        return new Action1<Long>() {
            public void call(Long v) {
                LOG.info("{} get:{}",msg,v);
            }
        };
    }
}
