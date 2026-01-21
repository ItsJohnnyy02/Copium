package me.johnny.copium.mixin;

import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatInputMixin {
    @Inject(method = "onDone", at = @At("HEAD"), cancellable = true)
    private void onChatSent(String message, CallbackInfo ci) {
        if (message.startsWith(".")) {
            me.johnny.copium.command.CommandManager.executeCommand(message);
            ci.cancel();
            this.minecraft.setScreen(null);
        }
    }
}
