package paxi.maokitty.verify;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.impl.DemoServiceImpl;
import paxi.maokitty.verify.service.DemoService;

/**
 * Created by maokitty on 2019/8/8.
 */
public class MySayServer {
    private static Logger LOG = LoggerFactory.getLogger(MySayServer.class);
    public static void main(String[] args) {
        try {
            //1:创建等待连接的serverSocket
            TServerSocket serverSocket=new TServerSocket(9000);
            //2:构建server所需要的参数
            TServer.Args serverArgs=new TServer.Args(serverSocket);
            //3:逻辑处理
            TProcessor processor=new DemoService.Processor<DemoService.Iface>(new DemoServiceImpl());
            //4:解析协议
            serverArgs.protocolFactory(new TBinaryProtocol.Factory());
            serverArgs.processor(processor);
            //5:组织组件完成功能
            TServer server=new TSimpleServer(serverArgs);
            LOG.info("main server start ... ");
            //6：等待连接到来
            server.serve();
        } catch (Exception e) {
            LOG.error("main",e);
        }


    }
}
