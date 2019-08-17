package paxi.maokitty.verify.handler;

import io.netty.channel.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.constant.EventType;
import paxi.maokitty.verify.constant.MsgType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/7/9.
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class);
    private final static Map<String,Channel> channels = new ConcurrentHashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
//        LOG.info("channelRegistered ctx:{}", ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("channelActive remoteAddress:{}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.info("channelInactive remoteAddress:{}", ctx.channel().remoteAddress().toString());
        channels.remove(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
//        LOG.info("channelUnregistered remoteAddress:{}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        String remoteAddress = ctx.channel().remoteAddress().toString();
//        LOG.info("userEventTriggered remoteAddress:{} evt:{}", remoteAddress,evt);
        if (evt instanceof EventType){
            if (evt == EventType.CHANNEL_IDLE){
                ctx.close();
                channels.remove(remoteAddress);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LOG.error("exceptionCaught remoteAddress:{}",ctx.channel().remoteAddress().toString(),cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        LOG.info("channelRead message:{}", message);
        if (StringUtils.isBlank(message)){
            ctx.channel().close();
            return;
        }
        int sepratorIndex = message.indexOf(":");
        String msgHeader =message;
        if (sepratorIndex>-1){
            msgHeader=message.substring(0, sepratorIndex);
        }
        MsgType type = MsgType.get(msgHeader);
        switch (type){
            case PING:
                ctx.writeAndFlush(MsgType.PONG.getDesc()).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()){
                            LOG.error("write fail:{} ",future.cause());
                        }
                    }
                });
                LOG.info("current links:{}",channels.size());
                break;
            case REGISTER:
                String key = ctx.channel().remoteAddress().toString();
                channels.put(key,ctx.channel());
                break;
            case ECHO:
                String sleepTime=message.substring(sepratorIndex,message.length());
                if (NumberUtils.isNumber(sleepTime)){
                    int sleepInMills = NumberUtils.toInt(sleepTime);
                    TimeUnit.MILLISECONDS.sleep(sleepInMills);
                    ctx.channel().writeAndFlush(MsgType.ECHO.getDesc()).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()){
                                LOG.error("write echo:{}",future);
                            }
                        }
                    });
                }
            default:
                LOG.info("unkonw msg:{}", message);
                ctx.channel().close();
                return;

        }
    }
}
