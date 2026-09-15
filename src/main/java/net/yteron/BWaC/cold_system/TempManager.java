package net.yteron.BWaC.cold_system;

import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TempManager {
    public static TempChunkHandler chunk = new TempChunkSimple();
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        chunk.receiveWorldLoad(event);
    }
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        chunk.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        chunk.receiveChunkLoad(event);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        chunk.receiveChunkSave(event);
    }
    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        chunk.receiveChunkUnload(event);
    }
}
