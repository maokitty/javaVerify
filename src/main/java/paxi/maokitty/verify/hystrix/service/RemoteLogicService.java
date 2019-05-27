package paxi.maokitty.verify.hystrix.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maokitty on 19/5/22.
 */
public class RemoteLogicService {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteLogicService.class);
    public void normalSay(String word){
        LOG.info("normalSay :{}",word);
    }

    public void errorByCommand(String command) throws Exception {
        LOG.info("errorByCommand command:{}",command);
        if ("1".endsWith(command)){
            throw new Exception("aa");
        }
    }

}
