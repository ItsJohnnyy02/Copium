package me.johnny.copium.module;

import java.util.ArrayList;
import java.util.List;

import me.johnny.copium.ui.clickgui.options.CheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class Module {
	public String name;
	public String description;
	public Category category;
	public boolean toggled;
	public int keycode;
	public Minecraft mc;
	
	private List<CheckBox> options = new ArrayList<>();
	
    public Module(String name, String description, Category category, int keycode) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.toggled = false;
        this.keycode = keycode;
        this.mc = Minecraft.getInstance();
    }
    
    public int getKeyCode() {
    	return keycode;
    }
    
    public void onEnabled() {}
    public void onDisabled() {}
    public void onUpdate() {}
    public abstract void setupOptions();
    
    public void toggle() {
    	this.toggled = !this.toggled;
    	if(this.toggled) {
    		onEnabled();
    	}else {
    		onDisabled();
    	}
    }
    
    public String getName() {
    	return name;
    }
    
    public Category getCategory() {
    	return category;
    }
    
    public boolean isToggled() {
    	return toggled;
    }
    
    public void setToggled(boolean toggled) {
    	this.toggled = toggled;
    }
    
    public void onRender() {
    	if(this.isToggled()) {
    		renderLogic();
    	}
    }
    
    protected void renderLogic() {}
    
    public String getDescription() {
    	return description;
    }

	public void renderOptions(GuiGraphics graphics, int x, int y) {
		int yOffset = 0;
		for(CheckBox option : options) {
			option.setPosition(10, yOffset);
			option.Render(graphics);
			yOffset += 20;
		}
	}
	
	public void addOption(CheckBox checkbox) {
		options.add(checkbox);
		
	}
	
	public List<CheckBox> getOptions() {
		return options;
		
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for(CheckBox option : options) {
			if(option.mouseClicked(mouseX, mouseY, button)){
			return true;
		    }
	     }
	     return false;
	     
	}
    
}

