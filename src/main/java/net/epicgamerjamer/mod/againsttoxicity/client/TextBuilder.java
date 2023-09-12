package net.epicgamerjamer.mod.againsttoxicity.client;

public class TextBuilder {
    private final String name;
    private final String response;
    private final boolean priv;

    public TextBuilder(String n, int l, boolean p) {
        name = n;
        priv = p;
        if (l == 1) {
            response = Lists.AntiToxicList[(int) (Math.random() * (Lists.AntiToxicList.length))];
        } else if (l == 2) {
            response = Lists.AntiSlurList[(int) (Math.random() * (Lists.AntiSlurList.length))];
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