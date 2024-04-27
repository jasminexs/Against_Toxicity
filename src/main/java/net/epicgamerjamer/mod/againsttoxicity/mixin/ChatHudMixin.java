package net.epicgamerjamer.mod.againsttoxicity.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.epicgamerjamer.mod.againsttoxicity.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    public void onGameMessage(Text m, CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(Config.class).getConfig().modEnabled) {
            String message = m.getString().replaceAll("§.","");
            String name = NameHelper.getUsername(message.replaceAll("[^a-zA-Z0-9_:<>()\\[\\]\\- ]", ""));
            if (name != null && !name.isEmpty()) {
                message = message.substring(message.indexOf(name) + name.length() + 1);
                message = message.toLowerCase()
                        .replaceAll("[^a-z0-9_ ]", "")
                        .trim() + " ";
                if (message.contains(" was blown up by ") || message.contains(" was slain by "))
                    message = message.substring(message.indexOf(name) + name.length());

                ChatProcessor processor = new ChatProcessor(message);
                int toxicity = processor.getToxicity();
                boolean isPrivate = processor.isPrivate();
                assert MinecraftClient.getInstance().player != null;
                ClientPlayNetworkHandler handler = MinecraftClient.getInstance().player.networkHandler;

                if (toxicity > 0) {
                    Random random = new Random();
                    String response = name + ((toxicity == 2) ? Lists.AntiSlur[random.nextInt(Lists.AntiSlur.length)]:Lists.AntiToxic[random.nextInt(Lists.AntiToxic.length)]);
                    if (isPrivate) handler.sendChatCommand(("msg " + name + " " + response));
                    else handler.sendChatMessage(response);
                }
            }
        }
    }
}