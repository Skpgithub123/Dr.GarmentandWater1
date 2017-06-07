package com.uohmac.com.drgarmentandwater.Pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by System-03 on 11/7/2016.
 */
public class DryCleaningPojo {

    @SerializedName("item_id")
    protected String id;

    @SerializedName("item_name")
    public String itemname;

    @SerializedName("item_img")
    public String itemimage;

    @SerializedName("item_price")
    public String itemprice;

    @SerializedName("category_id")
    public String categoryid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }
}
