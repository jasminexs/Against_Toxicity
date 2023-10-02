package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config.Gui.Background("againsttoxicity:textures/background.png")
@Config(name = "againsttoxicity")
public class ModConfig implements ConfigData {
    public boolean modEnabled = true;
    public boolean debug = false;
    public boolean privateDefault = false;

    @ConfigEntry.Gui.CollapsibleObject
    ServersDropdown servers = new ServersDropdown();
    public static class ServersDropdown {
        public String[] privateServers = {
                "mcpvp.club"
        };
        public String[] publicServers = {

        };
    }

    @ConfigEntry.Gui.Tooltip
    public String[] friends = {
            "Add names to ignore here!"
    };
}