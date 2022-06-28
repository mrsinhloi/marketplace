package com.ehubstar.marketplace.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ehubstar.marketplace.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.BuildConfig;

public class MyUtils {
    public static String getFormatDate(int number) {
        String kq = number + "";
        if (number < 10) {
            kq = "0" + number;
        }
        return kq;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openApplicationInStore(Context context, String packageName) {
        if (context != null && !TextUtils.isEmpty(packageName)) {
            try {
                String url = "market://details?id=" + packageName;
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            } catch (ActivityNotFoundException anfe) {
                String url = "https://play.google.com/store/apps/details?id=" + packageName;
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean checkInternetConnection(Context context) {
        boolean b = false;

        if (context != null) {
            ConnectivityManager conMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr != null) {
                NetworkInfo nw = conMgr.getActiveNetworkInfo();
                if (nw != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo()
                        .isConnectedOrConnecting()) {

                    b = true;

                }
            }
        }

        return b;


    }

    /**
     * http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
     * Real internet - faster
     *
     * @return
     */
    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * CPUID tuong duong voi hostname, dung de phan biet 2 may khac nhau
     *
     * @return
     */
    public static String getCPUID(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    private static InetAddress getInetAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void showThongBao(Context context) {
        if (context != null) {
            new MaterialAlertDialogBuilder(context)
                    .setMessage(R.string.mp_no_internet)
                    .setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    public static final void showToast(Context context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static final void showToast(Context context, int message) {
        if (context != null) {
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show();
        }
    }

    public static final void showToastDebug(Context context, String message) {
        if (BuildConfig.DEBUG) {
            if (context != null && !TextUtils.isEmpty(message)) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static final void showToast(Activity context, int message) {
        if (context != null) {
            Toast.makeText(context, context.getResources().getString(message), Toast.LENGTH_SHORT).show();
        }
    }

    public static final void showToast(Activity context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////////////////////
    public static void showAlertDialog(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btn.setTextColor(Color.BLACK);
    }

    public static void showAlertDialog(Context context, String message) {

        if (context != null && !TextUtils.isEmpty(message)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(message);
//        alertDialogBuilder.setIcon(R.drawable.fail);
            alertDialogBuilder.setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setTextColor(Color.BLACK);
        }
    }

    public static void showAlertDialogHtml(Context context, String messageHtml) {

        if (context != null && !TextUtils.isEmpty(messageHtml)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(Html.fromHtml(messageHtml));
//        alertDialogBuilder.setIcon(R.drawable.fail);
            alertDialogBuilder.setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setTextColor(Color.BLACK);
        }
    }


    public static void showAlertDialog(Context context, int message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
//        builder.setIcon(R.drawable.fail);
        builder.setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btn.setTextColor(Color.BLACK);
    }



    public static void showAlertDialog(Activity context, int message, boolean isFinish) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        if (isFinish) {
            alertDialogBuilder.setCancelable(false);
        }
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                context.finish();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.mp_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                if (isFinish && context != null) {
                    context.finish();
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btn.setTextColor(Color.BLACK);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static byte[] toUTF8ByteArray(String s) {
        /*int len = s.length();
        byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }

	    return data;*/
        return toUTF8ByteArray(s.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] string) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        char[] c = string;
        int i = 0;

        while (i < c.length) {
            char ch = c[i];

            if (ch < 0x0080) {
                bOut.write(ch);
            } else if (ch < 0x0800) {
                bOut.write(0xc0 | (ch >> 6));
                bOut.write(0x80 | (ch & 0x3f));
            }
            // surrogate pair
            else if (ch >= 0xD800 && ch <= 0xDFFF) {
                // in error - can only happen, if the Java String class has a
                // bug.
                if (i + 1 >= c.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                char W1 = ch;
                ch = c[++i];
                char W2 = ch;
                // in error - can only happen, if the Java String class has a
                // bug.
                if (W1 > 0xDBFF) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int codePoint = (((W1 & 0x03FF) << 10) | (W2 & 0x03FF)) + 0x10000;
                bOut.write(0xf0 | (codePoint >> 18));
                bOut.write(0x80 | ((codePoint >> 12) & 0x3F));
                bOut.write(0x80 | ((codePoint >> 6) & 0x3F));
                bOut.write(0x80 | (codePoint & 0x3F));
            } else {
                bOut.write(0xe0 | (ch >> 12));
                bOut.write(0x80 | ((ch >> 6) & 0x3F));
                bOut.write(0x80 | (ch & 0x3F));
            }

            i++;
        }

        return bOut.toByteArray();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final Pattern mPattern = Pattern.compile("([1-9]{1}[0-9]{0,2}([0-9]{3})*(\\.[0-9]{0,2})?|[1-9]{1}[0-9]{0,}(\\.[0-9]{0,2})?|0(\\.[0-9]{0,2})?|(\\.[0-9]{1,2})?)");

    public static final boolean isMoneyFormat(String value) {
        Matcher matcher = mPattern.matcher(value);
        return matcher.matches();
    }


    public static void transparentStatusBar(Window window) {
        if (window != null) {
            // In Activity's onCreate() for instance
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
    }

    public static String formatDuration(long second) {
        String duration = "00:00";
        if (second > 0) {
            long minute = second / 60;
            long hour = minute / 60;
            if (hour > 0) {
                duration = String.format("%d:%02d:%02d", hour, minute, second);
            } else {
                duration = String.format("%02d:%02d", minute, second);
            }

        }
        return duration;
    }

    public static long getDurationOfVideo(Context context, String path) {
        if (context != null && !TextUtils.isEmpty(path)) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, Uri.fromFile(new File(path)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);

            retriever.release();

            return timeInMillisec;
        }
        return 0;
    }



    /////DATE/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getNameOfDay() {
        Calendar c = Calendar.getInstance();
        java.util.Date date = c.getTime();
        CharSequence time = android.text.format.DateFormat.format("EEEE", date.getTime()); // gives like (Wednesday)
        return time.toString();
    }

    /*
     * Lay ngay hien tai
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static String getDateStringNotify(String date) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_SERVER);
            java.util.Date d = new java.util.Date();
            try {
                d = sdf.parse(date);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormat.DATE_FORMAT_HH_MM_AA);
                return sdf2.format(d).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_HH_MM_AA);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static java.util.Date getDateDateNotify(String date) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_SERVER);
            java.util.Date d = new java.util.Date();
            try {
                d = sdf.parse(date);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Calendar.getInstance().getTime();
    }

    public static String getCurrentDateLong() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_LONG);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime()).toUpperCase();//"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";//
    }

    public static long getCurrentLongDate() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }


    /**
     * Yesterday
     *
     * @return
     */
    public static String getPreviousDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return sdf.format(cal.getTime()).toUpperCase();
    }

    /**
     * @param days ex: -5
     * @return
     */
    public static String getPreviousDate(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return sdf.format(cal.getTime()).toUpperCase();
    }


    /**
     * Tomorrow
     *
     * @return
     */
    public static String getNextDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return sdf.format(cal.getTime()).toUpperCase();
    }

    public static String getNextNextDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        return sdf.format(cal.getTime()).toUpperCase();
    }

    public static String convertDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(cal.getTime()).toUpperCase();
    }

    public static String convertDate(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(date).toUpperCase();
    }

    /**
     * String date="1900-01-04T00:00:00";
     *
     * @param date
     * @return
     */
    public static String convertDate(String date, String fromFormat, String toFormat) {
        if (!TextUtils.isEmpty(date)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
            java.util.Date d = null;
            try {
                d = dateFormat.parse(date);

                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
                SimpleDateFormat sdf = new SimpleDateFormat(toFormat);
                sdf.setCalendar(cal);
                cal.setTime(d);
                return sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String getFlightDate(String date) {
        if (!TextUtils.isEmpty(date)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_SERVER);
            java.util.Date d = null;
            try {
                d = dateFormat.parse(date);

                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
                SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_EN);
                sdf.setCalendar(cal);
                cal.setTime(d);
                return sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

     public static Calendar convertDate(String date) {
        if (!TextUtils.isEmpty(date)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_SERVER);
            java.util.Date d = null;
            try {
                d = dateFormat.parse(date);

                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
                cal.setTime(d);
                return cal;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }





    /**
     * @param date
     * @return 20160504 - yyyyMMdd
     */
    public static int convertDateReminder(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_REMINDER_FORMAT);
        return Integer.parseInt(sdf.format(date));
    }

    /*
     * Lay ngay hien tai
     */
    public static String getCurrentDateDetail() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime()).toUpperCase();
    }

    /**
     * String date="\\/Date(1401969054401)\\/";
     *
     * @param date
     * @return
     */
    public static String getDateFromTickNumber(String date) {
        if (TextUtils.isEmpty(date) == false) {
            date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
            Date d = new Date(Long.parseLong(date));

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
            sdf.setCalendar(cal);
            cal.setTime(d);
            return sdf.format(d);
        }
        return "";
    }

    public static String getDateDetailFromTickNumber(String date) {
        if (TextUtils.isEmpty(date) == false) {
            date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
            Date d = new Date(Long.parseLong(date));

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY_HHMM);
            sdf.setCalendar(cal);
            cal.setTime(d);
            return sdf.format(d);
        }
        return "";
    }
    /*public static String getDateFromTickNumber(String date){
        String dateString="";
        long d = getLongDate(date);
        if (d != 0) {
            dateString = new SimpleDateFormat(DateFormat.DATE_FORMAT).format(new Date(d));
        }
        return dateString;
    }*/

    public static Date getDateFromTickNumber2(String date) {
        Date d = null;
        if (date != null && !date.equals("")) {
            date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
            d = new Date(Long.parseLong(date));
        }
        return d;
    }

    /**
     * String date="\\/Date(1401969054401)\\/";
     *
     * @param date
     * @return
     */
    public static String getDateFromTickNumberDetail(String date) {
        if (date != null && !date.equals("")) {
            date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
            Date d = new Date(Long.parseLong(date));

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY_HHMM);
            sdf.setCalendar(cal);
            cal.setTime(d);
            return sdf.format(d);
        }
        return "";
    }

    public static String createDateTickNumberCurrent() {
        long date = System.currentTimeMillis();
        return "\\/Date(" + date + ")\\/";
    }

    ///WEEK AND MONTH////////////////////////////////////////////////////////////////////

    public static String firstDayOfWeek() {

        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(cal.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static long firstDayOfWeekLong() {

        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal.getTimeInMillis();//"dd/MM/yyyy";//
    }


    public static String endDayOfWeek() {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        // Add 6 days to reach the last day of the current week
        cal.add(Calendar.DAY_OF_YEAR, 6);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(cal.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static long endDayOfWeekLong() {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        // Add 6 days to reach the last day of the current week
        cal.add(Calendar.DAY_OF_YEAR, 6);

        return cal.getTimeInMillis();//"dd/MM/yyyy";//
    }

    public static String firstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DATE, 1);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(calendar.getTime()).toUpperCase();//"dd/MM/yyyy";//
    }

    public static long firstDayOfMonthLong() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DATE, 1);

        return calendar.getTimeInMillis();
    }

    public static String endDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        // int days=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
        return sdf.format(calendar.getTime()).toUpperCase();//"12DEC2013";//
    }

    public static long endDayOfMonthLong() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        // int days=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return calendar.getTimeInMillis();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String getIMEI(Context context) {
        String kq = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        /*if (context != null) {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null)
                kq = mTelephony.getDeviceId(); //*** use for mobiles
            else
                kq = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); //*** use for tablets
        }*/
        return kq.toUpperCase();
    }


    public static String getModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getAppVersion(Context context) {
        int code = -1;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            code = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getText(String taskName) {
        if (taskName == null) taskName = "";
        return taskName;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param context
     * @param timeDelay milisecond
     */
    public static void setTimeoutScreen(Context context, int timeDelay) {
        //Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, timeDelay);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Bi loi 'Hà Nội' -> Hà Nọi
    public static String getUnsignedString2(String s) {
        StringBuffer unsignedString = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            unsignedString.append(getUnsignedChar(s.charAt(i)));
        }

        return unsignedString.toString();
    }

    public static char getUnsignedChar(char c) {

        if (c == '\u00E1' || c == '\u00E0' || c == '\u1EA3' || c == '\u00E3'
                || c == '\u1EA1' || c == '\u0103' || c == '\u1EAF'
                || c == '\u1EB1' || c == '\u1EB3' || c == '\u1EB5'
                || c == '\u1EB7' || c == '\u00E2' || c == '\u1EA5'
                || c == '\u1EA7' || c == '\u1EA9' || c == '\u1EAB'
                || c == '\u1EAD') {
            return 'a';
        } else if (c == '\u00C1' || c == '\u00C0' || c == '\u1EA2'
                || c == '\u00C3' || c == '\u1EA0' || c == '\u0102'
                || c == '\u1EAE' || c == '\u1EB0' || c == '\u1EB2'
                || c == '\u1EB4' || c == '\u1EB6' || c == '\u00C2'
                || c == '\u1EA4' || c == '\u1EA6' || c == '\u1EA8'
                || c == '\u1EAA' || c == '\u1EAC') {
            return 'A';
        } else if (c == '\u00E9' || c == '\u00E8' || c == '\u1EBB'
                || c == '\u1EBD' || c == '\u1EB9' || c == '\u00EA'
                || c == '\u1EBF' || c == '\u1EC1' || c == '\u1EC3'
                || c == '\u1EC5' || c == '\u1EC7') {
            return 'e';
        } else if (c == '\u00C9' || c == '\u00C8' || c == '\u1EBA'
                || c == '\u1EBC' || c == '\u1EB8' || c == '\u00CA'
                || c == '\u1EBE' || c == '\u1EC0' || c == '\u1EC2'
                || c == '\u1EC4' || c == '\u1EC6') {
            return 'E';
        } else if (c == '\u00ED' || c == '\u00EC' || c == '\u1EC9'
                || c == '\u0129' || c == '\u1ECB') {
            return 'i';
        } else if (c == '\u00CD' || c == '\u00CC' || c == '\u1EC8'
                || c == '\u0128' || c == '\u1ECA') {
            return 'I';
        } else if (c == '\u00F3' || c == '\u00F2' || c == '\u1ECF'
                || c == '\u00F5' | c == '\u1ECD' || c == '\u00F4'
                || c == '\u1ED1' || c == '\u1ED3' || c == '\u1ED5'
                || c == '\u1ED7' || c == '\u1ED9' || c == '\u01A1'
                || c == '\u1EDB' || c == '\u1EDD' || c == '\u1EDF'
                || c == '\u1EE1' || c == '\u1EE3') {
            return 'o';
        } else if (c == '\u00D3' || c == '\u00D2' || c == '\u1ECE'
                || c == '\u00D5' | c == '\u1ECC' || c == '\u00D4'
                || c == '\u1ED0' || c == '\u1ED2' || c == '\u1ED4'
                || c == '\u1ED6' || c == '\u1ED8' || c == '\u01A0'
                || c == '\u1EDA' || c == '\u1EDC' || c == '\u1EDE'
                || c == '\u1EE0' || c == '\u1EE2') {
            return 'O';
        } else if (c == '\u00FA' || c == '\u00F9' || c == '\u1EE7'
                || c == '\u0169' | c == '\u1EE5' || c == '\u01B0'
                || c == '\u1EE9' || c == '\u1EEB' || c == '\u1EED'
                || c == '\u1EEF' || c == '\u1EF1') {
            return 'u';
        } else if (c == '\u00DA' || c == '\u00D9' || c == '\u1EE6'
                || c == '\u0168' | c == '\u1EE4' || c == '\u01AF'
                || c == '\u1EE8' || c == '\u1EEA' || c == '\u1EEC'
                || c == '\u1EEE' || c == '\u1EF0') {
            return 'U';
        } else if (c == '\u00FD' || c == '\u1EF3' || c == '\u1EF7'
                || c == '\u1EF9' || c == '\u1EF5') {
            return 'y';
        } else if (c == '\u00DD' || c == '\u1EF2' || c == '\u1EF6'
                || c == '\u1EF8' || c == '\u1EF4') {
            return 'Y';
        } else if (c == '\u0111') {
            return 'd';
        } else if (c == '\u0110') {
            return 'D';
        }
        return c;
    }

    //Bi loi 'Hà Nội' -> Hà Nọi
    public static String getUnsignedString3(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        return str;
    }

    public static String getUnsignedString(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*@Deprecated
    public static String getPath(Uri uri, Activity context) {
        String path="";
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }*/

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public static long getLongDate(String date) {
        long result = 0;
        if (date != null && !date.equals("")) {
            date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
            try {
                result = Long.parseLong(date);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Qua han
     *
     * @param due_date
     * @return
     */
    public static boolean isOver_DueDate(String due_date) {
        boolean result = false;
        long d = getLongDate(due_date);
        if (d != 0) {
            if (System.currentTimeMillis() > d) {
                result = true;
            }
        }

        return result;
    }
    /////END//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static SimpleDateFormat dateFormatBegin = new SimpleDateFormat(DateFormat.DATE_FORMAT_LONG);

    /**
     * Hom nay: "HH:mm"
     * Cac ngay khac: "dd/MM/yy"
     *
     * @param dateString
     * @return
     */
    public static String getDateChat(String dateString) {
        String time = "";
        try {
            java.util.Date date = (java.util.Date) dateFormatBegin.parse(dateString);
            SimpleDateFormat newFormat;
            //neu la ngay hom nay
            String day = getDateChatFull(dateString);
            if (getCurrentDate().equals(day)) {
                newFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_HH_MM_AA);//de chieu dai bang voi ngay, nhin de hon
            } else {//neu la cac ngay khac
                newFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHORT);
            }
            time = newFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
        //		return dateString;
    }

    /**
     * Hien thi HH:mm
     *
     * @param dateString
     * @return
     */
    public static String getDateChatHHmm(String dateString) {
        String time = "";
        try {
            java.util.Date date = (java.util.Date) dateFormatBegin.parse(dateString);
            SimpleDateFormat newFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_HH_MM_AA);//de chieu dai bang voi ngay, nhin de hon
            time = newFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
        //		return dateString;
    }

    public static String getDateChatFull(String dateString) {
        String time = "";
        if (TextUtils.isEmpty(dateString) == false) {
            try {

                java.util.Date date = (java.util.Date) dateFormatBegin.parse(dateString);
                SimpleDateFormat newFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT_VN_DDMMYYYY);
                time = newFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
        //		return dateString;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    public static String getMessage(String userName, String message, String createDate) {
        String result = message;
        if (message != null) {
            if (message.equals("CREATEGROUPROOM")) {
                result = userName + " đã tạo nhóm - " + getDateChatFull(createDate);
            } else if (message.contains("ADDROOMMEMBER")) {
                String[] arr = message.split("&");
                result = userName + " đã thêm " + arr[1] + " vào nhóm - " + getDateChatFull(createDate);
            } else if (message.contains("REMOVEROOMMEMBER")) {
                String[] arr = message.split("&");
                result = userName + " đã mời " + arr[1] + " khỏi nhóm - " + getDateChatFull(createDate);
            } else if (message.contains("USERLEAVEROOM")) {
                String[] arr = message.split("&");
                result = userName + " đã rời khỏi nhóm - " + getDateChatFull(createDate);
            } else if (message.contains("NOTROOMMEMBER")) {
                String[] arr = message.split("&");
                result = userName + " không còn là thành viên của nhóm - " + getDateChatFull(createDate);
            }

        } else {
            result = "";
        }
        return result;
    }


    private static Pattern pattern;
    private static Matcher matcher;

    public static boolean isImageUrl(String image) {
        if (TextUtils.isEmpty(image)) return false;

        //remove bank place
        image = image.replaceAll(" ", "");
        String regex = "([^\\s]+(\\.(?i)(/bmp|jpg|gif|png|jpeg|webp))$)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(image);
        return matcher.matches();
    }

    public static boolean isJpg_Png(String path) {
        if (TextUtils.isEmpty(path)) return false;
        if (
                path.endsWith(".jpg") ||
                        path.endsWith(".pgeg") ||
                        path.endsWith(".png")
        ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMp4(String path) {
        if (TextUtils.isEmpty(path)) return false;
        if (
                path.endsWith(".mp4")
        ) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isDocumentUrl(String document) {
        /*if (document == null) return false;
        String regex = "([^\\s]+(\\.(?i)(/doc|docx|xls|xlsx|pdf|ppt|pptx))$)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);
        return matcher.matches();*/
        if (document.endsWith(".doc") ||
                document.endsWith(".docx") ||
                document.endsWith(".xls") ||
                document.endsWith(".xlsx") ||
                document.endsWith(".ppt") ||
                document.endsWith(".pptx") ||
                document.endsWith(".pdf")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function will take an URL as input and return the file name.
     * <p>Examples :</p>
     * <ul>
     * <li>http://example.com/a/b/c/test.txt -> test.txt</li>
     * <li>http://example.com/ -> an empty string </li>
     * <li>http://example.com/test.txt?param=value -> test.txt</li>
     * <li>http://example.com/test.txt#anchor -> test.txt</li>
     * </ul>
     *
     * @param urlString
     * @return
     */
    public static String getFileNameFromUrl(String urlString) {
//		String fileNameWithExtension = URLUtil.guessFileName(urlString, null, null);
        urlString = urlString.replace("\\", "/");
        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }


    public static String getMention(String userName, long userId) {
        return "@[" + userName + "](userid:" + userId + ")";
    }

    public static ArrayList<String> findUserMention(String text) {
        ArrayList<String> list = new ArrayList<String>();
        String patternString1 = "\\[([\\s\\S\\d _][^\\]]+)\\]";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
//			System.out.println("found: " + matcher.group(1));
            list.add(matcher.group(1));
        }

        return list;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static long howLongTask(long startTime) {
        long endTime = System.currentTimeMillis();
        long secondMilis = endTime - startTime;
        long second = TimeUnit.MILLISECONDS.toSeconds(secondMilis);
        Log.e("test", "howLongTask: " + secondMilis);
        return secondMilis;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    //AZURE BLOCK STORAGE///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param roomId
     * @param userId
     * @param fileName (file or image)
     * @return
     */
    public static String azureCreateChatLink(long roomId, long userId, String fileName) {
        //block name chat:   RoomId/UserId/yyyyMMddHHmmss/filename.type
        String result = roomId + "/" + userId + "/";
        result += azureGetCurrentDateMilisecond() + "/";
        result += fileName;


        return result;
    }

    /**
     * @param projectId
     * @param taskId
     * @param fileName  (file or image)
     * @return
     */
    public static String azureCreateAttachLink(long projectId, long taskId, String fileName) {
        //block name attach: ProjectId/TaskId/yyyyMMddHHmmss/filename.type
        String result = projectId + "/" + taskId + "/";
        result += azureGetCurrentDateMilisecond() + "/";
        result += fileName;

        return result;
    }

    public static String azureCreateAttachLinkAvatar(String fileName) {
        //block name attach: yyyyMMddHHmmss/filename.type
        String result = "";
        result += azureGetCurrentDateMilisecond() + "/";
        result += fileName;

        return result;
    }
    /**
     * Tao chuoi thoi gian duy nhat
     *
     * @return
     */
    public static String azureGetCurrentDateMilisecond() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_MILISECOND_AZURE);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());//"yyyyMMddHHmmss";//
    }

    public static byte[] getRandomBuffer(int size) {
        byte[] buffer = new byte[size];
        Random random = new Random();
        random.nextBytes(buffer);
        return buffer;
    }


    public static String generateRandomBlobNameWithPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        String blobName = prefix + UUID.randomUUID().toString();
        return blobName.replace("-", "");
    }

    public static void log(String message) {
        Log.i("TEST", "my log: " + message);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////
    //Read more: http://www.androidhub4you.com/2012/12/listview-into-scrollview-in-android.html#ixzz3kZiWOs8n
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void gotoMap(String lat, String lon, Context context) {
        if (context != null) {
            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon + "?z=17");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getAddress(Context context, String lat, String lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address : ");
                /*for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (i == returnedAddress.getMaxAddressLineIndex() - 1) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    } else {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                    }
                }*/
                strReturnedAddress.append(returnedAddress.getAddressLine(0));
                ret = strReturnedAddress.toString();
            } else {
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Boolean isSoftKeyBoardVisible(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
//            Log.d(TAG,"Software Keyboard was shown");
            return true;
        } else {
//            Log.d(TAG,"Software Keyboard was not shown");
            return false;
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Kiem tra hinh da rotation bao nhieu do
     *
     * @param path
     * @return
     */
    public static int getRotationForImage(String path) {
        int rotation = 0;

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    rotation = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rotation;
    }

    public static int getExifOrientation(String src) {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor.newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]{tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orientation;
    }


    public static Bitmap resizeAndRotateImage(String path, int targetWidth, int targetHeight) {
        int rotation = getRotationForImage(path);
        return resizeImage(path, rotation, targetWidth, targetHeight);
    }

    public static Bitmap resizeImage(String path, int rotationToRoot, int targetWidth, int targetHeight) {
        Bitmap scaledBitmap = null;
        scaledBitmap = MyUtils.decodeFile(path, targetWidth, targetHeight);

        //resize bitmap follow width, auto height
        float aspectRatio = (float) scaledBitmap.getWidth() / (float) scaledBitmap.getHeight();
        int width = targetWidth;    //your width
        if (width > scaledBitmap.getWidth()) {
            width = scaledBitmap.getWidth();
        }
        int height = Math.round(width / aspectRatio);

        //neu hinh bi xoay thi xoay lai
        if (rotationToRoot != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationToRoot);
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, width, height, matrix, true);
        } else {
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, width, height);
        }

        return scaledBitmap;
    }

    public static Bitmap decodeFile(String path, int targetWidth, int targetHeight) {
        try {
            //decode image size
            int scale = calculateInSampleSize(path, targetWidth, targetHeight);

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            return BitmapFactory.decodeStream(new FileInputStream(path), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static Point decodeFile(String path) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(path), null, o);
            //Find the correct scale value. It should be the power of 2.

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            return new Point(width_tmp, height_tmp);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(String path,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        Size size = ImageUtils.Companion.getImageSize(path);
        final int width = size.getWidth();
        final int height = size.getHeight();
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        } catch (Exception e) {
            return "jpg";
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getPhoneNumber(Context context) {
        String phone = "";
        /*try {
            TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            phone= tMgr.getLine1Number();
            if(phone==null)phone="";
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return phone;
    }


    public static void copy(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void setFullscreen(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getSupportActionBar().hide();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String KEY_M_ITEM = "KEY_M_ITEM";

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean writeObjectToFile(Object obj, String key, Context context) {
        boolean isSuccess = false;
        if (context != null) {
            File myfile = context.getFileStreamPath(key);
            try {
                if (myfile.exists() || myfile.createNewFile()) {
                    FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(obj);
                    fos.close();

                    isSuccess = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static Object getUserModelFromFile(String key, Context context) {
        Object info = null;
        if (context != null) {
            File myfile = context.getFileStreamPath(key);
            try {
                if (myfile.exists()) {
                    FileInputStream fis = context.openFileInput(key);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    info = ois.readObject();
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return info;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String MONEY_FORMAT = "###,###,###.###";

    public static String getMoneyFormat(double money) {
        return new DecimalFormat(MONEY_FORMAT)
                .format(money);
    }

    public static void howLong(long start, String message) {
//        long time = SystemClock.elapsedRealtime() - start;
//        Log.d("test", "how long = " + time + " - " + message);
    }

    private void getScreenSize(Activity context) {
        // Method 1 Obtain by WindowManager
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width1 = outMetrics.widthPixels;
        int height1 = outMetrics.heightPixels;
//        Log.i(TAG, "Method 1: height::" + height + "  width::" + width);

        //Method 2 // get through Resources
        DisplayMetrics dm1 = context.getResources().getDisplayMetrics();
        int height2 = dm1.heightPixels;
        int width2 = dm1.widthPixels;
//        Log.i(TAG, "Method 2: height::" + height1 + "  width::" + width1);

        //Method 3 // get the default screen resolution
        Display display = context.getWindowManager().getDefaultDisplay();
        int width3 = display.getWidth();
        int height3 = display.getHeight();
//        Log.i(TAG, "Method 3: height::" + height2 + "  width::" + width2);//Method 3: height::1080  width::1920
    }

    public static int getScreenWidth(Context context) {
        int widthScreen = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            widthScreen = size.x;
        } else {
            widthScreen = display.getWidth();  // Deprecated
        }
        return widthScreen;
    }

    public static int getScreenHeight(Context context) {
        int heightScreen = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            heightScreen = size.y;
        } else {
            heightScreen = display.getHeight();
        }
        return heightScreen;
    }


    public static void showKeyboard(Activity context) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void hideKeyboard(Activity context) {
        if (context != null) {
            try {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (context.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_id_root);
     setHideKeyboard(this, layout);
     * @param context
     * @param view
     */
    public static void hideKeyboard(final Context context, View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(), 0);//InputMethodManager.HIDE_NOT_ALWAYS
                        return false;
                    }

                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    hideKeyboard(context, innerView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
