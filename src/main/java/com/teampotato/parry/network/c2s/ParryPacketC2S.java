//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.teampotato.parry.network.c2s;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public record ParryPacketC2S(long gametime) {
    public ParryPacketC2S(long gametime) {
        this.gametime = gametime;
    }

    public static ParryPacketC2S parry(long gametime) {
        return new ParryPacketC2S(gametime);
    }

    public static void encode(ParryPacketC2S packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packet.gametime);
    }

    public static ParryPacketC2S decode(FriendlyByteBuf friendlyByteBuf) {
        return new ParryPacketC2S(friendlyByteBuf.readLong());
    }

    public static void handle(ParryPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                CompoundTag compoundTag = player.getPersistentData();
                compoundTag.putBoolean("parry", true);
                compoundTag.putLong("parry_time", packet.gametime);
            }
        });
        context.setPacketHandled(true);
    }
}
