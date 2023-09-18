package net.epicgamerjamer.mod.againsttoxicity.client;

import static net.epicgamerjamer.mod.againsttoxicity.client.AgainstToxicityClient.LOGGER;

public class DebugLogger {
    public static void debug(String message) {
        if (!ModConfig.debug) {
            return;
        }
        LOGGER.info(message);
    }
}
