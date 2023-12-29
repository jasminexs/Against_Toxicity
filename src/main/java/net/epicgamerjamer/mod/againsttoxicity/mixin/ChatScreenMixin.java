package net.epicgamerjamer.mod.againsttoxicity.mixin;

import net.epicgamerjamer.mod.againsttoxicity.client.ChatProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void onSendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        if (new ChatProcessor(chatText.toLowerCase()).getToxicity() > 0 && !chatText.startsWith("/")) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                    Text.literal("Try rewriting that to be less toxic.").formatted(Formatting.GRAY)
            );
            cir.setReturnValue(false);
        }
    }
}
