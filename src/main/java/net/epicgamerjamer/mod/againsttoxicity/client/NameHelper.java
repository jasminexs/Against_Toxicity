package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameHelper {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(<)?(BOOSTER |MOD |ADMIN |DEV |YOUTUBE |Stray |VIP|<-- |🌙 |☽ |❤ |⚡ |☠ |✟ |⚔ |⚒ |☀ |Party]|\\[)?([^>\\s:]+)");
    private static final Pattern BLOWN_UP_PATTERN = Pattern.compile("was blown up by (\\w+)");
    private static final Pattern SLAIN_PATTERN = Pattern.compile("was slain by (\\w+)");

    static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    public static String getUsername(String input) {
        Matcher matcher1 = PREFIX_PATTERN.matcher(input);
        boolean ignorePlayer = false;
        String[] ignoreNames = Lists.ignoreNames;
        String[] friends = config.getFriends();

        if (input.contains("was blown up by")) {
            Matcher matcher2 = BLOWN_UP_PATTERN.matcher(input.substring(input.indexOf(" was ")));
            if (config.isDebug()) System.out.println("[AgainstToxicity] NameHelper - player was blown up");
            if (matcher2.find()) return matcher2.group(1);
        }

        if (input.contains("was slain by")) {
            Matcher matcher3 = SLAIN_PATTERN.matcher(input.substring(input.indexOf(" was ")));
            if (config.isDebug()) System.out.println("[AgainstToxicity] NameHelper - player was slain");
            if (matcher3.find()) return matcher3.group(1);
        }

        if (matcher1.find() && matcher1.group(3) != null) {
            for (String i : ignoreNames) {
                if (i.matches(matcher1.group(3).toLowerCase())) {
                    ignorePlayer = true;
                    break;
                }
            }
            for (String i : friends) {
                if (i.matches(matcher1.group(3))) {
                    ignorePlayer = true;
                    break;
                }
            }
            if (!ignorePlayer) {
                String name = matcher1.group(3);
                if (matcher1.group(3).contains("--")) name = matcher1.group(3).substring(2);
                return name;
            }
        }


        return null;
    }
}