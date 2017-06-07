package com.uohmac.com.drgarmentandwater.Api;

import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.OrderedDetailsPojo;
import com.uohmac.com.drgarmentandwater.Pojos.PromoCodesPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderHistoryPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderPojo;
import com.uohmac.com.drgarmentandwater.Pojos.Timeslotspojo;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by SUNIL on 9/8/2016.
 */
public interface UohmacAPI  {

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/register")
    void RegisterUser(@Field("first_name") String fname,
                      @Field("last_name") String lname,
                      @Field("email") String email,
                      @Field("password") String password,
                      @Field("mobile") String mob,
                      @Field("device_id") String deviceid,
                      @Field("regis_referral_code") String referalcode,
                      Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/auth/login_process")
    void Login(@Field("email_mobile") String loginemail,
               @Field("password") String loginpassword,
               Callback<Response> callback);


    @GET("/water_laundry/index.php/api/laundry/laundry_home")
    void GetLaundryData(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_item_dtl")
    void GetDryCleaningDetails(@Field("category_id") String cat_id,
                               Callback<List<DryCleaningPojo>>callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/register")
    void FBRegisterDetails(@Field("first_name") String fname,
                           @Field("last_name") String lname,
                           @Field("email") String email,
                           @Field("mobile") String mob,
                           @Field("profile_pic") String fbprofilepic,
                           @Field("fb_id") String facebookid,
                           @Field("device_id") String deviceid,
                           Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/chk_fb_login")
    void CheckFBLogin(@Field("msg") String fID,
                      Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_item_dtl")
    void GetLaundryDetails(@Field("category_id") String c_id,
                           Callback<Response> callback);

    @GET("/water_laundry/index.php/api/laundry/laundry_timeslot")
    void GetTimeSlots(Callback<List<Timeslotspojo>> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/add_order_dtl")
    void PostDetailsfororder_withoutwallet(@Field("total_price") String totalprice,
                             @Field("pickup_date") String pickupdate,
                             @Field("pickup_time") String pickuptime,
                             @Field("auth_key") String authkey,
                             @Field("order_type") String ordertype,
                             @Field("payment_method") String paymentmethod,
                             Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/add_order_dtl")
    void PostDetailsfororder_wallet(@Field("total_price") String totalprice,
                             @Field("pickup_date") String pickupdate,
                             @Field("pickup_time") String pickuptime,
                             @Field("auth_key") String authkey,
                             @Field("order_type") String ordertype,
                             @Field("wallet_amount") String walletamount,
                             @Field("payment_method") String paymentmethod,
                             Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/add_order_dtl")
    void PostDetailsfororder_waterwallet(@Field("total_price") String totalprice,
                                           @Field("auth_key") String authkey,
                                           @Field("order_type") String ordertype,
                                           @Field("wallet_amount") String walletamount,
                                           @Field("payment_method") String paymetnmethod,
                                           @Field("delivery_type") String del_type,
                                           Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/add_order_dtl")
    void PostDetailsfororder_waterwihoutwallet(@Field("total_price") String totalprice,
                                         @Field("auth_key") String authkey,
                                         @Field("order_type") String ordertype,
                                         @Field("payment_method") String paymentmethod,
                                               @Field("delivery_type") String del_type,
                                         Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/auth/change_password")
    void ChangePassword(@Field("current_password") String currentpwd,
                        @Field("new_password") String newpassword,
                        @Field("conf_password") String confirmowd,
                        @Field("auth_key")String authkey,
                        Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/auth/forgot_password")
    void ForgotPassword(@Field("mobile") String mobile,
                        @Field("new_password") String new_password,
                        @Field("conf_password") String confirm_password,
                        Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/edit_profile")
    void EditProfile(@Field("auth_key") String authkey,
                     @Field("first_name") String fn,
                     @Field("last_name") String ln,
                     @Field("mobile") String mob,
                     Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/referral_dtl")
    void ReferandEarn(@Field("auth_key") String authkey,
                      Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/get_address")
    void GetAddress(@Field("auth_key") String authkey,
                     Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/update_address")
    void UpdateAddress(@Field("auth_key") String authkey,
                       @Field("pincode") String pincode,
                       @Field("flat_no") String flatno,
                       @Field("address") String address,
                       @Field("city") String city,
                       @Field("state") String state,
                       @Field("landmark") String landmark,
                       @Field("country") String country,
                       @Field("lat") Double latitude,
                       @Field("long") Double longtitude,
                       Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/add_to_cart")
    void IncreamentAPI(@Field("auth_key") String auth_key_increament,
                       @Field("item_id") String itemid,
                       Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/remove_to_cart")
    void DecrementAPI(@Field("auth_key") String auth_key_decrement,
                      @Field("item_id") String itemid,
                      Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_total_cart_price")
    void TotalCartPrice(@Field("cart_type") String carttype,
                        @Field("auth_key") String auth_key_totalprice,
                        Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_cart_data")
    void GetCartData(@Field("cart_type") String cart_typeforlaundry,
                    @Field("auth_key") String auth_key_cartdata,
                     Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/remove_item")
    void RemoveItem(@Field("auth_key") String auth_key_removeitem,
                    @Field("item_id") String itemid_removeitem,
                    Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/payment/apply_promo_code")
    void PromoCode(@Field("auth_key") String auth_key_promocode,
                   @Field("promo_title") String promotitle,
                   @Field("total_cart_amount") String totalcartprice,
                   Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/payment/get_promo_dtl_desc")
    void GetPromoCodeList(@Field("auth_key") String auth_key_getpromocode,
                          Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/get_wlt_balance")
        void CheckWalletBalance(@Field("auth_key") String auth_key_wallet,
                            Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/payment/cr_wallet")
    void AddMoneyToWallet(@Field("auth_key") String auth_key_addmoneywallet,
                          @Field("amount") String amount,
                          Callback<Response> callback);


    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_order_history_item")
     void View_Orderhistory_fulldetails(
            @Field("auth key") String username,
            @Field("id") String ids,
            Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_order_history")
    void Get_Orderhistory(@Field("auth_key") String auth_key_recentorders,
                          Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_order_history_item")
    void RecentOrdersHistory(@Field("auth_key") String auth_key_recentorderhostory,
                             @Field("id") String id,
                             Callback<List<RecentOrderHistoryPojo>> recentorderhistorypojo);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/water/get_empty_can_detail")
    void ReturnEmptyCan(@Field("auth_key") String auth_keyreturnemptycan,
                        @Field("item_id") String itemid,
                        Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/water/add_empty_can")
    void AddEmptyCan(@Field("auth_key") String auth_key_addemptycan,
                     @Field("item_id") String item_id,
                     Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/water/remove_empty_can")
    void SubEmptyCan(@Field("auth_key") String auth_key_subemptycan,
                     @Field("item_id") String itemid,
                     Callback<Response> callback);

    @GET("/water_laundry/index.php/api/app/get_page?flag=faq")
    void FAQ(Callback<Response> callback);


    @GET("/water_laundry/index.php/api/app/get_page?flag=about_us")
    void AboutUs(Callback<Response> callback);

    @GET("/water_laundry/index.php/api/app/get_page?flag=terms")
    void TermsandConditions(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/user/is_delivery_area")
    void CheckForDelivery(@Field("pincode") String pincode,
                            Callback<Response> callback);

    @FormUrlEncoded
    @POST("/water_laundry/index.php/api/laundry/get_item_quantity")
    void getitemquantity(@Field("auth_key") String authkey,
                         @Field("item_id") String itemid,
                         @Field("cart_type") String carttype,
                         Callback<Response> callback);

}
