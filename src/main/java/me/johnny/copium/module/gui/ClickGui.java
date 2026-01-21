package me.johnny.copium.module.gui;

import me.johnny.copium.module.Category;
import org.lwjgl.glfw.GLFW;
import me.johnny.copium.module.Module;
import me.johnny.copium.ui.clickgui.ClickGuiMain;

public class ClickGui extends Module {
    private boolean isGuiOpen = false;

    public ClickGui() {
        super("ClickGui", "Displays ClickGui", Category.GUI, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onUpdate() {
        if (this.isToggled() && !isGuiOpen && mc.screen == null) {
            openGui();
        }
    }

    private void openGui() {
        if (!isGuiOpen && mc.screen == null) {
            mc.execute(() -> {
                mc.setScreen(new ClickGuiMain(this));
            });
            isGuiOpen = true;
            super.onEnabled();
        }
    }

    public void closeGui() {
        if (isGuiOpen) {
            mc.setScreen(null);
            isGuiOpen = false;
            this.setToggled(false);
            super.onDisabled();
        }
    }

    @Override
    public void onEnabled() {
        if (mc.screen == null && !isGuiOpen) {
            openGui();
        }
    }

    @Override
    public void onDisabled() {
        if (isGuiOpen) {
            closeGui();
        }
    }

    @Override
    public void setupOptions() {
    }
}
