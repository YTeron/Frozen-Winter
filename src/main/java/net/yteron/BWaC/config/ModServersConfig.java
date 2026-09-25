package net.yteron.BWaC.config;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


public class ModServersConfig {
    private static final double  DEFAULT_TEMP = 0.0;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final Map<String, ForgeConfigSpec.DoubleValue> BIOME_TEMPS = new HashMap<>();

    static{
        BUILDER.push("Tempter systems config");
        for (Biome biome : ForgeRegistries.BIOMES) {
            ResourceLocation id = biome.getRegistryName();
            if (id == null) continue;
            String biomeId = id.toString();
            String key = biomeId.replace(":", "_") + "_temperature";
            ForgeConfigSpec.DoubleValue value = BUILDER
                    .comment("Температура " + biomeId)
                    .defineInRange(key, DEFAULT_TEMP, -100.0, 100.0);
            BIOME_TEMPS.put(biomeId, value);
        }
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    public static float getFloatTemp(String idBiome){
        ForgeConfigSpec.DoubleValue v = BIOME_TEMPS.get(idBiome);
        return v != null ? v.get().floatValue() : 0f;
    }
}
