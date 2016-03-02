package com.sorcerer.wecheater;

import android.hardware.Sensor;
import android.util.SparseArray;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Sorcerer on 2016/3/2 0002.
 */
public class Xposed implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    static int WechatStepCount = 1;

    XSharedPreferences mPreferences;

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        mPreferences = new XSharedPreferences(MainActivity.PREF_NAME);
    }

    @SuppressWarnings("null")
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.tencent.mm")) {
            return;
        }
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        try {
            final Class<?> sensorEL =
                    XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue",
                            lpparam.classLoader);
            // onSensorChanged
            XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new
                    XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws
                                Throwable {
                            XposedBridge.log(" mzheng  Hooked method: " + param.method);

                            ((float[]) param.args[1])[0] =
                                    ((float[]) param.args[1])[0] +
                                            mPreferences.getInt(MainActivity.PREF_KEY, 500) *
                                                    WechatStepCount;
                            WechatStepCount += 1;

                            XposedBridge.log("   SensorEvent: handle=" + param.args[0]);
                            XposedBridge.log("   SensorEvent: x=" + ((float[]) param.args[1])[0]);
                            XposedBridge.log("   SensorEvent: y=" + ((float[]) param.args[1])[1]);
                            XposedBridge.log("   SensorEvent: z=" + ((float[]) param.args[1])[2]);
                            XposedBridge.log("   SensorEvent: accuracy=" + param.args[2]);
                            XposedBridge.log("   SensorEvent: timestamp=" + param.args[3]);
                            XposedBridge.log("   Class: " + param.thisObject.getClass());
                            XposedBridge.log("   Enclosing Class: " +
                                    param.thisObject.getClass().getEnclosingClass());

                            Field field = param.thisObject.getClass().getEnclosingClass()
                                    .getDeclaredField("sHandleToSensor");
                            field.setAccessible(true);
                            XposedBridge.log("   Field: " + field.toString());
                            int handle = (Integer) param.args[0];
                            Sensor ss = ((SparseArray<Sensor>) field.get(0)).get(handle);
                            XposedBridge.log("   SensorEvent: sensor=" + ss);
                        }
                    });
        } catch (Throwable t) {
            throw t;
        }
    }
}