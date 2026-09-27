package net.yteron.BWaC.block.system;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ClimbingSystem {
    private BlockPos pos;
    private PlayerEntity player;
    public ClimbingSystem(BlockPos pos, PlayerEntity player){
        this.pos =pos;
        this.player = player;
    }

    public void forServer(){

    }
    public void forClient(){

    }

    public void setPos() {
        player.setPos(pos.getX(),pos.getY(),pos.getZ());

    }
}
