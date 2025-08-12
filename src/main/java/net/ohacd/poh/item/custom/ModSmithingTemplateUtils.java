package net.ohacd.poh.item.custom;

import net.minecraft.util.Identifier;
import java.util.List;

public class ModSmithingTemplateUtils {
    private static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE = Identifier.ofVanilla("item/empty_armor_slot_chestplate");
    private static final Identifier EMPTY_SLOT_INGOT = Identifier.ofVanilla("item/empty_slot_ingot");

    public static List<Identifier> getIronUpgradeEmptyBaseSlotTextures() {
        return List.of(EMPTY_ARMOR_SLOT_CHESTPLATE);
    }

    public static List<Identifier> getIronUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT);
    }
}
