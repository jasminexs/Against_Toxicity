package net.epicgamerjamer.mod.againsttoxicity.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.epicgamerjamer.mod.againsttoxicity.client.ChatProcessor;
import net.epicgamerjamer.mod.againsttoxicity.client.Config;
import net.epicgamerjamer.mod.againsttoxicity.client.NameHelper;
import net.epicgamerjamer.mod.againsttoxicity.client.TextBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    public void onGameMessage(Text m, CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(Config.class).getConfig().modEnabled) {
            String message = m.getString().replaceAll("§.","");
            String name = NameHelper.getUsername(message.replaceAll("[^a-zA-Z0-9_:<>()\\[\\]\\- ]", ""));
            if (name != null && !name.isEmpty()) {
                System.out.println("1 " + message);
                message = message.substring(message.indexOf(name) + name.length() + 1);
                message = message.toLowerCase()
                        .replaceAll("[^a-z0-9_ ]", "")
                        .trim() + " ";
                        System.out.println("2 " + message);
                if (message.contains(" was blown up by ") || message.contains(" was slain by "))
                    message = message.substring(message.indexOf(name) + name.length());

                ChatProcessor processor = new ChatProcessor(message);
                int toxicity = processor.getToxicity();
                boolean isPrivate = processor.isPrivate();
                assert MinecraftClient.getInstance().player != null;
                ClientPlayNetworkHandler handler = MinecraftClient.getInstance().player.networkHandler;

                if (toxicity > 0) {
                    String response = new TextBuilder(name, toxicity).getString();
                    if (isPrivate) handler.sendChatCommand(("msg " + name + " " + response));
                    else handler.sendChatMessage(response);
                }
            }
        }
    }
}