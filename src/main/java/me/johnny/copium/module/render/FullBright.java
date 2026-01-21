package me.johnny.copium.module.render;

import me.johnny.copium.module.Category;
import me.johnny.copium.module.Module;
import net.minecraft.client.Options;

public class FullBright extends Module {

    private Double oldGamma = (double) -1.0f; // -1 means not saved yet

    public FullBright() {
        super("Fullbright", "Lights Up The Caves", Category.RENDER, 0);
    }

    @Override
    public void onEnabled() {
        if (mc.options == null || mc.options.gamma() == null) {
            return;
        }

        // Save current gamma only if we haven't already
        if (oldGamma == -1.0f) {
            oldGamma = mc.options.gamma().get();
        }

        // Set to maximum brightness (1.0 is full bright in modern versions)
        mc.options.gamma().set(16.0);  // Many clients use 16.0+ for true fullbright
    }

    @Override
    public void onUpdate() {
        if (this.isToggled() && mc.player != null) {
            mc.player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.NIGHT_VISION, 999999, 0, false, false, false
            ));
        }
    }

    @Override
    public void onDisabled() {
        if (mc.player != null) {
            mc.player.removeEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION);
        }
        // restore gamma too if you kept it
    }

    @Override
    public void setupOptions() {
        // Add settings later if you want (e.g. slider for gamma strength)
        // For now it's empty
    }
}