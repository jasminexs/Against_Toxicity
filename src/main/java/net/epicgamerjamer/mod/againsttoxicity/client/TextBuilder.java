package net.epicgamerjamer.mod.againsttoxicity.client;

import java.util.Random;

public class TextBuilder {
    private static final Random random = new Random();
    private final String name;
    private final String response;
    private final boolean previous;

    public TextBuilder(String name, int l, boolean previous) {
        this.name = name;
        this.previous = previous;
        if (l == 1) {
            this.response = Lists.AntiToxicList[random.nextInt(Lists.AntiToxicList.length)];
        } else if (l == 2) {
            this.response = Lists.AntiSlurList[random.nextInt(Lists.AntiSlurList.length)];
        } else {
            this.response = null;
        }
    }

    public String toString() {
        if (this.previous) {
            return("msg " + this.name + " " + this.name + this.response);
        }
        else {
            return (this.name + this.response);
        }
    }
}