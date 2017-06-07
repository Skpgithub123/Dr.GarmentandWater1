package com.uohmac.com.drgarmentandwater.Pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by uOhmac Technologies on 24-Jan-17.
 */
public class RecentOrderPojo {



  @SerializedName("id")
  protected String id;

   @SerializedName("order_id")
    protected String orderno;

    @SerializedName("total_price")
    protected String totlaorderamount;

    @SerializedName("pickup_date")
    protected String pickupdate;

    @SerializedName("pickup_time")
    protected String pickuptime;

    @SerializedName("added_date")
    protected String date;

   @SerializedName("status")
   protected String status;


 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getOrderno() {
  return orderno;
 }

 public void setOrderno(String orderno) {
  this.orderno = orderno;
 }

 public String getTotlaorderamount() {
  return totlaorderamount;
 }

 public void setTotlaorderamount(String totlaorderamount) {
  this.totlaorderamount = totlaorderamount;
 }

 public String getPickupdate() {
  return pickupdate;
 }

 public void setPickupdate(String pickupdate) {
  this.pickupdate = pickupdate;
 }

 public String getPickuptime() {
  return pickuptime;
 }

 public void setPickuptime(String pickuptime) {
  this.pickuptime = pickuptime;
 }

 public String getDate() {
  return date;
 }

 public void setDate(String date) {
  this.date = date;
 }


 public String getStatus() {
  return status;
 }

 public void setStatus(String status) {
  this.status = status;
 }
}


