package net.epicgamerjamer.mod.againsttoxicity.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.epicgamerjamer.mod.againsttoxicity.config.Config;
import org.jetbrains.annotations.NotNull;

public class ChatProcessor {
    private final String msg;
    public ChatProcessor(String m) {
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
    public boolean checkServer(String address) {
        String[] list = new Config().getServers();

        for (String s : list) {
            if (s.contains(address)) {
                return true;
            }
        }

        return false;
    }
    private boolean checkToxic(String message) {
        String[] list = new Config().getToxicList();
        String[] words = message.toLowerCase().split(" ");

        for (String s : list) {
            for (String word : words) {
                if (s.matches(word)) {
                    return true;
                }
            }
        }

        return false;
    }
    private boolean checkSlurs(@NotNull String message) {
        Pattern regex = Pattern.compile(String.join("|", new Config().getSlurList()), Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(String.join("", message.split(" ")));

        return matcher.find();
    }
}