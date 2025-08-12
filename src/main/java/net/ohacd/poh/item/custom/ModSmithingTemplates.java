package net.ohacd.poh.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;

public class ModSmithingTemplates {

    public static SmithingTemplateItem ofIronUpgrade(Item.Settings settings) {
        return new SmithingTemplateItem(
                Text.translatable("item.poh.iron_upgrade_smithing_template.applies_to"),
                Text.translatable("item.poh.iron_upgrade_smithing_template.ingredients"),
                Text.translatable("item.poh.iron_upgrade_smithing_template.base_slot_description"),
                Text.translatable("item.poh.iron_upgrade_smithing_template.additions_slot_description"),
                ModSmithingTemplateUtils.getIronUpgradeEmptyBaseSlotTextures(),
                ModSmithingTemplateUtils.getIronUpgradeEmptyAdditionsSlotTextures(),
                settings
        );
    }
}
