package paxi.maokitty.verify.spring.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.spring.transaction.service.RequireTransactionService;

/**
 * Created by maokitty on 19/5/12.
 */
public class RequireTransactionVerify {
    private static final Logger LOG = LoggerFactory.getLogger(RequireTransactionVerify.class);

    public static void main(String[] args) {
        LOG.info("change local mysql username and password before run");
        ApplicationContext ac = new ClassPathXmlApplicationContext("application.xml");
        RequireTransactionService tx = (RequireTransactionService) ac.getBean("requireTransactionService");
        try {
            tx.requireService();
        }catch (Exception e){
            LOG.error("main",e);
        }

        try {
            tx.requireNewService();
        }catch (Exception e){
            LOG.error("main",e);
        }

        try {
            tx.nestedService();
        }catch (Exception e){
            LOG.error("main",e);
        }
    }
}
