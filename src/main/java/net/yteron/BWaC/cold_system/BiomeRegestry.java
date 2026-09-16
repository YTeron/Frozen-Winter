package net.yteron.BWaC.cold_system;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class BiomeRegestry {
    public static final Map<ResourceLocation, Float> ALL_BIOMES = new HashMap<>();
    public static void init() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            ResourceLocation id = biome.getRegistryName();
            if (id != null) {
                ALL_BIOMES.put(id, 0f);
            }
        }
    }
}
