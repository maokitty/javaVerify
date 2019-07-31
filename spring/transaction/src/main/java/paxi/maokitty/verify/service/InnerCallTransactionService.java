package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maokitty on 19/5/6.
 * 使用默认的代理模式，目标Transaction的内部调用方法不会生效
 */
@Service
public class InnerCallTransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(InnerCallTransactionService.class);

    /**
     *
     * 间接调用不会生效
     main [INFO]  (InnerCallTransactionService.java:16) outerInsert execute
     main [INFO]  (InnerCallTransactionService.java:21) innerInsert execute add default transaction won't be execute if call by InnerCallTransactionService.outerInsert
     main [ERROR]  (TargetInnerCallTransactionVerify.java:20) main
     java.lang.ArithmeticException: / by zero
     */
    public void outerInsert(){
        LOG.info("outerInsert execute ");
        innerInsert();
    }

    /** 直接调用生效
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@79278ae3]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@79278ae3] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (TargetInnerCallTransactionVerify.java:25) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void innerInsert(){
        LOG.info("innerInsert execute add default transaction won't be execute if call by InnerCallTransactionService.outerInsert");
        int i=1/0;
    }

    /**
     内部嵌套仍然生效
     main [DEBUG]  (AbstractPlatformTransactionManager.java:851) Initiating transaction rollback
     main [DEBUG]  (DataSourceTransactionManager.java:284) Rolling back JDBC transaction on Connection [com.mysql.jdbc.JDBC4Connection@1d483de4]
     main [DEBUG]  (DataSourceTransactionManager.java:327) Releasing JDBC Connection [com.mysql.jdbc.JDBC4Connection@1d483de4] after transaction
     main [DEBUG]  (DataSourceUtils.java:327) Returning JDBC Connection to DataSource
     main [ERROR]  (TargetInnerCallTransactionVerify.java:35) main
     java.lang.ArithmeticException: / by zero
     */
    @Transactional
    public void wrapOfInnerInsert(){
       LOG.info("wrapOfInnerInsert execute");
        error();
    }
    private void error(){
        LOG.info("error execute");
        int i=1/0;
    }

}
