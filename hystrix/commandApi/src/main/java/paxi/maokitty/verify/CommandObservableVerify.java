package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.command.CommandObservable;
import paxi.maokitty.verify.service.RemoteLogicService;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by maokitty on 19/5/20.
 */
public class CommandObservableVerify {
    private static final Logger LOG = LoggerFactory.getLogger(CommandObservableVerify.class);
    public static void main(String[] args) {
        RemoteLogicService logicService = new RemoteLogicService();
        observableVerify(logicService);
    }

    /**
     * 发生错误的回调方法是 resumeWithFallback
     * @param logicService
     */
    private static void observableVerify(RemoteLogicService logicService) {
        for (int i = 0;i<2;i++){
            String word = "1";
            if (i<1){
                word="another day";
            }
            //内部在调用observe()的时候实际上已经订阅了,仍然会返回一个可订阅的对象，方便用户自己处理
            Observable<String> px=new CommandObservable(word,logicService).observe();
            px.doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LOG.error("observableVerify error",throwable);
                }
            }).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    LOG.info("observableVerify return {}", s);
                }
            });
        }

    }
}
