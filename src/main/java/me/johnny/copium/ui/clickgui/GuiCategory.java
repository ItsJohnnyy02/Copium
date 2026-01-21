package me.johnny.copium.ui.clickgui;

import java.util.List;
import me.johnny.copium.copium;
import me.johnny.copium.module.Category;
import me.johnny.copium.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class GuiCategory {
    private final Category category;
    public int x, y;
    public int width = 180;
    public int height = 24;
    private boolean expanded = false;
    private boolean dragging = false;
    private boolean resizing = false;
    private int dragOffsetX, dragOffsetY;
    private Module selectedModule;

    private static final int TITLE = 0xFF8A2BE2;
    private static final int TITLE_HOVER = 0xCC8A2BE2;
    private static final int BG = 0xD00F0F0F;
    private static final int BORDER = 0xFF222222;
    private static final int MODULE_BG = 0xFF161616;
    private static final int HOVER = 0x338A2BE2;
    private static final int TOGGLED = 0xFF8A2BE2;
    private static final int TEXT = 0xFFE0E0E0;

    public GuiCategory(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public void render(GuiGraphics g, int mx, int my, float delta) {
        if (dragging) {
            x = mx - dragOffsetX;
            y = my - dragOffsetY;
        }

        List<Module> mods = copium.instance.mm.getModulesByCategory(category);  // <-- move here

        // Title bar
        boolean th = mx >= x && mx <= x + width && my >= y && my <= y + height;
        g.fill(x, y, x + width, y + height, th ? TITLE_HOVER : TITLE);
        g.drawCenteredString(Minecraft.getInstance().font, category.name(), x + width / 2, y + 8, 0xFFFFFFFF);

        if (expanded) {
            int h = mods.size() * 20 + 8;
            g.fill(x, y + height, x + width, y + height + h, BG);
            g.renderOutline(x, y, width, height + h, BORDER);

            int cy = y + height + 4;
            for (Module m : mods) {
                boolean hvr = mx >= x && mx <= x + width && my >= cy && my <= cy + 20;
                g.fill(x + 2, cy, x + width - 2, cy + 20, hvr ? HOVER : MODULE_BG);

                int col = m.isToggled() ? TOGGLED : TEXT;
                g.drawString(Minecraft.getInstance().font, m.getName(), x + 8, cy + 6, col);

                if (hvr) {
                    String d = m.getDescription();
                    if (!d.isEmpty()) {
                        int dw = Minecraft.getInstance().font.width(d);
                        g.fill(mx + 12, my, mx + dw + 20, my + 20, BG);
                        g.drawString(Minecraft.getInstance().font, d, mx + 16, my + 6, TEXT);
                    }
                }

                if (m == selectedModule) {
                    m.renderOptions(g, x + width + 10, cy);
                }

                cy += 20;
            }

            // Resize corner (now mods is in scope)
            g.fill(x + width - 8, y + height + mods.size() * 20 + 4 - 8, x + width, y + height + mods.size() * 20 + 4, 0x44FFFFFF);
        }
    }

    public boolean mouseClicked(double mx, double my, int button) {
        if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
            if (button == 0) {
                dragging = true;
                dragOffsetX = (int)(mx - x);
                dragOffsetY = (int)(my - y);
            } else if (button == 1) {
                expanded = !expanded;
            }
            return true;
        }

        if (expanded) {
            int cy = y + height + 4;
            for (Module m : copium.instance.mm.getModulesByCategory(category)) {
                if (mx >= x && mx <= x + width && my >= cy && my <= cy + 20) {
                    if (button == 0) m.toggle();
                    if (button == 1) selectedModule = m;
                    return true;
                }
                cy += 20;
            }
        }

        return false;
    }

    public boolean mouseReleased(double mx, double my, int button) {
        dragging = false;
        resizing = false;
        return true;
    }

    public Category getCategory() { return category; }
    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean e) { expanded = e; }
}