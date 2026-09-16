package net.yteron.BWaC.init;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.BWaC.BetterWinter;

public class ModBlock {
    public static final DeferredRegister<Block> BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BetterWinter.MOD_ID);
    static {

    }
    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }
}
