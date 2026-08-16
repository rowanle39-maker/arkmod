package com.arkmod;

import com.arkmod.entity.EntityRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArkMod.MODID, name = ArkMod.NAME, version = ArkMod.VERSION)
public class ArkMod {

    public static final String MODID = "arkmod";
    public static final String NAME = "ArkMod";
    public static final String VERSION = "1.0.0";

    @Instance
    public static ArkMod instance;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("ArkMod yükleniyor...");
        EntityRegistry.registerEntities();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        
    }
}
