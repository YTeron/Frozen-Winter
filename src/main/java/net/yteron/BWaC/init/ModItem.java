package net.yteron.BWaC.init;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.BWaC.BetterWinter;

public class ModItem {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BetterWinter.MOD_ID);
    static {

    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus); // <-- Исправлено: убраны лишние скобки
    }
}
