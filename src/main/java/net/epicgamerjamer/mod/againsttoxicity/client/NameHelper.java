package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.epicgamerjamer.mod.againsttoxicity.config.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameHelper {
    static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    public static String getUsername(String input) {
        Pattern pattern = Pattern.compile("(<)?(BOOSTER |MOD |ADMIN |DEV |YOUTUBE |Stray |VIP |🌙 |✟ |⚔ |\\[)?([^>\\s:]+)");
        Matcher matcher = pattern.matcher(input);
        boolean hasFriends = false;
        String[] ignoreNames = new Config().getIgnoreNames();
        String[] friends = config.getFriends();

        if (matcher.find() && matcher.group(3) != null) {
            for (String i : ignoreNames) {
                if (i.matches(matcher.group(3))) {
                    hasFriends = true;
                    break;
                }
            }
            for (String i : friends) {
                if (i.matches(matcher.group(3))) {
                    hasFriends = true;
                    break;
                }
            }
            if (!hasFriends) {
                return matcher.group(3);
            }
        }
        return null;
    }
}