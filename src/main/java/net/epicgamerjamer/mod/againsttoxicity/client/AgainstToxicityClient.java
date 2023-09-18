package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgainstToxicityClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("againsttoxicity");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        LOGGER.info("Against Toxicity is Initialized! :3");
    }
}