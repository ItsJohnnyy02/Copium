package me.johnny.copium.command;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class Command {
    protected final Minecraft mc = Minecraft.getInstance();
    protected final String name;
    protected final String usage;
    protected final String description;

    public Command(String name, String usage, String description) {
        this.name = name.toLowerCase();
        this.usage = usage;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(String[] args);

    protected void sendMessage(String msg) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.literal(msg), false);
        }
    }

    protected void sendError(String msg) {
        sendMessage("§c" + msg);
    }

    protected void sendUsage() {
        sendError("Usage: ." + name + " " + usage);
    }
}