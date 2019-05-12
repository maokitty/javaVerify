package paxi.maokitty.verify.spring.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 * 运行环境如果有事务就抛出异常
 */
@Service
public class NeverTransactionService {
    private static Logger LOG = LoggerFactory.getLogger(NeverTransactionService.class);

    @Autowired
    private InnerTransactionService innerTransactionService;

    /**
     main [INFO]  (NeverTransactionService.java:28) noTransaction
     main [INFO]  (InnerTransactionService.java:30) neverInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:869) Should roll back transaction but cannot - no transaction available
     main [ERROR]  (NeverTransactionVerify.java:21) main
     java.lang.ArithmeticException: / by zero
     */
    public void noTransaction(){
        LOG.info("noTransaction");
        innerTransactionService.neverInnerService();
    }

    /**
     main [INFO]  (NeverTransactionService.java:43) transaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@243e9ccb]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@243e9ccb] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (NeverTransactionVerify.java:27) main
     org.springframework.transaction.IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'
     */
    @Transactional
    public void transaction(){
        LOG.info("transaction");
        innerTransactionService.neverInnerService();
    }
}
