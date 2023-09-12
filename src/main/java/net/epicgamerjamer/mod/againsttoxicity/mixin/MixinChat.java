package net.epicgamerjamer.mod.againsttoxicity.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.epicgamerjamer.mod.againsttoxicity.client.ChatProcessor;
import net.epicgamerjamer.mod.againsttoxicity.client.ModConfig;
import net.epicgamerjamer.mod.againsttoxicity.client.NameHelper;
import net.epicgamerjamer.mod.againsttoxicity.client.TextBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class MixinChat {
    @Unique
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))

    public void onGameMessage(Text m, CallbackInfo ci) {
        if (config.isModEnabled()) {
            if (config.isDebug())
                System.out.println("[AgainstToxicity] MixinChat - Game message received, processing...");
            String message = m.getString();
            String name = NameHelper.getUsername(message);

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            ClientPlayNetworkHandler handler = player.networkHandler;

            if (message.contains("!at ")) {
                // Higher priority than checking for toxicity - responds based on the sender of the "command"
                if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - Command received");
                if (message.contains(":3") && name != null) {
                    if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - :3 received");
                    if (name.matches("epicgamerjamer")) {
                        if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - :3 public");
                        handler.sendChatMessage("I support trans rights! :3");
                    } else {
                        if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - :3 private");
                        handler.sendCommand("msg " + name + " I support trans rights! :3");
                    }
                }
                if (message.contains("-users") && name != null) {
                    if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - -users received");
                    if (name.matches("epicgamerjamer")) {
                        if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - -users public");
                        handler.sendChatMessage("I am using AgainstToxicity v1.3.3 for 1.20.1. Debug = " + config.isDebug());
                    } else {
                        if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - -users private");
                        handler.sendCommand("msg " + name + " I am using AgainstToxicity.");
                    }
                }
                if (message.contains("-download") && name != null) {
                    if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - -download received");
                    handler.sendChatCommand("msg " + name + " Download AgainstToxicity -> https://modrinth.com/mod/againsttoxicity");
                }

            } else  if (name != null && new ChatProcessor(message).processChat() > 0) {
                if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - Received toxic message");
                if ((new ChatProcessor((message)).isPrivate())) {
                    // Privately message players if certain conditions are met
                    if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - Sending private message");
                    handler.sendChatCommand(new TextBuilder(name, new ChatProcessor(message).processChat(), true).toString());
                } else {
                    // Otherwise say publicly
                    if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - Sending public message");
                    handler.sendChatMessage(new TextBuilder(name, new ChatProcessor(message).processChat(), false).toString());
                }
            }
        } else if (config.isDebug()) System.out.println("[AgainstToxicity] MixinChat - Mod is disabled");
    }
}