package com.uohmac.com.drgarmentandwater.Activites;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppMapFragment;

/**
 * Created by Bharathee SM on 11/28/2016.
 */
public class AddNewAddress extends AppCompatActivity  {


    private TextInputLayout inputLayoutAddress, inputLayoutAddress1, inputLayoutAddress2, inputLayoutCity, inputLayoutLandmark, inputLayoutState, inputLayoutCountry, inputLayoutPincode;
    private EditText etAddressName, etAddress1, etAddress2, etAddressCity, etAddressLandmark, etAddressState, etAddressPincode;
    private Button btnSaveAddress;
    private ImageView btnGetAddress;
    private CheckBox checkBoxIsDefault;
    private LatLng latLng;
    private GoogleMap mMap;
    private AppMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewaddress);



    }


}
