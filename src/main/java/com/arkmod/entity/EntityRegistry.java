package com.arkmod.entity;

import com.arkmod.ArkMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityRegistry {

    public static void registerEntities() {
        registerEntity("girlfriend", EntityGirlfriend.class, 201, 0xFF69B4, 0xFFFFFF);
    }

    private static void registerEntity(String name, Class<? extends Entity> entityClass, int id, int eggPrimary, int eggSecondary) {
        EntityRegistry.registerModEntity(
                new ResourceLocation(ArkMod.MODID, name),
                entityClass,
                name,
                id,
                ArkMod.instance, // Ana mod sınıfında instance ekleyeceğiz
                64,  // tracking range
                1,   // update frequency
                true, // send velocity
                eggPrimary,
                eggSecondary
        );
    }
}
