package paxi.maokitty.verify.rxjava1x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.rxjava1x.domain.User;
import paxi.maokitty.verify.rxjava1x.domain.UserWithDeferObservable;
import paxi.maokitty.verify.rxjava1x.domain.UserWithJustObservable;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/1.
 */
public class MakeThingsObservableVerify {
    private static final Logger LOG = LoggerFactory.getLogger(MakeThingsObservableVerify.class);
    public static void main(String[] args) {
        justVerify();
        deferVerify();
        intervalVerify();
        fromVerify();
    }

    private static void fromVerify() {
        List<Long> lists=new ArrayList<>();
        for (long i=0;i<4;i++){
            lists.add(i);
        }
        Observable.from(lists).subscribe(new Action1<Long>() {
            @Override
            public void call(Long val) {
                LOG.info("fromVerify val:{}",val);
            }
        });
    }

    private static void intervalVerify() {
        //第一个参数是发送第一个数字的延迟，第二个参数是发送之后主动发送的时间间隔
        Observable<Long> observable = Observable.interval(0, 500, TimeUnit.MILLISECONDS);
        Subscription subscribe = observable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long val) {
                LOG.info("intervalVerify val:{}", val);
            }
        });
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            LOG.error("intervalVerify ", e);
        }
        subscribe.unsubscribe();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            LOG.error("intervalVerify ", e);
        }
    }

    private static void justVerify() {
        User user = new User();
        user.setAge(1);
        Observable.just(user).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                LOG.info("justVerify just user age:{}", user.getAge());
            }
        });
    }

    /*
     defer 只有在有订阅者订阅的的时候才真正的创建 Observable的对象
     just 则是立马就创建了
     */
    private static void deferVerify() {
        UserWithJustObservable user = new UserWithJustObservable();
        user.setAge(1);
        Observable<Integer> observableUser = user.getObservable();
        user.setAge(2);
        observableUser.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer age) {
                LOG.info("deferVerify UserWithJustObservable age will still be 1 age:{}", age);
            }
        });
        UserWithDeferObservable userWithDeferObservable = new UserWithDeferObservable();
        userWithDeferObservable.setAge(1);
        Observable observable = userWithDeferObservable.getObservable();
        userWithDeferObservable.setAge(2);
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer age) {
                LOG.info("deferVerify UserWithDeferObservable age will still be 2 age:{}", age);
            }
        });
    }
}
