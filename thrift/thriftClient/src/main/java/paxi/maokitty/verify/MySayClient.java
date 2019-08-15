package paxi.maokitty.verify;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.DemoService;

/**
 * Created by maokitty on 2019/8/8.
 */
public class MySayClient {
    private static Logger LOG = LoggerFactory.getLogger(MySayClient.class);

    public static void main(String[] args) {
        TTransport transport=null;
        try {
            //1:网路请求相关设置
            transport=new TSocket("127.0.0.1",9000,1000);
            //2:传输数据的编码方式
            TProtocol protocol=new TBinaryProtocol(transport);
            //3:建立连接
            transport.open();
            //4:创建客户端
            DemoService.Client client=new DemoService.Client(protocol);
            //5:发起请求
            String say = client.say("i am client");
            LOG.info("back result:{}",say);
        }catch (Exception e){
           LOG.error("main",e);
        }finally {
            if (transport!=null){
                transport.close();
            }
        }
    }
}
