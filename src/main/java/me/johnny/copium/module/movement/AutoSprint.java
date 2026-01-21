package me.johnny.copium.module.movement;

import me.johnny.copium.module.Module;
import me.johnny.copium.module.Category;
import me.johnny.copium.ui.clickgui.options.CheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class AutoSprint extends Module {

    private CheckBox onlyWhenMoving;
    private CheckBox pauseInGui;
    private CheckBox hungerSafe;

    public AutoSprint() {
        super("AutoSprint", "Automatically sprints when moving", Category.MOVEMENT, 0);
    }

    @Override
    public void setupOptions() {
        onlyWhenMoving = new CheckBox("Only When Moving", true, 0, 0);
        pauseInGui     = new CheckBox("Pause in GUI", true, 0, 0);
        hungerSafe     = new CheckBox("Hunger Safe (6+ food)", true, 0, 0);

        addOption(onlyWhenMoving);
        addOption(pauseInGui);
        addOption(hungerSafe);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Pause when in GUI/inventory/chat
        if (pauseInGui.isChecked() && mc.screen != null) {
            if (mc.screen instanceof AbstractContainerScreen || mc.screen instanceof ChatScreen) {
                mc.player.setSprinting(false);
                return;
            }
        }

        // Check conditions for sprinting
        boolean shouldSprint = true;

        // Only sprint when actually moving forward/left/right
        if (onlyWhenMoving.isChecked()) {
            boolean moving = mc.options.keyUp.isDown() ||
                             mc.options.keyLeft.isDown() ||
                             mc.options.keyRight.isDown() ||
                             mc.options.keyDown.isDown();
            if (!moving) {
                shouldSprint = false;
            }
        }

        // Hunger safe mode - don't sprint if food < 6 (vanilla sprint requirement)
        if (hungerSafe.isChecked() && mc.player.getFoodData().getFoodLevel() <= 6) {
            shouldSprint = false;
        }

        // Apply sprint
        if (shouldSprint && !mc.player.isSprinting()) {
            mc.player.setSprinting(true);
        }

        // Release if conditions no longer met
        if (!shouldSprint && mc.player.isSprinting()) {
            mc.player.setSprinting(false);
        }
    }
    
    @Override
    public void renderOptions(GuiGraphics graphics, int x, int y) {
        int yOffset = y;
        for (CheckBox option : getOptions()) {
            option.setPosition(x + 10, yOffset);
            option.Render(graphics);
            yOffset += 20;
        }
    }

    @Override
    public void onDisabled() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
    }
}
