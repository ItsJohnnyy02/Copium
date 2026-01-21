package me.johnny.copium.module.movement;

import me.johnny.copium.module.Module;
import me.johnny.copium.module.Category;
import me.johnny.copium.ui.clickgui.options.CheckBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.tags.FluidTags;

public class Jesus extends Module {

    private CheckBox modeSolid;
    private CheckBox modePacket;
    private CheckBox modeMatrix;
    private CheckBox noFallDamage;

    public Jesus() {
        super("Jesus", "Walk on water/lava like Jesus", Category.MOVEMENT, 0);
    }

    @Override
    public void setupOptions() {
        modeSolid      = new CheckBox("Solid Mode", false, 0, 0);
        modePacket     = new CheckBox("Packet Mode", true, 0, 0);
        modeMatrix     = new CheckBox("Matrix Bypass", false, 0, 0);
        noFallDamage   = new CheckBox("No Fall Damage", true, 0, 0);

        addOption(modeSolid);
        addOption(modePacket);
        addOption(modeMatrix);
        addOption(noFallDamage);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        boolean inLiquid = mc.player.isInWater() || mc.player.isInLava();

        if (!inLiquid) return;

        // No fall damage in liquids
        if (noFallDamage.isChecked()) {
            mc.player.fallDistance = 0.0f;
        }

        // Solid mode - prevent sinking + stand on surface
        if (modeSolid.isChecked()) {
            if (mc.player.isInWater() || mc.player.isInLava()) {
                if (mc.player.getDeltaMovement().y < 0.0) {
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.1, mc.player.getDeltaMovement().z);
                }
            }
        }

        // Packet mode - spoof on-ground / stop falling
        if (modePacket.isChecked()) {
            if (mc.player.isInWater() || mc.player.isInLava()) {
                // Send START_RIDING_JUMP to spoof "on ground" in liquid (common bypass)
                mc.player.connection.send(new ServerboundPlayerCommandPacket(
                    mc.player,
                    ServerboundPlayerCommandPacket.Action.START_RIDING_JUMP,
                    100
                ));
            }
        }

        // Matrix-style - simple on-ground spoof + velocity hold
        if (modeMatrix.isChecked()) {
            if (mc.player.isInWater() || mc.player.isInLava()) {
                mc.player.setOnGround(true);

                if (mc.player.getDeltaMovement().y < -0.04) {
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.04, mc.player.getDeltaMovement().z);
                }
            }
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
            mc.player.setOnGround(false);
        }
    }
}
