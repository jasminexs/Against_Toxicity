package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config.Gui.Background("againsttoxicity:textures/background.png")
@Config(name = "againsttoxicity")
public class ModConfig implements ConfigData {
    public static final boolean modEnabled = true;
    public static final boolean debug = false;
    public static final boolean privateDefault = false;
    @ConfigEntry.Gui.CollapsibleObject
    public static final ServersDropdown servers = new ServersDropdown();
    public static final String[] friends = {
            "Add names to ignore here!"
    };

    static class ServersDropdown {
        public static final String[] privateServers = {
                "mcpvp.club"
        };
        public static final String[] publicServers = {

        };
    }
}