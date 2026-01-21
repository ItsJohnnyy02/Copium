package me.johnny.copium.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;

import me.johnny.copium.copium;
import me.johnny.copium.util.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class InGameOverlay {

    private final Minecraft mc = Minecraft.getInstance();

    private static final int PURPLE = 0xFF8A2BE2;
    private static final int TEXT = 0xFFFFFFFF;

    public InGameOverlay() {}

    public void render(GuiGraphics guiGraphics) {
        if (mc.player == null) return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        renderTopLeft(guiGraphics);
        renderTopRight(guiGraphics);
        renderBottomLeft(guiGraphics);
        renderArrayList(guiGraphics);

        RenderSystem.disableBlend();
    }

    private void renderTopLeft(GuiGraphics guiGraphics) {
        String nameVersion = "Copium v1.1";
        String fpsText = "FPS: " + mc.getFps();

        int fontHeight = mc.font.lineHeight;
        int padding = 4;
        int boxHeight = fontHeight + padding * 2;
        int boxWidth = mc.font.width(nameVersion) + padding * 2;

        int x = 4;
        int y = 4;

        guiGraphics.fill(x, y, x + boxWidth, y + boxHeight, 0xAA111111);
        guiGraphics.renderOutline(x, y, boxWidth, boxHeight, PURPLE);

        guiGraphics.drawString(mc.font, nameVersion, x + padding, y + padding, PURPLE);
        guiGraphics.drawString(mc.font, fpsText, x + padding, y + boxHeight + 4, TEXT);
    }

    private void renderTopRight(GuiGraphics guiGraphics) {
        float yaw = Mth.wrapDegrees(mc.player.getYRot());
        int index = Math.round(yaw / 45f) & 7;

        String[] directions = {"South", "South-West", "West", "North-West", "North", "North-East", "East", "South-East"};
        String dir = directions[index];

        String xAxis = (yaw >= -90 && yaw <= 90) ? "+X" : "-X";
        String zAxis = (yaw >= 0 && yaw <= 180) ? "+Z" : "-Z";

        int x = mc.getWindow().getGuiScaledWidth() - mc.font.width(dir + " " + xAxis + " / " + zAxis) - 8;
        int y = 4;

        guiGraphics.drawString(mc.font, dir + " ", x, y, TEXT);

        int dirWidth = mc.font.width(dir + " ");
        guiGraphics.drawString(mc.font, xAxis + " / " + zAxis, x + dirWidth, y, PURPLE);
    }

    private void renderBottomLeft(GuiGraphics guiGraphics) {
        double px = mc.player.getX();
        double py = mc.player.getY();
        double pz = mc.player.getZ();

        String xOW = String.format("%.2f", px);
        String yOW = String.format("%.2f", py);
        String zOW = String.format("%.2f", pz);

        String xNether = String.format("%.2f", px / 8.0);
        String zNether = String.format("%.2f", pz / 8.0);

        String coordsText = "x: " + xOW + " (" + xNether + "), y: " + yOW + ", z: " + zOW + " (" + zNether + ")";

        int x = 4;
        int y = mc.getWindow().getGuiScaledHeight() - mc.font.lineHeight - 8;

        int labelXWidth = mc.font.width("x: ");
        int labelYWidth = mc.font.width("y: ");
        int labelZWidth = mc.font.width("z: ");

        // Split drawing for purple labels + white coords
        guiGraphics.drawString(mc.font, "x: ", x, y, PURPLE);
        guiGraphics.drawString(mc.font, xOW + " (" + xNether + "), ", x + labelXWidth, y, TEXT);

        int commaX = x + labelXWidth + mc.font.width(xOW + " (" + xNether + ")") + 2;
        guiGraphics.drawString(mc.font, "y: ", commaX, y, PURPLE);
        guiGraphics.drawString(mc.font, yOW + ", ", commaX + mc.font.width("y: "), y, TEXT);

        int zX = commaX + mc.font.width("y: ") + mc.font.width(yOW + ", ");
        guiGraphics.drawString(mc.font, "z: ", zX, y, PURPLE);
        guiGraphics.drawString(mc.font, zOW + " (" + zNether + ")", zX + mc.font.width("z: "), y, TEXT);
    }

    private void renderArrayList(GuiGraphics guiGraphics) {
        var mods = copium.mm.getEanbledModules();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int y = 50;

        int spacing = mc.font.lineHeight + 1;

        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            String name = mod.getName();

            if (name.equalsIgnoreCase("ClickGui")) continue;

            int width = mc.font.width(name);
            int x = screenWidth - width - 6;

            int rainbow = RainbowUtil.getRainbowColor(i * 0.15f);

            guiGraphics.drawString(mc.font, name, x, y + i * spacing, rainbow);
        }
    }
}
