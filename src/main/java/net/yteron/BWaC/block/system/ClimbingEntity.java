package net.yteron.BWaC.block.system;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.yteron.BWaC.init.ModClimbing;

public class ClimbingEntity extends Entity {


    public ClimbingEntity(EntityType<?> p_i48580_1_, World world) {
        super(p_i48580_1_, world);
    }
    public ClimbingEntity(World world, BlockPos pos){
        super(ModClimbing.SIT_ENTITY_TYPE, world);
        setPos(pos.getX() -0.3D , pos.getY() - 0.25D, pos.getZ() + 0.5D);
        noCulling = true;
        noPhysics =true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }
    public Pose getPose() {
        if (this.isPassenger() && this.getVehicle() instanceof LivingEntity) {
            return Pose.SWIMMING;
        }
        return super.getPose();
    }
    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        player.setForcedPose(Pose.SWIMMING);
        if (!this.getCommandSenderWorld().isClientSide) {
            player.startRiding(this);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void addPassenger(Entity p_184200_1_) {
        super.addPassenger(p_184200_1_);
        if (p_184200_1_ instanceof PlayerEntity) {
            ((PlayerEntity) p_184200_1_).setForcedPose(Pose.SWIMMING);
        }
    }
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    @Override
    public void remove()
    {
        super.remove();
    }

    @Override
    protected void removePassenger(Entity p_184225_1_) {
        super.removePassenger(p_184225_1_);
        if (p_184225_1_ instanceof PlayerEntity) {
            ((PlayerEntity) p_184225_1_).setForcedPose(null);
        }
        if (!this.level.isClientSide) {
            this.remove();
        }
    }
}
