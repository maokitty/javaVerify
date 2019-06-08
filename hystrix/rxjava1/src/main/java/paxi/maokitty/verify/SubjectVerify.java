package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by maokitty on 19/5/30.
 * Subject 同时继承了 Observable和Observe,意味着它能够监听自己
 */
public class SubjectVerify {
    private static Logger LOG = LoggerFactory.getLogger(SubjectVerify.class);
    public static void main(String[] args) {
        publishSubjectVerify();
        behaviorSubjectVerify();
    }

    /**
     * PublishSubject 只会接收在订阅之后产生的数据
     */
    private static void publishSubjectVerify() {
        PublishSubject<Integer> integerPublishSubject = PublishSubject.<Integer>create();
        for (int i=0;i<10;i++){
            integerPublishSubject.onNext(i);
            if (i==8){
                LOG.info("only subscribe when i equals 8,it will only get the data 9");
                integerPublishSubject.subscribe(new Action1<Integer>() {
                    public void call(Integer integer) {
                        LOG.info("subscribe value:{}", integer);
                    }
                });
            }
        }
    }
    private static void behaviorSubjectVerify() {
        BehaviorSubject<Integer> integerPublishSubject = BehaviorSubject.<Integer>create();
        for (int i=0;i<10;i++){
            integerPublishSubject.onNext(i);
            if (i==8){
                LOG.info("only subscribe when i equals 8,it will  get the data 8,9");
                integerPublishSubject.subscribe(new Action1<Integer>() {
                    public void call(Integer integer) {
                        LOG.info("subscribe value:{}", integer);
                    }
                });
            }
        }
    }
}
