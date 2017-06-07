package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;;import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 24-Apr-17.
 */
public class AboutUsFragment extends Fragment {

    private View myFragmentView;
    WebView webView;
    ProgressDialog progressDialog;
    String response_faq;
    public AboutUsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar));

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("About Us");
        myFragmentView = inflater.inflate(R.layout.aboutus, container, false);


        webView = (WebView) myFragmentView.findViewById(R.id.webView_aboutus);
        webView.setWebViewClient(new MyBrowser());

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
               /* *//**//*progressBar.setProgress(progress);
                progressBar.setClickable(false);*//**//**/

                progressDialog.setMessage("Please Wait....");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (progress == 100) {
                    progressDialog.dismiss();
                    // progressBar.setVisibility(View.GONE);

                } else {
                    progressDialog.show();
                    //   progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getaboutus();
        }else{
            Toast.makeText(getActivity(), "No network found.!! please try again", Toast.LENGTH_SHORT).show();
        }

        return myFragmentView;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(PROGRESS_URL);

            return true;
        }
    }


    private void getaboutus(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_aboutus = adapter.create(UohmacAPI.class);

        api_aboutus.AboutUs(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response_faq = sb.toString();

                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.loadDataWithBaseURL("", response_faq, "text/html", "UTF-8", "");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());
            }
        });
    }
}
