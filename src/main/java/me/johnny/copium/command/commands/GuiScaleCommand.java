package me.johnny.copium.command.commands;

import me.johnny.copium.command.Command;
import net.minecraft.client.Minecraft;

public class GuiScaleCommand extends Command {

    public GuiScaleCommand() {
        super("gui", "scale <value>", "Sets the GUI scale (0.5 to 4.0)");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            sendUsage();
            return;
        }

        try {
            float scale = Float.parseFloat(args[0]);
            if (scale < 0.5f || scale > 4.0f) {
                sendError("Scale must be between 0.5 and 4.0");
                return;
            }

            int intScale = (int)(scale * 100);
            Minecraft mc = Minecraft.getInstance();
            mc.options.guiScale().set(intScale);
            mc.resizeDisplay();  // Apply immediately

            sendMessage("§aGUI scale set to " + scale);
        } catch (NumberFormatException e) {
            sendError("Invalid number");
        }
    }
}
