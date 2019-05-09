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
        ApplicationContext ac = new ClassPathXmlApplicationContext("application.xml");
        InnerCallTransactionService transactionService = (InnerCallTransactionService) ac.getBean("innerCallTransactionService");
        try {
            transactionService.outerInsert();
        }catch (Exception e){
            LOG.error("main",e);
        }
        try {
            transactionService.innerInsert();
        }catch (Exception e){
            LOG.error("main",e);
        }

    }
}
