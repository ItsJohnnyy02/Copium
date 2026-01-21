package me.johnny.copium.ui.clickgui.options;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CheckBox {
    private String label;
    private boolean checked;
    private int x, y;

    public static final int ACCENT = 0xFF8A2BE2;
    public static final int ACCENT_HOVER = 0xCC8A2BE2;
    public static final int BACKGROUND = 0xCC1A1A1A;
    private static final int BORDER = 0xFF111111;
    public static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BOX_BG = 0xFF2A2A2A;

    public CheckBox(String label, boolean initialState, int x, int y) {
        this.label = label;
        this.checked = initialState;
        this.x = x;
        this.y = y;
    }

    public void Render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        int boxSize = 12;
        int padding = 4;
        int textOffset = boxSize + 8;
        int labelWidth = mc.font.width(label);
        int totalWidth = boxSize + textOffset + labelWidth;

        graphics.fill(x - padding, y - padding, x + totalWidth + padding, y + boxSize + padding, BACKGROUND);
        graphics.renderOutline(x - padding - 1, y - padding - 1, totalWidth + padding * 2 + 2, boxSize + padding * 2 + 2, BORDER);

        if (checked) {
            graphics.fillGradient(x - 2, y - 2, x + boxSize + 2, y + boxSize + 2, ACCENT_HOVER, 0x00000000);
        }

        graphics.fill(x, y, x + boxSize, y + boxSize, BOX_BG);
        graphics.renderOutline(x, y, boxSize, boxSize, checked ? ACCENT : 0xFF555555);

        if (checked) {
            graphics.fill(x + 3, y + 5, x + boxSize - 3, y + boxSize - 3, ACCENT);
            graphics.fill(x + 3, y + 6, x + 5, y + boxSize - 2, ACCENT);
            graphics.fill(x + 4, y + 5, x + boxSize - 4, y + 7, ACCENT);
        }

        graphics.drawString(mc.font, label, x + textOffset, y + (boxSize - 8) / 2, TEXT_COLOR);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxSize = 12;
        if (mouseX >= x && mouseX <= x + boxSize && mouseY >= y && mouseY <= y + boxSize && button == 0) {
            this.checked = !this.checked;
            return true;
        }
        return false;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
