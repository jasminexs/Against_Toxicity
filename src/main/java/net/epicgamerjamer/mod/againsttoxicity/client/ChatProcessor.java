package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatProcessor {
    final String[] regexArr = {
            "aa+", "bbb+", "ccc+", "ddd+", "eee+", "fff+", "ggg+", "hh+", "ii+", "jj+", "kk+", "lll+", "mmm+", "nnn+", "ooo+", "ppp+", "qqq+", "rrr+", "sss+", "ttt+", "uu+", "vv+", "ww+", "xx+", "yy+", "zz+"
    };
    final String[] replaceArr = {
            "a", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "i", "j", "k", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "u", "v", "w", "x", "y", "z"
    };
    Pattern[] patterns = new Pattern[26];
    public String msg;
    public String address;
    @Unique
    Config config = AutoConfig.getConfigHolder(Config.class).getConfig();

    public ChatProcessor(String m) {
        msg = " " + (m.replaceAll("[^a-z0-9 ]", "")
                .replace(" are", " r")
                .replace(" youre", " ur")
                .replace(" your", " ur")
                .replace(" you", " u")
                .replace(" just", "")
                .replace(" really", ""));

        for (int i = 0; i < 26; i++) patterns[i] = Pattern.compile(regexArr[i]);
        for (int i = 0; i < 26; i++) {
            Matcher matcher = patterns[i].matcher(msg);
            msg = matcher.replaceAll(replaceArr[i]);
        }

        if (!MinecraftClient.getInstance().isInSingleplayer()) address = MinecraftClient.getInstance().getNetworkHandler().getServerInfo().address;
        else address = "single player";
    }

    public int getToxicity() {
        for (String s:Lists.Slurs) if (msg.replace(" ", "").contains(s)) return 2;
        if (isRude()) return 1;
        else return 0;
    } // Determines the toxicity level of a message; 2 = has slurs, 1 = toxic but no slurs, 0 = not toxic

    public boolean isPrivate() {
        // true if server is in private overrides
        if (!config.privateDefault) {
            for (String s : config.lists.privateServers) if (address.contains(s)) return true;
        } else {
            for (String s : config.lists.publicServers) if (address.contains(s)) return false;
            return true;
        } // false if server is in the public overrides, true if not
        return NameHelper.isPrivate; // true if before conditions weren't met and the message is sent to the player privately
    } // Checks whether to send the message privately or publicly

    private boolean isRude() {
        if (msg.contains(" was blown up by ")) msg = msg.substring(msg.indexOf(" was blown up by "));
        if (msg.contains(" was slain by ")) msg = msg.substring(msg.indexOf(" was slain by "));
        String[] words = msg.split(" "); // Converts message to array of strings
        for (String s : Lists.Words) for (String word : words) if (s.equals(word)) return true; // Matches whole words only
        for (String s : Lists.ToxicList2) if (msg.contains(s)) return true; // Matches phrases
        return false;
    }
}