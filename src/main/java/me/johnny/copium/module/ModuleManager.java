package me.johnny.copium.module;

import java.util.List;

import java.util.ArrayList;
import java.util.stream.Collectors;

import me.johnny.copium.module.combat.TriggerBot;
import me.johnny.copium.module.gui.ClickGui;
import me.johnny.copium.module.movement.AutoSprint;
import me.johnny.copium.module.movement.AutoWalk;
import me.johnny.copium.module.movement.Flight;
import me.johnny.copium.module.movement.Jesus;
import me.johnny.copium.module.movement.NoFall;
import me.johnny.copium.module.render.FullBright;

public class ModuleManager {
	
	private static List<Module> modules;
	
	public ModuleManager() {
		modules = new ArrayList<>();
		
		//combat
		//register(new Aura());
		register(new TriggerBot());
		
		//movement
		register(new AutoSprint());
		register(new AutoWalk());
		register(new Flight());
		register(new Jesus());
		register(new NoFall());
		
		//player
		
		//render
		register(new FullBright());
		
		//misc
		
		//gui
		register(new ClickGui());
	}
	
	public void register(Module module) {
		modules.add(module);
	}
	
	public static List<Module> getAllModules() {
		return modules;
	}
	
	public List<Module> getEanbledModules() {
		return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
	}
	
	public static List<Module> getModulesByCategory(Category category) {
		return modules.stream()
				.filter(m -> m.getCategory() == category)
				.collect(Collectors.toList());			
	}
	
	public Module getModulesByName(String name) {
		return modules.stream()
				.filter(m -> m.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}
	
	public void toggleModule(String name) {
		Module module = getModulesByName(name);
		if (module != null) {
			module.toggle();
		}
	}
	
	public void onUpdate() {
        for (Module module : modules) {
                module.onUpdate();
        }
    }
	
	public void onRender() {
		for(Module module : modules) {
			module.onRender();
			
		}
	}
	
	public void onTick(KeyManager keyManager) {
		keyManager.checkKeys(this);
		
		for(Module module : modules) {
			if(module.isToggled()) {
				module.onUpdate();
			}
		}
			
	}
	
}
