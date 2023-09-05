package net.epicgamerjamer.mod.againsttoxicity.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.epicgamerjamer.mod.againsttoxicity.config.Config;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatProcessor {
    @Unique
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final String msg;
    String address = ((MinecraftClient.getInstance().getNetworkHandler()).getServerInfo().address);
    public ChatProcessor(@NotNull String m) {
        msg = m.replace("\\", "")
                .replace("?", "")
                .replace("!", "")
                .replace("*", "")
                .replace(".", "")
                .replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
        ;
    }
    public int processChat() {
        // Determines the toxicity level of a message; 2 means it has slurs, 1 means its toxic but no slurs, 0 means not toxic
        for (int i = 0; i < new Config().getIgnore().length; i++) {
            if (msg.contains(new Config().getIgnore()[i])) {
                return 0;
            }
        }


        if (checkSlurs(msg)) {
            return 2;
        } else if (checkToxic(msg)) {
            return 1;
        } else {
            return 0;
        }
    }
    public boolean isPrivate() {
        if (!config.isPrivateDefault()) {
            String[] list = config.getPrivateServers();
            for (String s : list) {
                if (address.contains(s)) {
                    return true;
                }
            }
        } // true if server is in private overrides
        if (msg.contains("->")) {
            String[] list = {
                    "-> you",
                    "-> me"
            };
            for (String s:list) {
                if (msg.toLowerCase().contains(s)) {
                    System.out.println("Received a private message");
                    return true;
                }
            }
        } // true if toxic message is determined to be a pm
        System.out.println("privateDefault = " + config.isPrivateDefault());
        if (config.isPrivateDefault()) {
            String[] list = config.getPublicServers();
            for (String s : list) {
                if (address.contains(s)) {
                    return false;
                }
            }
            return true;
        } // false if server is in the public overrides, true if not

        return false; // false if none of the conditions are met (shouldn't occur but just in case)
    }
    private boolean checkToxic(@NotNull String message) {
        // Return true if the 1+ word(s) matches an entry in list, OR true if the message contains any phrase in list2
        String[] list = new Config().getToxicList(); // Single words; prevents false positives such as "assist"
        String[] list2 = new Config().getToxicList2(); // Phrases; doesn't flag without space ("urbad" = false, "ur bad" = true)
        String[] words = message.toLowerCase().split(" "); // Converts message to array of lowercase strings

        // Matches whole words online
        for (String s : list) {
            for (String word : words) {
                if (s.matches(word)) {
                    return true;
                }
            }
        }

        // Matches phrases, must include spaces
        for (String s : list2) {
            if (message.toLowerCase().contains(s)) {
                return true;
            }
        }

        return false;
    }
    private boolean checkSlurs(@NotNull String message) {
        // Return true if the chat message has a slur, ignores spaces (VERY sensitive)
        Pattern regex = Pattern.compile(String.join("|", new Config().getSlurList()), Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(String.join("", message.split(" ")));

        return matcher.find();
    }
}