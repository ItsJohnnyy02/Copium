package me.johnny.copium.module.movement;

import org.lwjgl.glfw.GLFW;

import me.johnny.copium.module.Category;
import me.johnny.copium.module.Module;

public class Flight extends Module{

	public Flight() {
		super("Flight", "Enables Creative Fly In Survival", Category.MOVEMENT, GLFW.GLFW_KEY_F);
	}
	
	@Override
	public void onEnabled() {
		if(mc.player == null || mc.player.getAbilities() == null) return;
		mc.player.getAbilities().mayfly = true;
		mc.player.getAbilities().flying = true;
		mc.player.getAbilities().setFlyingSpeed(0.1f);
	}
	
	@Override
	public void onDisabled() {
		if(mc.player != null) {
			mc.player.getAbilities().mayfly = false;
			mc.player.getAbilities().flying = false;
			
		}
		super.onDisabled();
	}
	
	@Override
	public void onUpdate() {
		if(this.isToggled()) {
			mc.player.getAbilities().mayfly = true;
		}
	}

	@Override
	public void setupOptions() {
		
	}
	

}
