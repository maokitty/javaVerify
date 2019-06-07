package paxi.maokitty.verify.hystrix.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by maokitty on 19/5/20.
 * HystrixObservableCommand  是一种特殊的 HystrixCommand
 */
public class CommandObservable extends HystrixObservableCommand<String> {
    private static final Logger LOG  = LoggerFactory.getLogger(CommandObservable.class);
    private RemoteLogicService logicService;
    private String word;
    public CommandObservable(String name,RemoteLogicService remoteLogicService){
        //这里做配置用
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.word=name;
        this.logicService=remoteLogicService;

    }
    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        logicService.errorByCommand(word);
                        observer.onNext("run success");
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    @Override
    protected Observable<String> resumeWithFallback() {
        LOG.info("fallback run");
        return Observable.just("run resumeWithFallback");
    }
}
