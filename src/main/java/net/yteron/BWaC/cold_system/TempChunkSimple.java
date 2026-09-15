package net.yteron.BWaC.cold_system;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.Map;

public class TempChunkSimple extends TempChunkHandler {
    private Map<World,TemChunk> perWorld  =new HashMap<>();
    private static final String NBT_KEY_CHUNK_TEMP = "chunk_temp";
    @Override
    public void updateSystem() {
        for(Map.Entry<World, TemChunk> entry : perWorld.entrySet()) {

            Map<ChunkPos, Float> temp = entry.getValue().temp;
            Map<ChunkPos, Float> buff = new HashMap<>(temp);
            temp.clear();

            for (Map.Entry<ChunkPos, Float> chunk : buff.entrySet()) {
                if (chunk.getValue() == 0)
                    continue;
                ChunkPos coord = chunk.getKey();

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        float percent;
                        if (i == 0 && j == 0)      percent = 1.0f;
                        else if (i == 0 || j == 0) percent = 0.25f;
                        else                        percent = 0.125f;

                        ChunkPos newCoord = new ChunkPos(coord.x+i,coord.z+j);
                        if(buff.containsKey(newCoord)) {

                            Float val = temp.get(newCoord);
                            float temputer = val == null ? 0 : val;
                            float newTemp = temputer + chunk.getValue() * percent;
                            if(temputer<newTemp)
                                temp.put(newCoord, newTemp);
                        } else {
                            temp.put(newCoord, chunk.getValue() * percent);
                        }
                    }
                }
            }

        }
    }

    @Override
    public float getTemp(World world, int x, int y, int z) {
        if(!world.isClientSide)
        {
            TemChunk tempWorld = perWorld.get(world);
            if (tempWorld == null) return 0;
            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            Float temputer = tempWorld.temp.get(chunkPos);
            if (temputer == null) {
                return 0;
            }
//            System.out.println("📊 [ПОЛУЧЕНИЕ] Чанк " + chunkPos + " → " + String.format("%.2f", temputer) + " temp");
            return temputer;
        }
        return 0;
    }

    @Override
    public void setTemp(World world, int x, int y, int z, float temputer) {
        if(!world.isClientSide)
        {
            BlockPos pos = new BlockPos(x, 0, z);
            if (!world.isLoaded(pos)) return;

            ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
            TemChunk tempWorld = perWorld.computeIfAbsent(world,
                    k -> new TemChunk());
            if (temputer < 0.05f) {
                tempWorld.temp.remove(chunkPos);
            } else {
                tempWorld.temp.put(chunkPos,temputer);
            }
            world.getChunk(chunkPos.x, chunkPos.z).markUnsaved();
        }
    }

    @Override
    public void incrementTemp(World world, int x, int y, int z, float temputer) {

    }

    @Override
    public void decrementTemp(World world, int x, int y, int z, float temputer) {

    }

    @Override
    public void clearSystem(World world) {

    }
    public class TemChunk{
        public Map<ChunkPos, Float> temp = new HashMap<>();
    }
    @Override
    public void receiveWorldLoad(WorldEvent.Load event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        perWorld.put(world, new TemChunk());
        System.out.println("🌍 [ЗАГРУЗКА МИРА] " + world.dimension().location() +
                " (ID: " + world.dimension().location() + ")");
    }

    @Override
    public void receiveWorldUnload(WorldEvent.Unload event) {
        World world =(World) event.getWorld();
        if(world.isClientSide) return;
        TemChunk radWorld = perWorld.get(world);
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

            TemChunk radWorld = perWorld.computeIfAbsent(world,
                    k -> new TemChunk());

            float temputer = event.getData().getFloat(NBT_KEY_CHUNK_TEMP);
            if (temputer > 0) {
                radWorld.temp.put(event.getChunk().getPos(), temputer);
                //System.out.println("мир "+world+" позиция "+event.getChunk().getPos()+" радиация " +String.format("%.2f", temputer));
            }
        }
    }
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        World world =(World) event.getWorld();
        if(!world.isClientSide) {
            TemChunk radWorld = perWorld.get(world);

            if(radWorld != null) {
                Float val = radWorld.temp.get(event.getChunk().getPos());
                float temputer = val == null ? 0F : val;
                if (temputer >-0.5F) {
                    event.getData().putFloat(NBT_KEY_CHUNK_TEMP, temputer);
//                    System.out.println("💾 [СОХРАНЕНИЕ ЧАНКА] " + event.getChunk().getPos() + " радиация " +
//                            String.format("%.2f", temputer));
                }
            }
        }
    }
}
