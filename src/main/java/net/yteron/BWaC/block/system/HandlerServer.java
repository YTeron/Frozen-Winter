package net.yteron.BWaC.block.system;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HandlerServer {
    private final BlockPos pos;
    HandlerServer(BlockPos pos){
        this.pos =pos;
    }
    public HandlerServer(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }
    public void encode(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                net.minecraft.entity.player.ServerPlayerEntity player = context.getSender();
                if (player != null) {
                    System.out.println("Сервер получил pos: " + pos);
                }
            });
        } else {
            context.enqueueWork(() -> {
                net.minecraft.client.entity.player.ClientPlayerEntity player =
                        net.minecraft.client.Minecraft.getInstance().player;
                if (player != null) {
                    player.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                }
            });
        }

        context.setPacketHandled(true);
    }
}
