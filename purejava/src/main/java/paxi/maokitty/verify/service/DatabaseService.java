package paxi.maokitty.verify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by maokitty on 19/5/19.
 */
public class DatabaseService {
   private static final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);
    private static final String USER_NAME="xxx";
    private static final String PASSWORD="xxx";
    private static final String URL="jdbc:mysql://127.0.0.1:3306/test?autoReconnect=true";

    /**
     * mysql> desc
     -> a ;
     +-------+---------+------+-----+---------+----------------+
     | Field | Type    | Null | Key | Default | Extra          |
     +-------+---------+------+-----+---------+----------------+
     | age   | int(11) | YES  | UNI | NULL    |                |
     | id    | int(11) | NO   | PRI | NULL    | auto_increment |
     +-------+---------+------+-----+---------+----------------+
     */
    public void insert(){
        LOG.info("insert make sure change username and password to local mysql setting");
        Connection con=null;
        PreparedStatement ps=null;
        try {
            //获取数据库的连接
            con= DriverManager.getConnection(URL,USER_NAME,PASSWORD);
            //设置为非自动提交
            con.setAutoCommit(false);

            ps=con.prepareStatement("insert into a (age) values(?)");
            ps.setInt(1, 20);
            int lines = ps.executeUpdate();
            LOG.info("insert first lines:{}",lines);
            //提交
            con.commit();

        } catch (Exception e) {
            LOG.error("insert ",e);
            try {
                LOG.info("rollback before");
                //异常回滚
                if (con!=null)
                {
                    con.rollback();
                }else{
                    LOG.warn("conn is null");
                }
                LOG.info("rollback after");
            } catch (SQLException e1) {
                LOG.error("insert rollback",e);
            }

        }finally {

            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    LOG.error("insert ",e);
                }
            }
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    LOG.error("insert ",e);
                }
            }
        }
    }

}
