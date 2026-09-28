package net.yteron.BWaC.block.system;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClimbingSystem {
    private final BlockPos pos;
    private final PlayerEntity player;
    private boolean isActive;

    public ClimbingSystem(BlockPos pos, PlayerEntity player,boolean isActive) {
        this.pos = pos;
        this.player = player;
        this.isActive = isActive;
    }
    public void startClimbing() {
        if (player.getCommandSenderWorld().isClientSide) return;
        while (isActive) {
            teleportToRope();
        }
    }
    public void stopClimbing() {
        isActive =false;
    }
    private void teleportToRope() {
        if (!(player instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) player;
        sp.connection.teleport(
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                sp.yRot,
                sp.xRot,
                java.util.Collections.emptySet()
        );
    }
}
