package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatProcessor {
    public String msg;
    public String address;
    @Unique
    Config config = AutoConfig.getConfigHolder(Config.class).getConfig();

    public ChatProcessor(String m) {
        msg = " " + (m.replaceAll("[^a-z0-9_ ]", "")
                .replace("are", "r")
                .replace("youre", "ur")
                .replace("your", "ur")
                .replace("u", "you")
                .replace("just", "")
                .replace("really", ""));
        System.out.println("Before: " + msg);
        if (config.removeLetterSpam) msg = removeLetterSpam(msg);
        System.out.println("After: " + msg);

        if (!MinecraftClient.getInstance().isInSingleplayer()) address = MinecraftClient.getInstance().getNetworkHandler().getServerInfo().address;
        else address = "singleplayer";
    }
    public int getToxicity() {
        if (checkSlurs()) return 2;
        else if (checkToxicity()) return 1;
        else return 0;
    } // Determines the toxicity level of a message; 2 = has slurs, 1 = toxic but no slurs, 0 = not toxic
    public boolean isPrivate() {
        // true if server is in private overrides
        if (!config.privateDefault) {
            for (String s : config.servers.privateServers) if (address.contains(s)) return true;
        } else {
            for (String s : config.servers.publicServers) if (address.contains(s)) return false;
            return true;
        } // false if server is in the public overrides, true if not
        return NameHelper.isPrivate; // true if before conditions weren't met and the message is sent to the player privately
    } // Checks whether to send the message privately or publicly
    private boolean checkToxicity() {
        if (msg.contains(" was blown up by ")) msg = msg.substring(msg.indexOf(" was blown up by "));
        if (msg.contains(" was slain by ")) msg = msg.substring(msg.indexOf(" was slain by "));
        String[] words = msg.split(" "); // Converts message to array of strings
        for (String s : Lists.Words) for (String word : words) if (s.equals(word)) return true; // Matches whole words only
        for (String s : Lists.ToxicList2) if (msg.contains(s)) return true; // Matches phrases
        return false;
    }
    private boolean checkSlurs() {
        for (String s:Lists.Slurs) if (msg.contains(s)) return true;
        return false;
    } // true if the chat message has a slur, ignores spaces
    private String removeLetterSpam(String s) {
        final String[] regexArr = {
                "aaa+", "bbb+", "ccc+", "ddd+", "eee+", "fff+", "ggg+", "hhh+", "iii+", "jjj+", "kkk+", "lll+", "mmm+", "nnn+", "ooo+", "ppp+", "qqq+", "rrr+", "sss+", "ttt+", "uuu+", "vvv+", "www+", "xxx+", "yyy+", "zzz+"
        };
        final String[] replaceArr = {
                "aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj", "kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz"
        };

        // Compile the regex patterns
        Pattern[] patterns = new Pattern[26];
        for (int i = 0; i < 26; i++) {
            patterns[i] = Pattern.compile(regexArr[i]);
        }

        // Use Matcher to perform replacements
        for (int i = 0; i < 26; i++) {
            Matcher matcher = patterns[i].matcher(s);
            s = matcher.replaceAll(replaceArr[i]);
        }
        return s;
    }
}