package com.vanbios.beaconsranging.objects;

/**
 * Created by Ihor Bilous on 27.01.2016.
 */
public class LogBase {
    private String text;
    private int type;


    public LogBase(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}
