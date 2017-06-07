package com.uohmac.com.drgarmentandwater.Pojos;

/**
 * Created by uOhmac Technologies on 27-Jan-17.
 */
public class VariableClass {


    private String id;
    private String order_id;
    private String item_id;
    private String item_name;
    private String total_price;
    private String item_quantity;
    private String item_pickupdate;
    private String item_pickuptime;
    private String orderitem_image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderitem_image() {
        return orderitem_image;
    }

    public void setOrderitem_image(String orderitem_image) {
        this.orderitem_image = orderitem_image;
    }


    public String getItem_pickupdate() {
        return item_pickupdate;
    }

    public void setItem_pickupdate(String item_pickupdate) {
        this.item_pickupdate = item_pickupdate;
    }



    public String getItem_pickuptime() {
        return item_pickuptime;
    }

    public void setItem_pickuptime(String item_pickuptime) {
        this.item_pickuptime = item_pickuptime;
    }




    public String getItem_date() {
        return item_date;
    }

    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }

    private String item_date;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }



    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
