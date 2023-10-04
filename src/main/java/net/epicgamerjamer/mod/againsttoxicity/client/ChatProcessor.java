package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatProcessor {
    public String msg;
    public String name;
    public String address;
    public boolean isSingleplayer;
    @Unique
    Config config = AutoConfig.getConfigHolder(Config.class).getConfig();
    @Unique
    private boolean debug = config.debug;
    @Unique
    private boolean privateDefault = config.privateDefault;
    @Unique
    private String[] publicServers = config.servers.publicServers;
    @Unique
    private String[] privateServers = config.servers.privateServers;

    public ChatProcessor(String m, String n) {
        name = n;
        msg = m.toLowerCase()
                .replaceAll("[^a-zA-Z0-9_ ]", "")
                .replace(" are ", " r ")
                .replace(" youre ", " ur ")
                .replace(" u ", " you ")
                .replace(" just ", " ")
                .replace(name.toLowerCase(), "");
        MinecraftClient instance = MinecraftClient.getInstance();
        if (debug) System.out.println("[AgainstToxicity] ChatProcessor - \"msg\" = " + msg);
        if (debug) System.out.println("[AgainstToxicity] ChatProcessor - \"name\" = " + name);
        if (!instance.isInSingleplayer()) {
            address = instance.getNetworkHandler().getServerInfo().address;
        } else address = "singeplayer";
    }
    public int getToxicity() {
        if (checkSlurs()) {
            return 2;
        } else if (checkToxicity()) {
            return 1;
        } else {
            return 0;
        }
    } // Determines the toxicity level of a message; 2 = has slurs, 1 = toxic but no slurs, 0 = not toxic
    public boolean isPrivate() {
        if (!privateDefault) {
            for (String s : privateServers) {
                if (address.contains(s)) {
                    return true;
                }
            }
        } // true if server is in private overrides
        if (privateDefault) {
            for (String s : publicServers) {
                if (address.contains(s)) {
                    return false;
                }
            }
            return true;
        } // false if server is in the public overrides, true if not
        return NameHelper.isPrivate; // true if before conditions weren't met and the message is sent to the player privately
    } // Checks whether to send the message privately or publicly
    private boolean checkToxicity() {
        String[] list = Lists.ToxicList; // Single words; prevents false positives ("assist" flagged by "ass")
        String[] list2 = Lists.ToxicList2; // Phrases; doesn't flag without space ("urbad" = false, "ur bad" = true)
        if (msg.contains(" was blown up by ")) msg = msg.substring(msg.indexOf("was blown"));
        if (msg.contains(" was slain by ")) msg = msg.substring(msg.indexOf("was slain"));
        String[] words = msg.toLowerCase().split(" "); // Converts message to array of lowercase strings

        for (String s : list) {
            for (String word : words) {
                if (s.matches(word)) {
                    return true;
                }
            }
        } // Matches whole words only

        // Matches phrases, must include spaces
        for (String s : list2) {
            if (msg.toLowerCase().contains(s)) {
                return true;
            }
        }

        return false;
    } // Return true if the 1+ word(s) matches an entry in list, OR true if the message contains any phrase in list2
    private boolean checkSlurs() {
        Pattern regex = Pattern.compile(String.join("|", Lists.SlurList), Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(msg.replace(" ", ""));

        return matcher.find();
    } // Return true if the chat message has a slur, ignores spaces (VERY sensitive)
}