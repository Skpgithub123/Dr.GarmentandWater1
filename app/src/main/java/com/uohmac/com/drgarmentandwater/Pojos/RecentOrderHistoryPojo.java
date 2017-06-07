package com.uohmac.com.drgarmentandwater.Pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by uOhmac Technologies on 30-Jan-17.
 */
public class RecentOrderHistoryPojo {


    @SerializedName("item_id")
    protected String itemid;

    @SerializedName("item_name")
    public String itemname;

    @SerializedName("item_quantity")
    public String itemqty;

    @SerializedName("item_img")
    public String itemimg;


    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getItemimg() {
        return itemimg;
    }

    public void setItemimg(String itemimg) {
        this.itemimg = itemimg;
    }
}
