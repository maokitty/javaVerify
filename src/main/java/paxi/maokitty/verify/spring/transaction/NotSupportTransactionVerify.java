package paxi.maokitty.verify.spring.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.spring.transaction.service.NotSupportTransactionService;

/**
 * Created by maokitty on 19/5/12.
 */
public class NotSupportTransactionVerify {
    private static Logger LOG = LoggerFactory.getLogger(NotSupportTransactionVerify.class);
    public static void main(String[] args) {
        LOG.info("change local mysql username and password before run");
        ApplicationContext ac = new ClassPathXmlApplicationContext("tx-application.xml");
        NotSupportTransactionService tx = (NotSupportTransactionService) ac.getBean("notSupportTransactionService");
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
