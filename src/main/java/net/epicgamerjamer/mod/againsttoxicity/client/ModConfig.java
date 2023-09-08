package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config.Gui.Background("againsttoxicity:textures/background.png")
@Config(name = "againsttoxicity")
public class ModConfig implements ConfigData {

    public boolean isModEnabled() {
        return modEnabled;
    }
    public boolean isPrivateDefault() {
        return privateDefault;
    }
    public String[] getPrivateServers() {
        return servers.privateServers;
    }
    public String[] getPublicServers() {
        return servers.publicServers;
    }
    public String[] getFriends() {
        return friends;
    }
    public boolean isDebug() {
        return debug;
    }

    boolean modEnabled = true;
    boolean debug = false;
    boolean privateDefault = false;
    static class ServersDropdown {
        String[] privateServers = {
                "mcpvp.club"
        };
        String[] publicServers = {

        };
    }
    @ConfigEntry.Gui.CollapsibleObject
    ServersDropdown servers = new ServersDropdown();
    String[] friends = {
            "Add names to ignore here!"
    };
}