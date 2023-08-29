package nl.itz_kiwisap_.spigot.common.network.interceptor;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

final class PacketInterceptorListener extends ChannelDuplexHandler {

    private final PacketInterceptorHandler handler;
    private final Player player;

    PacketInterceptorListener(PacketInterceptorHandler handler, Player player) {
        this.handler = handler;
        this.player = player;
    }

    @SuppressWarnings("unchecked, rawtypes")
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.player == null || !this.player.isOnline()) {
            super.write(ctx, msg, promise);
            return;
        }

        Map<Class<? extends KPacket>, Collection<PacketInterceptor<?>>> serverboundInterceptors = this.handler.clientboundInterceptors;
        if (serverboundInterceptors.isEmpty()) { // No interceptors to listen to
            super.write(ctx, msg, promise);
            return;
        }

        KPacket kPacket = KiwiNMS.getInstance().transformClientboundPacket(msg);
        if (kPacket == null) { // Not a packet we have to listen to
            super.write(ctx, msg, promise);
            return;
        }

        Collection<PacketInterceptor<?>> interceptors = serverboundInterceptors.get(kPacket.getClass());
        if (interceptors == null || interceptors.isEmpty()) { // No interceptors to listen to
            super.write(ctx, msg, promise);
            return;
        }

        for (PacketInterceptor interceptor : interceptors) {
            if (interceptor.intercept(this.player, kPacket)) {
                return;
            }
        }

        super.write(ctx, msg, promise);
    }

    @SuppressWarnings("unchecked, rawtypes")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.player == null || !this.player.isOnline()) {
            super.channelRead(ctx, msg);
            return;
        }

        Map<Class<? extends KPacket>, Collection<PacketInterceptor<?>>> serverboundInterceptors = this.handler.serverboundInterceptors;
        if (serverboundInterceptors.isEmpty()) { // No interceptors to listen to
            super.channelRead(ctx, msg);
            return;
        }

        KPacket kPacket = KiwiNMS.getInstance().transformServerboundPacket(msg);
        if (kPacket == null) { // Not a packet we have to listen to
            super.channelRead(ctx, msg);
            return;
        }

        Collection<PacketInterceptor<?>> interceptors = serverboundInterceptors.get(kPacket.getClass());
        if (interceptors == null || interceptors.isEmpty()) { // No interceptors to listen to
            super.channelRead(ctx, msg);
            return;
        }

        for (PacketInterceptor interceptor : interceptors) {
            if (interceptor.intercept(this.player, kPacket)) {
                return;
            }
        }

        super.channelRead(ctx, msg);
    }
}