package net.ohacd.poh.material;

import net.minecraft.item.ToolMaterial;
import net.ohacd.poh.util.ModTags;

public class ModToolMaterial {
    public static final ToolMaterial SHARP_TOOL_MATERIAL = new ToolMaterial(
            ModTags.Blocks.NEEDS_SHARP_TOOL, 10, 0.35F, 1.5F, 1, ModTags.Items.SHARP_TOOL_REPAIR);
}
