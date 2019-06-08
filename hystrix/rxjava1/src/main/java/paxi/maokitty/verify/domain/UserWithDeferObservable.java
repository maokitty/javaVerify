package paxi.maokitty.verify.domain;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by maokitty on 19/6/1.
 */
public class UserWithDeferObservable {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Observable getObservable(){
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(age);
            }
        });
    }
}
