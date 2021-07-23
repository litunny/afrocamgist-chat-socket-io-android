package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleManager {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ ENGLISH, FRENCH, ARABIC, SPANISH, PORTUGUESE})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = { ENGLISH, FRENCH, ARABIC, SPANISH, PORTUGUESE };
    }
    public static final String ENGLISH = "en";
    public static final String FRENCH = "fr";
    public static final String ARABIC = "ar";
    public static final String SPANISH = "es";
    public static final String PORTUGUESE = "pt";
    private static final String LANGUAGE_KEY = "language_key";

    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Context setNewLocale(Context mContext, @LocaleDef String language) {
        setLanguagePref(mContext, language);
        return updateResources(mContext, language);
    }

    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mPreferences.getString(LANGUAGE_KEY, ENGLISH);
    }

    public static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).apply();
    }

    private static Context updateResources(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }

        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }

}
