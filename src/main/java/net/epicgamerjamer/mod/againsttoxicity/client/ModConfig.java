package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config.Gui.Background("againsttoxicity:textures/background.png")
@Config(name = "againsttoxicity")
public class ModConfig implements ConfigData {
    public static boolean modEnabled = true;
    public static boolean debug = false;
    public static boolean privateDefault = false;
    @ConfigEntry.Gui.CollapsibleObject
    public static ServersDropdown servers = new ServersDropdown();
    public static String[] friends = {
            "Add names to ignore here!"
    };

    static class ServersDropdown {
        public static String[] privateServers = {
                "mcpvp.club"
        };
        public static String[] publicServers = {

        };
    }
}