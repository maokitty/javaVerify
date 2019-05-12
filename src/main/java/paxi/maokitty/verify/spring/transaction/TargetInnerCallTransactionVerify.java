package paxi.maokitty.verify.spring.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.spring.transaction.service.InnerCallTransactionService;

/**
 * Created by maokitty on 19/5/5.
 */
public class TargetInnerCallTransactionVerify {
    private static Logger LOG = LoggerFactory.getLogger(TargetInnerCallTransactionVerify.class);
    public static void main(String[] args) {
        LOG.info("change local mysql username and password before run");
        ApplicationContext ac = new ClassPathXmlApplicationContext("application.xml");
        InnerCallTransactionService transactionService = (InnerCallTransactionService) ac.getBean("innerCallTransactionService");
        LOG.info("start outerInsert");
        try {
            transactionService.outerInsert();
        }catch (Exception e){
            LOG.error("main",e);
        }
        LOG.info("start innerInsert");
        try {
            transactionService.innerInsert();
        }catch (Exception e){
            LOG.error("main",e);
        }

    }
}
