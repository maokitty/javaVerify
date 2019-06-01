package paxi.maokitty.verify.rxjava1x.domain;

import rx.Observable;

/**
 * Created by maokitty on 19/6/1.
 */
public class UserWithJustObservable {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Observable getObservable(){
        return Observable.just(age);
    }
}
