package me.johnny.copium.module.movement;

import me.johnny.copium.module.Module;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import me.johnny.copium.module.Category;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Negates all Fall Damage", Category.MOVEMENT, 0);
    }

    @Override
    public void onUpdate() {
        if (!this.isToggled() || mc.player == null) {
            return;
        }

        if (mc.player.getAbilities().instabuild ||
            mc.player.getAbilities().flying ||
            mc.player.isFallFlying() ||
            mc.player.isPassenger()) {
            return;
        }

        // Send when falling downward (catches earlier than fallDistance alone)
        if (mc.player.getDeltaMovement().y < -0.5) {
            mc.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(
                true,
                mc.player.horizontalCollision
            ));

            mc.player.fallDistance = 0.0f;
        }
    }

    @Override
    public void setupOptions() {
    }
}