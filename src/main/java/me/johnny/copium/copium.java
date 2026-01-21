package me.johnny.copium;

import me.johnny.copium.module.KeyManager;
import me.johnny.copium.module.ModuleManager;
import me.johnny.copium.ui.clickgui.InGameOverlay;
import net.minecraft.client.gui.GuiGraphics;

public class copium {
	
	public static String name = "Copium", creator = "Johnny";
	
	public static copium instance = new copium();
	
	//copium Managers
	public static ModuleManager mm;
	public static KeyManager km;
	public static InGameOverlay ingameoverlay;

	
	
	public static void startClient() {
		mm = new ModuleManager();
		km = new KeyManager();
		ingameoverlay = new InGameOverlay();
	}
		
		
	//initiates code every Tick
	public static void onTick() {
		mm.onTick(km);			
	}
	
	public void onRender(GuiGraphics guiGraphics) {
		ingameoverlay.render(guiGraphics);
				
	}

}
