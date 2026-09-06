package com.thevortex.potionsmaster.network;

import com.thevortex.potionsmaster.PotionsMaster;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class PacketHandler {

    public PacketHandler() {
    }

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PotionsMaster.MOD_ID);
        registrar.playToClient(PotionPacket.TYPE, PotionPacket.CODEC, PotionPacket.Handler::handle);
        registrar.playToClient(OreSightEnablePacket.TYPE, OreSightEnablePacket.CODEC, OreSightEnablePacket.Handler::handle);
    }

    /**
     * Sends a packet to the server.<br> Must be called Client side.
     */
    public static void sendToServer(CustomPacketPayload msg) {

    }

    /**
     * Send a packet to a specific player.<br> Must be called Server side.
     */
    public static void sendTo(CustomPacketPayload msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer)) {
            player.connection.send(msg);
        }
    }
}