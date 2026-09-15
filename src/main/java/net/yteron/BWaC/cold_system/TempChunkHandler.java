package net.yteron.BWaC.cold_system;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public abstract class TempChunkHandler {
    public abstract void updateSystem();
    public abstract float getTemp(World world, int x, int y, int z);
    public abstract void setTemp(World world, int x, int y, int z, float rad);
    public abstract void incrementTemp(World world, int x, int y, int z, float rad);
    public abstract void decrementTemp(World world, int x, int y, int z, float rad);
    public abstract void clearSystem(World world);
//    public abstract void handleWorldDestruction();
    /*
     * Proxy'd event handlers
     */
    public void receiveWorldLoad(WorldEvent.Load event) { }
    public void receiveWorldUnload(WorldEvent.Unload event) { }
    public void receiveWorldTick(TickEvent.ServerTickEvent event) { }

    public void receiveChunkLoad(ChunkDataEvent.Load event) { }
    public void receiveChunkSave(ChunkDataEvent.Save event) { }
    public void receiveChunkUnload(ChunkEvent.Unload event) { }
}
