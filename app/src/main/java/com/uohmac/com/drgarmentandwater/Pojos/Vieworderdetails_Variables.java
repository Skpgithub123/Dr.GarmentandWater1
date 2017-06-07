package com.uohmac.com.drgarmentandwater.Pojos;

/**
 * Created by Saravanan on 07/04/2017.
 */
public class Vieworderdetails_Variables {


        private  String item_img;
    private String item_name;
    private String item_quantity;
    private String item_price;

    public String getTotal_item_price() {
        return total_item_price;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    private String total_item_price;


}
