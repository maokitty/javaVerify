package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/5/22.
 */
public class RemoteLogicService {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteLogicService.class);
    public void normalSay(String command){
        LOG.info("normalSay :{}",command);
    }

    public void sayNothing(String command) throws Exception {
        if ("1".endsWith(command)){
            throw new Exception("run command 1");
        }
    }

    public void errorByCommand(String command) throws Exception {
        LOG.info("errorByCommand command:{}",command);
        TimeUnit.MILLISECONDS.sleep(50);
        if ("1".endsWith(command)){
            throw new Exception("run command 1");
        }
    }

}
