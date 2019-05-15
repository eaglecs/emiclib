package basecode.com.data.service;

import android.app.ActivityManager;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

public class ServiceUtil {
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceName.equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

//    public static boolean isServiceAutoSyncOrdersRunning(Context context) {
//        return isServiceRunning(context, AutoSyncOrdersIntentService.class.getName());
//    }
//
//    public static void startAutoSyncOrdersService(Context context) {
//        Intent serviceIntent = new Intent(context, AutoSyncOrdersIntentService.class);
//        context.startService(serviceIntent);
//    }
}
