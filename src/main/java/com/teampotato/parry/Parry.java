package com.teampotato.parry;

import com.mojang.logging.LogUtils;
import com.teampotato.parry.config.ModConfigs;
import com.teampotato.parry.misc.ModSound;
import com.teampotato.parry.network.NetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Parry.MODID)
public class Parry {
    public static final String MODID = "parry";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Parry() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModSound.SOUNDS.register(bus);
        NetworkHandler.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.configSpec);
    }
}
