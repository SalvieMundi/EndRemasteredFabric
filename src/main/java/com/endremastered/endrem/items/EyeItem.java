package com.endremastered.endrem.items;

import com.endremastered.endrem.EndRemastered;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;

public class EyeItem extends Item {
    public EyeItem() {
        super(new FabricItemSettings().fireproof().maxCount(16).rarity(Rarity.EPIC).group(EndRemastered.ENDREM_TAB));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText(String.format("item.endrem.%s.description", this.asItem().toString())));
    }
}
