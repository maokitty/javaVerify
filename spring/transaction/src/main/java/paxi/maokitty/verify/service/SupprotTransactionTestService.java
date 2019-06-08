package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 * 如果当前运行环境中有事务，就在事务中执行，否则不按照事务执行
 */
@Service
public class SupprotTransactionTestService {
    private static Logger LOG = LoggerFactory.getLogger(SupprotTransactionTestService.class);
    @Autowired
    private InnerTransactionService innerTransactionService;

    /**
     main [INFO]  (SupprotTransactionTestService.java:27) outerNoTransaction
     main [INFO]  (InnerTransactionService.java:18) supportInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:869) Should roll back transaction but cannot - no transaction available
     main [ERROR]  (SupportTransactionVerify.java:20) main
     java.lang.ArithmeticException: / by zero
     */
    public void outerNoTransaction(){
        LOG.info("outerNoTransaction");
        innerTransactionService.supportInnerService();
    }

    /**
     main [INFO]  (SupprotTransactionTestService.java:46) outerTransaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:476) Participating in existing transaction
     main [INFO]  (InnerTransactionService.java:18) supportInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:858) Participating transaction failed - marking existing transaction as rollback-only
     main [DEBUG]  (DataSourceTransactionManager.java:298) Setting JDBC transaction [com.mysql.jdbc.JDBC4Connection@4c389dc6] rollback-only
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@4c389dc6]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@4c389dc6] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (SupportTransactionVerify.java:25) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void outerTransaction(){
        LOG.info("outerTransaction");
        innerTransactionService.supportInnerService();
    }
}
