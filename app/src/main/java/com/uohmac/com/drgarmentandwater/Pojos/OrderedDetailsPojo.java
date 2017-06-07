package com.uohmac.com.drgarmentandwater.Pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by uOhmac Technologies on 02-Jan-17.
 */
public class OrderedDetailsPojo {




      protected String id_cart;


    public String itemname_name;


    public String itemimage_cart;


    public String price;


    public String no_of_item;




    public String getId() {
        return id_cart;
    }

    public void setId(String id) {
        this.id_cart = id;
    }

    public String getItemname() {
        return itemname_name;
    }

    public void setItemname(String itemname) {
        this.itemname_name = itemname;
    }

    public String getItemimage() {
        return itemimage_cart;
    }

    public void setItemimage(String itemimage) {
        this.itemimage_cart = itemimage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNo_of_item() {
        return no_of_item;
    }

    public void setNo_of_item(String no_of_item) {
        this.no_of_item = no_of_item;
    }



}
