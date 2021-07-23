package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.hmp.cognitive.afrocamgistchat.AfrocamgistApplication;
import com.app.hmp.cognitive.afrocamgistchat.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    private static Dialog popup;
    private static Dialog popup1;

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) AfrocamgistApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm==null)
            return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static void showAlertWithFinish(Activity activity, String message) {

        if (activity!=null && !activity.isFinishing()) {
            popup = new Dialog(activity, R.style.DialogCustom);
            popup.setContentView(R.layout.dialog_error);
            popup.setCancelable(true);
            popup.show();

            TextView error = popup.findViewById(R.id.message);
            error.setText(message);
            popup.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                    activity.finish();
                }
            });
        }
    }
    public static void showAlert(Activity activity, String message) {

        if (activity!=null && !activity.isFinishing()) {
            popup = new Dialog(activity, R.style.DialogCustom);
            popup.setContentView(R.layout.dialog_error);
            popup.setCancelable(true);
            popup.show();

            TextView error = popup.findViewById(R.id.message);
            error.setText(message);
            popup.findViewById(R.id.ok).setOnClickListener(v ->
                    popup.dismiss());
        }
    }


    public static void showSuccessAlert(Activity activity, String message) {

        if (activity!=null && !activity.isFinishing()) {
            popup1 = new Dialog(activity, R.style.DialogCustom);
            popup1.setContentView(R.layout.dialog_error);
            popup1.setCancelable(true);
            popup1.show();

            ImageView error1 = popup1.findViewById(R.id.error);
            error1.setVisibility(View.GONE);

            ImageView success = popup1.findViewById(R.id.success);
            success.setVisibility(View.VISIBLE);

            TextView error = popup1.findViewById(R.id.message);
            error.setText(message);

            popup1.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup1.dismiss();
                }
            });
        }
    }

    public static void closeSuccessAlert(){
        if (popup1!=null && popup1.isShowing()) {
            popup1.dismiss();
        }
    }

    public static void closeAlert() {
        try {
                if (popup!=null && popup.isShowing()) {
                    popup.dismiss();
                }
        }catch (Exception e){

        }


    }

    public static int getCurrentCountryCode(Activity context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso;
        if (telephonyManager==null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                countryIso = context.getResources().getConfiguration().getLocales().get(0).getCountry();
            } else {
                countryIso = context.getResources().getConfiguration().locale.getCountry();
            }
            if (countryIso != null && countryIso.length() == 2) {
                return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso.toUpperCase());
            }else{
                return 0;
            }
        } else {
            if (telephonyManager.getSimCountryIso()==null || telephonyManager.getSimCountryIso().isEmpty())
                countryIso = telephonyManager.getNetworkCountryIso().toUpperCase();
            else
                countryIso = telephonyManager.getSimCountryIso().toUpperCase();

            return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso);
        }

    }

    public static String getSocialStyleTime(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = 0;
        try {
            time = sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        //return TimeAgo.using(time).replace("about","");
        return TimeAgo.using(time)
                .replace("about","")
                .replace("hour","hr")
                .replace("minutes","min");
    }

    public static String getFormattedDate(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long timeInMilliseconds = 0L;
        try {
            Date mDate = sdf.parse(time);
            if (mDate != null) {
                timeInMilliseconds = mDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(timeInMilliseconds);
        smsTime.setTimeZone(TimeZone.getDefault());
        //smsTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        //now.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd/MM/yyyy";

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return DateFormat.format(timeFormatString, smsTime).toString();
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday";
        } else {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        }
    }

    public static String getFormattedTimeForChatMessage(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long timeInMilliseconds = 0L;
        try {
            Date mDate = sdf.parse(time);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(timeInMilliseconds);
        smsTime.setTimeZone(TimeZone.getDefault());

        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd/MM/yyyy";

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return DateFormat.format(timeFormatString, smsTime).toString();
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday at " + DateFormat.format(timeFormatString, smsTime).toString();
        } else {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        }
    }

    public static String convertDateFormat(String inputDate) {

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
        inputDateFormat.setTimeZone(TimeZone.getDefault());
        outputDateFormat.setTimeZone(TimeZone.getDefault());

        String outputDate = "";
        try {
            outputDate = outputDateFormat.format(inputDateFormat.parse(inputDate));
        } catch (Exception e){
            e.printStackTrace();
        }

        return outputDate;
    }
    public static String convertDateFormat1(String inputDate) {

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        inputDateFormat.setTimeZone(TimeZone.getDefault());
        outputDateFormat.setTimeZone(TimeZone.getDefault());

        String outputDate = "";
        try {
            outputDate = outputDateFormat.format(inputDateFormat.parse(inputDate));
        } catch (Exception e){
            e.printStackTrace();
        }

        return outputDate;
    }

    public static void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) AfrocamgistApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
