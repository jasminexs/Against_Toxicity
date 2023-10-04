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
public class MixinChat {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    public void onGameMessage(Text m, CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(Config.class).getConfig().modEnabled) {
            String modVer = "v1.5";

            String message = m.getString().replace("§7","").replace("§r","") + " ";
            String name = NameHelper.getUsername(message);

            message = message.replaceAll("[^a-zA-Z0-9_:<>()\\[\\] ]", "");

            if (message.contains(" was blown up by ")) {
                message = message.substring(message.indexOf(" was blown up by ")+17);
            }
            if (message.contains(" was slain by ")) {
                message = message.substring(message.indexOf(" was slain by ")+13);
            }

            if (name != null) {
                ChatProcessor processor = new ChatProcessor(message, name);
                int toxicity = processor.getToxicity();
                boolean isPrivate = processor.isPrivate();
                assert MinecraftClient.getInstance().player != null;
                ClientPlayNetworkHandler handler = MinecraftClient.getInstance().player.networkHandler;

                if (toxicity > 0) {
                    String response = new TextBuilder(name, toxicity).toString();
                    if (isPrivate) handler.sendChatCommand(("msg " + name + " " + response).replace("§", ""));
                    else handler.sendChatMessage(response.replace("§", ""));
                } else if (message.contains("!at")) {
                    // Lower priority than checking for toxicity - responds based on the sender of the "command"
                    if (name.matches("epicgamerjamer") && message.contains(":3")) {
                        handler.sendChatMessage("I support trans rights! :3");
                    }
                    else if (message.contains("-users")) {
                        if (name.matches("epicgamerjamer")) {
                            handler.sendChatMessage("I am using AgainstToxicity " + modVer + ".");
                        } else {
                            handler.sendCommand("msg " + name + " I am using AgainstToxicity.");
                        }
                    }
                    else if (message.contains("-download")) {
                        handler.sendChatCommand("msg " + name + " Download AgainstToxicity -> https://modrinth.com/mod/againsttoxicity");
                    }
                    else if (name.matches("epicgamerjamer")) {
                        handler.sendChatMessage("Unknown !at Command (Did you forget the dash?)");
                    }
                    else {
                        handler.sendCommand("msg " + name + " Unknown !at Command (Did you forget the dash?)");
                    }
                }
            }
        }
    }
}