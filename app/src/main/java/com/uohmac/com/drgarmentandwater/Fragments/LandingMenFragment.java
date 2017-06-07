package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Adpater.CustomButtonListener;
import com.uohmac.com.drgarmentandwater.Adpater.MenAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/4/2016.
 */

public class LandingMenFragment extends Fragment {
    private View myFragmentmenView;
    List<DryCleaningPojo> dryCleaningmenPojoList;
    MenAdapter _Adapter;
    ListView rvMyRequest;
    ProgressDialog mProgressDialog;


    /************gowtham woring conceptions**************/

    ListView listView;


    private ListAdapter listAdapter;
    ArrayList<String>  arrayList = new ArrayList<>();





    public static Fragment newInstance(Bundle args) {
        LandingMenFragment fragment = new LandingMenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LandingMenFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentmenView = inflater.inflate(R.layout.fragmentmen, container, false);

        rvMyRequest = (ListView) myFragmentmenView.findViewById(R.id.lvMenList);





        /*gowthaman*/



      /*  listAdapter = new ListAdapter(getActivity(),dryCleaningmenPojoList);
        listView.setAdapter(listAdapter);
        listAdapter.setCustomButtonListener(this);*/


            rvMyRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                }
            });




        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getdrycleaningdataformen();

        } else{
            Toast.makeText(getActivity(), "No network found.!", Toast.LENGTH_SHORT).show();
        }




        return myFragmentmenView;
    }

    private void getdrycleaningdataformen(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_drycleaningdataformen = adapter.create(UohmacAPI.class);

        api_drycleaningdataformen.GetDryCleaningDetails("4", new Callback<List<DryCleaningPojo>>() {
            @Override
            public void success(List<DryCleaningPojo> dryCleaningPojo, Response response) {
                dryCleaningmenPojoList = dryCleaningPojo;

                if (getActivity() != null) {
                    _Adapter = new MenAdapter(getActivity(),R.layout.mensingleitem, dryCleaningPojo);
                    rvMyRequest.setAdapter(_Adapter);
                }

                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }

   /* @Override
    public void onButtonClickListener(int position, TextView editText, int value) {
        int quantity = Integer.parseInt(editText.getText().toString());
        quantity = quantity +  1*value;
        if(quantity<0)
            quantity=0;
        editText.setText(quantity+"");
    }*/


    /*class ListAdapter extends BaseAdapter
    {
        public ArrayList<Integer> quantity = new ArrayList<Integer>();
        private List<DryCleaningPojo> listViewItems;
        TypedArray images;
        private Context context;
        CustomButtonListener customButtonListener;
        String pos;
        int myCount;

        public ListAdapter(Context context,List<DryCleaningPojo> listViewItems)
        {

            this.context = context;
            this.listViewItems = listViewItems;

            for(int i =0; i< listViewItems.size();i++)
            {
                quantity.add(0);
                //quantity[i]=0;
            }

        }
        public void setCustomButtonListener(CustomButtonListener customButtonListner)
        {
                    this.customButtonListener = customButtonListner;
        }

        @Override
        public int getCount() {
            return listViewItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            View row;
            final ListViewHolder listViewHolder;

            if(convertView == null)
            {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.mensingleitem,parent,false);
                listViewHolder = new ListViewHolder();
                *//*listViewHolder.tvFruitName = (TextView) row.findViewById(R.id.tvFruitName);
                listViewHolder.Img_increment= (ImageView) row.findViewById(R.id.ivFruit);
                listViewHolder.tvPrices = (TextView) row.findViewById(R.id.tvFruitPrice);
                listViewHolder.btnPlus = (ImageButton) row.findViewById(R.id.ib_addnew);
                listViewHolder.edTextQuantity = (EditText) row.findViewById(R.id.editTextQuantity);
                listViewHolder.btnMinus = (ImageButton) row.findViewById(R.id.ib_remove);*//*

                listViewHolder.Img_increment = (ImageView) row.findViewById(R.id.image_add);
                listViewHolder.Img_decrement = (ImageView) row.findViewById(R.id.img_mensub);
                listViewHolder.tv_noof_qty     = (TextView) row.findViewById(R.id.txt_itemqtyincrement);
                //listViewHolder.tv_samePrice     = (TextView) row.findViewById(R.id.tv_samepleprice);
                row.setTag(listViewHolder);
            }
            else
            {
                row=convertView;
                listViewHolder= (ListViewHolder) row.getTag();
            }
            //listViewHolder.tv_noof_qty.setText(listViewItems.get(position));
            try{

                listViewHolder.tv_noof_qty.setText(quantity.get(position)+"");


            }catch(Exception e){

                Log.d("sqlexeception","exectpoin"+e.toString());
                e.printStackTrace();
            }

            pos = arrayList.get(position);
            myCount = getCount();
            View v = getActivity().getLayoutInflater().inflate(R.layout.mensingleitem, null);

            listViewHolder.Img_increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customButtonListener != null) {
                        customButtonListener.onButtonClickListener(position, listViewHolder.tv_noof_qty, 1);
                        quantity.set(position, quantity.get(position) + 1);
                    }
                }
            });

            listViewHolder.Img_decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customButtonListener != null) {
                        customButtonListener.onButtonClickListener(position,listViewHolder.tv_noof_qty,-1);
                        if(quantity.get(position)>0)
                            quantity.set(position, quantity.get(position) - 1);
                    }
                }
            });



            return row;
        }


    }

*/

   /* public class ListViewHolder {

      *//*  TextView tvFruitName ;
        ImageView ivFruit;
        TextView tvPrices ;
        ImageButton btnPlus;
        EditText edTextQuantity;
        ImageButton btnMinus;*//*

        ImageView Img_increment,Img_decrement;
        TextView tv_noof_qty,tv_samePrice;

    }*/



}
