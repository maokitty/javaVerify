package paxi.maokitty.verify;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.handler.ServerHandler;
import paxi.maokitty.verify.handler.ServerIdleHandler;

import java.util.concurrent.TimeUnit;


/**
 * Created by maokitty on 19/7/9.
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ChannelFuture future = bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addFirst("idleHandler",new ServerIdleHandler(0,0,10, TimeUnit.SECONDS));
                            //将收到的Bytebuf转成String
                            pipeline.addLast("stringDecoder", new StringDecoder());
                            //将写的String转成byteBuf
                            pipeline.addLast("stringEncoder", new StringEncoder());
                            pipeline.addLast("serverHandler", new ServerHandler());
                        }
                    })
                    .localAddress(9000)
                    .bind()
                    .sync();
            LOG.info("init future");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.info("error",e);
        }finally {
            try {
                parentGroup.shutdownGracefully().sync();
                childGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LOG.error("error",e);
            }
        }
    }
}
