package paxi.maokitty.verify.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.constant.EventType;

import java.util.concurrent.TimeUnit;

/**
 * Created by maokitty on 19/7/11.
 */
public class ServerIdleHandler extends IdleStateHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ServerIdleHandler.class);
    public ServerIdleHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
//        super.channelIdle(ctx, evt);
        if (evt.state() == IdleState.ALL_IDLE){
            ctx.fireUserEventTriggered(EventType.CHANNEL_IDLE);
        }
    }
}
