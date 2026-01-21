package me.johnny.copium.command;

import java.util.HashMap;
import java.util.Map;

import me.johnny.copium.command.commands.GuiScaleCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CommandManager {
    private static final Map<String, Command> commands = new HashMap<>();

    public static void init() {
        register(new GuiScaleCommand());
        // register(new ToggleCommand());
        // add more here
    }

    private static void register(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public static void executeCommand(String message) {
        if (!message.startsWith(".")) return;

        String[] parts = message.substring(1).split(" ", 2);
        String cmdName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        Command cmd = commands.get(cmdName);
        if (cmd != null) {
            cmd.execute(args);
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("§cUnknown command: ." + cmdName), false);
        }
    }
}