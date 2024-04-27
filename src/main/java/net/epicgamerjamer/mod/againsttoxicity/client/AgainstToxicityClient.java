package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class AgainstToxicityClient implements ClientModInitializer {
    private static KeyBinding openAgainstToxicityMenu;
    public static void setOpenAgainstToxicityMenu() {
        MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(Config.class, null).get());
    }

    @Override
    public void onInitializeClient() {
        openAgainstToxicityMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.open_against_toxicity_menu",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_KP_9,
                "category.against_toxicity"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openAgainstToxicityMenu.wasPressed()) {
                MinecraftClient.getInstance().setScreen(null);
                setOpenAgainstToxicityMenu();
            }
        });
        AutoConfig.register(Config.class, JanksonConfigSerializer::new);
    }
}