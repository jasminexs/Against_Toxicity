package net.epicgamerjamer.mod.againsttoxicity.client;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.epicgamerjamer.mod.againsttoxicity.client.AgainstToxicityClient.LOGGER;

public class ChatProcessor {
    public String msg;
    public String name;
    public String address;
    public boolean isSingleplayer;

    public ChatProcessor(@NotNull String m) {
        this.msg = m.replace("\\", "")
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
                .replace("$", "")
                .replace(" youre", " ur")
                .replace(" you", " u")
                .replace(" are", " r")
                .toLowerCase();
        this.name = NameHelper.getUsername(m);
        this.isSingleplayer = MinecraftClient.getInstance().isInSingleplayer();
        DebugLogger.debug("ChatProcessor - \"msg\" = " + msg);
        DebugLogger.debug("ChatProcessor - \"name\" = " + name);
        if (!isSingleplayer)
            this.address = (Objects.requireNonNull((Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler())).getServerInfo()).address);
        else this.address = "singeplayer";
    } // Constructor; also removes characters that screw up the ChatProcessor
    public int processChat() {
        if (checkSlurs()) {
            return 2;
        } else if (checkToxic()) {
            return 1;
        } else {
            return 0;
        }
    } // Determines the toxicity level of a message; 2 means it has slurs, 1 means its toxic but no slurs, 0 means not toxic
    public boolean isPrivate() {
        if (!ModConfig.privateDefault) {
            String[] list = ModConfig.ServersDropdown.privateServers;
            for (String s : list) {
                if (address.contains(s)) {
                    return true;
                }
            }
        } // true if server is in private overrides

        String[] pmList = {
                "-> you",
                "-> me",
                "<--"
        };
        for (String s : pmList) {
            if (msg.toLowerCase().contains(s)) {
                return true;
            }
        }
        // true if toxic message is determined to be a pm
        if (ModConfig.privateDefault) {
            String[] list = ModConfig.ServersDropdown.publicServers;
            for (String s : list) {
                if (address.contains(s)) {
                    return false;
                }
            }
            return true;
        } // false if server is in the public overrides, true if not

        return false; // false if none of the conditions are met (shouldn't occur but just in case)
    } // Checks certain conditions to determine whether to send the message privately or publicly
    private boolean checkToxic() {
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
        Matcher matcher = regex.matcher(msg.replace(" ", "").replace(name.toLowerCase(), ""));

        return matcher.find();
    } // Return true if the chat message has a slur, ignores spaces (VERY sensitive)
}