package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 * 方法必须在事务中运行，如果没有事务就抛出异常
 */
@Service
public class MandatoryTransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(MandatoryTransactionService.class);

    @Autowired
    private InnerTransactionService innerTransactionService;

    /**
     main [INFO]  (MandatoryTransactionService.java:26) noTransaction
     main [ERROR]  (MandatoryTransactionVerify.java:21) main
     org.springframework.transaction.IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'
     */
    public void noTransaction(){
        LOG.info("noTransaction");
        innerTransactionService.mandatoryInnerService();
    }

    /**
     main [INFO]  (MandatoryTransactionService.java:45) transaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:476) Participating in existing transaction
     main [INFO]  (InnerTransactionService.java:36) mandatoryInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:858) Participating transaction failed - marking existing transaction as rollback-only
     main [DEBUG]  (DataSourceTransactionManager.java:298) Setting JDBC transaction [com.mysql.jdbc.JDBC4Connection@5dfc8cea] rollback-only
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@5dfc8cea]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@5dfc8cea] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (MandatoryTransactionVerify.java:27) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void transaction(){
        LOG.info("transaction");
        innerTransactionService.mandatoryInnerService();
    }
}
