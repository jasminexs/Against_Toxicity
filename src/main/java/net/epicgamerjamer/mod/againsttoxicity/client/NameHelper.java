package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameHelper {
    //    Thanks to Crosby/RacoonDog for optimizing regex
    private static final Pattern prefixes = Pattern.compile("(<)?(BOOSTER |MOD |ADMIN |DEV |YOUTUBE |Stray |VIP|<-- |Party]|Dead]|\\[)?([^>\\s:]+)");
    private static final Pattern blownup = Pattern.compile("was blown up by (\\w+)");
    private static final Pattern slain = Pattern.compile("was slain by (\\w+)");
    @Unique
    private static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    @Unique
    private static String[] friends = config.friends;
    public static String getUsername(String input) {
        if (config.debug) System.out.println(Arrays.toString(friends));
        input = input.replace("\\", "")
                .replace("/", "")
                .replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("(", "")
                .replace(")", "")
                .replace("?", "")
                .replace("!", "")
                .replace("*", "")
                .replace(".", "")
                .replace(";", "")
                .replace(":", "")
                .replace("'", "")
                .replace("\"", "")
                .replace("|", "")
                .replace("@", "")
                .replace(",", "")
                .replace(".", "")
                .replace("$", "");
        Matcher matcher1 = prefixes.matcher(input);
        boolean ignorePlayer = false;
        String[] ignoreNames = Lists.IgnoreNames;

        if (input.contains("was blown up by")) {
            Matcher matcher2 = blownup.matcher(input.substring(input.indexOf(" was ")));
            if (matcher2.find()) return matcher2.group(1);
        }

        if (input.contains("was slain by")) {
            Matcher matcher3 = slain.matcher(input.substring(input.indexOf(" was ")));
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
                if (name.length() == 1) {
                    return getUsername(input.substring(1).replace("§",""));
                }
                return name.replace("§","");
            }
        }
        return null;
    }
}