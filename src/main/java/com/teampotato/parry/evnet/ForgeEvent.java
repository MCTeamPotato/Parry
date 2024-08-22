package com.teampotato.parry.evnet;

import com.teampotato.parry.Parry;
import com.teampotato.parry.config.ModConfigs;
import com.teampotato.parry.misc.ModSound;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Parry.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void hitsun(TickEvent.PlayerTickEvent event){
        if (!ModConfigs.isParryCoolDown.get()) return;
        if (event.player instanceof ServerPlayer serverPlayer){
            long gameTime = serverPlayer.getServer().overworld().getGameTime();
            CompoundTag compoundTag = serverPlayer.getPersistentData();
            if (compoundTag.getLong("hurt") == 0){
                compoundTag.putInt("hitstun", ModConfigs.parryCount.get());
                compoundTag.putLong("hurt", 1);
                serverPlayer.displayClientMessage(Component.translatable("message.parry.cooldown", ModConfigs.parryCount.get(), ModConfigs.parryCount.get()), true);
            }
            long coolTime = gameTime - compoundTag.getLong("hurt");
            int hitstun = (int) ((coolTime - 100) / ModConfigs.parryCoolDown.get());

            if (coolTime > 100) {
                if (ModConfigs.parryCount.get() >= hitstun && hitstun > compoundTag.getInt("hitstun")) {
                    compoundTag.putInt("hitstun", hitstun);
                    serverPlayer.displayClientMessage(Component.translatable("message.parry.cooldown", hitstun, ModConfigs.parryCount.get()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void parryHurt(LivingHurtEvent event){
        if (event.getSource().getEntity() instanceof LivingEntity attacker && event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!isFront(attacker, serverPlayer)) return;

            long gameTime = serverPlayer.getServer().overworld().getGameTime();
            CompoundTag compoundTag = serverPlayer.getPersistentData();
            compoundTag.putLong("hurt", gameTime);

            int hitsun = compoundTag.getInt("hitstun");
            if ((compoundTag.getBoolean("parry") && gameTime - compoundTag.getLong("parry_time") < ModConfigs.parryNeedTime.get()) || !ModConfigs.isParryCoolDown.get()){
                if (hitsun > 0) {
                    serverPlayer.level().playSound(
                            null,
                            serverPlayer.getX(),
                            serverPlayer.getY(),
                            serverPlayer.getZ(),
                            ModSound.PARRY.get(),
                            serverPlayer.getSoundSource(),
                            0.75F,
                            1.0F
                    );
                    compoundTag.putInt("hitstun", --hitsun);
                    compoundTag.putBoolean("parry", false);

                    serverPlayer.displayClientMessage(
                            Component.translatable("message.parry.success")
                                    .append(ModConfigs.isParryCoolDown.get() ? Component.translatable(
                                            "message.parry.success_append",
                                            hitsun,
                                            ModConfigs.parryCount.get()) : Component.literal("")),
                            true);

                    event.setCanceled(true);
                }
                if (hitsun <= 0){
                    MobEffect mobEffect  = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(ModConfigs.parryEffect.get()));
                    serverPlayer.addEffect(new MobEffectInstance(
                            mobEffect,
                            ModConfigs.parryEffectDuration.get(),
                            ModConfigs.parryEffectAmplifier.get(),
                            ModConfigs.parryEffectAmbient.get(),
                            ModConfigs.parryEffectVisible.get(),
                            ModConfigs.parryEffectShowIcon.get()
                    ));
                    serverPlayer.displayClientMessage(Component.translatable("message.parry.failure"), true);
                }
            }
        }
    }

    public static boolean isFront(LivingEntity attacker, ServerPlayer target){
        Vec3 attackerPos = attacker.position();
        Vec3 targetPos = target.position();

        Vec3 direction = attackerPos.subtract(targetPos).normalize();
        Vec3 lookVec = target.getLookAngle().normalize();

        double dotProduct = lookVec.dot(direction);

        double angleInRadians = Math.acos(dotProduct);
        double angleInDegrees = Math.toDegrees(angleInRadians);

        return angleInDegrees <= 67.5;
    }
}
