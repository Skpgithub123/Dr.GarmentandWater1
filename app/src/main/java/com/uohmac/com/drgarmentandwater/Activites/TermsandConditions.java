package com.uohmac.com.drgarmentandwater.Activites;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
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
 * Created by uOhmac Technologies on 15-Apr-17.
 */
public class TermsandConditions extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    String response_termsandconditions="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsandconditions);
        webView = (WebView) findViewById(R.id.webView_termsandconditions);
        webView.setWebViewClient(new MyBrowser());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar));

        }

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
               /* *//**//*progressBar.setProgress(progress);
                progressBar.setClickable(false);*//**//**/

                if (progress == 100) {
                    progressDialog.dismiss();
                    // progressBar.setVisibility(View.GONE);

                } else {
                    progressDialog.show();
                    //   progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        if(AppNetworkInfo.isConnectingToInternet(TermsandConditions.this)){
            gettermsandconditions();
        }else{
            Toast.makeText(TermsandConditions.this, "No network found.!! please try again", Toast.LENGTH_SHORT).show();
        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(PROGRESS_URL);

            return true;
        }
    }

    private void gettermsandconditions(){
        progressDialog = new ProgressDialog(TermsandConditions.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_termsandconditions = adapter.create(UohmacAPI.class);

        api_termsandconditions.TermsandConditions(new Callback<Response>() {
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
                response_termsandconditions = sb.toString();

                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.loadDataWithBaseURL("", response_termsandconditions, "text/html", "UTF-8", "");
                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(TermsandConditions.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
                progressDialog.dismiss();
            }
        });
    }
}
