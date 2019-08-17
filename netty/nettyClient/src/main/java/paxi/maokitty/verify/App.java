package paxi.maokitty.verify;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.constant.MsgType;
import paxi.maokitty.verify.handler.ClientHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/7/9.
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            final Channel channel = new Bootstrap().group(worker).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("stringEncoder",new StringEncoder());
                            ch.pipeline().addLast("stringDecoder",new StringDecoder());
                            ch.pipeline().addLast("clientHandler",new ClientHandler());
                        }
                    })
                    .remoteAddress("localhost", 9000)
                    .connect().sync().channel();

            channel.writeAndFlush(MsgType.REGISTER.getDesc()).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    hearbeatDaemon(channel);
                }
            });
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("main ",e);
        }finally {
            try {
                worker.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LOG.error("error",e);
            }
        }

    }

    private static void hearbeatDaemon(final Channel channel) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        channel.writeAndFlush(MsgType.PING.getDesc()).addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                LOG.info("future:{}",future);
                            }
                        });
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
