package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 * 方法不会应该在事务中执行，如果存在事务，那么这个任务会先被挂起
 */
@Service
public class NotSupportTransactionService {
    private static Logger LOG = LoggerFactory.getLogger(NotSupportTransactionService.class);
    @Autowired
    private InnerTransactionService innerTransactionService;

    /**
     main [INFO]  (NotSupportTransactionService.java:19) noTransaction
     main [INFO]  (InnerTransactionService.java:24) notSupportInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:869) Should roll back transaction but cannot - no transaction available
     main [ERROR]  (NotSupportTransactionVerify.java:20) main
     java.lang.ArithmeticException: / by zero
     */
    public void noTransaction(){
        LOG.info("noTransaction");
        innerTransactionService.notSupportInnerService();
    }

    /**
     main [INFO]  (NotSupportTransactionService.java:25) transaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:411) Suspending current transaction
     main [INFO]  (InnerTransactionService.java:24) notSupportInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:869) Should roll back transaction but cannot - no transaction available
     main [DEBUG]  (AbstractPlatformTransactionManager.java:1020) Resuming suspended transaction after completion of inner transaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@579b7698]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@579b7698] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (NotSupportTransactionVerify.java:26) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void transaction(){
        LOG.info("transaction");
        innerTransactionService.notSupportInnerService();
    }

}
