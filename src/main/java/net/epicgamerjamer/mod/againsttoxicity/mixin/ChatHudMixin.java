package net.epicgamerjamer.mod.againsttoxicity.mixin;

import net.epicgamerjamer.mod.againsttoxicity.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.epicgamerjamer.mod.againsttoxicity.client.AgainstToxicityClient.LOGGER;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    public void onGameMessage(Text msg, CallbackInfo ci) {
        if (!ModConfig.modEnabled) {
            DebugLogger.debug("ChatHudMixin - Mod is disabled");
            return;
        }
        DebugLogger.debug("ChatHudMixin - Game message received, processing...");

        String message = msg.getString();
        String name = NameHelper.getUsername(message);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        ClientPlayNetworkHandler handler = player.networkHandler;
        if (!message.contains("!at ")) {
            return;
        }

        // Higher priority than checking for toxicity - responds based on the sender of the "command"
        DebugLogger.debug("ChatHudMixin - Command received");

        if (name == null) {
            return;
        }
        switch (message) {
            case ":3":
                DebugLogger.debug("ChatHudMixin - :3 received");
                if (name.matches("epicgamerjamer")) {
                    DebugLogger.debug("ChatHudMixin - :3 public");
                    handler.sendChatMessage("I support trans rights! :3");
                } else {
                    DebugLogger.debug("ChatHudMixin - :3 private");
                    handler.sendCommand("msg " + name + " I support trans rights! :3");
                }
                break;
            case "-users":
                DebugLogger.debug("ChatHudMixin - -users received");
                if (name.matches("epicgamerjamer")) {
                    DebugLogger.debug("ChatHudMixin - -users public");
                    handler.sendChatMessage("I am using AgainstToxicity v1.3.3 for 1.20.1. Debug = " + ModConfig.debug);
                } else {
                    DebugLogger.debug("ChatHudMixin - -users private");
                    handler.sendCommand("msg " + name + " I am using AgainstToxicity.");
                }
                break;
            case "-download":
                DebugLogger.debug("ChatHudMixin - -download received");
                handler.sendChatCommand("msg " + name + " Download AgainstToxicity -> https://modrinth.com/mod/againsttoxicity");
            default:
                break;
        }
        
        if (new ChatProcessor(message).processChat() > 0) {
            DebugLogger.debug("ChatHudMixin - Received toxic message");
            if ((new ChatProcessor((message)).isPrivate())) {
                // Privately message players if certain conditions are met
                DebugLogger.debug("ChatHudMixin - Sending private message");
                handler.sendChatCommand(new TextBuilder(name, new ChatProcessor(message).processChat(), true).toString());
            } else {
                // Otherwise say publicly
                DebugLogger.debug("ChatHudMixin - Sending public message");
                handler.sendChatMessage(new TextBuilder(name, new ChatProcessor(message).processChat(), false).toString());
            }
        }
    }
}