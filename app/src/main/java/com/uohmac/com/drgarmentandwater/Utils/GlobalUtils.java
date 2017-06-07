package com.uohmac.com.drgarmentandwater.Utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 5/4/2016.
 */
public class GlobalUtils {
    public static Snackbar showSnackBar(View v, String message) {

        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setMinimumHeight(10);
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(2);
        snackbar.show();

        return snackbar;

    }
}
