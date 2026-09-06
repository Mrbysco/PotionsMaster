package com.thevortex.potionsmaster.network;

import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.render.util.BlockData;
import com.thevortex.potionsmaster.render.util.BlockStore;
import com.thevortex.potionsmaster.render.util.xray.Controller;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OreSightEnablePacket(String effectType) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, OreSightEnablePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			OreSightEnablePacket::effectType,
			OreSightEnablePacket::new);
	public static final Type<OreSightEnablePacket> TYPE = new Type<>(PotionsMaster.getId("ore_sight_enable_packet"));


	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static class Handler {
		public static void handle(final OreSightEnablePacket message, IPayloadContext ctx) {
			ctx.enqueueWork(() -> enable(message.effectType))
					.exceptionally(e -> {
						ctx.disconnect(Component.translatable("potionsmaster.networking.ore_sight_enable_packet.failed", e.getMessage()));
						return null;
					});
		}

		private static void enable(String effectType) {
			BlockStore.BlockDataWithUUID bdUUID = PotionsMaster.blockStore.getStoreByReference(effectType);
			if (bdUUID == null) return;

			BlockData oreSight = bdUUID.getBlockData();
			if (!oreSight.isDrawing()) {
				oreSight.setDrawing(true);
				if (!Controller.drawOres()) {
					Controller.toggleDrawOres();
				}
			}
		}
	}
}
