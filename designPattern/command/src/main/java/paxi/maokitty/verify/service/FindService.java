package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maokitty on 19/6/16.
 */
public class FindService {
    private static final Logger LOG = LoggerFactory.getLogger(FindService.class);
    public boolean findTargetFilePath(String target){
//        LOG.info("findTargetFilePath :{}",target);
        return "0".equals(target)?false:true;
    }
}
