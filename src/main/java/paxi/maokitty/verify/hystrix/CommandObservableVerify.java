package paxi.maokitty.verify.hystrix;

import paxi.maokitty.verify.hystrix.command.CommandObservable;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by maokitty on 19/5/20.
 */
public class CommandObservableVerify {
    public static void main(String[] args) {
        //toObservable()  和 observe() 是干啥的？
        //toObservable() :when you subscribe to it, will execute the Hystrix command and emit its responses
        //observe(): subscribes to the Observable that represents the response(s) from the dependency and returns an Observable that replicates that source Observable
        Observable<String> px=new CommandObservable("paxi").observe();
        px.subscribe(new Action1<String>() {
            public void call(String s) {
                System.out.println("onNext:"+s);
            }
        });
    }
}
