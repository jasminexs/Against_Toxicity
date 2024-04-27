package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config.Gui.Background("againsttoxicity:textures/background.png")
@me.shedaniel.autoconfig.annotation.Config(name = "againsttoxicity")
public class Config implements ConfigData {
    public boolean modEnabled = true;
    public boolean privateDefault = false;
    @ConfigEntry.Gui.CollapsibleObject
    Lists lists = new Lists();
    public static class Lists {
        public String[] friends = {};
        public String[] privateServers = {};
        public String[] publicServers = {};
    }
}