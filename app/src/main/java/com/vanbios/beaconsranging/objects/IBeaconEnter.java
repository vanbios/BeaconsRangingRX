package com.vanbios.beaconsranging.objects;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Ihor Bilous on 09.12.2015.
 */

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "isNotify")
public class IBeaconEnter {
    private String UUID;
    private int minor, major;
    private boolean isNotify;


    public IBeaconEnter(String UUID,  int major, int minor) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
    }

    @Override
    public boolean equals(Object o) {
        return (this.UUID.equals(((IBeaconEnter) o).UUID) &&
                this.major == (((IBeaconEnter) o).major) &&
                this.minor == (((IBeaconEnter) o ).minor));
    }

}
