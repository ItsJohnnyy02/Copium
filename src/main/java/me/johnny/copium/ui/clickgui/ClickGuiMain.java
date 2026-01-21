package me.johnny.copium.ui.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.glfw.GLFW;
import me.johnny.copium.module.Category;
import me.johnny.copium.module.gui.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClickGuiMain extends Screen {
    private final ClickGui clickGui;
    private final Minecraft mc = Minecraft.getInstance();
    private List<GuiCategory> categories = new ArrayList<>();

    public static Map<Category, int[]> positions = new HashMap<>();
    public static Map<Category, Boolean> expanded = new HashMap<>();

    public ClickGuiMain(ClickGui clickGui) {
        super(Component.literal("ClickGui"));
        this.clickGui = clickGui;

        int x = 40;
        int y = 60;
        for (Category c : Category.values()) {
            if (c == Category.GUI) continue;
            int[] p = positions.getOrDefault(c, new int[]{x, y});
            GuiCategory cat = new GuiCategory(c, p[0], p[1]);
            cat.setExpanded(expanded.getOrDefault(c, false));
            categories.add(cat);
            x += 200;
            if (x > 800) {
                x = 40;
                y += 300;
            }
        }
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float d) {
        if (mc == null) return;

        g.fill(0, 0, width, height, 0xAA000000);

        for (GuiCategory c : categories) {
            c.render(g, mx, my, d);
        }

        super.render(g, mx, my, d);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int b) {
        for (GuiCategory c : categories) {
            if (c.mouseClicked(mx, my, b)) return true;
        }
        return super.mouseClicked(mx, my, b);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int b) {
        for (GuiCategory c : categories) {
            c.mouseReleased(mx, my, b);
            positions.put(c.getCategory(), new int[]{c.x, c.y});
            expanded.put(c.getCategory(), c.isExpanded());
        }
        return super.mouseReleased(mx, my, b);
    }

    @Override
    public boolean keyPressed(int k, int s, int m) {
        if (k == GLFW.GLFW_KEY_ESCAPE) {
            clickGui.closeGui();
            return true;
        }
        return super.keyPressed(k, s, m);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}