package com.uohmac.com.drgarmentandwater.Pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by System-03 on 11/10/2016.
 */
public class Timeslotspojo {

    @SerializedName("timing")

    public String timing;

    public String getTiming(){
        return timing;
    }
    public void setTiming(String timing){
        this.timing = timing;
    }
}
