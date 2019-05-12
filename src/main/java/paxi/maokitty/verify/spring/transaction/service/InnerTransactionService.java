package paxi.maokitty.verify.spring.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 */
@Service
public class InnerTransactionService {
    private static Logger LOG = LoggerFactory.getLogger(InnerTransactionService.class);

    @Transactional(propagation = Propagation.SUPPORTS)
    public void  supportInnerService(){
        LOG.info("supportInnerService");
        uncheckedError();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void notSupportInnerService(){
        LOG.info("notSupportInnerService");
        uncheckedError();
    }

    @Transactional(propagation = Propagation.NEVER)
    public void neverInnerService(){
        LOG.info("neverInnerService");
        uncheckedError();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void mandatoryInnerService(){
        LOG.info("mandatoryInnerService");
        uncheckedError();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewInnerService(){
        LOG.info("requiresNewInnerService");
        uncheckedError();
    }

    @Transactional
    public void requiredInnerService(){
        LOG.info("requiredInnerService");
        uncheckedError();
    }

    @Transactional(propagation = Propagation.NESTED)
    public void nestedInnerService(){
        LOG.info("nestedInnerService");
        uncheckedError();
    }

    //异常继承了 java.lang.RuntimeException
    private void uncheckedError(){
        int a=1/0;
    }
}
