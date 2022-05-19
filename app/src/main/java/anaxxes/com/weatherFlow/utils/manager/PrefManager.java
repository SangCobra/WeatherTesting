package anaxxes.com.weatherFlow.utils.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import anaxxes.com.weatherFlow.settings.SettingsOptionManager;

public class PrefManager {

    private static volatile PrefManager instance;
    SharedPreferences sharedPreferences;

    private PrefManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefManager getInstance(Context context){
        if (instance == null) {
            synchronized (PrefManager.class) {
                if (instance == null) {
                    instance = new PrefManager(context);
                }
            }
        }
        return instance;

    }


    public  void setBooleanPref(String key,Boolean value){
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    public Boolean getBooleanPref(String key){
        return sharedPreferences.getBoolean(key,false);
    }


    public  void setStringPref(String key,String value){
        sharedPreferences.edit().putString(key,value).apply();
    }

    public  String getStringPref(String key){
        return sharedPreferences.getString(key,"");
    }

    public  void setIntPref(String key,int value){
        sharedPreferences.edit().putInt(key,value).apply();
    }

    public  int getIntPref(String key){
        return sharedPreferences.getInt(key,-1);
    }
}
