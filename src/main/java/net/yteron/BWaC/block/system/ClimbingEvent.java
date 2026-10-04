package net.yteron.BWaC.block.system;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jline.utils.Display;


@Cancelable
public class ClimbingEvent extends EntityEvent {
    private final BlockPos climbPos;

    public ClimbingEvent(Entity entity, BlockPos pos) {
        super(entity);
        this.climbPos = pos;
    }

    public BlockPos getClimbPos() { return climbPos; }
    public PlayerEntity getPlayer() { return (PlayerEntity) getEntity(); }
    public static void spawnEntity(ServerPlayerEntity player) {
        //Display
    }
}
