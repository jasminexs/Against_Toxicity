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
            String message = m.getString();
            String name = NameHelper.getUsername(message);

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            ClientPlayNetworkHandler handler = player.networkHandler;

            if (message.contains("!at ")) {
                // Higher priority than checking for toxicity - responds based on the sender of the "command"
                if (message.contains(":3") && name != null) {
                    if (name.matches("epicgamerjamer")) {
                        handler.sendChatMessage("I support trans rights! :3");
                    } else {
                        handler.sendCommand("msg " + name + " I support trans rights! :3");
                    }
                }
                if (message.contains("-users") && name != null) {
                    if (name.matches("epicgamerjamer")) {
                        handler.sendChatMessage("I am using AgainstToxicity.");
                    } else {
                        handler.sendCommand("msg " + name + " I am using AgainstToxicity.");
                    }
                }
                if (message.contains("-download") && name != null) {
                    handler.sendChatCommand("msg " + name + " Download AgainstToxicity -> github.com/epicgamerjamer/Against_Toxicity/");
                }

            } else  if (name != null && new ChatProcessor(message).processChat() > 0) {

                if ((new ChatProcessor((message)).isPrivate())) {
                    // Privately message players if certain conditions are met
                    handler.sendChatCommand(new TextBuilder(name, new ChatProcessor(message).processChat(), true).toString());
                }
                else {
                    // Otherwise say publicly
                    handler.sendChatMessage(new TextBuilder(name, new ChatProcessor(message).processChat(), false).toString());
                }
            }
        }
    }
}