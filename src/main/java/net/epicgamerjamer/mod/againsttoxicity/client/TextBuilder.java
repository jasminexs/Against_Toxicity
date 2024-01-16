package net.epicgamerjamer.mod.againsttoxicity.client;

import java.util.Random;

public class TextBuilder {
    private static final Random random = new Random();
    private final String name;
    private final String response;

    public TextBuilder(String s, int i) {
        name = s;
        switch (i) {
            case 2 -> response = Lists.AntiSlur[random.nextInt(Lists.AntiSlur.length)];
            case 1 -> response = Lists.AntiToxic[random.nextInt(Lists.AntiToxic.length)];
            default -> response = null;
        }
    }
    public String getString() {
        return name + response;
    }
}