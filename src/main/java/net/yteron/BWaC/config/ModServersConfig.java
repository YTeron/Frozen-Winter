package net.yteron.BWaC.config;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.BWaC.cold_system.TempChunkSimple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class ModServersConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BIOMES;
    static {
        BUILDER.push("Tempter systems config");

        BIOMES = BUILDER
                .comment("Формат: 'namespace:path=temperature'",
                        "Пример: 'minecraft:snowy_taiga=-1.5'")
                .defineList(
                        "Biomes",
                        new ArrayList<String>(),
                        o -> o instanceof String
                );

        BUILDER.pop();
        SPEC =BUILDER.build();
    }
    public static void forRegistry(){
        if (!BIOMES.get().isEmpty()) return;
        List<String>list = new ArrayList<>();
        for (Biome biome : ForgeRegistries.BIOMES) {
            switch (biome.getBiomeCategory()){
                case ICY:  list.add(biome.getRegistryName()+"=-10.0");break;
                case MESA: list.add(biome.getRegistryName()+"=30.0");break;
                case BEACH: list.add(biome.getRegistryName()+"=25.0");break;
                case OCEAN: list.add(biome.getRegistryName()+"=10.0");break;
                case TAIGA: list.add(biome.getRegistryName()+"=35.0");break;
                case SWAMP: list.add(biome.getRegistryName()+"=20.0");break;
                case PLAINS: list.add(biome.getRegistryName()+"=22.5");break;
                case NETHER: list.add(biome.getRegistryName()+"=66.6");break;
                default: list.add(biome.getRegistryName()+"=0.0");break;
            }
        }
        BIOMES.set(list);
        SPEC.save();
    }
    public static Map<ResourceLocation,Float> getTempFromBiomse(){
        Map<ResourceLocation, Float> map = new HashMap<>();
        for (String b: BIOMES.get()) {
            int index = b.indexOf("=");
            if (index < 0) continue;
            String idPart   = b.substring(0, index).trim();
            try {
                ResourceLocation id = new ResourceLocation(idPart);
                float temp = Float.parseFloat(b.substring(index+1).trim());
                map.put(id, temp);
            }
            catch (Exception e) {
                LOGGER.warn("Не удалось распарсить строку конфига: {}", b, e);
            }
        }
        return map;
    }

}
