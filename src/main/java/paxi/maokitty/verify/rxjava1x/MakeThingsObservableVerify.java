package paxi.maokitty.verify.rxjava1x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.rxjava1x.domain.User;
import paxi.maokitty.verify.rxjava1x.domain.UserWithDeferObservable;
import paxi.maokitty.verify.rxjava1x.domain.UserWithJustObservable;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by maokitty on 19/6/1.
 */
public class MakeThingsObservableVerify {
    private static final Logger LOG = LoggerFactory.getLogger(MakeThingsObservableVerify.class);
    public static void main(String[] args) {
        justVerify();
        deferVerify();
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
