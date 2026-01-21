package me.johnny.copium.ui.clickgui;

import me.johnny.copium.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class ModuleButton {
	
	private final Module module;
	private int x, y;
	
	public ModuleButton(Module module, int x, int y) {
		this.module = module;
		this.x = x;
		this.y = y;
	}
	
	public void render(GuiGraphics graphics, int mousex, int mouseY, float delta) {
		Minecraft mc = Minecraft.getInstance();
		
		graphics.fill(x,  y,  x + 50,  y + 20, module.isToggled() ? 0xFF00FF00 : 0xFFFF0000);
		
		graphics.drawString(mc.font,  module.getName(), x + 5, y + 5, 0xFFFFFFFF);
			
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 10) {
			module.toggle();
			return true;
		}
		return false;
	}

}
