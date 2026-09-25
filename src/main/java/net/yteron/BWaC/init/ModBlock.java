package net.yteron.BWaC.init;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.BWaC.BetterWinter;
import net.yteron.BWaC.block.ClimbingRope;

public class ModBlock {
    public static final DeferredRegister<Block> REGISTRY;
    public static final RegistryObject<Block> CLIMBING_ROPE;
    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterWinter.MOD_ID);
        CLIMBING_ROPE = REGISTRY.register("climbing_rope",()->new ClimbingRope());
    }
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
