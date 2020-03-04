package com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ufla.gustavo.uflatracker.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HRManager {

    static class AntPlusProxy {
        // AntPlus module may be disabled when building, only available in a few phones
        private static final String Lib = "org.runnerup.hr.AntPlus";
        static final String Name = "AntPlus";

//        static boolean checkAntPlusLibrary(Context ctx) {
//            if (BuildConfig.ANTPLUS_ENABLED) {
//                try {
//                    Class<?> clazz = Class.forName(Lib);
//                    Method method = clazz.getDeclaredMethod("checkLibrary", Context.class);
//                    boolean res = (boolean) method.invoke(null, ctx);
//                    return res;
//                } catch (Exception e) {
//                    Log.d(Lib, Name + "Library is not loaded "+e);
//                }
//            }
//            return false;
//        }

        static HRProvider createProviderByReflection(Context ctx, boolean experimental) {
            try {
                Class<?> classDefinition = Class.forName(Lib);
                Constructor<?> cons = classDefinition.getConstructor(Context.class);
                HRProvider ap = (HRProvider) cons.newInstance(ctx);
                if (!ap.isEnabled()) {
                    return null;
                }
                return ap;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Creates an {@link HRProvider}. This will be wrapped in a {@link RetryingHRProviderProxy}.
     * *
     * @param src The type of {@link HRProvider} to create.
     * @return A new instance of an {@link HRProvider} or null if
     *   A) 'src' is not a valid {@link HRProvider} type
     *   B) the device does not support an {@link HRProvider} of type 'src'
     */
    public static HRProvider getHRProvider(Context ctx, String src) {
        HRProvider provider = getHRProviderImpl(ctx, src);
        if (provider != null) {
            return new RetryingHRProviderProxy(provider);
        }
        return null;
    }


    private static HRProvider getHRProviderImpl(Context ctx, String src) {
        System.err.println("getHRProvider(" + src + ")");
        if (src.contentEquals(AndroidBLEHRProvider.NAME)) {
            if (!AndroidBLEHRProvider.checkLibrary(ctx))
                return null;
            return new AndroidBLEHRProvider(ctx);
        }

        if (src.contentEquals(Bt20Base.ZephyrHRM.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.ZephyrHRM(ctx);
        }

        if (src.contentEquals(Bt20Base.PolarHRM.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.PolarHRM(ctx);
        }

//        if (src.contentEquals(AntPlusProxy.Name)) {
//            if (!AntPlusProxy.checkAntPlusLibrary(ctx))
//                return null;
//            HRProvider hrprov = AntPlusProxy.createProviderByReflection(ctx, true);
//            if (hrprov != null && src.contentEquals(hrprov.getProviderName())) {
//                return hrprov;
//            }
//        }

        if (src.contentEquals(Bt20Base.StHRMv1.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.StHRMv1(ctx);
        }


        return null;
    }

    /**
     * Returns a list of {@link HRProvider}'s that are available on this device.
     *
     * It is recommended to use this list only for selecting a valid {@link HRProvider}.
     * For connecting to the device, use the instance returned by {@link #getHRProvider(android.content.Context, String)}
     *
     * @return A list of all {@link HRProvider}'s that are available on this device.
     */
    public static List<HRProvider> getHRProviderList(Context ctx) {
        Resources res = ctx.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean experimental = prefs
                .getBoolean("pref_bt_experimental", false);

        List<HRProvider> providers = new ArrayList<>();
        if (AndroidBLEHRProvider.checkLibrary(ctx)) {
            providers.add(new AndroidBLEHRProvider(ctx));
        }

        if (experimental && Bt20Base.checkLibrary(ctx)) {
            providers.add(new Bt20Base.ZephyrHRM(ctx));
        }

        if (experimental && Bt20Base.checkLibrary(ctx)) {
            providers.add(new Bt20Base.PolarHRM(ctx));
        }

        if (experimental && Bt20Base.checkLibrary(ctx)) {
            providers.add(new Bt20Base.StHRMv1(ctx));
        }

//        if (AntPlusProxy.checkAntPlusLibrary(ctx)) {
//            HRProvider hrprov = AntPlusProxy.createProviderByReflection(ctx, experimental);
//            if (hrprov != null) {
//                providers.add(hrprov);
//            }
//        }

        return providers;
    }
}
