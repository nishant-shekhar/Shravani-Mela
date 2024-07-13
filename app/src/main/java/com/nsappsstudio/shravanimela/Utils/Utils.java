package com.nsappsstudio.shravanimela.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String todayFullDate(){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        String year=String.valueOf(c.get(Calendar.YEAR));
        String month=String.valueOf(c.get(Calendar.MONTH)+1);
        if (month.length()==1){
            month="0"+month;
        }
        String date=String.valueOf(c.get(Calendar.DATE));
        if (date.length()==1){
            date="0"+date;
        }
        return year+month+date;
    }
    public static String getFullDate(Long ts){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(ts);
        String year=String.valueOf(c.get(Calendar.YEAR));
        String month=String.valueOf(c.get(Calendar.MONTH)+1);
        if (month.length()==1){
            month="0"+month;
        }
        String date=String.valueOf(c.get(Calendar.DATE));
        if (date.length()==1){
            date="0"+date;
        }
        return year+month+date;
    }

    public static int todayDate(){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        return c.get(Calendar.DATE);
    }

    public static int thisMonth(){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        return (c.get(Calendar.MONTH)+1);
    }
    public static int thisYear(){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        return (c.get(Calendar.YEAR));
    }
    public static int getHour(Long ts){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(ts);

        return (c.get(Calendar.HOUR_OF_DAY));
    }
    public static int getMinute(Long ts){
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(ts);

        return (c.get(Calendar.MINUTE));
    }
    public static int weekNumber(int date){

        if (date < 8){
            return 1;
        }else if (date < 15){
            return 2;
        }else if (date < 22){
            return 3;
        }else if (date < 29){
            return 4;
        }else {
            return 5;
        }
    }

    public static String DayOfWeek(int year,int month, int date){
        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month-1, date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay;
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "M";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "T";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "W";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "T";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "F";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "S";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sun";
        }else {weekDay="";}
        return weekDay;
    }
    public static int getMaxMonthDays(int year,int month) {

        Calendar monthStart = new GregorianCalendar(year, month-1, 1);
        return (monthStart.getActualMaximum(Calendar.DAY_OF_MONTH)+1);
    }
    public static boolean compareFuture(int year,int month,int day){
        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month-1, day);
        long dateTime=calendar.getTimeInMillis();
        return dateTime < System.currentTimeMillis();

    }
    public static boolean compareDeadLine(String targetDate){
        try {
            int year = Integer.parseInt(targetDate.substring(0, 4));
            int month = Integer.parseInt(targetDate.substring(4, 6));
            int date = Integer.parseInt(targetDate.substring(6, 8));

            Calendar calendar= Calendar.getInstance();
            calendar.set(year, month-1, date);
            long dateTime=calendar.getTimeInMillis();
            return dateTime < System.currentTimeMillis();
        }catch (NumberFormatException e){
            return false;
        }


    }
    public static String DayOfWeek3Letter(int year,int month, int date){
        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month-1, date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay;
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Mon";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tue";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wed";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thu";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Fri";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Sat";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sun";
        }else {weekDay="";}
        return weekDay;
    }
    public static String getDisplayMonth(int month) {

        String monthFinal=String.valueOf(month);
        if (monthFinal.length()==1){
            monthFinal="0"+monthFinal;
        }
        switch (monthFinal) {
            case "01":
                monthFinal = "January";
                break;
            case "02":
                monthFinal = "February";
                break;
            case "03":
                monthFinal = "March";
                break;
            case "04":
                monthFinal = "April";
                break;
            case "05":
                monthFinal = "May";
                break;
            case "06":
                monthFinal = "June";
                break;
            case "07":
                monthFinal = "July";
                break;
            case "08":
                monthFinal = "August";
                break;
            case "09":
                monthFinal = "September";
                break;
            case "10":
                monthFinal = "October";
                break;
            case "11":
                monthFinal = "November";
                break;
            case "12":
                monthFinal = "December";
                break;
        }
        return monthFinal;
    }
    public static String getDateSuperScript(String dateString){
        int date=Integer.parseInt(dateString);
        if(date==1||date==21||date==31){
            return "st";
        }else if (date==2||date==22){
            return "nd";
        }else {
            return "th";
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
    public static int PxToDp(float px, Context context){
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round(px/density);
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static float GetDistance(double mLat1,double mLang1,double mLat2,double mLang2){

        float[] results = new float[10];
        Location.distanceBetween(mLat1, mLang1, mLat2, mLang2, results);
        return results[0];
    }
    public static float GetDistanceNumber(Double mLat1,Double mLang1,Double mLat2,Double mLang2){
        if (mLang1!=null&& mLat1!=null && mLang2!=null && mLat2!=null) {
            float[] results = new float[10];
            Location.distanceBetween(mLat1, mLang1, mLat2, mLang2, results);
            return Float.parseFloat(String.format(Locale.getDefault(),"%.0f",results[0]));

        }else {
            return -1;
        }
    }
    public static String getDeviceId(Context context) {

        String android_id = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        return android_id;

    }
    public static String agoTS(Long ts,Context context){
        if (ts==null){
            return "";
        }
        CharSequence ago =
                DateUtils.getRelativeDateTimeString(context, ts, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS,0);

        return String.valueOf(ago);
    }
    public static String threeLetterMonth(String monthDigit){

        String monthFinal="";

        switch (monthDigit){
            case "01":
                monthFinal="Jan";
                break;
            case "02":
                monthFinal="Feb";
                break;
            case "03":
                monthFinal="Mar";
                break;
            case "04":
                monthFinal="Apr";
                break;
            case "05":
                monthFinal="May";
                break;
            case "06":
                monthFinal="Jun";
                break;
            case "07":
                monthFinal="Jul";
                break;
            case "08":
                monthFinal="Aug";
                break;
            case "09":
                monthFinal="Sep";
                break;
            case "10":
                monthFinal="Oct";
                break;
            case "11":
                monthFinal="Nov";
                break;
            case "12":
                monthFinal="Dec";
                break;

        }

        return monthFinal;
    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE,Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My address", strReturnedAddress.toString());
            } else {
                Log.w("My address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My address", "Canont get Address!");
        }
        return strAdd;
    }
}
