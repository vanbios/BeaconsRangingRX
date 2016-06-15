package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 27.01.2016.
 */

@AllArgsConstructor
@Getter
public class LogBase {
    private String text;
    private int type;


    /*public LogBase(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }*/
}
