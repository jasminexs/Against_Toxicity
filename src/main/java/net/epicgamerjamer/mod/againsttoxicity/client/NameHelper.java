package net.epicgamerjamer.mod.againsttoxicity.client;

import net.epicgamerjamer.mod.againsttoxicity.config.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameHelper {
    public static String getUsername(String input) {
        Pattern pattern = Pattern.compile("(<)?(BOOSTER |MOD |ADMIN |DEV |YOUTUBE |🌙 |✟ |⚔ |\\[)?([^>\\s:]+)");
        Matcher matcher = pattern.matcher(input);
        boolean hasFriends = false;
        String[] friends = new Config().getFriends();

        if (matcher.find() && matcher.group(3) != null) {
            for (String friend : friends) {
                if (friend.matches(matcher.group(3))) {
                    hasFriends = true;
                    break;
                }
            }
            if (!hasFriends) {
                return matcher.group(3);
            }
        }
        return null; // Return null if no match is found
    }
}