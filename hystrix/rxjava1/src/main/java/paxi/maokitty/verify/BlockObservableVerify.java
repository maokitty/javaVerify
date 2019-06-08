package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by maokitty on 19/6/2.
 */
public class BlockObservableVerify {
    private static Logger LOG = LoggerFactory.getLogger(BlockObservableVerify.class);
    public static void main(String[] args) {
        blockFutureVerify();
    }




    private  static final Func0<Observable<Integer>> sleepObservableFunc = new Func0<Observable<Integer>>() {
        @Override
        public Observable<Integer> call() {

            return Observable.just(1).map(new Func1<Integer, Integer>() {
                @Override
                public Integer call(Integer integer) {
                    try {
                        LOG.info("call sleep start thread:{}",Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(1);
                        LOG.info("call sleep end thread:{}",Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        LOG.error("call", e);
                    }
                    return integer;
                }
            });
        }
    };



    /**
     1:toBlocking之后的函数调用无法再产生 Observable元素
     2:toFuture需要Observable只能产生一个元素
     */
    private static void blockFutureVerify() {
        //对于阻塞的语义来讲，在tofuture执行完之前，根本不会执行下面的get语句,所以toFuture除了只约定有1个返回值之外没什么意义
        Future<Integer> uselessFuture = Observable.defer(sleepObservableFunc).toBlocking().toFuture();
        try {
            LOG.info("blockFutureVerify future waiting start ,this waiting is useless");
            Integer v = uselessFuture.get(100, TimeUnit.MILLISECONDS);
            LOG.info("blockFutureVerify  block get value:{}", v);
        } catch (InterruptedException e) {
            LOG.error("blockFutureVerify", e);
        } catch (ExecutionException e) {
            LOG.error("blockFutureVerify", e);
        } catch (TimeoutException e) {
            LOG.error("blockFutureVerify",e);
        }

        //调度到别的线程异步执行
        Future<Integer> future = Observable.defer(sleepObservableFunc).subscribeOn(Schedulers.computation()).toBlocking().toFuture();
        try {
            LOG.info("blockFutureVerify future waiting start");
            Integer integer = future.get(100,TimeUnit.MILLISECONDS);
            LOG.info("blockFutureVerify  block get value:{}",integer);
        } catch (InterruptedException e) {
            LOG.error("blockFutureVerify",e);
        } catch (ExecutionException e) {
            LOG.error("blockFutureVerify", e);
        } catch (TimeoutException e) {
            LOG.error("blockFutureVerify", e);
        }
    }
}
