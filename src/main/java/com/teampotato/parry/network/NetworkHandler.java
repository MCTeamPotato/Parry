//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.teampotato.parry.network;

import com.teampotato.parry.network.c2s.ParryPacketC2S;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("parry", "main"), () -> {
        return "1";
    }, "1"::equals, "1"::equals);
    private static int packetId = 0;

    public NetworkHandler() {
    }

    public static void register() {
        CHANNEL.registerMessage(packetId++, ParryPacketC2S.class, ParryPacketC2S::encode, ParryPacketC2S::decode, ParryPacketC2S::handle);
    }
}
