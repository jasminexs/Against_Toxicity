package net.epicgamerjamer.mod.againsttoxicity.mixin;

import net.epicgamerjamer.mod.againsttoxicity.client.ChatProcessor;
import net.epicgamerjamer.mod.againsttoxicity.client.NameHelper;
import net.epicgamerjamer.mod.againsttoxicity.client.TextBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
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
        if (NameHelper.getUsername(message.getString()) != null && new ChatProcessor(message.getString()).processChat() > 0) {
            assert MinecraftClient.getInstance().player != null;
            String address = (Objects.requireNonNull(MinecraftClient.getInstance().player.networkHandler.getServerInfo()).address);
            if (new ChatProcessor((message.getString())).checkServer(address)) {
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand(new TextBuilder(NameHelper.getUsername(message.getString()), new ChatProcessor(message.getString()).processChat(), true).toString());
            } else {
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(new TextBuilder(NameHelper.getUsername(message.getString()), new ChatProcessor(message.getString()).processChat(), false).toString());
            }

        }
    }
}