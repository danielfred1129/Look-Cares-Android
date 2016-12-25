package thelookcompany.lookcares.utils;

/**
 * Created by buddy on 12/5/2016.
 */


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import thelookcompany.lookcares.datamodel.UserObject;

public class UserUtils {

    private static final String SESSION_KEY = "session";
    private static final String SELECTED_CAR = "selected_car";
    private static final String REMEMBER_ME = "remember_me";
    private static final String SELECTED_CLIENT = "selected_client";
    private static final String SELECTED_LOCATION = "selected_location";
    private static final String SELECTED_FRAME = "selected_frame";
    private static final String INSTALLED_FABRICS = "installed_fabrics";
    private static final String SELECTED_FABRIC = "selected_fabric";
    private static final String SELECTED_STORE_LOCATION = "selected_store_location";
    private static final String SELECTED_BAR_CODE = "selected_bar_code";
    private static final String VALET = "valet";
    private static final String SCHEDULE_DATA = "scheduleData";
    private static final String SELECTED_CATEGORY = "selected_category";
    private static final String APP = "com.unlimitec.porschetower";

    public static void storeSession(Context context, UserObject session) {

        JSONObject json = new JSONObject();
        try {
            if (session != null) {

                json.put("token", session.getToken());
                json.put("userKey", session.getUserKey());
                json.put("userName", session.getUserName());
                json.put("userPass", session.getUserPass());
                json.put("clientKey", session.getClientKey());

                context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit()
                        .putString(SESSION_KEY, json.toString()).commit();
            } else {
                context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit()
                        .putString(SESSION_KEY, null).commit();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static UserObject getSession(Context context) {
        String jsString = context.getSharedPreferences(APP,
                Context.MODE_PRIVATE).getString(SESSION_KEY, null);
        UserObject user = null ;
        try {
            if (jsString != null) {
                user = new UserObject();
                JSONObject json = new JSONObject(jsString);

                user.setToken(json.getString("token"));
                user.setUserKey(json.getString("userKey"));
                user.setUserName(json.getString("userName"));
                user.setUserPass(json.getString("userPass"));
                user.setClientKey(json.getString("clientKey"));
            } else {
                return null;
            }
        } catch (JSONException e) {
            return user;
        }
        return user;
    }
    public static void storeRememberMe(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(REMEMBER_ME, valet).commit();
    }
    public static String getRememberMe(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(REMEMBER_ME, null);
        return valet;
    }
    public static void storeSelectedClient(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CLIENT, valet).commit();
    }
    public static String getSelectedClient(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CLIENT, null);
        return valet;
    }
    public static void storeSelectedLocation(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_LOCATION, valet).commit();
    }
    public static String getSelectedLocation(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_LOCATION, null);
        return valet;
    }
    public static void storeSelectedFrame(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_FRAME, valet).commit();
    }
    public static String getSelectedFrame(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_FRAME, null);
        return valet;
    }
    public static void storeInstalledFabrics(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(INSTALLED_FABRICS, valet).commit();
    }
    public static String getInstalledFabrics(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(INSTALLED_FABRICS, null);
        return valet;
    }
    public static void storeSelectedFabric(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_FABRIC, valet).commit();
    }
    public static String getSelectedFabric(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_FRAME, null);
        return valet;
    }
    public static void storeSelectedStoreLocation(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_STORE_LOCATION, valet).commit();
    }
    public static String getSelectedStoreLocation(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_STORE_LOCATION, null);
        return valet;
    }
    public static void storeSelectedBarcode(Context context, String valet) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_BAR_CODE, valet).commit();
    }
    public static String getSelectedBarcode(Context context) {
        String valet = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_BAR_CODE, null);
        return valet;
    }
    public static void storeScheduleDataArray(Context context, String scheduleString) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SCHEDULE_DATA, scheduleString).commit();
    }
    public static String getScheduleDataArray(Context context) {
        String scheduleString = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SCHEDULE_DATA, null);
        return scheduleString;
    }
    public static void storeScheduleData(Context context, String scheduleString) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SCHEDULE_DATA, scheduleString).commit();
    }
    public static String getScheduleData(Context context) {
        String scheduleString = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CAR, null);
        return scheduleString;
    }
    public static void storeSelectedCar(Context context, JSONObject selectedCar) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CAR, selectedCar.toString()).commit();
    }
    public static String getSelectedCategory(Context context) {
        String categoryString = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CATEGORY, null);
        return categoryString;
    }
    public static void storeSelectedCategory(Context context, String selectedCategory) {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CATEGORY, selectedCategory).commit();
    }

    public static JSONObject getSelectedCar(Context context) {
        String jsString = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CAR, null);
        JSONObject jsonObject = null;
        try {
            if (jsString != null) {
                jsonObject = new JSONObject(jsString);
            }
            else
            {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }
}
