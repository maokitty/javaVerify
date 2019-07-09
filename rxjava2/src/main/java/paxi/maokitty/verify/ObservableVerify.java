package paxi.maokitty.verify;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/6/9.
 */
public class ObservableVerify {
    private static Logger LOG = LoggerFactory.getLogger(ObservableVerify.class);
    public static void main(String[] args) {
        justVerify();
        Observable.just(1).window(1,12, TimeUnit.DAYS);
    }

    private static void justVerify() {
        Disposable disposable = Observable.just(1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LOG.info("accept:{}", integer);
            }
        });
    }
}
