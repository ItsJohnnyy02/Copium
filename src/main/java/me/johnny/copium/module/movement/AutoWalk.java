package me.johnny.copium.module.movement;

import me.johnny.copium.module.Module;
import me.johnny.copium.module.Category;
import me.johnny.copium.ui.clickgui.options.CheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class AutoWalk extends Module {

    private CheckBox autoJump;
    private CheckBox sprint;
    private CheckBox antiStuck;
    private CheckBox pauseInGui;

    private int stuckTimer = 0;
    private double lastPosX = 0;
    private double lastPosZ = 0;

    public AutoWalk() {
        super("AutoWalk", "Automatically walks forward with smart features", Category.MOVEMENT, 0);
    }

    @Override
    public void setupOptions() {
        autoJump   = new CheckBox("Auto Jump", true, 0, 0);
        sprint     = new CheckBox("Sprint", true, 0, 0);
        antiStuck  = new CheckBox("Anti Stuck", true, 0, 0);
        pauseInGui = new CheckBox("Pause in GUI", true, 0, 0);

        addOption(autoJump);
        addOption(sprint);
        addOption(antiStuck);
        addOption(pauseInGui);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Safety guard if checkboxes failed to initialize
        if (pauseInGui == null) return;

        if (pauseInGui.isChecked() && mc.screen != null) {
            if (mc.screen instanceof AbstractContainerScreen || mc.screen instanceof ChatScreen) {
                releaseKeys();
                return;
            }
        }

        mc.options.keyUp.setDown(true);

        if (sprint.isChecked() && !mc.player.isSprinting() && mc.player.getFoodData().getFoodLevel() > 6) {
            mc.player.setSprinting(true);
        }

        if (autoJump.isChecked() && !mc.player.isInWater() && !mc.player.isFallFlying()) {
            if (mc.player.onGround() && mc.player.getDeltaMovement().y <= 0.0) {
                mc.player.jumpFromGround();
            }
        }

        if (antiStuck.isChecked()) {
            double dx = mc.player.getX() - lastPosX;
            double dz = mc.player.getZ() - lastPosZ;

            if (Math.abs(dx) + Math.abs(dz) < 0.03) {
                stuckTimer++;
            } else {
                stuckTimer = 0;
            }

            lastPosX = mc.player.getX();
            lastPosZ = mc.player.getZ();

            if (stuckTimer > 20) {
                boolean goLeft = Math.random() < 0.5;
                mc.options.keyLeft.setDown(goLeft);
                mc.options.keyRight.setDown(!goLeft);

                if (mc.player.onGround()) {
                    mc.player.jumpFromGround();
                }

                stuckTimer = 0;
            } else {
                mc.options.keyLeft.setDown(false);
                mc.options.keyRight.setDown(false);
            }
        }
    }

    @Override
    public void onDisabled() {
        releaseKeys();
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

    private void releaseKeys() {
        if (mc.player != null) {
            mc.options.keyUp.setDown(false);
            mc.options.keyLeft.setDown(false);
            mc.options.keyRight.setDown(false);
            mc.player.setSprinting(false);
        }
    }
}
