package net.yteron.BWaC.cold_system;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.yteron.BWaC.cold_system.TempChunkHandler;
import net.yteron.BWaC.config.ModServersConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempChunkSimple extends TempChunkHandler {

    private Map<World, tempChunk> perWorld = new HashMap<>();
    private static final float maxRad = 100_000F;
    private static final String NBT_KEY_CHUNK_RADIATION = "chunk_radiation";
    private int ticks=0;
    @Override
    public void updateSystem() {
        for(Map.Entry<World, tempChunk> entry : perWorld.entrySet()) {
            World world = entry.getKey();
            Map<ChunkPos, Float> radiation = entry.getValue().temp;
            Map<ChunkPos, Float> buff = new HashMap<>(radiation);
            radiation.clear();

            for (Map.Entry<ChunkPos, Float> chunk : buff.entrySet()) {
                ChunkPos coord = chunk.getKey();
                Biome biome = world.getBiome(coord.getWorldPosition());
                ResourceLocation resourceLocation = biome.getRegistryName();
                Float temp = chunk.getValue() ==null? ModServersConfig.getFloatTemp(resourceLocation.toString()):chunk.getValue();
                if (chunk.getValue() ==ModServersConfig.getFloatTemp(resourceLocation.toString()))
                    continue;
                radiation.put(coord, temp);
            }
        }
    }


    @Override
    public float getTemp(World world, int x, int y, int z) {

        if(!world.isClientSide)
        {
            BlockPos pos = new BlockPos(x, y, z);
            Biome biome = world.getBiome(pos);
            ResourceLocation id = biome.getRegistryName();
            tempChunk radWorld = perWorld.get(world);
            if (radWorld == null) return 0;
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            Float rad = radWorld.temp.get(chunkPos);
            if (rad == null) {
                return ModServersConfig.getFloatTemp(id.toString());
            }
//            System.out.println("📊 [ПОЛУЧЕНИЕ] Чанк " + chunkPos + " → " + String.format("%.2f", rad) + " рад");
            return rad;
        }
        return 0;
    }

    @Override
    public void setTemp(World world, int x, int y, int z, float rad) {
        if(!world.isClientSide)
        {
            BlockPos pos = new BlockPos(x, 0, z);
            if (!world.isLoaded(pos)) return;

            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            tempChunk radWorld = perWorld.computeIfAbsent(world,
                    k -> new tempChunk());
                radWorld.temp.put(chunkPos,rad);
            world.getChunk(chunkPos.x, chunkPos.z).markUnsaved();
        }
    }

    @Override
    public void incrementTemp(World world, int x, int y, int z, float rad) {
        float current = getTemp(world, x, y, z);
        setTemp(world, x, y, z, current + rad);
    }

    @Override
    public void decrementTemp(World world, int x, int y, int z, float rad) {
        float current = getTemp(world, x, y, z);
        setTemp(world, x, y, z, current - rad);

    }

    @Override
    public void clearSystem(World world) {

    }


    @Override
    public void receiveWorldLoad(WorldEvent.Load event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        perWorld.put(world, new tempChunk());
        System.out.println("🌍 [ЗАГРУЗКА МИРА] " + world.dimension().location() +
                " (ID: " + world.dimension().location() + ")");
    }

    @Override
    public void receiveWorldUnload(WorldEvent.Unload event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        tempChunk radWorld = perWorld.get(world);
        int chunkCount = radWorld != null ? radWorld.temp.size() : 0;
        perWorld.remove(world);
        System.out.println("🌍 [ВЫГРУЗКА МИРА] " + world.dimension().location() +
                ", чанков с радиацией: " + chunkCount);
    }
    public void receiveWorldTick(TickEvent.ServerTickEvent event) {

    }

    public void receiveChunkLoad(ChunkDataEvent.Load event) {
        World world =(World) event.getWorld();
        if (world == null || world.isClientSide()) return;
        if (event.getChunk() == null) return;
        if(!world.isClientSide) {

            tempChunk radWorld = perWorld.computeIfAbsent(world,
                    k -> new tempChunk());

            float rad = event.getData().getFloat(NBT_KEY_CHUNK_RADIATION);
            if (rad > 0) {
                radWorld.temp.put(event.getChunk().getPos(), rad);
                //System.out.println("мир "+world+" позиция "+event.getChunk().getPos()+" радиация " +String.format("%.2f", rad));
            }
        }
    }
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        World world =(World) event.getWorld();
        if(!world.isClientSide) {
            tempChunk radWorld = perWorld.get(world);

            if(radWorld != null) {
                Float val = radWorld.temp.get(event.getChunk().getPos());
                float rad = val == null ? 0f : val;
                if (rad != 0f) {
                    event.getData().putFloat(NBT_KEY_CHUNK_RADIATION, rad); System.out.println("💾 [СОХРАНЕНИЕ ЧАНКА] " + event.getChunk().getPos() + " радиация " +
                            String.format("%.2f", rad));
                }

//
            }
        }
    }
    public void receiveChunkUnload(ChunkEvent.Unload event) {
        World world =(World) event.getWorld();
        if(!world.isClientSide) {
            tempChunk radWorld = perWorld.get(world);

            if(radWorld != null) {
                radWorld.temp.remove(event.getChunk().getPos());
            }
        }
    }
    public static class tempChunk{
        public Map<ChunkPos, Float> temp = new HashMap<>();
    }

//    @Override
//    public void playerTick(TickEvent.PlayerTickEvent event) {
//        World world =event.player.getCommandSenderWorld();
//        PlayerEntity player = event.player;
//        ticks +=1;
//        for (int i = 0; i < ticks; i++) {
//            if (!world.isClientSide) {
//                float temp = getTemp(world, (int) player.getX(), 0, (int) player.getZ());
//
//                String message = "🌡️ Температура в чанке: " + String.format("%.2f", temp);
//
//                player.sendMessage(new StringTextComponent(message), Util.NIL_UUID);
//                ticks=0;
//            }
//        }
//    }
}