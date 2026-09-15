package net.yteron.BWaC.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModTab {
    public static final ItemGroup BETTER_WINTER = new ItemGroup("Better Winter") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.SNOW_BLOCK);
        }

    };
}
