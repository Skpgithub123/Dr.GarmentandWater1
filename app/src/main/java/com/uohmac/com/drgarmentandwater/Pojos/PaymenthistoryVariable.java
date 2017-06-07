package com.uohmac.com.drgarmentandwater.Pojos;

/**
 * Created by Saravanan on 05/04/2017.
 */
public class PaymenthistoryVariable {

    private String id,txn_no,created_date,amount,status;

    private String Water_id;
    private String Water_txn_no;

    public String getLaundry_id() {
        return laundry_id;
    }

    public void setLaundry_id(String laundry_id) {
        this.laundry_id = laundry_id;
    }

    public String getLaundry_txn_no() {
        return laundry_txn_no;
    }

    public void setLaundry_txn_no(String laundry_txn_no) {
        this.laundry_txn_no = laundry_txn_no;
    }

    public String getLaundry_created_date() {
        return laundry_created_date;
    }

    public void setLaundry_created_date(String laundry_created_date) {
        this.laundry_created_date = laundry_created_date;
    }

    public String getLaundry_amount() {
        return laundry_amount;
    }

    public void setLaundry_amount(String laundry_amount) {
        this.laundry_amount = laundry_amount;
    }

    public String getLaundry_status() {
        return laundry_status;
    }

    public void setLaundry_status(String laundry_status) {
        this.laundry_status = laundry_status;
    }

    private String laundry_id,laundry_txn_no,laundry_created_date,laundry_amount,laundry_status;

    public String getWater_id() {
        return Water_id;
    }

    public void setWater_id(String water_id) {
        Water_id = water_id;
    }

    public String getWater_txn_no() {
        return Water_txn_no;
    }

    public void setWater_txn_no(String water_txn_no) {
        Water_txn_no = water_txn_no;
    }

    public String getWater_created_date() {
        return Water_created_date;
    }

    public void setWater_created_date(String water_created_date) {
        Water_created_date = water_created_date;
    }

    public String getWater_amount() {
        return Water_amount;
    }

    public void setWater_amount(String water_amount) {
        Water_amount = water_amount;
    }

    public String getWater_status() {
        return Water_status;
    }

    public void setWater_status(String water_status) {
        Water_status = water_status;
    }

    private String Water_created_date;
    private String Water_amount;
    private String Water_status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxn_no() {
        return txn_no;
    }

    public void setTxn_no(String txn_no) {
        this.txn_no = txn_no;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /* "id": "1",
                "txn_no": "WLT-1490953054",
                "created_date": "2017-04-04 13:12:22",
                "amount": "110",
                "status": "Success"*/





}
