package net.epicgamerjamer.mod.againsttoxicity.client;

import java.util.Random;

public class TextBuilder {
    private static final Random RANDOM = new Random();
    private final String name;
    private final String response;
    private final boolean priv;

    public TextBuilder(String n, int l, boolean p) {
        name = n;
        priv = p;
        if (l == 1) {
            response = Lists.AntiToxicList[RANDOM.nextInt(Lists.AntiToxicList.length)];
        } else if (l == 2) {
            response = Lists.AntiSlurList[RANDOM.nextInt(Lists.AntiSlurList.length)];
        } else {
            response = null;
        }
    }

    public String toString() {
        if (priv) {
            return("msg " + name + " " + name + response);
        }
        else {
            return (name + response);
        }
    }
}