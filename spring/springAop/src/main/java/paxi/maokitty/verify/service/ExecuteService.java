package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import paxi.maokitty.verify.domain.ComplexClass;
import paxi.maokitty.verify.service.inter.ExecuteInterface;

/**
 * Created by maokitty on 19/5/14.
 */
@Service
public class ExecuteService implements ExecuteInterface {
    private static final Logger LOG = LoggerFactory.getLogger(ExecuteService.class);

    public void  logicError(){
        LOG.info("logicError");
        int a=1/0;
    }

    public void logicHandleMultipleParam(int p,ComplexClass c){
        LOG.info("logicHandleMultipleParam p:{} c:{}",p,c);
    }
}
