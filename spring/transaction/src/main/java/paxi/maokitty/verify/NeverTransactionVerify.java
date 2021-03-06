package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.service.NeverTransactionService;

/**
 * Created by maokitty on 19/5/12.
 */
public class NeverTransactionVerify {
    private static final Logger LOG = LoggerFactory.getLogger(NeverTransactionVerify.class);

    public static void main(String[] args) {
        LOG.info("change local mysql username and password before run");
        ApplicationContext ac = new ClassPathXmlApplicationContext("tx-application.xml");
        NeverTransactionService tx = (NeverTransactionService) ac.getBean("neverTransactionService");
        try {
            tx.noTransaction();
        }catch (Exception e){
            LOG.error("main",e);
        }

        try {
            tx.transaction();
        }catch (Exception e){
            LOG.error("main",e);
        }
    }
}
