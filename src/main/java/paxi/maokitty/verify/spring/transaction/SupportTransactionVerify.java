package paxi.maokitty.verify.spring.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.spring.transaction.service.SupprotTransactionTestService;

/**
 * Created by maokitty on 19/5/12.
 */
public class SupportTransactionVerify {
    private static final Logger LOG = LoggerFactory.getLogger(SupportTransactionVerify.class);
    public static void main(String[] args) {
        LOG.info("change local mysql username and password before run");
        ApplicationContext ac = new ClassPathXmlApplicationContext("tx-application.xml");
        SupprotTransactionTestService tx = (SupprotTransactionTestService) ac.getBean("supprotTransactionTestService");
        try {
            tx.outerNoTransaction();
        }catch (Exception e){
            LOG.error("main",e);
        }
        try {
            tx.outerTransaction();
        }catch (Exception e){
            LOG.error("main",e);
        }

    }
}
