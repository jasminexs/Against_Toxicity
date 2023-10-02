package net.epicgamerjamer.mod.againsttoxicity.client;

import java.util.Random;

public class TextBuilder {
    private static final Random random = new Random();
    private final String name;
    private final String response;

    public TextBuilder(String n, int l) {
        name = n;
        if (l == 1) {
            response = Lists.AntiToxicList[random.nextInt(Lists.AntiToxicList.length)];
        } else if (l == 2) {
            response = Lists.AntiSlurList[random.nextInt(Lists.AntiSlurList.length)];
        } else {
            response = null;
        }
    }
    public String toString() {
            return (name + response);
    }
}