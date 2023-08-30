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

        Map<Class<? extends KPacket>, Collection<PacketInterceptor<?>>> clientboundInterceptors = this.handler.clientboundInterceptors;
        if (clientboundInterceptors.isEmpty()) { // No interceptors to listen to
            super.write(ctx, msg, promise);
            return;
        }

        KiwiNMS nms = KiwiNMS.getInstance();

        Collection<KPacket> kPackets = nms.transformClientboundPacket(msg);
        if (kPackets == null || kPackets.isEmpty()) { // Not a packet we have to listen to
            super.write(ctx, msg, promise);
            return;
        }

        boolean intercepted = false;
        for (KPacket kPacket : kPackets) {
            Class<?> kPacketInterface = (kPacket.getClass().getInterfaces().length == 0) ? null : kPacket.getClass().getInterfaces()[0];
            if (kPacketInterface == null) continue;

            Collection<PacketInterceptor<?>> interceptors = clientboundInterceptors.get(kPacketInterface);
            if (interceptors == null || interceptors.isEmpty()) { // No interceptors to listen to
                super.write(ctx, msg, promise);
                return;
            }

            for (PacketInterceptor interceptor : interceptors) {
                if (interceptor.intercept(this.player, kPacket)) {
                    if (nms.isBundlePacket(msg)) {
                        nms.removePacketFromBundle(msg, kPacket.getNMSInstance());

                        if (nms.isBundleEmpty(msg)) {
                            intercepted = true;
                        }

                        continue;
                    }

                    intercepted = true;
                }
            }
        }

        if (!intercepted) {
            super.write(ctx, msg, promise);
        }
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

        KiwiNMS nms = KiwiNMS.getInstance();

        Collection<KPacket> kPackets = nms.transformServerboundPacket(msg);
        if (kPackets == null) { // Not a packet we have to listen to
            super.channelRead(ctx, msg);
            return;
        }

        boolean intercepted = false;
        for (KPacket kPacket : kPackets) {
            Class<?> kPacketInterface = (kPacket.getClass().getInterfaces().length == 0) ? null : kPacket.getClass().getInterfaces()[0];
            if (kPacketInterface == null) continue;

            Collection<PacketInterceptor<?>> interceptors = serverboundInterceptors.get(kPacketInterface);
            if (interceptors == null || interceptors.isEmpty()) { // No interceptors to listen to
                super.channelRead(ctx, msg);
                return;
            }

            for (PacketInterceptor interceptor : interceptors) {
                if (interceptor.intercept(this.player, kPacket)) {
                    if (nms.isBundlePacket(msg)) {
                        nms.removePacketFromBundle(msg, kPacket.getNMSInstance());

                        if (nms.isBundleEmpty(msg)) {
                            intercepted = true;
                        }

                        continue;
                    }

                    intercepted = true;
                }
            }
        }

        if (!intercepted) {
            super.channelRead(ctx, msg);
        }
    }
}