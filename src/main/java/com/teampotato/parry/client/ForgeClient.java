package com.teampotato.parry.client;

import com.teampotato.parry.Parry;
import com.teampotato.parry.config.ModConfigs;
import com.teampotato.parry.network.NetworkHandler;
import com.teampotato.parry.network.c2s.ParryPacketC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Parry.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClient {
    @SubscribeEvent
    public static void parryUp(TickEvent.ClientTickEvent event){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            if (KeyBindings.PARRY.get().consumeClick()){
                if (canBeWeapon(localPlayer.getMainHandItem())) return;

                long gametime = localPlayer.clientLevel.getGameTime();
                CompoundTag compoundTag = localPlayer.getPersistentData();
                if (compoundTag.getInt("parry_click") == 0 || gametime - compoundTag.getInt("parry_click") >= ModConfigs.parryKeyCoolDown.get() || !ModConfigs.isParryKeyCoolDown.get()){
                    compoundTag.putLong("parry_click", gametime);
                    localPlayer.swing(InteractionHand.MAIN_HAND);
                    NetworkHandler.CHANNEL.sendToServer(ParryPacketC2S.parry(gametime));
                } else {
                    localPlayer.displayClientMessage(Component.translatable("message.parry.key_failure"), true);
                }
            }
        }
    }

    private static boolean canBeWeapon(@NotNull ItemStack stack) {
        return stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE);
    }
}
