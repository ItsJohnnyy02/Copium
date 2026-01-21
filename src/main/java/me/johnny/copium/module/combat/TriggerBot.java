package me.johnny.copium.module.combat;

import me.johnny.copium.module.Module;
import me.johnny.copium.module.Category;
import me.johnny.copium.ui.clickgui.options.CheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Random;

public class TriggerBot extends Module {

    private CheckBox onlyPlayers;
    private CheckBox checkCooldown;
    private CheckBox randomCPS;
    private CheckBox throughWalls;

    private long lastAttackTime = 0;
    private final Random random = new Random();

    public TriggerBot() {
        super("TriggerBot", "Automatically attacks entities in crosshair", Category.COMBAT, 0);
    }

    @Override
    public void setupOptions() {
        onlyPlayers    = new CheckBox("Only Players", true, 0, 0);
        checkCooldown  = new CheckBox("Attack Cooldown", true, 0, 0);
        randomCPS      = new CheckBox("Random CPS", true, 0, 0);
        throughWalls   = new CheckBox("Through Walls", false, 0, 0);

        addOption(onlyPlayers);
        addOption(checkCooldown);
        addOption(randomCPS);
        addOption(throughWalls);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Get what the crosshair is looking at
        HitResult hit = mc.hitResult;

        if (hit == null || hit.getType() != HitResult.Type.ENTITY) {
            return;
        }

        Entity target = ((EntityHitResult) hit).getEntity();

        // Skip if not living entity or dead
        if (!(target instanceof LivingEntity) || !target.isAlive()) {
            return;
        }

        // Only players option
        if (onlyPlayers.isChecked() && !(target instanceof Player)) {
            return;
        }

        // Through walls check (simple raytrace bypass or ignore)
        if (!throughWalls.isChecked()) {
            // If walls are blocking, skip (mc.hitResult already handles this in vanilla)
            // You can add extra raytrace check if needed
        }

        // Cooldown check (respect attack cooldown)
        if (checkCooldown.isChecked() && mc.player.getAttackStrengthScale(0.0f) < 0.9f) {
            return;
        }

        // CPS control
        long currentTime = System.currentTimeMillis();
        int cps = randomCPS.isChecked() ? 8 + random.nextInt(5) : 10; // 8-12 CPS random, or fixed 10
        long delay = 1000 / cps;

        if (currentTime - lastAttackTime < delay) {
            return;
        }

        // Attack
        mc.player.attack(target);
        mc.player.swing(InteractionHand.MAIN_HAND);
        lastAttackTime = currentTime;
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
}

