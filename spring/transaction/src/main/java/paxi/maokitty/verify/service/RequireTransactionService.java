package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/12.
 * requires_new:每次都新建一个事务，如果当前有另一个正在运行的事务，则将这个事务暂停，等新的完成之后，再恢复上下文执行
 * require:一定在事务中执行，如果已经存在事务，仍然在这个事务中执行
 * nested:如果当前正有一个事务运行中，则对应方法会运行在一个嵌套的事务中，被嵌套的事务可以独立与封装事件进行提交或者回滚
 */
@Service
public class RequireTransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(RequireTransactionService.class);

    @Autowired
    private InnerTransactionService innerTransactionService;

    /**
     main [INFO]  (RequireTransactionService.java:42) requireNewService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:421) Suspending current transaction, creating new transaction with name [paxi.maokitty.verify.spring.transaction.service.InnerTransactionService.requiresNewInnerService]
     main [DEBUG]  (DataSourceTransactionManager.java:206) Acquired Connection [com.mysql.jdbc.JDBC4Connection@17da28cf] for JDBC transaction
     main [DEBUG]  (DataSourceTransactionManager.java:223) Switching JDBC Connection [com.mysql.jdbc.JDBC4Connection@17da28cf] to manual commit
     main [INFO]  (InnerTransactionService.java:42) requiresNewInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@17da28cf]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@17da28cf] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [DEBUG]  (AbstractPlatformTransactionManager.java:1020) Resuming suspended transaction after completion of inner transaction
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (RequireTransactionVerify.java:27) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void requireNewService(){
        LOG.info("requireNewService");
        innerTransactionService.requiresNewInnerService();
    }

    /**
     main [INFO]  (RequireTransactionService.java:61) requireService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:476) Participating in existing transaction
     main [INFO]  (InnerTransactionService.java:48) requiredInnerService
     main [DEBUG]  (AbstractPlatformTransactionManager.java:858) Participating transaction failed - marking existing transaction as rollback-only
     main [DEBUG]  (DataSourceTransactionManager.java:298) Setting JDBC transaction [com.mysql.jdbc.JDBC4Connection@70af9dc4] rollback-only
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (RequireTransactionVerify.java:21) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void requireService(){
        LOG.info("requireService");
        innerTransactionService.requiredInnerService();
    }

    /**
     main [INFO]  (RequireTransactionService.java:79) nestedService
    main [DEBUG]  (AbstractPlatformTransactionManager.java:450) Creating nested transaction with name [paxi.maokitty.verify.spring.transaction.service.InnerTransactionService.nestedInnerService]
    main [INFO]  (InnerTransactionService.java:54) nestedInnerService
    main [DEBUG]  (AbstractPlatformTransactionManager.java:845) Rolling back transaction to savepoint
    main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
    main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4]
    main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@70af9dc4] after transaction
    main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
    main [ERROR]  (RequireTransactionVerify.java:33) main
    java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void nestedService(){
        LOG.info("nestedService");
        innerTransactionService.nestedInnerService();
    }

}
