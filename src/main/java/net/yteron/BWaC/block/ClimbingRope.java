package net.yteron.BWaC.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ToolType;
import net.yteron.BWaC.block.system.ClimbingEntity;
import net.yteron.BWaC.block.system.ClimbingSystem;
import net.yteron.BWaC.cold_system.TempChunkSimple;
import net.yteron.BWaC.cold_system.TempManager;
//import net.yteron.nucrad.radiation.ChunkRaditonManager;

public class ClimbingRope extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(0)
            .strength(5.0f, 10.0f)
            .harvestTool(ToolType.AXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.WOOL);
    public ClimbingRope() {
        super(PROPERTIES);
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            TempManager.proxy.incrementTemp(world, pos.getX(), pos.getY(), pos.getZ(), -1.0F);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && newState.getBlock() != this) {
            TempManager.proxy.decrementTemp(world, pos.getX(), pos.getY(), pos.getZ(), -1.0F);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (hand != Hand.MAIN_HAND) return ActionResultType.PASS;
        if (player.isShiftKeyDown()) return ActionResultType.PASS;
        if (!world.isClientSide) {

            BlockPos sitPos = pos.above();
            ClimbingEntity entity = new ClimbingEntity(world, sitPos);
            world.addFreshEntity(entity);

            player.startRiding(entity);
        }
        player.setForcedPose(Pose.SWIMMING);
        return ActionResultType.SUCCESS;
    }

}
