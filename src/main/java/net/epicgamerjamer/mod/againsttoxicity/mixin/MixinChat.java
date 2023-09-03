package net.epicgamerjamer.mod.againsttoxicity.mixin;

import net.epicgamerjamer.mod.againsttoxicity.client.ChatProcessor;
import net.epicgamerjamer.mod.againsttoxicity.client.NameHelper;
import net.epicgamerjamer.mod.againsttoxicity.client.TextBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ChatHud.class)
public class MixinChat {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))

    public void onGameMessage(Text message, CallbackInfo ci) {
        assert MinecraftClient.getInstance().player != null;
        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().player.networkHandler;
        if (message.getString().contains("!at ")) {
            if (message.getString().contains("!at :3")) {
                handler.sendChatMessage("I support trans rights! :3");
            }
            if (message.getString().contains("!at -users")) {
                handler.sendChatMessage("I am using AgainstToxicity.");
            }
            if (message.getString().contains("!at -discord")) {
                handler.sendChatMessage("Join the AgainstToxicity Discord! discord.gg/stqwNAezeP");
            }
        } else  if (NameHelper.getUsername(message.getString()) != null && new ChatProcessor(message.getString()).processChat() > 0) {
            String address = (Objects.requireNonNull(MinecraftClient.getInstance().player.networkHandler.getServerInfo()).address);
            if (new ChatProcessor((message.getString())).checkServer(address)) {
                handler.sendChatCommand(new TextBuilder(NameHelper.getUsername(message.getString()), new ChatProcessor(message.getString()).processChat(), true).toString());
            } else {
                handler.sendChatMessage(new TextBuilder(NameHelper.getUsername(message.getString()), new ChatProcessor(message.getString()).processChat(), false).toString());
            }

        }
    }
}