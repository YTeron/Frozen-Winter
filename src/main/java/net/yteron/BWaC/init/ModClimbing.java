package net.yteron.BWaC.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;
import net.yteron.BWaC.BetterWinter;
import net.yteron.BWaC.block.system.ClimbingEntity;
import net.yteron.BWaC.config.Configuration;

@Mod(BetterWinter.MOD_ID)
@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModClimbing {
    public static final String MODID = "better_winter";
    @ObjectHolder(MODID + ":entity_climbing")
    public static final EntityType<ClimbingEntity> SIT_ENTITY_TYPE = null;

    public ModClimbing()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.CONFIG_SPEC);
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event)
    {
        event.getRegistry().register(EntityType.Builder.<ClimbingEntity>of(ClimbingEntity::new, EntityClassification.MISC)
                .setCustomClientFactory((spawnEntity, world) -> SIT_ENTITY_TYPE.create(world))
                .setTrackingRange(256)
                .setUpdateInterval(20)
                .sized(0.0001F, 0.0001F)
                .build(MODID + ":entity_climbing")
                .setRegistryName(new ResourceLocation(MODID, "entity_climbing")));
    }

}
