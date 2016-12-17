
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Copyright 2016 Lelight
 * All right reserved.
 * <p/>
 * Created by itLowly
 * <p/>
 * on 2016/5/17 16:26
 * 缓存数据工具类
 */
public class ShareUtils {
    private static ShareUtils utils;
    private static Context mContext;
    private static SharedPreferences mPre;

    private ShareUtils() {
        mPre = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static ShareUtils getInstance() {
        if (utils == null) {
            return new ShareUtils();
        } else {
            return utils;
        }
    }

    public static void initUtils(Context context){
        mContext = context;
    }

    public <T extends Object> void setValue(String key, T value) {
        if (value instanceof Boolean) {
            Boolean t = (Boolean) value;
            mPre.edit().putBoolean(key, t).commit();
        } else if (value instanceof String) {
            mPre.edit().putString(key, (String) value).commit();
        } else if (value instanceof Integer) {
            mPre.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Float) {
            mPre.edit().putFloat(key, (Float) value).commit();
        } else if (value instanceof Long) {
            mPre.edit().putLong(key, (Long) value).commit();
        }
    }

    public <T extends Object> T getValue(String key, String name) {
        if (name.equals("Boolean")) {
            return (T) ((Boolean) mPre.getBoolean(key, false));
        } else if (name.equals("String")) {
            return (T) mPre.getString(key, "unKown");
        } else if (name.equals("Integer")) {
            return (T) ((Integer) mPre.getInt(key, 0));
        } else if (name.equals("Float")) {
            return (T) ((Float) mPre.getFloat(key, 0));
        } else if (name.equals("Long")) {
            return (T) ((Long) mPre.getLong(key, 0));
        } else {
            return null;
        }

    }

    public void removeData(String key) {
        mPre.edit().remove(key).commit();
    }
}
