package net.ohacd.poh.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;

public class ClayFurnaceScreen extends HandledScreen<ClayFurnaceScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.of(ProgressionOverhaul.MOD_ID, "textures/gui/clay_furnace/clay_furnace_gui.png");
    private static final Identifier ARROW_TEXTURE =
            Identifier.of(ProgressionOverhaul.MOD_ID, "textures/gui/arrow_progress.png");
//    private static final Identifier FLAME_TEXTURE =
//            Identifier.of(ProgressionOverhaul.MOD_ID, "textures/gui/flame_progress.png");

    public ClayFurnaceScreen(ClayFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderLayer::getGuiTextured, GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);

//        renderFlame(context, x, y);
        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            int progress = handler.getScaledArrowProgress(); // typically scales 0–24
            context.drawTexture(RenderLayer::getGuiTextured,
                    ARROW_TEXTURE,
                    x + 79, y + 34,    // draw position on screen (matches vanilla layout)
                    176, 14,           // source position inside GUI_TEXTURE (vanilla arrow coords)
                    progress, 16,      // width scales, height fixed
                    256, 256           // full texture size
            );
        }
    }

//    private void renderFlame(DrawContext context, int x, int y) {
//        int flameHeight = handler.getScaledFuelProgress(); // Scaled from 0–14
//        if (flameHeight > 0) {
//            context.drawTexture(RenderLayer::getGuiTextured,
//                    GUI_TEXTURE,
//                    x + 56, y + 36 + (14 - flameHeight), // screen position
//                    176, 14 - flameHeight,               // source x/y in GUI texture
//                    14, flameHeight,                     // width/height to draw
//                    256, 256);                           // full texture size
//        }
//    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
