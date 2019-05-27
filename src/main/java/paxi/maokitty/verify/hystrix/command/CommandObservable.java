package paxi.maokitty.verify.hystrix.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by maokitty on 19/5/20.
 * HystrixObservableCommand  是一种特殊的 HystrixCommand
 */
public class CommandObservable extends HystrixObservableCommand<String> {
    private final String name;
    public CommandObservable(String name){
        //这里做配置用
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name=name;

    }
    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>(){

            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()){
                        observer.onNext("hello");
                        observer.onNext(name);
                        observer.onCompleted();
                    }
                }catch (Exception e){
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
