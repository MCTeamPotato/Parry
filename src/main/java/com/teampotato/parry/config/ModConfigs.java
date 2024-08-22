package com.teampotato.parry.config;

import com.teampotato.parry.Parry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Parry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {
    public static ForgeConfigSpec configSpec;
    public static ForgeConfigSpec.BooleanValue isParryKeyCoolDown, isParryCoolDown, parryEffectAmbient, parryEffectVisible, parryEffectShowIcon;
    public static ForgeConfigSpec.ConfigValue<String> parryEffect;
    public static ForgeConfigSpec.ConfigValue<Integer> parryKeyCoolDown, parryCoolDown, parryCount, parryNeedTime, parryEffectDuration, parryEffectAmplifier;
    public static ForgeConfigSpec.ConfigValue<List<String>> parryWeapon;
    public static List<String> weaponList = List.of("minecraft:wooden_sword", "minecraft:stone_sword", "minecraft:iron_sword", "minecraft:golden_sword", "minecraft:diamond_sword", "minecraft:diamond_sword", "minecraft:trident", "minecraft:bow");

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Parry");
        isParryKeyCoolDown = builder.define("isParryKeyCoolDown", true);
        parryKeyCoolDown = builder.define("parryKeyCoolDown", 20);

        isParryCoolDown = builder.define("isParryCoolDown", true);
        parryWeapon = builder.define("parryWeapon", weaponList);
        parryCoolDown = builder.define("parryCoolDown", 20);
        parryCount = builder.define("parryCounts", 5);
        parryNeedTime = builder.define("parryNeedTime", 20);
        parryEffect = builder.define("parryEffect", "minecraft:mining_fatigue");
        parryEffectDuration = builder.define("parryEffectDuration", 100);
        parryEffectAmplifier = builder.define("parryEffectAmplifier", 0);
        parryEffectAmbient = builder.define("parryEffectAmbient", false);
        parryEffectVisible = builder.define("parryEffectVisible", true);
        parryEffectShowIcon = builder.define("parryEffectShowIcon", true);

        configSpec = builder.build();
    }
}
